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
package hjow.common.ui.extend;

import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;

import hjow.common.script.PublicMethodOpenedClass;

public class HComboBox extends JComboBox {
	private static final long serialVersionUID = 238077368780030634L;
	public HComboBox() {
		super();
	}
	
	public HComboBox(Vector<?> elements) {
		super(elements);
	}
	
	public List<String> availables() {
    	return PublicMethodOpenedClass.getAvailableMethods(this);
    }
}
