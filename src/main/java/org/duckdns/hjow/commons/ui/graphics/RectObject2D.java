package org.duckdns.hjow.commons.ui.graphics;

import java.awt.Rectangle;
import java.awt.geom.Area;

public class RectObject2D implements Object2D {
	private static final long serialVersionUID = -7567482184562278087L;
	protected Coordinate2D leftTop;
	protected long w, h;
	public RectObject2D() {}
	public Coordinate2D getLeftTop() {
		return leftTop;
	}
	public void setLeftTop(Coordinate2D leftTop) {
		this.leftTop = leftTop;
	}
	public long getW() {
		return w;
	}
	public void setW(long w) {
		this.w = w;
	}
	public long getH() {
		return h;
	}
	public void setH(long h) {
		this.h = h;
	}
	@Override
	public Area area() {
		return new Area(new Rectangle((int) getLeftTop().getX(), (int) getLeftTop().getY(), (int) getW(), (int) getH()));
	}
}
