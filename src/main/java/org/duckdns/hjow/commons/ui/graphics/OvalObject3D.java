package org.duckdns.hjow.commons.ui.graphics;

import java.awt.Color;

/** 3D 구 도형 */
public class OvalObject3D implements Object3D {
	private static final long serialVersionUID = -7351115784660813970L;
	protected Coordinate3D center;
	protected int r;
	protected Color color = Color.BLUE;
	public OvalObject3D() {}
	public Coordinate3D getCenter() {
		return center;
	}
	public void setCenter(Coordinate3D center) {
		this.center = center;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public long getX() {
		return center.getX();
	}
	public long getY() {
		return center.getY();
	}
	public long getZ() {
		return center.getZ();
	}
}
