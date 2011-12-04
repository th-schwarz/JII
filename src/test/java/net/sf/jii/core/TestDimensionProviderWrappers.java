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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.jii.DimensionProvider;
import net.sf.jii.ImageFileInfo;
import net.sf.jii.ImageType;
import net.sf.jii.PropertiesHolder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class TestDimensionProviderWrappers {
	private DimensionProvider dp;

	public TestDimensionProviderWrappers(DimensionProvider dp) {
		this.dp = dp;
	}

	@Parameters
	public static Collection<DimensionProvider[]> data() {
		String commandDirIM = PropertiesHolder.get("im.command");
		DimensionProvider[][] providers = new DimensionProvider[][] { 
				{ new ImageMagickWrapper(commandDirIM) }, 
				{ new ImageInfoWrapper() }, 
				{ new SimpleImageInfoWrapper() },
				{ new iTextImageWrapper() } };
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
		}
	}
}
