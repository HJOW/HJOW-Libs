package org.duckdns.hjow.commons.ui.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Area;

/** 텍스트 출력을 위한 객체 */
public class TextObject2D implements Object2D {
    private static final long serialVersionUID = -1577361436962945761L;
    protected Coordinate2D left;
    protected String content;
    protected Color color = Color.BLUE;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    @Override
    public Area area() {
        return null; // 미지원 (텍스트의 정확한 길이를 알려면 FontMetrics 객체를 구해야 함
    }
    @Override
	public Area area(Graphics2D g) {
    	return getBox(g).area(g);
	}
    /** 이 텍스트의 가상의 테두리 사각형을 반환 */
    public RectObject2D getBox(Graphics2D g) {
    	FontMetrics metric = g.getFontMetrics();
    	int w = metric.stringWidth(getContent());
    	int h = metric.getHeight();
    	
    	RectObject2D rc = new RectObject2D();
    	rc.setLeftTop(getLeft());
    	rc.setW(w);
    	rc.setH(h);
    	
    	return rc;
    }
    public Coordinate2D getLeft() {
        return left;
    }
    public void setLeft(Coordinate2D left) {
        this.left = left;
    }
}
