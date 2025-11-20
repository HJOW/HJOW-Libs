package org.duckdns.hjow.commons.ui.graphics;

import java.io.Serializable;
import java.math.BigDecimal;

import org.duckdns.hjow.commons.json.JsonObject;
import org.duckdns.hjow.commons.util.DataUtil;

/** 3D 좌표 */
public class Coordinate3D implements Serializable {
	private static final long serialVersionUID = -3845428403760941094L;
	protected long x, y, z;
	public Coordinate3D() {}
	public Coordinate3D(long x, long y, long z) {this.x = x; this.y = y; this.z = z;}

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
	
	public long getZ() {
		return z;
	}
	
	public void setZ(long z) {
		this.z = z;
	}
	
	/** 2D 영역에 투사, 새 2D 좌표 반환 (Z값이 초기화되지 않음) */
	public Coordinate2D project(Coordinate3D camera, double screenCenterX, double screenCenterY) {
		return project(camera, 500, screenCenterX, screenCenterY);
	}
	
	/** 2D 영역에 투사, 새 2D 좌표 반환 (Z값이 초기화되지 않음) focalLength 는 카메라의 초점 거리를 의미 */
	public Coordinate2D project(Coordinate3D camera, double focalLength, double screenCenterX, double screenCenterY) {
		// Translate point relative to camera
        double x_prime = getX() - camera.getX();
        double y_prime = getY() - camera.getY();
        double z_prime = getZ() - camera.getZ();

        // Apply perspective projection formula
        double scale = focalLength / z_prime;
        double x2d = (x_prime * scale) + screenCenterX;
        double y2d = (y_prime * scale) + screenCenterY;

        return new Coordinate2D((long) x2d, (long) y2d);
	}
	
	/** 2D 영역에 투사, 새 2D 좌표 반환 (Z값이 초기화되지 않음) focalLength 는 카메라의 초점 거리를 의미, yaw 는 카메라의 X축 회전 (방향), pitch 는 카메라의 Y축 회전 (방향) */
	public Coordinate2D project(Coordinate3D camera, double yaw, double pitch, double focalLength, double screenCenterX, double screenCenterY) {
		// Translate point relative to camera
        double dx = getX() - camera.getX();
        double dy = getY() - camera.getY();
        double dz = getZ() - camera.getZ();
        
        // yaw (Y축 회전)
        double cosY = Math.cos(yaw);
        double sinY = Math.sin(yaw);
        double tx = dx * cosY - dx * sinY;
        double tz = dx * sinY + dz * cosY;
        dx = tx;
        dz = tz;
        
        // pitch (X축 회전)
        double cosP = Math.cos(pitch);
        double sinP = Math.sin(pitch);
        double ty = dy * cosP - dz * sinP;
        tz = dy * sinP + dz * cosP;
        dy = ty;
        dz = tz;
        
        double cx = dx;
        double cy = dy;
        double cz = dz;
        
        if(cz <= 0) return null; // 카메라 뒤에 있는 경우 제외
        
        double px = focalLength * cx / cz;
        double py = focalLength * cy / cz;
        
        double u = px + screenCenterX;
        double v = py + screenCenterY;
        
        return new Coordinate2D((long) u, (long) v);
	}
	
	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
	}
	
	/** JSON 객체로 변환 */
	public JsonObject toJSON() {
		JsonObject json = new JsonObject();
		json.put("x", String.valueOf(getX()));
		json.put("y", String.valueOf(getY()));
		json.put("z", String.valueOf(getZ()));
		return json;
	}
	
	/** 다른 좌표와의 거리 계산 */
	public long getDistance(Coordinate3D others) {
		BigDecimal dx = new BigDecimal(String.valueOf(getX() - others.getX()));
		BigDecimal dy = new BigDecimal(String.valueOf(getY() - others.getY()));
		BigDecimal dz = new BigDecimal(String.valueOf(getZ() - others.getZ()));
		
		BigDecimal sum = dx.pow(2).add(dy.pow(2)).add(dz.pow(2));
		return DataUtil.sqrt(sum, 50).longValue();
	}
}
