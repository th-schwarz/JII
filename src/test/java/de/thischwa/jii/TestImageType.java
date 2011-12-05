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

import static org.junit.Assert.*;

import org.junit.Test;

import de.thischwa.jii.ImageType;

public class TestImageType {

	@Test
	public void testGetMimeType() {
		assertEquals("image/gif", ImageType.GIF.getMimeType());
	}

	@Test
	public void testGetByExtension() {
		assertEquals(ImageType.JPG, ImageType.getByExtension("JpEG"));
		assertEquals(ImageType.JPG, ImageType.getByExtension("jpg"));
		assertEquals(ImageType.PNG, ImageType.getByExtension("png"));
	}


	@Test(expected=IllegalArgumentException.class)
	public void testGetByExtensionNull() {
		ImageType.getByExtension(null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetByExtensionBlank() {
		ImageType.getByExtension(" ");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetByExtensionWrong() {
		ImageType.getByExtension("NotExists");
	}
}
