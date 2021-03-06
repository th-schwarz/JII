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

import com.lowagie.text.Image;

/**
 * Wrapper to an internal class of <a href="http://sourceforge.net/projects/itext/">iText</a>.
 *
 * @author Thilo Schwarz
 */
public class iTextImageWrapper implements IDimensionProvider, IResolutionProvider {
	private static Logger logger = LoggerFactory.getLogger(iTextImageWrapper.class);
	private static ImageType[] supportedTypes = new ImageType[] { ImageType.BMP, ImageType.GIF, ImageType.JPG, ImageType.PNG };
	private Image image;
	private ImageType imageType;

	@Override
	public void set(File file) throws FileNotFoundException, ReadException {
		if (!file.exists())
			throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
		try {
			image = Image.getInstance(file.getAbsolutePath());
		} catch (Exception e) {
			throw new ReadException(e);
		}

		int type = image.getOriginalType();
		switch (type) {
			case Image.ORIGINAL_BMP:
				imageType = ImageType.BMP;
				break;
			case Image.ORIGINAL_GIF:
				imageType = ImageType.GIF;
				break;
			case Image.ORIGINAL_JPEG:
			case Image.ORIGINAL_JPEG2000:
				imageType = ImageType.JPG;
				break;
			case Image.ORIGINAL_PNG:
				imageType = ImageType.PNG;
				break;
			default: {
				logger.warn("Couldn't analyse format from {} or image type isn't supported.", file.getAbsolutePath());
				imageType = null;
			}
		} 
	}

	@Override
	public void set(InputStream in) throws ReadException, UnsupportedOperationException {
		throw new UnsupportedOperationException("Not supported in this context.");
	}

	@Override
	public ImageType getImageType() {
		return imageType;
	}

	@Override
	public Dimension getDimension() throws ReadException {
		int height = (int) image.getHeight();
		int width = (int) image.getWidth();
		return new Dimension(width, height);
	}
	
	@Override
	public Resolution getResolution() throws ReadException {
		int resX = image.getDpiX();
		int resY = image.getDpiY();
		if(resX == 0 && resY == 0)
			throw new ReadException("Is not supported for: " + getImageType());
		return new Resolution(resX, resY);
	}

	@Override
	public ImageType[] getSupportedTypes() {
		return supportedTypes;
	}
}
