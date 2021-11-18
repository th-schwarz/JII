package codes.thischwa.jii.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class ProcessBuilderWrapper {
	
	private boolean redirectToStdout;
	
	private ByteArrayOutputStream out = new ByteArrayOutputStream();

	private int status;
	
	private File workingDirectory = null;
	
	private String[] command;
	
	private Map<String, String> environment = null;
	
	public ProcessBuilderWrapper(String... command) {
		this.command = command;
	}
	
	public ProcessBuilderWrapper run() throws IOException, InterruptedException { 
		ProcessBuilder pb = new ProcessBuilder(command);
		if(workingDirectory != null)
			pb.directory(workingDirectory);
		if(environment != null && environment.size() > 0) 
			pb.environment().putAll(environment);
			
		// Consuming STDOUT and STDERR in same thread can lead to the process freezing if it writes large amounts.
		// That's why we have to redirect the error stream, see
		// https://stackoverflow.com/questions/69612116/how-to-print-the-live-data-from-processbuilder-to-console-in-java?noredirect=1#comment123140957_69612116
		pb.redirectErrorStream(true);
	
		Process process = pb.start();
		try (var infoStream = process.getInputStream()) {
			if(redirectToStdout)
				infoStream.transferTo(System.out);
			else
				infoStream.transferTo(this.out);
		}
		status = process.waitFor();
		return this;
	}
	
	public ProcessBuilderWrapper redirectToStdOut() {
		this.redirectToStdout = true;
		return this;
	}
	
	public ProcessBuilderWrapper workingDirectory(String dir)  {
		this.workingDirectory = Paths.get(dir).toFile();
		return this;
	}
	
	public ProcessBuilderWrapper environment(Map<String, String> environment) {
		this.environment = environment;
		return this;
	}

	public int getStatus() {
		return status;
	}
	
	public String getOutput() {
		return (redirectToStdout) ? "n/a" : out.toString();
	}
	
	public boolean hasErrors() {
		return getStatus() != 0;
	}
	
	public static void main(String[] args) throws Exception {
		ProcessBuilderWrapper builder = new ProcessBuilderWrapper("/usr/local/bin/identify", 
				"-units", "PixelsPerInch", 
				"-format", "%G;%xx%y;%m", 
				"src/test/resources/JII_120x65-140x72.png")
				.run();
		int status = builder.getStatus();
		System.out.println("status: "+status);
		System.out.println("out: "+builder.getOutput());
	}

}
