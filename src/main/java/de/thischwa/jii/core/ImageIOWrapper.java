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
package de.thischwa.jii.core;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import de.thischwa.jii.IDimensionProvider;
import de.thischwa.jii.ImageType;
import de.thischwa.jii.exception.ReadException;
import de.thischwa.jii.util.IOUtil;

/**
 * Wrapper to to {@link ImageReader}.
 * 
 * @author Thilo Schwarz
 */
public class ImageIOWrapper implements IDimensionProvider {

	private static ImageType[] supportedTypes = new ImageType[] { ImageType.GIF, ImageType.JPG, ImageType.PNG };

	private Dimension dimension = null;
	private ImageType imageType = null;

	@Override
	public ImageType[] getSupportedTypes() {
		return supportedTypes;
	}

	@Override
	public ImageType getImageType() {
		return imageType;
	}

	@Override
	public void set(File file) throws FileNotFoundException, ReadException {
		set(new BufferedInputStream(new FileInputStream(file)));
	}

	@Override
	public void set(InputStream in) throws ReadException, UnsupportedOperationException {
		ImageInputStream iin = null;
		ImageReader reader = null;
		try {
			iin = ImageIO.createImageInputStream(in);
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(iin);
			if (!readers.hasNext())
				return;
			reader = readers.next();
			reader.setInput(iin);

			// obtain the dimension
			dimension = new Dimension(reader.getWidth(0), reader.getHeight(0));

			// obtain the image type
			imageType = ImageType.getByExtension(reader.getFormatName());
		} catch (Exception e) {
			throw new ReadException(e);
		} finally {
			IOUtil.closeQuietly(reader);
			IOUtil.closeQuietly(iin);
		}
	}

	@Override
	public Dimension getDimension() throws ReadException {
		return dimension;
	}

	public static void main(String[] args) throws Exception {
		ImageIOWrapper iow = new ImageIOWrapper();
		iow.set(new File("src/test/resources/JII.png"));
		System.out.println(iow.getDimension());
		System.out.println(iow.getImageType());
	}
}
