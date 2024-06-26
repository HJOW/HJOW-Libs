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
package hjow.common.ui;

import java.awt.Component;
import java.math.BigDecimal;
import java.util.List;

import hjow.common.core.Releasable;
import hjow.common.json.JsonObject;
import hjow.common.script.PublicMethodOpenedClass;
import hjow.common.util.DataUtil;

public class ScriptJsonPane extends PublicMethodOpenedClass implements Releasable {
    private static final long serialVersionUID = 2177488459987647325L;
    protected JsonPane jsonPane;
    public ScriptJsonPane() {
        jsonPane = new JsonPane();
    }
    public ScriptJsonPane(Object scripts) {
        this();
        init(scripts);
    }
    
    public void init(Object scripts) {
        if(scripts instanceof JsonObject) {
            jsonPane.init((JsonObject) scripts);
        } else {
            jsonPane.init((JsonObject) DataUtil.parseJson(String.valueOf(scripts)));
        }
    }
    
    @Override
    public void releaseResource() {
        try { jsonPane.releaseResource(); } catch(Throwable t) {}
        jsonPane = null;
    }
    
    public Component getRootComponent() {
        return jsonPane.getRootComponent();
    }
    
    
    public Object findById(Object compIdObj) {
        long compId = -1;
        if(compIdObj instanceof Number) {
            compId = ((Number) compIdObj).longValue();
        } else {
            compId = new BigDecimal(String.valueOf(compIdObj)).longValue();
        }
        return jsonPane.findById(compId);
    }
    
    public Object findByName(Object name) {
        return jsonPane.findByName(String.valueOf(name));
    }
    
    public List<Object> findsByType(Object types) {
        return jsonPane.findsByType(String.valueOf(types));
    }
    
    public List<Object> findsByTag(Object tag) {
        return jsonPane.findsByTag(String.valueOf(tag));
    }
}
