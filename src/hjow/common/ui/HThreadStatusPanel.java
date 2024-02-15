/*
Copyright 2019 HJOW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package hjow.common.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import hjow.common.core.Releasable;
import hjow.common.thread.HThread;
import hjow.common.thread.RiskyRunnable;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HProgressBar;
import hjow.common.ui.extend.HTextField;

public class HThreadStatusPanel extends HPanel implements Releasable {
	private static final long serialVersionUID = -1785082144443003003L;

	protected HThread thread;
	protected HTextField tfThread; 
	protected long timeMillGapMax;
	protected HProgressBar progressMax, progressNow, progressMin;
	
	protected static transient HThread refresher;
	protected static transient Vector<HThreadStatusPanel> activatedThreadMonitor;
	protected static transient boolean exiting = false;
	
	public HThreadStatusPanel() {
		super();
		setLayout(new BorderLayout());
		
		JPanel pnProgress = new JPanel();
		add(pnProgress, BorderLayout.CENTER);
		
		JPanel[] pnProgressPns = new JPanel[4];
		pnProgress.setLayout(new GridLayout(pnProgressPns.length, 1));
		
		for(int idx=0; idx<pnProgressPns.length; idx++) {
			pnProgressPns[idx] = new JPanel();
			pnProgress.add(pnProgressPns[idx]);
			
			pnProgressPns[idx].setLayout(new BorderLayout());
		}
		
		tfThread = new HTextField();
		progressMax = new HProgressBar(JProgressBar.HORIZONTAL, 0, 1);
		progressNow = new HProgressBar(JProgressBar.HORIZONTAL, 0, 1);
		progressMin = new HProgressBar(JProgressBar.HORIZONTAL, 0, 1);
		
		tfThread.setEditable(false);
		
		pnProgressPns[0].add(tfThread   , BorderLayout.CENTER);
		pnProgressPns[1].add(progressMax, BorderLayout.CENTER);
		pnProgressPns[2].add(progressNow, BorderLayout.CENTER);
		pnProgressPns[3].add(progressMin, BorderLayout.CENTER);
		
		prepareRefresher(this);
	}
	
	public void setThread(HThread thread) {
		this.thread = thread;
		refresh();
	}
	
	public synchronized void refresh() {
		if(thread == null) {
			tfThread.setText("");
			progressNow.setValue(0);
			progressNow.setMaximum(1);
			progressMax.setValue(0);
			progressMax.setMaximum(1);
			progressMin.setValue(0);
			progressMin.setMaximum(1);
			return;
		}
		tfThread.setText(thread.getName());
		long nowTimeMillMax = thread.getMaxElapsedTime();
		if(timeMillGapMax != nowTimeMillMax) {
			timeMillGapMax = nowTimeMillMax;
			progressNow.setMaximum(createValue(timeMillGapMax));
			progressMax.setMaximum(createValue(timeMillGapMax));
			progressMin.setMaximum(createValue(timeMillGapMax));
		}
		progressNow.setValue(createValue(thread.getLastElapsedTime()));
		progressMin.setValue(createValue(thread.getMinElapsedTime()));
		progressMax.setValue(createValue(timeMillGapMax));
	}
	
	protected int createValue(long timeMills) {
		return (int) (Math.round((timeMills / 100) / 10.0));
	}

	@Override
	public void releaseResource() {
		if(refresher != null) {
			if(activatedThreadMonitor != null) {
				activatedThreadMonitor.remove(this);
			} else {
				releaseAll(this);
			}
		}
	}
	
	public HThread getThread() {
		return thread;
	}
	
	protected static synchronized void prepareRefresher(HThreadStatusPanel obj) {
		if(exiting) throw new RuntimeException("The program will be exited.");
		if(activatedThreadMonitor == null) {
			activatedThreadMonitor = new Vector<HThreadStatusPanel>();
		}
		if(obj != null) {
			activatedThreadMonitor.add(obj);
		}
		if(refresher == null) {
			refresher = new HThread(new RiskyRunnable() {
				@Override
				public void run() throws Throwable {
					for(HThreadStatusPanel p : activatedThreadMonitor) {
						p.refresh();
					}
				}
			}, null);
			refresher.setName("Thread monitor");
			refresher.setGap(500);
			refresher.start();
		}
	}
	
	public static synchronized void releaseAll() {
		releaseAll(null);
	}
	protected static synchronized void releaseAll(HThreadStatusPanel caller) {
		exiting = true;
		if(activatedThreadMonitor != null) {
			for(HThreadStatusPanel p : activatedThreadMonitor) {
				if(caller != null && p == caller) continue;
				try { p.releaseResource(); } catch(Throwable t) {}
			}
			activatedThreadMonitor.clear();
			activatedThreadMonitor = null;
		}
		
		if(refresher != null)
			refresher.releaseResource();
		refresher = null;
	}
}
