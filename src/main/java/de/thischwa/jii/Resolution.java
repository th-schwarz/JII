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

import java.awt.Dimension;

/**
 * Encapsulate the resolution of an image. Should be in DPI, PixelsPerInch or something equivalent.
 */
public class Resolution extends Dimension {
	private static final long serialVersionUID = 1L;

	public Resolution(int resX, int resY) {
		super(resX, resY);
	}

	public int getResX() {
		return super.width;
	}

	public int getResY() {
		return super.height;
	}

	@Override
	public String toString() {
		return String.format("%dx%d", super.width, super.height);
	}
}
