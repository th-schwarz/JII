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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <b>THE</b> parser of the output of the command 'identify' of ImageMagick (http://www.imagemagick.org) and provides corresponding
 * properties.
 */
class ImageMagickOutputParser {
	
	/** Separator for hierarchically properties. */
	static String TOKEN_SEPERATOR = ":";
	
	static String[] PROPERTIES_IGNORE_IDENT = new String[] { "histogram", "colormap" };
	
	/** Indention count to identify hierarchically properties. */
	private static final int standard_indention = 2;

	private Map<String, String> properties;
	private List<String> outputLines;
	private List<String> prefixIgnoreIdent;

	private int level = 1;
	private int ignorePos = 0;
	private String ignorePrefix = null;
	private String lastKey;

	ImageMagickOutputParser(List<String> outputLines) {
		this.outputLines = outputLines;
		properties = new LinkedHashMap<String, String>();
		prefixIgnoreIdent = new ArrayList<String>(Arrays.asList(PROPERTIES_IGNORE_IDENT));

		// start parsing, skipping the 1st, its useless
		parse("", 1);
	}

	private void parse(String prefix, int lineNo) {
		if (lineNo >= outputLines.size())
			return;
		String line = outputLines.get(lineNo);
		KeyValue keyValue = new KeyValue(line);
		int identPos = line.indexOf(line.trim());
		int lineLevel = (identPos / standard_indention);

		if (ignorePrefix != null && identPos > ignorePos) {
			prefix = ignorePrefix;
		} else if (identPos <= ignorePos) {
			prefix = "";
		} else if (lineLevel > level) {
			prefix = (prefix.length() == 0) ? lastKey : buildNewPrefix(prefix, lastKey);
		} else if (lineLevel < level) {
			int levelDiff = level - lineLevel;
			for (int i = 0; i < levelDiff; i++) {
				int pos = prefix.lastIndexOf(TOKEN_SEPERATOR);
				if (pos != -1)
					prefix = prefix.substring(0, pos);
				else {
					prefix = "";
					break;
				}
			}
		}
		if (!prefixIgnoreIdent.contains(prefix))
			level = lineLevel;

		String key = (prefix.length() == 0) ? keyValue.key : buildNewPrefix(prefix, keyValue.key);
		String value = keyValue.value;
		lastKey = keyValue.key;
		if (value != null) {
			if (value.trim().length() > 0) {
				properties.put(key, value);
			}
		}

		if (prefixIgnoreIdent.contains(keyValue.key)) {
			ignorePos = identPos;
			ignorePrefix = keyValue.key;
		} else if (identPos <= ignorePos) {
			ignorePos = 0;
			ignorePrefix = null;
		}

		parse(prefix, lineNo + 1);
	}

	String getValue(String key) {
		return properties.get(key);
	}
	
	private String buildNewPrefix(String oldPrefix, String key) {
		return String.format("%s%s%s", oldPrefix, TOKEN_SEPERATOR, key);
	}

	Set<String> keySet() {
		return Collections.unmodifiableSet(properties.keySet());
	}

	private class KeyValue {
		String key;
		String value = null;

		KeyValue(String line) {
			String kv[] = line.trim().toLowerCase().split(": ", 2);
			key = kv[0].trim();
			if (key.endsWith(":"))
				key = key.substring(0, key.length() - 1);
			if (kv.length == 2)
				value = kv[1].trim();
		}
	}
}