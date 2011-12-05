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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHolder {

	private static Properties properties = new Properties();
	
	static {
		try {
			InputStream in = PropertiesHolder.class.getResourceAsStream("/test.properties");
			if(in == null)
				throw new RuntimeException("create 'test.properties' in test/resources and add these preoperties: \nim.command=/opt/local/bin/identify");
			properties.load(in);
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	public static String get(String key) {
		return properties.getProperty(key);
	}
}
