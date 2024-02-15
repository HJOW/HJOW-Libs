/*
Copyright 2019 HJOW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package hjow.common.help;

import java.io.Serializable;
import java.util.Vector;

import hjow.common.json.JsonArray;
import hjow.common.json.JsonObject;

public class Help implements Serializable {
	private static final long serialVersionUID = -9034725817479064972L;
	
	protected Vector<HelpPage> pages;
	
	public Help() {
		pages = new Vector<HelpPage>();
	}
	
	public Help(JsonArray arr) {
		this();
		for(Object o : arr) {
			JsonObject obj = (JsonObject) o;
			pages.add(new HelpPage(obj));
		}
	}

	public Vector<HelpPage> getPages() {
		return pages;
	}

	public void setPages(Vector<HelpPage> pages) {
		this.pages = pages;
	}
	
	public JsonArray toJSON() {
		JsonArray arr = new JsonArray();
		for(HelpPage page : pages) {
			arr.add(page.toJSON());
		}
		return arr;
	}
	
	public void setLocale(String locale) {
		for(HelpPage page : pages) {
			page.setLocale(locale);
		}
	}
}
