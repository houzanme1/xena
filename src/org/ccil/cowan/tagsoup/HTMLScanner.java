// This file is part of TagSoup.
// 
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.  You may also distribute
// and/or modify it under version 2.1 of the Academic Free License.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
// 
// 
package org.ccil.cowan.tagsoup;
import java.io.*;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;

/**
This class implements a table-driven scanner for HTML, allowing for lots of
defects.  It implements the Scanner interface, which accepts a Reader
object to fetch characters from and a ScanHandler object to report lexical
events to.
*/

public class HTMLScanner implements Scanner, Locator {

	private static final int MAX_BYTE_ORDER_MARK_SIZE = 3;
	
	// Start of state table
		private static final int S_ANAME = 1;
	private static final int S_APOS = 2;
	private static final int S_AVAL = 3;
	private static final int S_BB = 4;
	private static final int S_BBC = 5;
	private static final int S_BBCD = 6;
	private static final int S_BBCDA = 7;
	private static final int S_BBCDAT = 8;
	private static final int S_BBCDATA = 9;
	private static final int S_CCRLF = 10;
	private static final int S_CDATA = 11;
	private static final int S_CDATA2 = 12;
	private static final int S_CDSECT = 13;
	private static final int S_CDSECT1 = 14;
	private static final int S_CDSECT2 = 15;
	private static final int S_COM = 16;
	private static final int S_COM2 = 17;
	private static final int S_COM3 = 18;
	private static final int S_COM4 = 19;
	private static final int S_COMCRLF = 20;
	private static final int S_CRLF = 21;
	private static final int S_DECL = 22;
	private static final int S_DECL2 = 23;
	private static final int S_DONE = 24;
	private static final int S_EMPTYTAG = 25;
	private static final int S_ENT = 26;
	private static final int S_EQ = 27;
	private static final int S_ETAG = 28;
	private static final int S_GI = 29;
	private static final int S_NCR = 30;
	private static final int S_PCDATA = 31;
	private static final int S_PI = 32;
	private static final int S_PICRLF = 33;
	private static final int S_PITARGET = 34;
	private static final int S_QUOT = 35;
	private static final int S_STAGC = 36;
	private static final int S_TAG = 37;
	private static final int S_TAGWS = 38;
	private static final int S_XNCR = 39;
	private static final int A_ADUP = 1;
	private static final int A_ADUP_SAVE = 2;
	private static final int A_ADUP_STAGC = 3;
	private static final int A_ANAME = 4;
	private static final int A_ANAME_ADUP = 5;
	private static final int A_ANAME_ADUP_STAGC = 6;
	private static final int A_AVAL = 7;
	private static final int A_AVAL_STAGC = 8;
	private static final int A_CDATA = 9;
	private static final int A_CMNT = 10;
	private static final int A_DECL = 11;
	private static final int A_EMPTYTAG = 12;
	private static final int A_ENTITY = 13;
	private static final int A_ETAG = 14;
	private static final int A_GI = 15;
	private static final int A_GI_STAGC = 16;
	private static final int A_LF = 17;
	private static final int A_LT = 18;
	private static final int A_LT_PCDATA = 19;
	private static final int A_MINUS = 20;
	private static final int A_MINUS2 = 21;
	private static final int A_MINUS3 = 22;
	private static final int A_PCDATA = 23;
	private static final int A_PCDATA_SAVE_PUSH = 24;
	private static final int A_PI = 25;
	private static final int A_PITARGET = 26;
	private static final int A_PITARGET_PI = 27;
	private static final int A_SAVE = 28;
	private static final int A_SAVE_PUSH = 29;
	private static final int A_SKIP = 30;
	private static final int A_SP = 31;
	private static final int A_STAGC = 32;
	private static final int A_UNGET = 33;
	private static final int A_UNSAVE_PCDATA = 34;
	private static int[] statetable = {
		S_ANAME, '/', A_ANAME_ADUP, S_EMPTYTAG,
		S_ANAME, '=', A_ANAME, S_AVAL,
		S_ANAME, '>', A_ANAME_ADUP_STAGC, S_PCDATA,
		S_ANAME, 0, A_SAVE, S_ANAME,
		S_ANAME, -1, A_ANAME_ADUP_STAGC, S_DONE,
		S_ANAME, ' ', A_ANAME, S_EQ,
		S_ANAME, '\r', A_ANAME, S_EQ,
		S_ANAME, '\n', A_ANAME, S_EQ,
		S_ANAME, '\t', A_ANAME, S_EQ,
		S_APOS, '&', A_SAVE_PUSH, S_ENT,
		S_APOS, '\'', A_AVAL, S_TAGWS,
		S_APOS, 0, A_SAVE, S_APOS,
		S_APOS, -1, A_AVAL_STAGC, S_DONE,
		S_APOS, ' ', A_SP, S_APOS,
		S_APOS, '\r', A_SP, S_APOS,
		S_APOS, '\n', A_SP, S_APOS,
		S_APOS, '\t', A_SP, S_APOS,
		S_AVAL, '"', A_SKIP, S_QUOT,
		S_AVAL, '\'', A_SKIP, S_APOS,
		S_AVAL, '>', A_AVAL_STAGC, S_PCDATA,
		S_AVAL, 0, A_SAVE, S_STAGC,
		S_AVAL, -1, A_AVAL_STAGC, S_DONE,
		S_AVAL, ' ', A_SKIP, S_AVAL,
		S_AVAL, '\r', A_SKIP, S_AVAL,
		S_AVAL, '\n', A_SKIP, S_AVAL,
		S_AVAL, '\t', A_SKIP, S_AVAL,
		S_BB, 'C', A_SKIP, S_BBC,
		S_BB, 0, A_SKIP, S_DECL,
		S_BB, -1, A_SKIP, S_DONE,
		S_BBC, 'D', A_SKIP, S_BBCD,
		S_BBC, 0, A_SKIP, S_DECL,
		S_BBC, -1, A_SKIP, S_DONE,
		S_BBCD, 'A', A_SKIP, S_BBCDA,
		S_BBCD, 0, A_SKIP, S_DECL,
		S_BBCD, -1, A_SKIP, S_DONE,
		S_BBCDA, 'T', A_SKIP, S_BBCDAT,
		S_BBCDA, 0, A_SKIP, S_DECL,
		S_BBCDA, -1, A_SKIP, S_DONE,
		S_BBCDAT, 'A', A_SKIP, S_BBCDATA,
		S_BBCDAT, 0, A_SKIP, S_DECL,
		S_BBCDAT, -1, A_SKIP, S_DONE,
		S_BBCDATA, '[', A_SKIP, S_CDSECT,
		S_BBCDATA, 0, A_SKIP, S_DECL,
		S_BBCDATA, -1, A_SKIP, S_DONE,
		S_CCRLF, 0, A_UNGET, S_CDATA,
		S_CCRLF, -1, A_SKIP, S_DONE,
		S_CCRLF, '\n', A_SKIP, S_CDATA,
		S_CDATA, '<', A_SAVE, S_CDATA2,
		S_CDATA, '\r', A_LF, S_CCRLF,
		S_CDATA, 0, A_SAVE, S_CDATA,
		S_CDATA, -1, A_PCDATA, S_DONE,
		S_CDATA2, '/', A_UNSAVE_PCDATA, S_ETAG,
		S_CDATA2, 0, A_SAVE, S_CDATA,
		S_CDATA2, -1, A_UNSAVE_PCDATA, S_DONE,
		S_CDSECT, ']', A_SAVE, S_CDSECT1,
		S_CDSECT, 0, A_SAVE, S_CDSECT,
		S_CDSECT, -1, A_SKIP, S_DONE,
		S_CDSECT1, ']', A_SAVE, S_CDSECT2,
		S_CDSECT1, 0, A_SAVE, S_CDSECT,
		S_CDSECT1, -1, A_SKIP, S_DONE,
		S_CDSECT2, '>', A_CDATA, S_PCDATA,
		S_CDSECT2, 0, A_SAVE, S_CDSECT,
		S_CDSECT2, -1, A_SKIP, S_DONE,
		S_COM, '-', A_SKIP, S_COM2,
		S_COM, '\r', A_LF, S_COMCRLF,
		S_COM, 0, A_SAVE, S_COM2,
		S_COM, -1, A_CMNT, S_DONE,
		S_COM2, '-', A_SKIP, S_COM3,
		S_COM2, '\r', A_LF, S_COMCRLF,
		S_COM2, 0, A_SAVE, S_COM2,
		S_COM2, -1, A_CMNT, S_DONE,
		S_COM3, '-', A_SKIP, S_COM4,
		S_COM3, '\r', A_LF, S_COMCRLF,
		S_COM3, 0, A_MINUS, S_COM2,
		S_COM3, -1, A_CMNT, S_DONE,
		S_COM4, '-', A_MINUS3, S_COM4,
		S_COM4, '>', A_CMNT, S_PCDATA,
		S_COM4, '\r', A_LF, S_COMCRLF,
		S_COM4, 0, A_MINUS2, S_COM2,
		S_COM4, -1, A_CMNT, S_DONE,
		S_COMCRLF, 0, A_UNGET, S_COM,
		S_COMCRLF, -1, A_CMNT, S_DONE,
		S_COMCRLF, '\n', A_SKIP, S_COM,
		S_CRLF, 0, A_UNGET, S_PCDATA,
		S_CRLF, -1, A_SKIP, S_DONE,
		S_CRLF, '\n', A_SKIP, S_PCDATA,
		S_DECL, '-', A_SKIP, S_COM,
		S_DECL, '>', A_SKIP, S_PCDATA,
		S_DECL, '[', A_SKIP, S_BB,
		S_DECL, 0, A_SAVE, S_DECL2,
		S_DECL, -1, A_SKIP, S_DONE,
		S_DECL2, '>', A_DECL, S_PCDATA,
		S_DECL2, 0, A_SAVE, S_DECL2,
		S_DECL2, -1, A_SKIP, S_DONE,
		S_EMPTYTAG, '>', A_EMPTYTAG, S_PCDATA,
		S_EMPTYTAG, 0, A_SAVE, S_ANAME,
		S_EMPTYTAG, ' ', A_SKIP, S_TAGWS,
		S_EMPTYTAG, '\r', A_SKIP, S_TAGWS,
		S_EMPTYTAG, '\n', A_SKIP, S_TAGWS,
		S_EMPTYTAG, '\t', A_SKIP, S_TAGWS,
		S_ENT, 0, A_ENTITY, S_ENT,
		S_ENT, -1, A_ENTITY, S_DONE,
		S_EQ, '=', A_SKIP, S_AVAL,
		S_EQ, '>', A_ADUP_STAGC, S_PCDATA,
		S_EQ, 0, A_ADUP_SAVE, S_ANAME,
		S_EQ, -1, A_ADUP_STAGC, S_DONE,
		S_EQ, ' ', A_SKIP, S_EQ,
		S_EQ, '\r', A_SKIP, S_EQ,
		S_EQ, '\n', A_SKIP, S_EQ,
		S_EQ, '\t', A_SKIP, S_EQ,
		S_ETAG, '>', A_ETAG, S_PCDATA,
		S_ETAG, 0, A_SAVE, S_ETAG,
		S_ETAG, -1, A_ETAG, S_DONE,
		S_ETAG, ' ', A_SKIP, S_ETAG,
		S_ETAG, '\r', A_SKIP, S_ETAG,
		S_ETAG, '\n', A_SKIP, S_ETAG,
		S_ETAG, '\t', A_SKIP, S_ETAG,
		S_GI, '/', A_SKIP, S_EMPTYTAG,
		S_GI, '>', A_GI_STAGC, S_PCDATA,
		S_GI, 0, A_SAVE, S_GI,
		S_GI, -1, A_SKIP, S_DONE,
		S_GI, ' ', A_GI, S_TAGWS,
		S_GI, '\r', A_GI, S_TAGWS,
		S_GI, '\n', A_GI, S_TAGWS,
		S_GI, '\t', A_GI, S_TAGWS,
		S_NCR, 0, A_ENTITY, S_NCR,
		S_NCR, -1, A_ENTITY, S_DONE,
		S_PCDATA, '&', A_PCDATA_SAVE_PUSH, S_ENT,
		S_PCDATA, '<', A_PCDATA, S_TAG,
		S_PCDATA, '\r', A_LF, S_CRLF,
		S_PCDATA, 0, A_SAVE, S_PCDATA,
		S_PCDATA, -1, A_PCDATA, S_DONE,
		S_PI, '>', A_PI, S_PCDATA,
		S_PI, '\r', A_LF, S_PICRLF,
		S_PI, 0, A_SAVE, S_PI,
		S_PI, -1, A_PI, S_DONE,
		S_PICRLF, 0, A_UNGET, S_PI,
		S_PICRLF, -1, A_PI, S_DONE,
		S_PICRLF, '\n', A_SKIP, S_PI,
		S_PITARGET, '>', A_PITARGET_PI, S_PCDATA,
		S_PITARGET, 0, A_SAVE, S_PITARGET,
		S_PITARGET, -1, A_PITARGET_PI, S_DONE,
		S_PITARGET, ' ', A_PITARGET, S_PI,
		S_PITARGET, '\r', A_PITARGET, S_PI,
		S_PITARGET, '\n', A_PITARGET, S_PI,
		S_PITARGET, '\t', A_PITARGET, S_PI,
		S_QUOT, '"', A_AVAL, S_TAGWS,
		S_QUOT, '&', A_SAVE_PUSH, S_ENT,
		S_QUOT, 0, A_SAVE, S_QUOT,
		S_QUOT, -1, A_AVAL_STAGC, S_DONE,
		S_QUOT, ' ', A_SP, S_QUOT,
		S_QUOT, '\r', A_SP, S_QUOT,
		S_QUOT, '\n', A_SP, S_QUOT,
		S_QUOT, '\t', A_SP, S_QUOT,
		S_STAGC, '>', A_AVAL_STAGC, S_PCDATA,
		S_STAGC, 0, A_SAVE, S_STAGC,
		S_STAGC, -1, A_AVAL_STAGC, S_DONE,
		S_STAGC, ' ', A_AVAL, S_TAGWS,
		S_STAGC, '\r', A_AVAL, S_TAGWS,
		S_STAGC, '\n', A_AVAL, S_TAGWS,
		S_STAGC, '\t', A_AVAL, S_TAGWS,
		S_TAG, '!', A_SKIP, S_DECL,
		S_TAG, '/', A_SKIP, S_ETAG,
		S_TAG, '?', A_SKIP, S_PITARGET,
		S_TAG, 0, A_SAVE, S_GI,
		S_TAG, -1, A_LT_PCDATA, S_DONE,
		S_TAG, ' ', A_LT, S_PCDATA,
		S_TAG, '\r', A_LT, S_PCDATA,
		S_TAG, '\n', A_LT, S_PCDATA,
		S_TAG, '\t', A_LT, S_PCDATA,
		S_TAGWS, '/', A_SKIP, S_EMPTYTAG,
		S_TAGWS, '>', A_STAGC, S_PCDATA,
		S_TAGWS, 0, A_SAVE, S_ANAME,
		S_TAGWS, -1, A_STAGC, S_DONE,
		S_TAGWS, ' ', A_SKIP, S_TAGWS,
		S_TAGWS, '\r', A_SKIP, S_TAGWS,
		S_TAGWS, '\n', A_SKIP, S_TAGWS,
		S_TAGWS, '\t', A_SKIP, S_TAGWS,
		S_XNCR, 0, A_ENTITY, S_XNCR,
		S_XNCR, -1, A_ENTITY, S_DONE,

	};
	private static final String[] debug_actionnames = { "", "A_ADUP", "A_ADUP_SAVE", "A_ADUP_STAGC", "A_ANAME", "A_ANAME_ADUP", "A_ANAME_ADUP_STAGC", "A_AVAL", "A_AVAL_STAGC", "A_CDATA", "A_CMNT", "A_DECL", "A_EMPTYTAG", "A_ENTITY", "A_ETAG", "A_GI", "A_GI_STAGC", "A_LF", "A_LT", "A_LT_PCDATA", "A_MINUS", "A_MINUS2", "A_MINUS3", "A_PCDATA", "A_PCDATA_SAVE_PUSH", "A_PI", "A_PITARGET", "A_PITARGET_PI", "A_SAVE", "A_SAVE_PUSH", "A_SKIP", "A_SP", "A_STAGC", "A_UNGET", "A_UNSAVE_PCDATA"};
	private static final String[] debug_statenames = { "", "S_ANAME", "S_APOS", "S_AVAL", "S_BB", "S_BBC", "S_BBCD", "S_BBCDA", "S_BBCDAT", "S_BBCDATA", "S_CCRLF", "S_CDATA", "S_CDATA2", "S_CDSECT", "S_CDSECT1", "S_CDSECT2", "S_COM", "S_COM2", "S_COM3", "S_COM4", "S_COMCRLF", "S_CRLF", "S_DECL", "S_DECL2", "S_DONE", "S_EMPTYTAG", "S_ENT", "S_EQ", "S_ETAG", "S_GI", "S_NCR", "S_PCDATA", "S_PI", "S_PICRLF", "S_PITARGET", "S_QUOT", "S_STAGC", "S_TAG", "S_TAGWS", "S_XNCR"};


	// End of state table

	private String thePublicid;			// Locator state
	private String theSystemid;
	private int theLastLine;
	private int theLastColumn;
	private int theCurrentLine;
	private int theCurrentColumn;

	int theState;					// Current state
	int theNextState;				// Next state
	char[] theOutputBuffer = new char[200];	// Output buffer
	int theSize;					// Current buffer size
	int[] theWinMap = {				// Windows chars map
		0x20AC, 0xFFFD, 0x201A, 0x0192, 0x201E, 0x2026, 0x2020, 0x2021,
		0x02C6, 0x2030, 0x0160, 0x2039, 0x0152, 0xFFFD, 0x017D, 0xFFFD,
		0xFFFD, 0x2018, 0x2019, 0x201C, 0x201D, 0x2022, 0x2013, 0x2014,
		0x02DC, 0x2122, 0x0161, 0x203A, 0x0153, 0xFFFD, 0x017E, 0x0178};

	// Compensate for bug in PushbackReader that allows
	// pushing back EOF.
	private void unread(PushbackReader r, int c) throws IOException {
		if (c != -1) r.unread(c);
		}

	// Locator implementation

	public int getLineNumber() {
		return theLastLine;
		}
	public int getColumnNumber() {
		return theLastColumn;
		}
	public String getPublicId() {
		return thePublicid;
		}
	public String getSystemId() {
		return theSystemid;
		}


	// Scanner implementation

	/**
	Reset document locator, supplying systemid and publicid.
	@param systemid System id
	@param publicid Public id
	*/

	public void resetDocumentLocator(String publicid, String systemid) {
		thePublicid = publicid;
		theSystemid = systemid;
		theLastLine = theLastColumn = theCurrentLine = theCurrentColumn = 0;
		}

	/**
	Scan HTML source, reporting lexical events.
	@param r0 Reader that provides characters
	@param h ScanHandler that accepts lexical events.
	*/

	public void scan(Reader r0, ScanHandler h) throws IOException, SAXException {
		theState = S_PCDATA;
		int savedState = 0;
		int savedSize = 0;
		PushbackReader r;
		if (r0 instanceof PushbackReader) {
			r = (PushbackReader)r0;
			}
		else if (r0 instanceof BufferedReader) {
			r = new PushbackReader(r0, MAX_BYTE_ORDER_MARK_SIZE);
			}
		else {
			r = new PushbackReader(new BufferedReader(r0), MAX_BYTE_ORDER_MARK_SIZE);
			}

		// Remove any leading UTF-16 Byte Order Mark
		int firstChar = r.read();	
		if (firstChar != '\uFEFF' && firstChar != '\uFFFE' && firstChar != -1) r.unread(firstChar);
		
		// Remove any leading UTF-8 Byte Order Mark
		char[] utf8BOM = {0xEF, 0xBB, 0xBF};
		char[] readArr = new char[MAX_BYTE_ORDER_MARK_SIZE];
		int charsRead = r.read(readArr);
		if (charsRead != MAX_BYTE_ORDER_MARK_SIZE || utf8BOM[0] != readArr[0] || utf8BOM[1] != readArr[1] || utf8BOM[2] != readArr[2])
		{
			r.unread(readArr);
		}

		while (theState != S_DONE) {
			int ch = r.read();
			if (ch >= 0x80 && ch <= 0x9F) ch = theWinMap[ch-0x80];
			if (ch == '\n') {
				theCurrentLine++;
				theCurrentColumn = 0;
				}
			else {
				theCurrentColumn++;
				}
			if (ch < 0x20 && ch != '\n' && ch != '\r' && ch != '\t' && ch != -1) continue;
			// Search state table
			int action = 0;
			for (int i = 0; i < statetable.length; i += 4) {
				if (theState != statetable[i]) {
					if (action != 0) break;
					continue;
					}
				if (statetable[i+1] == 0) {
					action = statetable[i+2];
					theNextState = statetable[i+3];
					}
				else if (statetable[i+1] == ch) {
					action = statetable[i+2];
					theNextState = statetable[i+3];
					break;
					}
				}
//			System.err.println("In " + debug_statenames[theState] + " got " + nicechar(ch) + " doing " + debug_actionnames[action] + " then " + debug_statenames[theNextState]);
			switch (action) {
			case 0:
				throw new Error(
"HTMLScanner can't cope with " + Integer.toString(ch) + " in state " +
Integer.toString(theState));
        		case A_ADUP:
				h.adup(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
        		case A_ADUP_SAVE:
				h.adup(theOutputBuffer, 0, theSize);
				theSize = 0;
				save(ch, h);
				break;
        		case A_ADUP_STAGC:
				h.adup(theOutputBuffer, 0, theSize);
				theSize = 0;
				h.stagc(theOutputBuffer, 0, theSize);
				break;
        		case A_ANAME:
				h.aname(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
        		case A_ANAME_ADUP:
				h.aname(theOutputBuffer, 0, theSize);
				theSize = 0;
				h.adup(theOutputBuffer, 0, theSize);
				break;
        		case A_ANAME_ADUP_STAGC:
				h.aname(theOutputBuffer, 0, theSize);
				theSize = 0;
				h.adup(theOutputBuffer, 0, theSize);
				h.stagc(theOutputBuffer, 0, theSize);
				break;
        		case A_AVAL:
				h.aval(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
        		case A_AVAL_STAGC:
				h.aval(theOutputBuffer, 0, theSize);
				theSize = 0;
				h.stagc(theOutputBuffer, 0, theSize);
				break;
			case A_CDATA:
				mark();
				// suppress the final "]]" in the buffer
				if (theSize > 1) theSize -= 2;
				h.pcdata(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
			case A_ENTITY:
				mark();
				char ch1 = (char)ch;
//				System.out.println("Got " + ch1 + " in state " + ((theState == S_ENT) ? "S_ENT" : ((theState == S_NCR) ? "S_NCR" : "UNK")));
				if (theState == S_ENT && ch1 == '#') {
					theNextState = S_NCR;
					save(ch, h);
					break;
					}
				else if (theState == S_NCR && (ch1 == 'x' || ch1 == 'X')) {
					theNextState = S_XNCR;
					save(ch, h);
					break;
					}
				else if (theState == S_ENT && Character.isLetterOrDigit(ch1)) {
					save(ch, h);
					break;
					}
				else if (theState == S_NCR && Character.isDigit(ch1)) {
					save(ch, h);
					break;
					}
				else if (theState == S_XNCR && (Character.isDigit(ch1) || "abcdefABCDEF".indexOf(ch1) != -1)) {
					save(ch, h);
					break;
					}

//				System.err.println("%%" + new String(theOutputBuffer, 0, theSize));
				h.entity(theOutputBuffer, savedSize + 1, theSize - savedSize - 1);
				int ent = h.getEntity();
//				System.err.println("%% value = " + ent);
				if (ent != 0) {
					theSize = savedSize;
					if (ent >= 0x80 && ent <= 0x9F) {
						ent = theWinMap[ent-0x80];
						}
					if (ent < 0x20) ent = 0x20;
					if (ent < 0x10000) {
						save(ent, h);
						}
					else {
						ent -= 0x10000;
						save((ent>>10) + 0xD800, h);
						save((ent&0x3FF) + 0xDC00, h);
						}
					if (ch != ';') {
						unread(r, ch);
						theCurrentColumn--;
						}
					}
				else {
					unread(r, ch);
					theCurrentColumn--;
					}
				theNextState = savedState;
				break;
        		case A_ETAG:
				h.etag(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
        		case A_DECL:
				h.decl(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
        		case A_GI:
				h.gi(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
			case A_GI_STAGC:
				h.gi(theOutputBuffer, 0, theSize);
				theSize = 0;
				h.stagc(theOutputBuffer, 0, theSize);
				break;
        		case A_LF:
				save('\n', h);
				break;
        		case A_LT:
				mark();
				save('<', h);
				break;
			case A_LT_PCDATA:
				mark();
				save('<', h);
				h.pcdata(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
        		case A_PCDATA:
				mark();
				h.pcdata(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
			case A_CMNT:
				mark();
				h.cmnt(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
			case A_MINUS3:
				save('-', h);
				save(' ', h);
				break;
			case A_MINUS2:
				save('-', h);
				save(' ', h);
				// fall through into A_MINUS
			case A_MINUS:
				save('-', h);
				save(ch, h);
				break;
        		case A_PI:
				mark();
				h.pi(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
        		case A_PITARGET:
				h.pitarget(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
        		case A_PITARGET_PI:
				h.pitarget(theOutputBuffer, 0, theSize);
				theSize = 0;
				h.pi(theOutputBuffer, 0, theSize);
				break;
			case A_PCDATA_SAVE_PUSH:
				h.pcdata(theOutputBuffer, 0, theSize);
				theSize = 0;
				// fall through into A_SAVE_PUSH
        		case A_SAVE_PUSH:
				savedState = theState;
				savedSize = theSize;
				// fall through into A_SAVE
        		case A_SAVE:
				save(ch, h);
				break;
        		case A_SKIP:
				break;
        		case A_SP:
				save(' ', h);
				break;
        		case A_STAGC:
				h.stagc(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
			case A_EMPTYTAG:
				mark();
//				System.err.println("%%% Empty tag seen");
				if (theSize > 0) h.gi(theOutputBuffer, 0, theSize);
				theSize = 0;
				h.stage(theOutputBuffer, 0, theSize);
				break;
			case A_UNGET:
				unread(r, ch);
				theCurrentColumn--;
				break;
        		case A_UNSAVE_PCDATA:
				if (theSize > 0) theSize--;
				h.pcdata(theOutputBuffer, 0, theSize);
				theSize = 0;
				break;
			default:
				throw new Error("Can't process state " + action);
				}
			theState = theNextState;
			}
		h.eof(theOutputBuffer, 0, 0);
		}

	/**
	* Mark the current scan position as a "point of interest" - start of a tag,
	* cdata, processing instruction etc.
	*/

	private void mark() {
		theLastColumn = theCurrentColumn;
		theLastLine = theCurrentLine;
		}

	/**
	A callback for the ScanHandler that allows it to force
	the lexer state to CDATA content (no markup is recognized except
	the end of element.
	*/

	public void startCDATA() { theNextState = S_CDATA; }

	private void save(int ch, ScanHandler h) throws IOException, SAXException {
		if (theSize >= theOutputBuffer.length - 20) {
			if (theState == S_PCDATA || theState == S_CDATA) {
				// Return a buffer-sized chunk of PCDATA
				h.pcdata(theOutputBuffer, 0, theSize);
				theSize = 0;
				}
			else {
				// Grow the buffer size
				char[] newOutputBuffer = new char[theOutputBuffer.length * 2];
                                System.arraycopy(theOutputBuffer, 0, newOutputBuffer, 0, theSize+1);
				theOutputBuffer = newOutputBuffer;
				}
			}
		theOutputBuffer[theSize++] = (char)ch;
		}
	
	/**
	 * Test procedure. Reads HTML from the standard input and writes PYX to the
	 * standard output.
	 */

	public static void main(String[] argv) throws IOException, SAXException {
		Scanner s = new HTMLScanner();
		Reader r = new InputStreamReader(System.in, "UTF-8");
		Writer w = new OutputStreamWriter(System.out, "UTF-8");
		PYXWriter pw = new PYXWriter(w);
		s.scan(r, pw);
		w.close();
		}


        private static final String nicechar(int in) {
            if (in=='\n') {
                return "\n";
            } else if (in=='\r') {
                return "\r";
            } else if (in < 32) {
                return "0x"+Integer.toHexString(in);
            } else {
                return "'"+((char)in)+"'";
            }
        }

	}
