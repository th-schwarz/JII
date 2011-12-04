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
package net.sf.jii.util;

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
	
	public String getErrors() {
		return errors.toString();
	}
	
	public boolean hasErrors() {
		return (errors != null && !getErrors().isEmpty());
	}
	
	public String getInfos() {
		return infos.toString();
	}
	
	public int getStatus() {
		return status;
	}

	class StreamBoozer extends Thread {
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
