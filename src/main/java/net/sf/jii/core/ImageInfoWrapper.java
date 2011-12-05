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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.sf.jii.IDimensionProvider;
import net.sf.jii.ImageType;
import net.sf.jii.Resolution;
import net.sf.jii.IResolutionProvider;
import net.sf.jii.exception.ReadException;

import org.devlib.schmidt.imageinfo.ImageInfo;

public class ImageInfoWrapper implements IDimensionProvider, IResolutionProvider {

	private static ImageType[] supportedTypes = new ImageType[] { ImageType.BMP, ImageType.GIF, ImageType.JPG, ImageType.PNG };
	private ImageInfo ii;

	public ImageInfoWrapper() {
		ii = new ImageInfo();
	}

	@Override
	public ImageType[] getSupportedTypes() {
		return supportedTypes;
	}

	@Override
	public void set(File file) throws FileNotFoundException, ReadException {
		set(new BufferedInputStream(new FileInputStream(file)));
	}

	@Override
	public void set(InputStream in) throws ReadException {
		ii.setInput(in);
		if (!ii.check())
			throw new ReadException();
	}

	@Override
	public Dimension getDimension() throws ReadException {
		int width = ii.getWidth();
		int height = ii.getHeight();
		return new Dimension(width, height);
	}

	@Override
	public Resolution getResolution() throws ReadException {
		int resX = ii.getPhysicalWidthDpi();
		int resY = ii.getPhysicalHeightDpi();
		return new Resolution(resX, resY);
	}

	@Override
	public ImageType getImageType() {
		return ImageType.getByMimeType(ii.getMimeType());
	}
}
