/**
 * This file is part of Xena.
 * 
 * Xena is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 * 
 * Xena is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Xena; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * 
 * @author Andrew Keeling
 * @author Dan Spasojevic
 * @author Justin Waddell
 */

package au.gov.naa.digipres.xena.kernel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.IllegalDataException;
import org.xml.sax.XMLReader;

/**
 * Utility methods related to XML serialization.
 */
public class ToXml {
	public static Element toXml(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof XmlSerializable) {
			return ((XmlSerializable) o).toXml();
		} else if (o instanceof XMLReader) {
			return toXmlObject(o);
		} else {
			return toXmlBasic(o);
		}
	}

	public static Element toXmlBasic(Object o) {
		Element basic = new Element("object");
		basic.setAttribute("type", o.getClass().getName());
		basic.setText(o.toString());
		return basic;
	}

	public static Object fromXmlBasic(Class cls, String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
	        InstantiationException {
		if (cls == Character.class) {
			Class[] types = {char.class};
			Constructor con = cls.getConstructor(types);
			Character[] args = {new Character(text.charAt(0))};
			return con.newInstance(args);
		} else {
			Class[] types = {String.class};
			Constructor con = cls.getConstructor(types);
			String[] args = {text};
			return con.newInstance(args);
		}
	}

	// public static Object fromXml(Element oe) {
	// if (oe == null) {
	// return null;
	// } else {
	// String type = oe.getAttributeValue("type");
	// Object rtn = null;
	// try {
	// Class cls = PluginManager.singleton().getDeserClassLoader().loadClass(type);
	// if (Reflect.conformsTo(cls, XmlSerializable.class)) {
	// rtn = (XmlSerializable)cls.newInstance();
	// ((XmlSerializable)rtn).fromXml(oe);
	// } else if (Reflect.conformsTo(cls, XMLReader.class)) {
	// fromXmlObject(rtn = cls.newInstance(), oe);
	// } else {
	// rtn = fromXmlBasic(cls, oe.getText());
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// return rtn;
	// }
	// }

	public static Element toXmlFile(String systemid, Element objs) throws FileNotFoundException, IOException {
		Element rtn = new Element("normalisation");
		rtn.addContent(objs);
		rtn.setAttribute("url", systemid);
		return rtn;
	}

	public static Element toXmlObject(Object obj) {
		Class cls = obj.getClass();
		Element rtn = new Element("object");
		rtn.setAttribute(new Attribute("type", cls.getName()));

		Field[] fields = cls.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			if ((f.getModifiers() & (Modifier.FINAL | Modifier.STATIC)) == 0) {
				try {
					Element fe = new Element("attribute");
					fe.setAttribute(new Attribute("name", f.getName()));
					Element sub = toXml(f.get(obj));
					if (sub != null) {
						fe.addContent(sub);
					}

					rtn.addContent(fe);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalDataException e) {
					e.printStackTrace();
				}
			}
		}
		return rtn;
	}

	public static Element fromXmlFile(Element element) {
		Element object = element.getChild("object");
		return object;
	}

	// public static void fromXmlObject(Object obj, Element element) {
	// Class cls = obj.getClass();
	// Field[] fields = cls.getFields();
	// Map fieldMap = new HashMap();
	// for (int i = 0; i < fields.length; i++) {
	// Field field = fields[i];
	// fieldMap.put(field.getName(), field);
	// }
	//        
	// List children = element.getChildren("attribute");
	// Iterator it = children.iterator();
	// while (it.hasNext()) {
	// Element fieldElement = (Element)it.next();
	// String name = fieldElement.getAttributeValue("name");
	// Field field = (Field)fieldMap.get(name);
	// try {
	// Element oe = fieldElement.getChild("object");
	// if (field != null) {
	// field.set(obj, fromXml(oe));
	// }
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (IllegalDataException e) {
	// e.printStackTrace();
	// }
	// }
	// }
}
