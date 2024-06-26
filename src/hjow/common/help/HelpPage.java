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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import hjow.common.json.JsonObject;
import hjow.common.util.DataUtil;

public class HelpPage implements Serializable {
    private static final long serialVersionUID = -744780347128969657L;
    
    public static final int CONTENT_TEXT = 0;
    public static final int CONTENT_HTML = 1;
    
    protected int contentType = CONTENT_TEXT;
    protected Map<String, String> titles;   // key : locale (kr, en, ...), value : help title
    protected Map<String, String> contents; // key : locale (kr, en, ...), value : help contents
    
    protected transient String locale = System.getProperty("user.language");
    
    public HelpPage() {
        titles = new HashMap<String, String>();
        contents = new HashMap<String, String>();
        
        if(locale == null) locale = "en";
    }
    
    public HelpPage(JsonObject json) {
        this();
        
        String typeVal = String.valueOf(json.get("type"));
        if(typeVal.equals("TEXT")) {
            contentType = CONTENT_TEXT;
        } else if(typeVal.equals("HTML")) {
            contentType = CONTENT_HTML;
        } else {
            contentType = new BigDecimal(typeVal).intValue();
        }
        
        JsonObject titleObj = (JsonObject) json.get("titles");
        Set<String> keys = titleObj.keySet();
        for(String k : keys) {
            titles.put(k, String.valueOf(titleObj.get(k)));
        }
        
        JsonObject contentObj = (JsonObject) json.get("contents");
        keys = contentObj.keySet();
        for(String k : keys) {
            contents.put(k, String.valueOf(contentObj.get(k)));
        }
    }
    
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        
        switch(contentType) {
        case CONTENT_TEXT:
            json.put("type", "TEXT");
            break;
        case CONTENT_HTML:
            json.put("type", "HTML");
            break;
        default:
            json.put("type", String.valueOf(contentType));
            break;
        }
        
        JsonObject titleObj = new JsonObject();
        Set<String> keys = titles.keySet();
        for(String k : keys) {
            titleObj.put(k, titles.get(k));
        }
        json.put("titles", titleObj);
        
        JsonObject contentObj = new JsonObject();
        keys = contents.keySet();
        for(String k : keys) {
            contentObj.put(k, contents.get(k));
        }
        json.put("contents", contentObj);
        
        return json;
    }
    
    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getContents() {
        return contents;
    }
    
    public String getTitle() {
        return getTitle(null);
    }
    
    public String getTitle(String locale) {
        if(DataUtil.isEmpty(locale)) return titles.get(this.locale);
        return titles.get(locale);
    }
    
    public String getContent() {
        return getContent(null);
    }
    
    public String getContent(String locale) {
        if(DataUtil.isEmpty(locale)) return contents.get(this.locale);
        return contents.get(locale);
    }
    
    @Override
    public String toString() {
        return getTitle();
    }

    public void setContents(Map<String, String> contents) {
        this.contents = contents;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }
    
    public void addContents(String locale, String title, String content) {
        if(DataUtil.isEmpty(locale)) locale = "en";
        titles.put(locale, title);
        contents.put(locale, content);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
