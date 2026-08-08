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
 * 
 * https://manual.calibre-ebook.com/generated/en/lrfviewer.html
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class LrfviewerOptions {

	/**
	 * Indicates whether disable hyphenation. Should significantly speed up rendering.
	 * Set the value of the {@code disable-hyphenation} {@code true} if the
	 * argument {@code --disable-hyphenation} was specified, otherwise {@code false}
	 */
	private boolean disableHyphenation;
	
	/**
	 * Indicates whether turn on visual aids to debugging the rendering engine.
	 * Set the value of the {@code visual-debug} {@code true} if the
	 * argument {@code --visual-debug} was specified, otherwise {@code false}
	 */
	private boolean visualDebug;
	
	/**
	 * By default the background is off white as I find this easier on the eyes.
	 * Use this option to make the background pure white.
	 * Set the value of the {@code white-background} {@code true} if the
	 * argument {@code --white-background} was specified, otherwise {@code false}
	 */
	private boolean whiteBackground;

	public boolean isDisableHyphenation() {
		return disableHyphenation;
	}

	public void setDisableHyphenation(boolean disableHyphenation) {
		this.disableHyphenation = disableHyphenation;
	}

	public boolean isVisualDebug() {
		return visualDebug;
	}

	public void setVisualDebug(boolean visualDebug) {
		this.visualDebug = visualDebug;
	}

	public boolean isWhiteBackground() {
		return whiteBackground;
	}

	public void setWhiteBackground(boolean whiteBackground) {
		this.whiteBackground = whiteBackground;
	}
	
}
