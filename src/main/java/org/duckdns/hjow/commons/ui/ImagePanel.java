package org.duckdns.hjow.commons.ui;

import java.awt.Graphics;
import java.awt.Image;

import org.duckdns.hjow.commons.ui.extend.HPanel;

/** 이미지 출력용 컴포넌트 */
public class ImagePanel extends HPanel {
	private static final long serialVersionUID = -3363354047576598055L;
    protected Image image;
	public ImagePanel(Image image) {
    	super();
    	setLayout(null);
    	setImage(image);
    }
	
	public ImagePanel() {
    	super();
    	setLayout(null);
    	setVisible(false);
    }
	
	public void clear() {
		image = null;
		setVisible(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image != null) g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		
		if(this.image != null) {
			repaint();
			setVisible(true);
		} else {
			setVisible(false);
		}
	}
}
