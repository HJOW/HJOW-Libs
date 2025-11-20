package org.duckdns.hjow.commons.ui.graphics;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.io.Serializable;

/** 2D 객체임을 표시 */
public interface Object2D extends Serializable {
	@Deprecated
    public Area area();
	
    public Area area(Graphics2D g);
}
