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
package hjow.common.module;

import java.io.File;
import java.util.Properties;

import hjow.common.script.HScriptEngine;

public final class CustomModule extends CustomAbstractModule {
	private static final long serialVersionUID = -2495981777714564691L;
	public CustomModule() {
		super();
    }
    
    public CustomModule(File file, HScriptEngine engine, String accessKey) {
        super(file, engine, accessKey);
    }
    
    public CustomModule(Properties prop, HScriptEngine engine, String accessKey) {
    	super(prop, engine, accessKey);
    }
}
