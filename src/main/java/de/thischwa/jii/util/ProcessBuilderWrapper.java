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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * A wrapper for the {@link ProcessBuilder} that reads all relevant streams which can cause an 'hanging' {@link Process}. The
 * read data of the streams is provided as strings, see {@link #getInfos()} and {@link #getErrors()}.<br/>
 * For more informations see: http://thilosdevblog.wordpress.com/2011/11/21/proper-handling-of-the-processbuilder/ <br/>
 * <br/>
 * Usage:
 * <pre>
 * List<String> cmd = new ArrayList<String>();
 * cmd.add("ls");
 * cmd.add("-al");
 * ProcessBuilderWrapper pbd = new ProcessBuilderWrapper(new File("/tmp"), cmd);
 * System.out.println("Command has terminated with status: " + pbd.getStatus());
 * System.out.println("Output:\n" + pbd.getInfos());
 * System.out.println("Error: " + pbd.getErrors());
 * </pre>
 */
public class ProcessBuilderWrapper {
	private StringWriter infos;
	private StringWriter errors;
	private int status;
	
	public ProcessBuilderWrapper(File directory, List<String> command) throws Exception {
		infos = new StringWriter();
		errors = new StringWriter();
		ProcessBuilder pb = new ProcessBuilder(command);      
		if(directory != null)
			pb.directory(directory);
		Process process = pb.start();
		StreamBoozer seInfo = new StreamBoozer(process.getInputStream(), new PrintWriter(infos, true));
		StreamBoozer seError = new StreamBoozer(process.getErrorStream(), new PrintWriter(errors, true));
		seInfo.start();
		seError.start();
		status = process.waitFor();		
	}

	public ProcessBuilderWrapper(List<String> command) throws Exception {
		this(null, command);
	}
	
	/**
	 * Get the content of the errorstream of the underlying process. Line separator is the system property <tt>line.separator</tt>.
	 * 
	 * @return Errors as String or an empty String if there are no errors.
	 */
	public String getErrors() {
		return errors.toString();
	}
	
	/**
	 * Obtain if errors are happened or not.
	 * 
	 * @return <code>true</code> if one or more errors are happened, otherwise <code>false</code>.
	 */
	public boolean hasErrors() {
		return (errors != null && !getErrors().isEmpty());
	}
	
	/**
	 * Get the content of the inputstream of the underlying process. Line separator is the system property <tt>line.separator</tt>.
	 * 
	 * @return Output of the process as String or an empty String if there are no errors.
	 */
	public String getInfos() {
		return infos.toString();
	}
	
	/**
	 * Get the return status of the underlying process.
	 * @return The return status. By convention, the value 0 indicates normal termination.
	 * @see Process#waitFor()
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Thread to 'booze' an {@link InputStream}. The content will be written to the desired {@link PrintWriter}. 
	 */
	private class StreamBoozer extends Thread {
		private InputStream in;
		private PrintWriter pw;
		
		StreamBoozer(InputStream in, PrintWriter pw) {
			this.in = in;
			this.pw = pw;
		}
		
		@Override
		public void run() {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(in));
				String line = null;
	            while ( (line = br.readLine()) != null) {
	            	pw.println(line);
	            }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}	
}
