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

import de.thischwa.jii.exception.ReadException;

/**
 * Interface for beans which provides the {@link Resolution} of an image.
 */
public interface IResolutionProvider extends IInfoProvider {

	/**
	 * Obtain the {@link Resolution} of the current image.
	 * 
	 * @return The {@link Resolution} of the current image.
	 * @throws ReadException
	 *             If the required operations to determine the resolution fail.
	 */
	public Resolution getResolution() throws ReadException;
}
