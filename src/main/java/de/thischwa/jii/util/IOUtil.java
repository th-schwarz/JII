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
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Static helper object for IO related issues.
 * 
 * @author Thilo Schwarz
 */
public class IOUtil {
	private static final int defaultBufferSize = 1024 * 4;

	/** Default character encoding. */
	public static final String defaultEncoding = "utf-8";
	
	private IOUtil() {
	}

	/**
	 * Unconditionally close the desired {@link Closeable}.
	 * 
	 * @param closeable
	 *            Can be <code>null</code>.
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable == null)
			return;
		try {
			closeable.close();
		} catch (IOException e) {
			// irrelevant
		}
	}
	/**
	 * Unconditionally close the desired {@link ImageReader}.
	 * 
	 * @param iReader
	 *            Can be <code>null</code>.
	 */
	public static void closeQuietly(ImageReader iReader) {
		if (iReader == null)
			return;
		try {
			iReader.dispose();
		} catch (Exception e) {
			// irrelevant
		}
	}
	
	/**
	 * Unconditionally close the desired {@link ImageInputStream}.
	 * 
	 * @param ii
	 *            Can be <code>null</code>.
	 */
	public static void closeQuietly(ImageInputStream ii) {
		if (ii == null)
			return;
		try {
			ii.close();
		} catch (IOException e) {
			// irrelevant
		}
	}

	/**
	 * Get the contents of an {@link InputStream} as a String using the default character encoding {@link #defaultEncoding}.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @return The requested String.
	 * @throws IOException
	 */
	public static String toString(InputStream in) throws IOException {
		return toString(in, defaultEncoding);
	}

	/**
	 * Get the contents of an {@link InputStream} as a String using the specified character encoding.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param encoding
	 *            The character encoding to use.
	 * @return The requested String.
	 * @throws IOException
	 */
	public static String toString(InputStream in, String encoding) throws IOException {
		char[] buf = new char[defaultBufferSize];
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));
		StringWriter strWriter = new StringWriter();
		BufferedWriter writer = new BufferedWriter(strWriter);
		int read = reader.read(buf);
		while (read != -1) {
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
	 * @param in
	 *            {@link InputStream} of the output.
	 * @return List of lines.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public static List<String> parseLineByLine(InputStream in) throws IOException {
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(isr);
		List<String> lines = new ArrayList<>();
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
