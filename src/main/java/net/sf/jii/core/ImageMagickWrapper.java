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
package net.sf.jii.core;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jii.IAdditionalPropertiesProvider;
import net.sf.jii.IDimensionProvider;
import net.sf.jii.ImageType;
import net.sf.jii.IInfoProvider;
import net.sf.jii.Resolution;
import net.sf.jii.IResolutionProvider;
import net.sf.jii.exception.ReadException;
import net.sf.jii.imagemagick.ImageMagick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageMagickWrapper implements IDimensionProvider, IResolutionProvider, IInfoProvider, IAdditionalPropertiesProvider {
	private static Logger logger = LoggerFactory.getLogger(ImageMagickWrapper.class);
	private ImageMagick igm;

	/** The set of keys of the properties provided by {@link ImageMagick}. */
	private Collection<String> keySet;

	private Pattern geometricPattern = Pattern.compile("([0-9\\.]+[x][0-9\\.]+).*");
	private Pattern imageTypePattern = Pattern.compile("([a-zA-Z]+) \\(.*\\)");

	public ImageMagickWrapper(String cmd) {
		logger.info("entered ImageMagickWrapper with command: {}", cmd);
		igm = new ImageMagick(cmd);
	}

	@Override
	public String[] getPropertiesNames() {
		return igm.keySet().toArray(new String[] {});
	}

	@Override
	public String getValue(String name) {
		return igm.getValue(name);
	}

	@Override
	public ImageType[] getSupportedTypes() {
		// all types supported
		return ImageType.values();
	}

	@Override
	public ImageType getImageType() {
		if (!keySet.contains("format"))
			return null;
		String format = igm.getValue("format");
		Matcher matcher = imageTypePattern.matcher(format);
		if (!matcher.matches()) {
			logger.warn("Couldn't analyse format from {}", format);
			return null;
		}
		return ImageType.getByExtension(matcher.group(1));
	}

	@Override
	public void set(File file) throws FileNotFoundException, ReadException {
		igm.set(file);
		keySet = igm.keySet();
	}

	@Override
	public void set(InputStream in) throws ReadException, UnsupportedOperationException {
		throw new UnsupportedOperationException("Not supported in this context.");
	}

	@Override
	public Dimension getDimension() throws ReadException {
		if (!keySet.contains("geometry"))
			return null;
		String geometry = igm.getValue("geometry");
		Matcher matcher = geometricPattern.matcher(geometry);
		if (!matcher.matches()) {
			logger.warn("Couldn't analyse geometry from {}", geometry);
			return null;
		}
		String dimStr = matcher.group(1);
		if (dimStr == null || dimStr.trim().length() < 3)
			throw new ReadException("Dimension string is blank, empty or null!");
		String[] xy = dimStr.split("x");
		if (xy.length != 2)
			throw new ReadException("Dimension string has not the correct pattern!");
		else
			return new Dimension(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
	}

	@Override
	public Resolution getResolution() throws ReadException {
		if (!keySet.contains("resolution"))
			return null;
		String resolution = igm.getValue("resolution");
		Matcher matcher = geometricPattern.matcher(resolution);
		if (!matcher.matches()) {
			logger.warn("Couldn't analyse resolution from {}", resolution);
			return null;
		}
		String resStr = matcher.group(1);
		if (resStr == null || resStr.trim().length() < 3)
			throw new ReadException("Resolution string is blank, empty or null!");
		String[] xy = resStr.split("x");
		if (xy.length != 2)
			throw new ReadException("Resolution string has not the correct pattern!");
		try {
			float resX = Float.parseFloat(xy[0]);
			float resY = Float.parseFloat(xy[1]);
			return new Resolution(Math.round(resX), Math.round(resY));
		} catch (NumberFormatException e) {
			throw new ReadException(e);
		}
	}

	@Override
	public void printAll(PrintStream printStream) {
		for (String key : igm.keySet())
			printStream.println(String.format("%40s  =  %s", key, igm.getValue(key)));
	}
}
