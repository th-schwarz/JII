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
package net.sf.jii.imagemagick;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import net.sf.jii.imagemagick.ImageMagickOutputParser;
import net.sf.jii.util.IOUtil;

import org.junit.Test;

public class TestIGMagickOutputParser {

	@Test
	public void testGMProperties() throws Exception {
		List<String> lines = IOUtil.parseLineByLine(this.getClass().getResourceAsStream("test-output.txt"));
		assertEquals(20, lines.size());
		
		ImageMagickOutputParser.TOKEN_SEPERATOR = ":";
		ImageMagickOutputParser parser = new ImageMagickOutputParser(lines);
		Collection<String> keys = parser.keySet();
		assertTrue(keys.contains("properties:date:create"));
		assertTrue(keys.contains("artifacts:verbose"));		
		assertTrue(keys.contains("channel statistics:gray:min"));		
	}
}
