package org.duckdns.hjow.commons.ui.graphics;

import java.io.Serializable;
import java.math.BigDecimal;

import org.duckdns.hjow.commons.json.JsonObject;
import org.duckdns.hjow.commons.util.DataUtil;

/** 2D 좌표 */
public class Coordinate2D implements Serializable {
	private static final long serialVersionUID = 1080468103919411939L;
	protected long x, y;
	public Coordinate2D() {}
	public Coordinate2D(long x, long y) {this.x = x; this.y = y;}

	public long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public long getY() {
		return y;
	}

	public void setY(long y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
	
	@Override
	public boolean equals(Object others) {
		if(others == null) return false;
		if(others instanceof Coordinate2D) {
			Coordinate2D o = (Coordinate2D) others;
			return (o.getX() == getX() && o.getY() == getY());
		}
		return false;
	}
	
	/** JSON 객체로 변환 */
	public JsonObject toJSON() {
		JsonObject json = new JsonObject();
		json.put("x", String.valueOf(getX()));
		json.put("y", String.valueOf(getY()));
		return json;
	}
	
	/** 다른 좌표와의 거리 계산 */
	public long getDistance(Coordinate2D others) {
		BigDecimal dx = new BigDecimal(String.valueOf(getX() - others.getX()));
		BigDecimal dy = new BigDecimal(String.valueOf(getY() - others.getY()));
		
		BigDecimal sum = dx.pow(2).add(dy.pow(2));
		return DataUtil.sqrt(sum, 50).longValue();
	}
}
