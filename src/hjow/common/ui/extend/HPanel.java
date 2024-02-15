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
package hjow.common.ui.extend;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import hjow.common.script.PublicMethodOpenedClass;

public class HPanel extends JPanel implements AlphaRatioEditable {
	private static final long serialVersionUID = 3111418734501422000L;
	protected float alphaRatio = 0.9F;
	protected float childAlpha = 0.9F;
	
	public List<String> availables() {
    	return PublicMethodOpenedClass.getAvailableMethods(this);
    }
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.setColor(getBackground());
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaRatio));
	    super.paintComponent(graphics);
	}
	
	@Override
	public void paintChildren(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.setColor(getBackground());
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, childAlpha));
	    super.paintChildren(graphics);
	}

	public float getAlphaRatio() {
		return alphaRatio;
	}

	@Override
	public void setAlphaRatio(float alphaRatio) {
		if(alphaRatio < 1.0) setOpaque(true);
		else setOpaque(false);
		this.alphaRatio = alphaRatio;
	}

	public float getChildAlpha() {
		return childAlpha;
	}

	public void setChildAlpha(float childAlpha) {
		this.childAlpha = childAlpha;
	}
	
	/** 패널에 붙은 하위 컴포넌트들에게 투명도를 부여합니다. 그 아래 자식/손자 중에도 패널이 있으면 마찬가지로 동일한 동작을 합니다. */
	public void setAlphaRatioRecursively(float alphaRatio) {
		Component[] children = getComponents();
		if(children != null) {
			for(Component c : children) {
				if(c instanceof AlphaRatioEditable) {
					((AlphaRatioEditable) c).setAlphaRatio(alphaRatio);
				}
			}
		}
		setAlphaRatio(alphaRatio);
	}
	
	/** 패널에 붙은 하위 컴포넌트들을 패널에서 제거합니다. 그 아래 자식/손자 중에도 패널이 있으면 마찬가지로 동일한 동작을 한 뒤 제거합니다. */
	public void removeAllRecursively() {
		Component[] children = getComponents();
		if(children != null) {
			for(Component c : children) {
				if(c instanceof Container) {
					((Container) c).removeAll();
				}
			}
		}
		removeAll();
	}
}
