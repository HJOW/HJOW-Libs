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
package org.duckdns.hjow.commons.ui.extend;

import java.awt.Graphics;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.duckdns.hjow.commons.core.Releasable;
import org.duckdns.hjow.commons.data.Binary;
import org.duckdns.hjow.commons.script.PublicMethodOpenedClass;
import org.duckdns.hjow.commons.util.DataUtil;

public class ScriptGraphics extends PublicMethodOpenedClass implements Releasable {
    private static final long serialVersionUID = -8652615469025283373L;
    protected Graphics g;
    
    public ScriptGraphics(Graphics g) {
        this.g = g;
    }
    
    @Override
    public void releaseResource() {
        this.g = null;
    }
    
    protected int parseInt(Object obj) {
        if(obj instanceof Number) return ((Number) obj).intValue();
        return new BigDecimal(String.valueOf(obj)).intValue();
    }
    
    public void dispose() {
        g.dispose();
    }
    
    public void clearRect(Object x, Object y, Object width, Object height) {
        g.clearRect(parseInt(x), parseInt(y), parseInt(width), parseInt(height));
    }
    
    public void clipRect(Object x, Object y, Object width, Object height) {
        g.clipRect(parseInt(x), parseInt(y), parseInt(width), parseInt(height));
    }
    
    public void copyArea(Object x, Object y, Object width, Object height, Object dx, Object dy) {
        g.copyArea(parseInt(x), parseInt(y), parseInt(width), parseInt(height), parseInt(dx), parseInt(dy));
    }
    
    public ScriptGraphics createClone(Object x, Object y, Object width, Object height) {
        return new ScriptGraphics(g.create(parseInt(x), parseInt(y), parseInt(width), parseInt(height)));
    }
    
    public void draw3DRect(Object x, Object y, Object width, Object height, Object raised) {
        g.draw3DRect(parseInt(x), parseInt(y), parseInt(width), parseInt(height), DataUtil.parseBoolean(raised));
    }
    
    public void drawBytes(Object binary, Object offset, Object length, Object x, Object y) {
        byte[] bytes = null;
        if(binary instanceof byte[]) bytes = (byte[]) binary;
        if(binary instanceof Binary) bytes = ((Binary) binary).getBinaryData();
        if(binary instanceof ByteArrayOutputStream) bytes = ((ByteArrayOutputStream) binary).toByteArray();
        
        g.drawBytes(bytes, parseInt(offset), parseInt(length), parseInt(x), parseInt(y));
    }
    
    public void drawChars(Object str, Object offset, Object length, Object x, Object y) {
        g.drawChars(String.valueOf(str).toCharArray(), parseInt(offset), parseInt(length), parseInt(x), parseInt(y));
    }
    
    public void drawLine(Object x1, Object y1, Object x2, Object y2) {
        g.drawLine(parseInt(x1), parseInt(y1), parseInt(x2), parseInt(y2));
    }
    
    public void drawOval(Object x, Object y, Object width, Object height) {
        g.drawOval(parseInt(x), parseInt(y), parseInt(width), parseInt(height));
    }
    
    public void drawRect(Object x, Object y, Object width, Object height) {
        g.drawRect(parseInt(x), parseInt(y), parseInt(width), parseInt(height));
    }
    
    public void drawRoundRect(Object x, Object y, Object width, Object height, Object arcWidth, Object arcHeight) {
        g.drawRoundRect(parseInt(x), parseInt(y), parseInt(width), parseInt(height), parseInt(arcWidth), parseInt(arcHeight));
    }
    
    public void drawString(Object str, Object x, Object y) {
        g.drawString(String.valueOf(str), parseInt(x), parseInt(y));
    }
    
    public void fill3DRect(Object x, Object y, Object width, Object height, Object raised) {
        g.fill3DRect(parseInt(x), parseInt(y), parseInt(width), parseInt(height), DataUtil.parseBoolean(raised));
    }
    
    public void fillArc(Object x, Object y, Object width, Object height, Object startAngle, Object arcAngle) {
        g.fillArc(parseInt(x), parseInt(y), parseInt(width), parseInt(height), parseInt(startAngle), parseInt(arcAngle));
    }
    
    public void fillOval(Object x, Object y, Object width, Object height) {
        g.fillOval(parseInt(x), parseInt(y), parseInt(width), parseInt(height));
    }
    
    public void fillRect(Object x, Object y, Object width, Object height) {
        g.fillRect(parseInt(x), parseInt(y), parseInt(width), parseInt(height));
    }
    
    public void fillRoundRect(Object x, Object y, Object width, Object height, Object arcWidth, Object arcHeight) {
        g.fillRoundRect(parseInt(x), parseInt(y), parseInt(width), parseInt(height), parseInt(arcWidth), parseInt(arcHeight));
    }
}
