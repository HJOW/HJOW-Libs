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
package org.duckdns.hjow.commons.script;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathObject extends ScriptObject {
    private static final long serialVersionUID = 2331133498892470240L;
    
    private static final MathObject uniqueObject = new MathObject();
    private MathObject() { }
    public static MathObject getInstance() {
        return uniqueObject;
    }
    @Override
    public String getPrefixName() {
        return "math";
    }
    @Override
    public String getInitScript(String accessKey) {
        StringBuilder initScript = new StringBuilder("");
        
        initScript = initScript.append("function math_PI() {                                                                 ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".getPI();                        ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_E() {                                                                  ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".getE();                         ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_abs(a) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".abs(a);                         ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_acos(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".acos(a);                        ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_asin(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".asin(a);                        ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_atan(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".atan(a);                        ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_cbrt(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".cbrt(a);                        ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_ceil(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".ceil(a);                        ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_cos(a) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".cos(a);                         ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_sin(a) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".sin(a);                         ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_tan(a) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".tan(a);                         ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_exp(a) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".exp(a);                         ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_floor(a) {                                                             ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".floor(a);                       ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_getExponent(a) {                                                       ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".getExponent(a);                 ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_log(a) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".log(a);                         ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_log10(a) {                                                             ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".log10(a);                       ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_isNegative(a) {                                                        ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".isNegative(a);                  ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_isPositive(a) {                                                        ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".isPositive(a);                  ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_compare(a, b) {                                                        ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".compare(a, b);                  ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_pow(a, b) {                                                            ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".pow(a, b);                      ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_random() {                                                             ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".random();                       ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_round(a) {                                                             ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".round(a);                       ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_sqrt(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".sqrt(a);                        ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_toDegrees(a) {                                                         ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".toDegrees(a);                   ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_toRadians(a) {                                                         ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".toRadians(a);                   ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_rint(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".rint(a);                        ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        
        initScript = initScript.append("function math_bigInteger(a) {                                                        ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".bigInteger(a);                  ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function math_bigDecimal(a) {                                                        ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".bigDecimal(a);                  ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        
        return initScript.toString();
    }
    private double parseDouble(Object a) {
        Number num = null;
        
        if(a instanceof Number) num = (Number) a;
        else num = new Double(String.valueOf(a));
        return num.doubleValue();
    }
    public double getPI() {
        return Math.PI;
    }
    
    public double getE() {
        return Math.E;
    }
    
    public double abs(Object a) {
        return Math.abs(parseDouble(a));
    }
    public double acos(Object a) {
        return Math.acos(parseDouble(a));
    }
    public double asin(Object a) {
        return Math.asin(parseDouble(a));
    }
    public double atan(Object a) {
        return Math.atan(parseDouble(a));
    }
    
    public double cbrt(Object a) {
        return Math.cbrt(parseDouble(a));
    }
    public double ceil(Object a) {
        return Math.ceil(parseDouble(a));
    }
    public double cos(Object a) {
        return Math.cos(parseDouble(a));
    }
    public double sin(Object a) {
        return Math.sin(parseDouble(a));
    }
    public double tan(Object a) {
        return Math.tan(parseDouble(a));
    }
    public double exp(Object a) {
        return Math.exp(parseDouble(a));
    }
    public double floor(Object a) {
        return Math.floor(parseDouble(a));
    }
    public double getExponent(Object a) {
        return Math.getExponent(parseDouble(a));
    }
    public double log(Object a) {
        return Math.log(parseDouble(a));
    }
    public double log10(Object a) {
        return Math.log10(parseDouble(a));
    }
    public int compare(Object a, Object b) {
        return new Double(parseDouble(a)).compareTo(new Double(parseDouble(b)));
    }
    public boolean isPositive(Object a) {
        return parseDouble(a) > 0;
    }
    public boolean isNegative(Object a) {
        return parseDouble(a) < 0;
    }
    public double pow(Object a, Object b) {
        return Math.pow(parseDouble(a), parseDouble(b));
    }
    public double random() {
        return Math.random();
    }
    public double round(Object a) {
        return Math.round(parseDouble(a));
    }
    public double sqrt(Object a) {
        return Math.sqrt(parseDouble(a));
    }
    public double toDegrees(Object a) {
        return Math.toDegrees(parseDouble(a));
    }
    public double toRadians(Object a) {
        return Math.toRadians(parseDouble(a));
    }
    public double rint(Object a) {
        return Math.rint(parseDouble(a));
    }
    public BigInteger bigInteger(Object a) {
        return new BigInteger(String.valueOf(a));
    }
    public BigDecimal bigDecimal(Object a) {
        return new BigDecimal(String.valueOf(a));
    }
}
