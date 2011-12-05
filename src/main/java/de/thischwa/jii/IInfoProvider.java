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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.thischwa.jii.exception.ReadException;


/**
 * Interface for all beans, which provides informations about an image file in general. It is the root interface for other more specific
 * interfaces.
 */
public interface IInfoProvider {

	/**
	 * Return all {@link ImageType}s supported by the implemented class.
	 * 
	 * @return A set of {@link ImageType}s.
	 */
	public ImageType[] getSupportedTypes();

	/**
	 * Obtain the {@link ImageType} of the current image file.
	 * 
	 * @return The {@link ImageType} of the current image file.
	 */
	public ImageType getImageType();

	/**
	 * Specify the image {@link File} to be examine.
	 * 
	 * @param file
	 *            The image {@link File} to be examine.
	 * @throws FileNotFoundException
	 *             If the file to be examine couldn't find.
	 * @throws ReadException
	 *             If the required reading operation fails.
	 */
	public void set(File file) throws FileNotFoundException, ReadException;

	/**
	 * Specify the {@link InputStream} of image file to be examine.
	 * 
	 * @param in
	 *            The {@link InputStream} of the image file to be examine.
	 * @throws ReadException
	 *             If the required reading operation fails.
	 * @throws UnsupportedOperationException
	 *             If the implemented class doesn't provide support for {@link InputStream}s.
	 */
	public void set(InputStream in) throws ReadException, UnsupportedOperationException;
}
