/**
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.maven.spring.boot.options;

/**
 * https://manual.calibre-ebook.com/generated/en/web2disk.html
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class Web2diskOptions {

	/**
	 * <p>Indicates whether Do not download CSS stylesheets.</p>
	 * Set the value of the {@code dont-download-stylesheets} {@code true} if the
	 * argument {@code --dont-download-stylesheets} was specified, otherwise {@code false}
	 */
	private boolean dontDownloadStylesheets;

	public boolean isDontDownloadStylesheets() {
		return dontDownloadStylesheets;
	}

	public void setDontDownloadStylesheets(boolean dontDownloadStylesheets) {
		this.dontDownloadStylesheets = dontDownloadStylesheets;
	}
	
}
