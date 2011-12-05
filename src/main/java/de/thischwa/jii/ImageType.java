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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An enum to holds infos for the different image types. Several static helper methods are provided to detect the correct image type.
 */
public enum ImageType {

	GIF("image/gif", new String[] { "gif" }),
	PNG("image/png", new String[] { "png" }),
	JPG("image/jpeg", new String[] { "jpg", "jpeg" }),
	BMP("image/bmp", new String[] { "bmp" });

	private String mimeType;
	private Set<String> extensions;

	private ImageType(String mimeType, String... extensions) {
		this.mimeType = mimeType;
		this.extensions = new HashSet<String>(extensions.length);
		this.extensions.addAll(Arrays.asList(extensions));
	}

	/**
	 * Return the mime type for this type.
	 * 
	 * @return The mime type.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Retrieve an {@link ImageType} of the desired file extension.
	 * 
	 * @param ext
	 *            A file extension.
	 * @return The corresponding {@link ImageType} or <code>null</code> if none.
	 * @throws IllegalArgumentException
	 *             If the extension is null, empty or no {@link ImageType} couldn't find.
	 */
	public static ImageType getByExtension(String ext) throws IllegalArgumentException {
		if (ext == null || ext.trim().length() == 0)
			throw new IllegalArgumentException("Extension shouldn't be null or empty.");
		String extension = ext.toLowerCase();
		for (ImageType type : ImageType.values()) {
			if (type.extensions.contains(extension))
				return type;
		}
		throw new IllegalArgumentException("No image type found for: " + extension);
	}

	/**
	 * Retrieve an {@link ImageType} of the desired mime type.
	 * 
	 * @param mimeType
	 *            A mime type.
	 * @return The corresponding {@link ImageType} or <code>null</code> if none.
	 * @throws IllegalArgumentException
	 *             If the mime type is null, empty or no {@link ImageType} couldn't find.
	 */
	public static ImageType getByMimeType(String mimeType) throws IllegalArgumentException {
		if (mimeType == null || mimeType.trim().length() == 0)
			throw new IllegalArgumentException("Mimetype shouldn't be null or empty.");
		String mt = mimeType.toLowerCase();
		for (ImageType type : ImageType.values()) {
			if (type.mimeType.equals(mt))
				return type;
		}
		throw new IllegalArgumentException("No image type found for: " + mt);

	}
}
