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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import codes.thischwa.jii.IDimensionProvider;
import codes.thischwa.jii.IResolutionProvider;
import codes.thischwa.jii.ImageType;
import codes.thischwa.jii.Resolution;
import codes.thischwa.jii.exception.ReadException;

/**
 * Wrapper to commons-imaging.
 *
 * @author Thilo Schwarz
 */
public class CommonsImageInfoWrapper implements IDimensionProvider, IResolutionProvider {
	private Dimension dimension = null;
	private ImageType imageType = null;
	private Resolution resolution = null;
	
	@Override
	public ImageType[] getSupportedTypes() {
		// all types supported
		return ImageType.values();
	}

	@Override
	public ImageType getImageType() {
		return imageType;
	}

	@Override
	public Dimension getDimension() throws ReadException {
		return dimension;
	}
	
	@Override
	public Resolution getResolution() throws ReadException {
		return resolution;
	}
	
	@Override
	public void set(File file) throws FileNotFoundException, ReadException {
		set(new BufferedInputStream(new FileInputStream(file)));
	}

	@Override
	public void set(InputStream in) throws ReadException, UnsupportedOperationException {
		try {
			ImageInfo ii = Imaging.getImageInfo(in, null);
			dimension = new Dimension(ii.getWidth(), ii.getHeight());
			resolution = new Resolution(ii.getPhysicalWidthDpi(), ii.getPhysicalHeightDpi());
			imageType = ImageType.getByExtension(ii.getFormat().getExtension());
		} catch (ImageReadException | IOException e) {
			throw new ReadException(e);
		}
	}


}
