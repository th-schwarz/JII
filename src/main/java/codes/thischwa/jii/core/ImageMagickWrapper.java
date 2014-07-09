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
package codes.thischwa.jii.core;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codes.thischwa.jii.IDimensionProvider;
import codes.thischwa.jii.IResolutionProvider;
import codes.thischwa.jii.ImageType;
import codes.thischwa.jii.Resolution;
import codes.thischwa.jii.exception.ReadException;
import codes.thischwa.jii.imagemagick.ImageMagick;

/**
 * Wrapper to the operation bean to the command 'identify' of <a href="http://www.imagemagick.org">ImageMagick</a>.
 *
 * @author Thilo Schwarz
 */
public class ImageMagickWrapper implements IDimensionProvider, IResolutionProvider {
	private static Logger logger = LoggerFactory.getLogger(ImageMagickWrapper.class);
	private ImageMagick igm;

	/**
	 * Initializes the wrapper.
	 * 
	 * @param cmd full path to the command 'identify'
	 */
	public ImageMagickWrapper(String cmd) {
		this(cmd, null);
	}
	
	/**
	 * Initializes the wrapper.
	 * 
	 * @param cmd full path to the command 'identify'
	 * @param libPath optional path of the library of ImageMagick
	 */
	public ImageMagickWrapper(String cmd, String libPath) {
		logger.info("entered ImageMagickWrapper with command: {}", cmd);
		igm = new ImageMagick(cmd, libPath);
	}

	@Override
	public ImageType[] getSupportedTypes() {
		// all types supported
		return ImageType.values();
	}

	@Override
	public ImageType getImageType() {
		return ImageType.getByExtension(igm.getTypeStr());
	}

	@Override
	public void set(File file) throws FileNotFoundException, ReadException {
		igm.set(file);
	}

	@Override
	public void set(InputStream in) throws ReadException, UnsupportedOperationException {
		throw new UnsupportedOperationException("Not supported in this context.");
	}

	@Override
	public Dimension getDimension() throws ReadException {
		if(igm.getGeometryStr() == null)
			return null;
		String geometry = igm.getGeometryStr();
		String[] xy = geometry.split("x");
		if (xy.length != 2)
			throw new ReadException("Dimension string has not the correct pattern!");
		else
			return new Dimension(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
	}

	@Override
	public Resolution getResolution() throws ReadException {
		if (igm.getResolutionStr() == null)
			return null;
		String resolution = igm.getResolutionStr();
		String[] xy = resolution.split("x");
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
}
