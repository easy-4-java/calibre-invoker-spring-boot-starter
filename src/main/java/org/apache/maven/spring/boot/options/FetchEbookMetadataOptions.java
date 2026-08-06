/**
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
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
 * Fetch book metadata from online sources. You must specify at least one of
 * title, authors or ISBN.
 * https://manual.calibre-ebook.com/generated/en/lrf2lrs.html
 * 
 * @author ： <a href="https://github.com/easy-4-java">hiwepy</a>
 */
public class FetchEbookMetadataOptions {

	/**
	 * Specify the name of a metadata download plugin to use. By default, all
	 * metadata plugins will be used. Can be specified multiple times for multiple
	 * plugins. All plugin names: Google, Google Images, Amazon.com, Edelweiss, Open
	 * Library, Overdrive, Douban Books, OZON.ru, Big Book Search. Set the value of
	 * the {@code allowed-plugin} {@code true} if the argument
	 * {@code --allowed-plugin} was specified, otherwise {@code false}
	 */
	private boolean allowedPlugin;

	public boolean isAllowedPlugin() {
		return allowedPlugin;
	}

	public void setAllowedPlugin(boolean allowedPlugin) {
		this.allowedPlugin = allowedPlugin;
	}

}
