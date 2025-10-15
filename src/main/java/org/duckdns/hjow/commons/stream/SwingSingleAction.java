package org.duckdns.hjow.commons.stream;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingWorker;

import org.duckdns.hjow.commons.core.Disposeable;

public abstract class SwingSingleAction extends SwingWorker<Object, Void> implements Disposeable {
	protected int key;
	protected transient SingleAction action;
	protected transient Throwable exception;
	protected transient SwingSimultaneousWork superInstance;
	protected transient Map<String, Object> parameters = new HashMap<String, Object>();
	
	@Override
	protected void done() {
		try {
			get();
		} catch(Throwable t) { exception = t; }
		if(superInstance != null) superInstance.onWorkEnd(this);
		dispose();
	}
	
	@Override
	protected Object doInBackground() throws Exception {
		if(action != null) { try { action.run(getKey()); } catch(Throwable t) { exception = t; } }
		return null;
	}
	
	
	public SingleAction getAction() {
		return action;
	}
	public void setAction(SingleAction action) {
		this.action = action;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	public void setSuperInstance(SwingSimultaneousWork superInstance) {
		this.superInstance = superInstance;
	}
	
	public void setParameter(String key, Object val) {
		parameters.put(key, val);
	}
	
	@Override
	public void dispose() {
		parameters.clear();
		superInstance = null;
		action = null;
	}
}
