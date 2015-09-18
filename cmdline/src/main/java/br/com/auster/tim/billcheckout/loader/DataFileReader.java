/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on 05/03/2010
 */
package br.com.auster.tim.billcheckout.loader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import br.com.auster.common.io.NIOBufferUtils;


/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class DataFileReader {

	
	private static final Logger logger = Logger.getLogger(DataFileReader.class);
	

	private static final char SEPARATOR_CHAR = StringEscapeUtils.unescapeJava(";").charAt(0);
	private static final char EACHLINE_CHAR = StringEscapeUtils.unescapeJava(System.getProperty("line.separator")).charAt(0);
	private static final int EACHLINE_SKIP_LEN = System.getProperty("line.separator").length();

	private ReadableByteChannel incomingChannel;	
	private CharsetDecoder decoder; 
	private BlockingQueue<RowRead> outQueue;
	
	private int lineCount;
	
	
	public DataFileReader(ReadableByteChannel _ic) {
		this.incomingChannel = _ic;
		// building decoder
		Charset latin1 = Charset.forName( "ISO-8859-1" );
		this.decoder = latin1.newDecoder();
	}
	
	public int getLineCount() { return this.lineCount; }
	
	public void setOutputQueue(BlockingQueue<RowRead> _outQueue) {
		this.outQueue = _outQueue;
	}
	
	public void readFile() throws IOException, InterruptedException {
		
		this.lineCount=0;
		ByteBuffer bb = ByteBuffer.allocate(1024);
		String reaminingFromLastLine = null;
		int readBytes = this.incomingChannel.read(bb);
		do {
			bb.flip();			
			CharBuffer cb = this.decoder.decode(bb);
			// handling each line
			int len = NIOBufferUtils.findToken(cb, EACHLINE_CHAR);
			do {
				reaminingFromLastLine = processLine(cb, len, reaminingFromLastLine);
				// skipping 'each-line' char
				for (int i=0; i < EACHLINE_SKIP_LEN; i++) { cb.get(); }
				len = NIOBufferUtils.findToken(cb, EACHLINE_CHAR) - cb.position();
			} while (len > 0);
			
			// reading next sequence of chars
			bb.clear();
			readBytes = this.incomingChannel.read(bb);
			
			// reading last line, when there is no 'each-line' char and we reached the end of the file
			if (cb.position() < cb.limit()) {
				len = cb.limit()-cb.position();
				if (readBytes <= 0) {
					reaminingFromLastLine = processLine(cb, len, reaminingFromLastLine);
				} else {
					char[] restOfLine = new char[len];
					cb.get(restOfLine);
					reaminingFromLastLine = String.valueOf(restOfLine);
				}
			}
		} while (readBytes > 0);
		logger.debug("Read " + this.lineCount + " from this file.");
	}
	
	protected String processLine(CharBuffer _cb, int _len, String _reaminingFromLastLine) throws InterruptedException {
		char[] readChars = new char[_len];
		_cb.get(readChars);
		this.lineCount++;
		logger.debug("Another line was read");
		// splitting line into columns
		StringBuffer tmp = new StringBuffer();
		if (_reaminingFromLastLine != null) {
			tmp.append(_reaminingFromLastLine);
		}
		tmp.append(readChars);
		// sending values over the queue
		this.outQueue.put( splitChars(tmp.toString().toCharArray()) );
		// done
		return null;
	}
	
	protected RowRead splitChars(char[] _readChars) {
		LinkedList<String> tempList = new LinkedList<String>();
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < _readChars.length; i++) {
			if (_readChars[i] == SEPARATOR_CHAR) {
				tempList.add(sb.toString());
				sb = new StringBuffer();
				continue;
			}
			sb.append(_readChars[i]);
		}
		// last element, if no trailing ';' is found
		if (sb.length() > 0) { tempList.add(sb.toString()); }
		// returning columns in form of array
		
		RowRead rr = new RowRead();
		rr.columns = tempList.toArray(new String[] {});
		rr.rowNumber = this.lineCount;
		return rr;
//		return tempList.toArray(new String[] {});
	}
}
