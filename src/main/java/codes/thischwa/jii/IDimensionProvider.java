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
package codes.thischwa.jii;

import java.awt.Dimension;

import codes.thischwa.jii.exception.ReadException;


/**
 * Interface for beans which provides the {@link Dimension} of an image.
 */
public interface IDimensionProvider extends IInfoProvider {

	/**
	 * Obtain the {@link Dimension} of the current image.
	 * 
	 * @return The {@link Dimension} of the current image.
	 * @throws ReadException
	 *             If the required operations to determine the dimension fail.
	 */
	public Dimension getDimension() throws ReadException;
}
