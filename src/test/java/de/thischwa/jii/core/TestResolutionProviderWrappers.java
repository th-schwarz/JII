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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.thischwa.jii.IResolutionProvider;
import de.thischwa.jii.ImageFileInfo;
import de.thischwa.jii.ImageType;
import de.thischwa.jii.PropertiesHolder;
import de.thischwa.jii.core.ImageMagickWrapper;
import de.thischwa.jii.core.iTextImageWrapper;

@RunWith(value = Parameterized.class)
public class TestResolutionProviderWrappers {
	private IResolutionProvider rp;

	public TestResolutionProviderWrappers(IResolutionProvider rp) {
		this.rp = rp;
	}
	
	@Parameters
	public static Collection<IResolutionProvider[]> data() {
		String commandDirIM = PropertiesHolder.get("im.command");
		IResolutionProvider[][] providers = new IResolutionProvider[][] {
				{ new ImageMagickWrapper(commandDirIM) },
				{ new iTextImageWrapper() }
		};
		return Arrays.asList(providers);
	}
	
	@Test
	public void testGetResolution() throws Exception {
		List<ImageType> supportedImageTypes = Arrays.asList(rp.getSupportedTypes());
		ImageFileInfo[] infos = ImageFileInfo.getAll();
		for (ImageFileInfo info : infos) {
			if (!supportedImageTypes.contains(info.getImageType())
					|| rp.getClass().equals(iTextImageWrapper.class))
				continue;
			rp.set(info.getFile());
			String errMsg = String.format("%s#%s", rp.getClass().getSimpleName(), info.getFile().getPath());
			assertEquals(errMsg, info.getResolution(), rp.getResolution());
		}		
	}
}
