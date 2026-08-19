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
 * <p>
 * Launch the calibre edit book tool. You can optionally also specify the names 
 * of files inside the book which will be opened for editing automatically.</p>
 * https://manual.calibre-ebook.com/generated/en/ebook-edit.html
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class EbookEditOptions {
	
	/**
	 * <p>Detach from the controlling terminal, if any (Linux only)</p>
	 * Set the value of the {@code detach} {@code true} if the
	 * argument {@code --detach} was specified, otherwise {@code false}
	 */
	private boolean detach;

	/** @return return whether detach is enabled. */
	public boolean isDetach() {
		return detach;
	}

	/** @param detach set the detach. */
	public void setDetach(boolean detach) {
		this.detach = detach;
	}
	
}
