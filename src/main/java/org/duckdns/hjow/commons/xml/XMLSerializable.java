package org.duckdns.hjow.commons.xml;

import java.io.Serializable;

import org.w3c.dom.Document;

public interface XMLSerializable extends Serializable {
    public Document toXMLDocument();
}
