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
package hjow.common.data;

import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hjow.common.util.SecurityUtil;
import hjow.common.xml.XMLSerializable;

/**
 * 이진 데이터를 관리하는 인스턴스를 만드는 클래스입니다/
 */
public class Binary implements XMLSerializable {
    private static final long serialVersionUID = 5047947104003858791L;
    protected byte[] binaryData;
    
    public Binary() {
        
    }
    public Binary(int size) {
        binaryData = new byte[size];
        zeroing();
    }
    public Binary(byte[] binary) {
        this.binaryData = binary;
    }
    public byte[] getBinaryData() {
        return binaryData;
    }
    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }
    public byte[] toByteArray() {
        return getBinaryData();
    }
    public int length() {
        return binaryData.length;
    }
    public byte getOneByte(int idx) {
        return binaryData[idx];
    }
    public void setOneByte(int idx, byte datum) {
        binaryData[idx] = datum;
    }
    public void zeroing() {
        for(int idx=0; idx<binaryData.length; idx++) {
            binaryData[idx] = 0;
        }
    }
    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean equals(Object targetObj) {
        if(targetObj == null) return false;
        
        if(targetObj instanceof Binary) {
            Binary binary = (Binary) targetObj;
            byte[] targets = binary.getBinaryData();
            if(this.binaryData.length != targets.length) return false;
            for(int idx=0; idx<this.binaryData.length; idx++) {
                if(targets[idx] != this.binaryData[idx]) return false;
            }
            return true;
        } else if(targetObj instanceof byte[]) {
            byte[] binary = (byte[]) targetObj;
            if(this.binaryData.length != binary.length) return false;
            for(int idx=0; idx<binaryData.length; idx++) {
                if(binary[idx] != binaryData[idx]) return false;
            }
            return true;
        } else if(targetObj instanceof ByteArrayOutputStream) {
            return equals(((ByteArrayOutputStream) targetObj).toByteArray());
        }
        return false;
    }
    @Override
    public Document toXMLDocument()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            doc.setXmlStandalone(true);
            
            String className = this.getClass().getName();

            Element root = doc.createElement(this.getClass().getSimpleName());
            root.setAttribute("class", className);
            doc.appendChild(root);
            root.setAttribute("classType", "binary");
            root.setNodeValue(SecurityUtil.hexString(getBinaryData()));
            return doc;
        }
        catch (ParserConfigurationException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        } 
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
