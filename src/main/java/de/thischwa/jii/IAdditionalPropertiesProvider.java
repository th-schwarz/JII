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

import java.io.PrintStream;

/**
 * Interface for beans which provides additional properties of an image. <br/>
 * <br/>
 * <b>Tips for implementations which parsing the output of a command line tool:</b> Use {@link #printAll(PrintStream)} to see how the
 * implemented object convert the output into the properties.
 */
public interface IAdditionalPropertiesProvider extends IInfoProvider {

	/**
	 * Get all available property names.
	 * 
	 * @return Array of all available property names.
	 */
	public String[] getPropertiesNames();

	/**
	 * Obtain the value of the desired property name.
	 * 
	 * @param name
	 *            Name of the property.
	 * 
	 * @return The value to the desired property or <code>null</code> if not exists.
	 */
	public String getValue(String name);

	/**
	 * Print all properties as formated key-value-pair to the desired {@link PrintStream}.<br/>
	 * A nice way to see how implementations which parsing the output of command line tools convert it into properties.
	 * 
	 * @param printStream
	 *            {@link PrintStream} to use for writing the formated key-value-pairs.
	 */
	public void printAll(PrintStream printStream);
}
