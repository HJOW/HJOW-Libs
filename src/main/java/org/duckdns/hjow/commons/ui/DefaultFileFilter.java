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
package org.duckdns.hjow.commons.ui;

import java.io.File;
import java.util.List;

import org.duckdns.hjow.commons.script.PublicMethodOpenedClass;

public class DefaultFileFilter extends javax.swing.filechooser.FileFilter {
    protected String prefix, description;
    
    public DefaultFileFilter() {
        
    }
    
    public DefaultFileFilter(String prefix, String description) {
        this.prefix = prefix;
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        String path = f.getAbsolutePath();
        path = path.trim().toLowerCase();
        return path.endsWith("." + prefix);
    }

    @Override
    public String getDescription() {
        return description + " (*." + prefix + ")";
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<String> availables() {
        return PublicMethodOpenedClass.getAvailableMethods(this);
    }
}
