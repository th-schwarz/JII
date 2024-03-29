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
package codes.thischwa.jii.imagemagick;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codes.thischwa.jii.exception.ConfigurationException;
import codes.thischwa.jii.exception.ReadException;
import codes.thischwa.jii.util.ProcessBuilderWrapper;

/**
 * That's the operation bean to the command 'identify' of ImageMagick (http://www.imagemagick.org). It parses its output and provides
 * corresponding properties. <p>
 * <p>
 * Hint: GraphicsMagick (http://www.graphicsmagick.org) can't be uses because the missing parameter 'units'.
 */
public class ImageMagick {
	private static Logger logger = LoggerFactory.getLogger(ImageMagick.class);
	
	private String libPath;

	private List<String> command;
	
	private String typeStr = null;
	private String geometryStr = null;
	private String resolutionStr = null;
	
	/**
	 * Initialize the bean with the desired command line name and environment variables.
	 * 
	 * @param commandFileName
	 * @param libPath optional path of the library of ImageMagick
	 * @throws ConfigurationException
	 */
	public ImageMagick(String commandFileName, String libPath) throws ConfigurationException {
		if (commandFileName == null)
			throw new ConfigurationException("Command file isn't set!");

		File basicCommandFile = new File(commandFileName);
		if (!basicCommandFile.exists())
			throw new ConfigurationException(String.format("Command file doesn't exists: %s", basicCommandFile.getAbsolutePath()));
		
		this.libPath = libPath;

		command = new ArrayList<>();
		command.add(basicCommandFile.getAbsolutePath());
		command.add("-units");
		command.add("PixelsPerInch");
		command.add("-format");
		command.add("%G;%xx%y;%m");
	}

	/**
	 * Initialize the bean with the desired command line name.
	 * 
	 * @param commandFileName
	 * @throws ConfigurationException
	 */
	public ImageMagick(String commandFileName) throws ConfigurationException {
		this(commandFileName, null);
	}

	/**
	 * Set the desired image file to inspect.
	 * 
	 * @param file The image.
	 * @throws FileNotFoundException If the image file doesn't exists.
	 * @throws ReadException If there are problems parsing the output.
	 */
	public void set(File file) throws FileNotFoundException, ReadException {
		if (!file.exists())
			throw new FileNotFoundException(String.format("Image file not found: %s", file.getAbsolutePath()));

		List<String> cmd = new ArrayList<>(command);
		cmd.add(file.getAbsolutePath());
		ProcessBuilderWrapper processBuilder = null;
		try {
			Map<String, String> env = null;
			if(libPath != null) { // if the lib path is set, build the corresponding environment var
				env = new HashMap<>();
				env.put("DYLD_LIBRARY_PATH", libPath);
			}
			processBuilder = new ProcessBuilderWrapper(cmd.toArray(new String[0]))
					.environment(env)
					.run();
			int status = processBuilder.getStatus();
			logger.debug("Command has terminated with status: {}", status);
		} catch (Exception e) {
			throw new ReadException(e);
		}
		if (processBuilder.hasErrors()) {
			String msg = String
					.format("Error while getting infos of the file [%s]: %s", file.getAbsolutePath(), processBuilder.getOutput());
			logger.error(msg);
			throw new ReadException(msg);
		}
		String info = processBuilder.getOutput();
		String[] infos = info.split(";");
		geometryStr = infos[0];
		resolutionStr = infos[1];
		typeStr = infos[2];
	}
	
	public String getTypeStr() {
		return typeStr;
	}
	
	public String getGeometryStr() {
		return geometryStr;
	}
	
	public String getResolutionStr() {
		return resolutionStr;
	}
	
	public static void main(String[] args) throws Exception {
		ImageMagick im = new ImageMagick("/usr/local/bin/identify", null);
		im.set(new File("src/test/resources/JII_120x65-140x72.png"));
		System.out.println(im.getGeometryStr());
	}
}
