package org.duckdns.hjow.commons.util.classwrapper;

import java.io.Serializable;

/** 단순 Class Wrapper, 이미 존재하는 Class 타입 객체를 필드에 담아 get, set 사용 */
public class SimpleClassWrapper implements ClassWrapper, Serializable {
	private static final long serialVersionUID = 767202698376484395L;
	protected Class<?> wrappedClass;

    public SimpleClassWrapper() { }
    public SimpleClassWrapper(Class<?> wrappedClass) {
		this();
		this.wrappedClass = wrappedClass;
	}
    
	@Override
	public Class<?> getWrappedClass() {
		return wrappedClass;
	}

	public void setWrappedClass(Class<?> wrappedClass) {
		this.wrappedClass = wrappedClass;
	}
}
