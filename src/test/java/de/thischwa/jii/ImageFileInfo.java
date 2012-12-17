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
package de.thischwa.jii;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.thischwa.jii.ImageType;
import de.thischwa.jii.Resolution;

/**
 * Info store to handle the data for each test image.
 */
public class ImageFileInfo {
	private static File resourceDir = new File("src/test/resources/");
	private File file;
	private Dimension dimension;
	private ImageType imageType;
	private Resolution resolution;
	

	public ImageFileInfo(String filename, ImageType imageType, int width, int height, int densityX, int densityY) {
		file = new File(resourceDir, filename);
		this.dimension = new Dimension(width, height);
		this.imageType = imageType;
		this.resolution = new Resolution(densityX, densityY);
	}

	public File getFile() {
		return file;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public ImageType getImageType() {
		return imageType;
	}

	public Resolution getResolution() {
		return resolution;
	}

	public static ImageFileInfo[] getAll() {
		List<ImageFileInfo> info = new ArrayList<ImageFileInfo>();
		info.add(new ImageFileInfo("JII_120x65-72x72.gif", ImageType.GIF, 120, 65, 0, 0)); // resolution is missing in some versions, see http://www.imagemagick.org/discourse-server/viewtopic.php?f=3&t=22455
		info.add(new ImageFileInfo("JII_120x65-72x72.bmp", ImageType.BMP, 120, 65, 72, 72));
		info.add(new ImageFileInfo("JII_240x130-300x300.jpg", ImageType.JPG, 240, 130, 300, 300));
		info.add(new ImageFileInfo("JII_120x65-140x72.png", ImageType.PNG, 120, 65, 140, 72));
		
		// test files
		for (ImageFileInfo imageFileInfo : info) {
			if(!imageFileInfo.getFile().exists())
				throw new RuntimeException(String.format("File not found: %s", imageFileInfo.getFile().getPath()));
		}
		return info.toArray(new ImageFileInfo[0]);
	}
}
