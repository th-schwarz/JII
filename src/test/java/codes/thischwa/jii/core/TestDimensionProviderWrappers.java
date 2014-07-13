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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import codes.thischwa.jii.IDimensionProvider;
import codes.thischwa.jii.ImageFileInfo;
import codes.thischwa.jii.ImageType;
import codes.thischwa.jii.PropertiesHolder;

@RunWith(value = Parameterized.class)
public class TestDimensionProviderWrappers {
	private IDimensionProvider dp;

	public TestDimensionProviderWrappers(IDimensionProvider dp) {
		this.dp = dp;
	}

	@Parameters
	public static Collection<IDimensionProvider[]> data() {
		String commandDirIM = PropertiesHolder.get("im.command");
		String dylIM = PropertiesHolder.get("im.dyld");
		IDimensionProvider[][] providers = new IDimensionProvider[][] { 
				{ new ImageMagickWrapper(commandDirIM, dylIM) }, 
				{ new ImageInfoWrapper() }, 
				{ new SimpleImageInfoWrapper() },
				{ new iTextImageWrapper() },
				{ new ImageIOWrapper() },
				//{ new CommonsImageInfoWrapper() } 
				};
		return Arrays.asList(providers);
	}

	@Test
	public void testGetDimension() throws Exception {
		List<ImageType> supportedImageTypes = Arrays.asList(dp.getSupportedTypes());
		ImageFileInfo[] infos = ImageFileInfo.getAll();
		for (ImageFileInfo info : infos) {
			if (!supportedImageTypes.contains(info.getImageType()))
				continue;
			dp.set(info.getFile());
			assertEquals(info.getFile().getName(), info.getDimension(), dp.getDimension());
			assertEquals(info.getFile().getName(), info.getImageType(), dp.getImageType());
		}
	}
}
