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

import codes.thischwa.jii.IResolutionProvider;
import codes.thischwa.jii.ImageFileInfo;
import codes.thischwa.jii.ImageType;
import codes.thischwa.jii.PropertiesHolder;
import codes.thischwa.jii.Resolution;

@RunWith(value = Parameterized.class)
public class TestResolutionProviderWrappers {
	private IResolutionProvider rp;

	public TestResolutionProviderWrappers(IResolutionProvider rp) {
		this.rp = rp;
	}
	
	@Parameters
	public static Collection<IResolutionProvider[]> data() {
		String commandDirIM = PropertiesHolder.get("im.command");
		String dylIM = PropertiesHolder.get("im.dyld");
		String ignoreIm = System.getProperty("im.ignore");
		IResolutionProvider[][] providers =  "true".equals(ignoreIm) ?  new IResolutionProvider[][] { 
				{ new iTextImageWrapper() },
				{ new CommonsImageInfoWrapper() }
		} :
			 new IResolutionProvider[][] { 
			{ new ImageMagickWrapper(commandDirIM, dylIM) }, 
			{ new iTextImageWrapper() },
			{ new CommonsImageInfoWrapper() }
		};
		return Arrays.asList(providers);
	}
	
	@SuppressWarnings("incomplete-switch")
	@Test
	public void testGetResolution() throws Exception {
		List<ImageType> supportedImageTypes = Arrays.asList(rp.getSupportedTypes());
		ImageFileInfo[] infos = ImageFileInfo.getAll();
		for (ImageFileInfo info : infos) {
			if (!supportedImageTypes.contains(info.getImageType())
					|| rp.getClass().equals(iTextImageWrapper.class))
				continue;
			rp.set(info.getFile());
			Resolution expected = info.getResolution();
			if(rp instanceof CommonsImageInfoWrapper) {
				switch (info.getImageType()) {
				case GIF:
					expected = new Resolution(72, 72);
					break;
				case BMP:
					expected = new Resolution(71, 71);
					break;
				}
			}
			String errMsg = String.format("%s#%s", rp.getClass().getSimpleName(), info.getFile().getPath());
			assertEquals(errMsg, expected, rp.getResolution());
		}		
	}
}
