/*
 *  -------------------------------------------------------------------------------

	This code is licensed under the Apache License, Version 2.0 (the "License"); 
	You may not use this file except in compliance with the License. 

	You may obtain a copy of the License at 

	http://www.apache.org/licenses/LICENSE-2.0 

	Unless required by applicable law or agreed to in writing, software 
	distributed under the License is distributed on an "AS IS" BASIS, 
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
	See the License for the specific language governing permissions and 
	limitations under the License.
	
 *  -------------------------------------------------------------------------------
 */
package de.thischwa.jii.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Static helper object for IO related issues.
 *
 * @author Thilo Schwarz
 */
public class IOUtil {
    private static final int defaultBufferSize = 1024 * 4;
    private static final String defaultEncoding = "utf-8";
    
	public static void closeQuietly(Reader reader) {
		if(reader == null)
			return;
		try {
			reader.close();
		} catch (IOException e) {
			// irrelevant
		}
	}
	
	public static void closeQuietly(Writer writer) {
		if(writer == null)
			return;
		try {
			writer.close();
		} catch (IOException e) {
			// irrelevant
		}
	}
	
	public static String toString(InputStream in) throws IOException {
		return toString(in, defaultEncoding);
	}
	
	public static String toString(InputStream in, String encoding) throws IOException {
		char[] buf = new char[defaultBufferSize];
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));
		StringWriter strWriter = new StringWriter();
		BufferedWriter writer = new BufferedWriter(strWriter);
		int read = reader.read(buf);
		while(read != -1) {
			writer.write(buf);
			read = reader.read(buf);
		}
		writer.flush();
		closeQuietly(writer);
		return strWriter.toString();
	}

	/**
	 * Parse an InputStream line-by-line.
	 *  
	 * @param in {@link InputStream} of the output.
	 * @return List of lines.
	 * @throws IOException If an I/O error occurs.
	 */
	public static List<String> parseLineByLine(InputStream in) throws IOException {
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(isr);
		List<String> lines = new ArrayList<String>();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} finally {
			closeQuietly(reader);
			closeQuietly(isr);
		}
		return lines;
	}
}
