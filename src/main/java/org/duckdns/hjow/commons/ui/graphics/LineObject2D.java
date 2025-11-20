package org.duckdns.hjow.commons.ui.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

/** 직선 도형 */
public class LineObject2D implements Object2D {
	private static final long serialVersionUID = 763902670992651439L;
	protected Coordinate2D from, to;
	protected Color color = Color.BLUE;
	public LineObject2D() {}
	public Coordinate2D getFrom() {
		return from;
	}
	public void setFrom(Coordinate2D from) {
		this.from = from;
	}
	public Coordinate2D getTo() {
		return to;
	}
	public void setTo(Coordinate2D to) {
		this.to = to;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	@Override
	public Area area(Graphics2D g) {
		return new Area(new Line2D.Double((double) from.getX(), (double) from.getY(), (double) to.getX(), (double) to.getY()));
	}
	@Override
	public Area area() {
		return area(null);
	}
}
