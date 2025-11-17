package org.duckdns.hjow.commons.ui.graphics;

import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/** 2D 원 도형 */
public class OvalObject2D implements Object2D {
	private static final long serialVersionUID = -1441714764004875901L;
	protected Coordinate2D center;
	protected int r;
	protected Color color = Color.BLUE;
	public OvalObject2D() {}
	public Coordinate2D getCenter() {
		return center;
	}
	public void setCenter(Coordinate2D center) {
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
	@Override
	public Area area() {
		return new Area(new Ellipse2D.Double((double) getX(), (double)getY(), (double)getR(), (double)getR()));
	}
}
