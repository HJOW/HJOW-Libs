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
package hjow.common.script;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import hjow.common.core.Core;
import hjow.common.data.Binary;
import hjow.common.json.JsonArray;
import hjow.common.json.JsonObject;
import hjow.common.util.DataUtil;

public class PrimitiveObject extends ScriptObject {
    private static final long serialVersionUID = 2331135498892470240L;
    
    private static final PrimitiveObject uniqueObject = new PrimitiveObject();
    private PrimitiveObject() { }
    public static PrimitiveObject getInstance() {
        return uniqueObject;
    }
    @Override
    public String getPrefixName() {
        return "priv";
    }
    @Override
    public String getInitScript(String accessKey) {
        StringBuilder initScript = new StringBuilder("");
        
        initScript = initScript.append("function String(obj) {                                                                     ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".convertString(obj);                   ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function Date(dateStr, formats) {                                                          ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".convertDate(dateStr, formats);        ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function convertDateFromTimeMills(timeMills) {                                             ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".convertDateFromTimeMills(timeMills);  ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function today() {                                                                         ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".today();                              ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function parseInt(obj) {                                                                   ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".parseInt(obj);                        ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function parseFloat(obj) {                                                                 ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".parseFloat(obj);                      ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function list() {                                                                          ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".list();                               ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function Array() {                                                                         ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".list();                               ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function map() {                                                                           ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".map();                                ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function Object() {                                                                        ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".map();                                ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function isEmpty(pbj) {                                                                    ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".isEmpty(obj);                         ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function isInteger(pbj) {                                                                  ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".isInteger(obj);                       ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function parseBoolean(pbj) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".parseBoolean(obj);                    ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function subtractDate(one, two) {                                                          ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".subtractDate(one, two);               ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function isNull(obj) {                                                                     ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".isNull(obj);                          ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function getClassType(obj) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".getClassType(obj);                    ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function isEquals(one, two) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".isEquals(one, two);                   ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function splitString(one, two) {                                                           ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".splitString(one, two);                ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function createEmptyByteArray(size) {                                                      ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".createEmptyByteArray(size);           ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function emptyBytes(size) {                                                                ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".createEmptyByteArray(size);           ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function newJsonObject() {                                                                 ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".newJsonObject();                      ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function newJsonArray() {                                                                  ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".newJsonArray();                       ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function parseJson(a) {                                                                    ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".parseJson(a);                         ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function translate(obj) {                                                                  ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".translate(obj);                       ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function getAppParamKeys() {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".getAppParamKeys();                    ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function getAppParam(obj) {                                                                ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".getAppParam(obj);                     ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        
        return initScript.toString();
    }
    
    public String convertString(Object obj) {
        if(obj instanceof byte[]) {
            try {
                return new String((byte[]) obj, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else if(obj instanceof Binary) {
        	return convertString(((Binary) obj).toByteArray());
        }
        return String.valueOf(obj);
    }
    
    public Date convertDate(Object obj, Object formats) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(convertString(formats));
        return formatter.parse(convertString(obj));
    }
    public Date convertDateFromTimeMills(Object timeMills) throws ParseException {
        long timeMillVal = -1;
        if(timeMills instanceof Number) timeMillVal = ((Number) timeMills).longValue();
        else timeMillVal = Long.parseLong(String.valueOf(timeMills));
        return new Date(timeMillVal);
    }
    public Date today() {
        return Calendar.getInstance().getTime();
    }
    public int parseInt(Object obj) {
        return new BigDecimal(convertString(obj)).intValue();
    }
    public double parseFloat(Object obj) {
        return Double.parseDouble(convertString(obj));
    }
    public List<Object> list() {
        return new ArrayList<Object>();
    }
    public Map<Object, Object> map() {
        return new HashMap<Object, Object>();
    }
    public boolean isEmpty(Object ob) {
        return DataUtil.isEmpty(ob);
    }
    public boolean isInteger(Object ob) {
        return DataUtil.isInteger(ob);
    }
    public boolean isNumber(Object ob) {
        return DataUtil.isNumber(ob);
    }
    public boolean parseBoolean(Object ob) {
        return DataUtil.parseBoolean(ob);
    }
    public Date subtractDate(Object one, Object two) throws ParseException {
        Date oneDate = null;
        Date twoDate = null;
        
        if(one instanceof Date) oneDate = (Date) one;
        else oneDate = convertDate(one, "yyyyMMdd");
        
        if(two instanceof Date) twoDate = (Date) two;
        else twoDate = convertDate(two, "yyyyMMdd");
        
        return DataUtil.subtract(oneDate, twoDate);
    }
    public boolean isNull(Object obj) {
        return obj == null;
    }
    public String getClassType(Object obj) {
        if(obj == null) return "null";
        if(obj instanceof Object[])   return "array";
        if(obj instanceof JsonArray)  return "array";
        if(obj instanceof byte[])     return "bytes";
        if(obj instanceof Binary)     return "bytes";
        if(obj instanceof String)     return "string";
        if(obj instanceof JsonObject) return "object";
        if(obj instanceof List<?>)    return "list";
        if(obj instanceof Map<?, ?>)  return "map";
        return obj.getClass().getSimpleName();
    }
    public boolean isEquals(Object one, Object two) {
        return one.equals(two);
    }
    public List<String> splitString(Object originals, Object delimiters) {
        StringTokenizer tokenizer = new StringTokenizer(String.valueOf(originals), String.valueOf(delimiters));
        List<String> results = new ArrayList<String>();
        while(tokenizer.hasMoreTokens()) {
            results.add(tokenizer.nextToken());
        }
        return results;
    }
    public Binary createEmptyByteArray(Object size) {
        return new Binary(parseInt(size));
    }
    public JsonObject newJsonObject() {
        return new JsonObject();
    }
    public JsonArray newJsonArray() {
        return new JsonArray();
    }
    public Object parseJson(Object jsonString) throws Throwable {
        if(jsonString == null) return null;
        if(jsonString instanceof Number ) return jsonString;
        if(jsonString instanceof Boolean) return jsonString;
        return JsonObject.parseJson(String.valueOf(jsonString));
    }
    public String translate(Object obj) {
        return Core.trans(convertString(obj));
    }
    public String getAppParam(Object keyOf) {
    	return Core.getApplicationParameter(convertString(keyOf));
    }
    public List<String> getAppParamKeys() {
    	return Core.getApplicationParameterKeys();
    }
}
