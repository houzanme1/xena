package au.gov.naa.digipres.xena.plugin.psd;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import au.gov.naa.digipres.xena.kernel.XenaException;
import au.gov.naa.digipres.xena.kernel.view.XenaView;


/**
 * View  for displaying both Xena PNG .
 *
 * @author NAA Digital Preservation
 */
public class PngViewer extends XenaView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class State {
		private double widthZoomFactor;
		private double heightZoomFactor;
		private boolean fitToWidth;
		private boolean fitToHeight;
		private boolean fitToSize;
		private String mesg;

		public State() {
			reset();
		}

		public double getZoomFactor() {
			if (widthZoomFactor == heightZoomFactor) {
				return widthZoomFactor;
			} else {
				return -1.0;
			}
		}

		void reset() {
			fitToWidth = fitToHeight = fitToSize = false;
			mesg = null;
			widthZoomFactor = 1.0F;
			heightZoomFactor = 1.0F;
		}

		public void setZoomFactor(double zoomFactor) {
			reset();
			this.widthZoomFactor = zoomFactor;
			this.heightZoomFactor = zoomFactor;
		}

		public void setFitToWidth() {
			reset();
			fitToWidth = true;
			mesg = "Fit to Width";
		}

		public boolean isFitToWidth() {
			return fitToWidth && !(fitToHeight || fitToSize);
		}

		public boolean isFitToHeight() {
			return fitToHeight && !(fitToWidth || fitToSize);
		}

		public boolean isFitToBoth() {
			return fitToWidth && fitToHeight && !fitToSize;
		}

		public boolean isFitToSize() {
			return fitToSize;
		}

		public void setFitToHeight() {
			reset();
			fitToHeight = true;
			mesg = "Fit to Height";
		}

		public void setFitToBoth() {
			reset();
			fitToWidth = true;
			fitToHeight = true;
			mesg = "Fit to Both";
		}

		public void setFitToSize() {
			reset();
			fitToWidth = true;
			fitToHeight = true;
			fitToSize = true;
			mesg = "Fit to Size";
		}

		void set(PngViewer view) {
			double w = widthZoomFactor;
			double h = heightZoomFactor;
			if (fitToWidth) {
				w = view.getWidthFit();
			}
			if (fitToHeight) {
				h = view.getHeightFit();
			}
			if (fitToSize) {
				if (w < h) {
					h = w;
				} else {
					w = h;
				}
			}
			view.label.setZoomFactor(w, h, mesg);
		}
	}

	State state = new State();

	static double FUDGE_FACTOR = 20.0;

	JScrollPane scrollPane = new JScrollPane();

	MyLabel label = new MyLabel();

	MyMenu popupItems = new MyMenu();

	JPopupMenu popup = new JPopupMenu();

	float scale;

	JLabel statusBar = new JLabel();

	public PngViewer() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*	public void updateViewFromElement() throws XenaException {
	  try {
	   sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	   byte[] bytes = decoder.decodeBuffer(getElement().getText());
	   ImageIcon icon = new ImageIcon(bytes);
	   label.setIcon(icon);
	   label.setZoomFactor(1.0F, 1.0F);
	  } catch (IOException e) {
	   throw new XenaException(e);
	  }
	 }
	 */

	@Override
	public ContentHandler getContentHandler() throws XenaException {
		XMLFilterImpl ch = new XMLFilterImpl() 
		{
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            StringBuilder remainderBuff = new StringBuilder();
            FileOutputStream fos;
            File imgFile;
            
            /* (non-Javadoc)
			 * @see org.xml.sax.helpers.XMLFilterImpl#startDocument()
			 */
			@Override
			public void startDocument() throws SAXException
			{
                try
				{
					imgFile = File.createTempFile("img", ".tmp");
					imgFile.deleteOnExit();
                    fos = new FileOutputStream(imgFile);
				}
				catch (IOException e)
				{
					throw new SAXException("Could not create temporary image file");
				}
			}
           
            /* (non-Javadoc)
			 * @see org.xml.sax.helpers.XMLFilterImpl#endDocument()
			 */
			@Override
			public void endDocument() throws SAXException
			{
				if (remainderBuff.length() != 0)
				{
					throw new SAXException("Invalid Base64 data - length not divisible by 4");
				}

				try
				{
					fos.flush();
					fos.close();
				}
				catch (IOException e)
				{
					throw new SAXException("Problem closing image output file");
				}
								
				ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
				label.setIcon(icon);
				label.setZoomFactor(1.0F, 1.0F);
				
			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException 
            {
                byte[] bytes = null;
                
                // Remove any formatting whitespace from the data
                String data = new String(ch, start, length).trim();            

                if (data.length() + remainderBuff.length() < 4)
                {
                	remainderBuff.append(data);
                }
                else
                {
 	                StringBuilder sb = new StringBuilder();
	                sb.append(remainderBuff);
	                remainderBuff = new StringBuilder();
	                try 
	                {
	                	// Need sets of 4 characters for Base64 decoding. So we add new characters
	                	// to those remaining from the last run, ensuring that the total number of characters
	                	// is divisible by 4. If there are any characters remaining, they are added to the
	                	// remainderBuff for the next run.
	                	int charsRemaining = (data.length() + sb.length()) % 4;
	                	sb.append(data, 0, data.length()-charsRemaining);
	                	remainderBuff.append(data, data.length()-charsRemaining, data.length());
	                	
	                	// Write decoded characters to output file
	                    bytes = decoder.decodeBuffer(sb.toString());
	                    fos.write(bytes);
	                    
	                } catch (IOException x) {
	                	throw new SAXException("Problem writing to image output file");
	                }
                }
            }
			
 		};
 		
		return ch;
	}

	@Override
	public String getViewName() {
		return "Image View";
	}

	@Override
	public boolean canShowTag(String tag) throws XenaException {
		return tag.equals(viewManager.getPluginManager().getTypeManager().lookupXenaFileType(XenaPngFileType.class).getTag());
	}

	@Override
	public void initListeners() {
		XenaView.addPopupListener(popup, label);
//		XenaMenu.initListenersAll(menus);
	}

//	public void makeMenu(JMenu menu) {
//		customItems.makeMenu(menu);
//	}

	private void jbInit() throws Exception {
//		menus = new MyMenu[] {
//			popupItems, customItems};
		popupItems.makeMenu(popup);
		this.setLayout(new BorderLayout());
		scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		statusBar.setText(" ");
		scrollPane.getViewport().add(label);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(statusBar, BorderLayout.SOUTH);
	}

	public State getXenaExternalState() {
		return state;
	}

	public void setXenaExternalState(State v) {
		state = v;
		v.set(this);
	}

	public class MyLabel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		ImageIcon image = new ImageIcon();

		double widthZoomFactor = 1.0F;

		double heightZoomFactor = 1.0F;

		public MyLabel() {
		}

		public void setZoomFactor(double w, double h) {
			setZoomFactor(w, h, null);
		}

		public void setZoomFactor(double w, double h, String mesg) {
			this.widthZoomFactor = w;
			this.heightZoomFactor = h;
			String s = "Zoom Factor: ";
			if (w == h) {
				s += widthZoomFactor * 100.0F + "%";
			} else {
				s += "Width: " + widthZoomFactor * 100.0F + "% Height: " + heightZoomFactor * 100.0F + "%";
			}
			if (mesg != null) {
				s += " " + mesg;
			}
			statusBar.setText(s);
			popupItems.sync();
			updateUI();
//			PngView.this.validate();
		}

		public void setIcon(ImageIcon image) {
			this.image = image;
		}

		public double getZoomFactor() {
			if (widthZoomFactor == heightZoomFactor) {
				return widthZoomFactor;
			} else {
				return -1.0F;
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(getZoomedImageWidth(), getZoomedImageHeight());
		}

		@Override
		public void paint(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image.getImage(), 0, 0, getZoomedImageWidth(), getZoomedImageHeight(), this);
		}

		int getZoomedImageWidth() {
			return (int)(image.getIconWidth() * widthZoomFactor);
		}

		int getZoomedImageHeight() {
			return (int)(image.getIconHeight() * heightZoomFactor);
		}

		int getImageWidth() {
			return image.getIconWidth();
		}

		int getImageHeight() {
			return image.getIconHeight();
		}
	}

	class MyMenu {
		/**
		 *  We seem to need a fudge Factor to make it fit exactly.
		 */

		public JRadioButtonMenuItem twentyFive = new JRadioButtonMenuItem("25%");

		public JRadioButtonMenuItem fifty = new JRadioButtonMenuItem("50%");

		public JRadioButtonMenuItem oneHundred = new JRadioButtonMenuItem("100%");

		public JRadioButtonMenuItem twoHundred = new JRadioButtonMenuItem("200%");

		public JRadioButtonMenuItem fourHundred = new JRadioButtonMenuItem("400%");

		public JRadioButtonMenuItem eightHundred = new JRadioButtonMenuItem("800%");

		public JRadioButtonMenuItem custom = new JRadioButtonMenuItem("Custom");

		public JMenu zoomMenu = new JMenu("Zoom");

		public JMenu fitMenu = new JMenu("Fit");

		public JRadioButtonMenuItem fit = new JRadioButtonMenuItem("Fit To Size");

		public JRadioButtonMenuItem fitWidth = new JRadioButtonMenuItem("Fit To Width");

		public JRadioButtonMenuItem fitHeight = new JRadioButtonMenuItem("Fit To Height");

		public JRadioButtonMenuItem fitAll = new JRadioButtonMenuItem("Fit To Both");

		MyMenu() {
			ButtonGroup group = new ButtonGroup();
			oneHundred.setSelected(true);
			group.add(twentyFive);
			group.add(fifty);
			group.add(oneHundred);
			group.add(twoHundred);
			group.add(fourHundred);
			group.add(eightHundred);
			group.add(custom);
			group.add(fit);
			group.add(fitWidth);
			group.add(fitHeight);
			group.add(fitAll);
			zoomMenu.add(twentyFive);
			zoomMenu.add(fifty);
			zoomMenu.add(oneHundred);
			zoomMenu.add(twoHundred);
			zoomMenu.add(fourHundred);
			zoomMenu.add(eightHundred);
			zoomMenu.add(custom);
			fitMenu.add(fit);
			fitMenu.add(fitWidth);
			fitMenu.add(fitHeight);
			fitMenu.add(fitAll);
			
			initListeners();
		}

		public void sync() {
			if (state.isFitToSize()) {
				fit.setSelected(true);
			} else if (state.isFitToWidth()) {
				fitWidth.setSelected(true);
			} else if (state.isFitToHeight()) {
				fitHeight.setSelected(true);
			} else if (state.isFitToBoth()) {
				fitAll.setSelected(true);
			} else if (state.getZoomFactor() == 0.25F) {
				twentyFive.setSelected(true);
			} else if (state.getZoomFactor() == 0.5F) {
				fifty.setSelected(true);
			} else if (state.getZoomFactor() == 1.0F) {
				oneHundred.setSelected(true);
			} else if (state.getZoomFactor() == 2.0F) {
				twoHundred.setSelected(true);
			} else if (state.getZoomFactor() == 4.0F) {
				fourHundred.setSelected(true);
			} else if (state.getZoomFactor() == 8.0F) {
				eightHundred.setSelected(true);
			} 
			else {
				custom.setSelected(true);
			}
		}

		public void makeMenu(Container component) {
			component.add(zoomMenu);
			component.add(fitMenu);
		}

		public void initListeners() {
			twentyFive.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setZoomFactor(0.25F);
					state.set(PngViewer.this);
//					label.setZoomFactor(0.25F, 0.25F);
				}
			});
			fifty.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setZoomFactor(0.5F);
					state.set(PngViewer.this);
				}
			});
			oneHundred.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setZoomFactor(1.0F);
					state.set(PngViewer.this);
				}
			});
			twoHundred.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setZoomFactor(2.0F);
					state.set(PngViewer.this);
				}
			});
			fourHundred.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setZoomFactor(4.0F);
					state.set(PngViewer.this);
				}
			});
			eightHundred.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setZoomFactor(8.0F);
					state.set(PngViewer.this);
				}
			});
			custom.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					double preValue = label.getZoomFactor();
					if (preValue < 0.0F) {
						preValue = 1.0F;
					}
					String pzoom = (String)
						JOptionPane.showInputDialog(null,
													"Zoom Percentage",
													"Zoom Percentage",
													JOptionPane.PLAIN_MESSAGE,
													null,
													null,
													Double.toString(preValue * 100));
					if (pzoom != null) {
						try {
							double zoom = Double.parseDouble(pzoom) / 100;
							state.setZoomFactor(zoom);
							state.set(PngViewer.this);
						} catch (Exception ex) {
							
						}
					}
				}
			});
			fit.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setFitToSize();
					state.set(PngViewer.this);
				}
			});
			fitWidth.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setFitToWidth();
					state.set(PngViewer.this);
				}
			});
			fitHeight.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setFitToHeight();
					state.set(PngViewer.this);
				}
			});
			fitAll.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.setFitToBoth();
					state.set(PngViewer.this);
				}
			});
		}
	}

	double getHeightFit() {
		return ((getHeight()) - PngViewer.FUDGE_FACTOR) / label.getImageHeight();
	}

	double getWidthFit() {
		return ((getWidth()) - PngViewer.FUDGE_FACTOR) / label.getImageWidth();
	}

	public State getState() {
		return state;
	}
}
