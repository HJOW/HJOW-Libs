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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JDesktopPane;

public class HDesktopPane extends JDesktopPane implements AlphaRatioEditable {
    private static final long serialVersionUID = 9221243459281438493L;
    protected float alphaRatio = 0.7F;
    protected float childAlpha = 0.7F;
    public HDesktopPane() {
        super();
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
}
