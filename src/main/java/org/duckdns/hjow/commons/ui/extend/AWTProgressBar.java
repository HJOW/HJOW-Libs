/*
 
 Copyright 2015 HJOW

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
package org.duckdns.hjow.commons.ui.extend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextField;
import java.util.List;

import org.duckdns.hjow.commons.script.PublicMethodOpenedClass;

/**
 * <p>AWT 기반 상태바 컴포넌트입니다. AWT와 호환됩니다.</p>
 * 
 * @author HJOW
 *
 */
public class AWTProgressBar extends TextField
{
    private static final long serialVersionUID = -5734354961048376337L;
    protected int nowState = 0;
    protected int maxValue = 100;
    protected int minValue = 0;
    protected boolean useGradient = true;

    /**
     * <p>상태바 객체를 만듭니다. 최대값은 100, 최소값은 0으로 기본값이 설정됩니다.</p>
     * 
     */
    public AWTProgressBar()
    {
        super(20);
        setEditable(false);
        setForeground(Color.GREEN);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        Color c;
        
        if(useGradient)
        {
            c = new Color((int)(getForeground().getRed() / 1.3), (int)(getForeground().getGreen() / 1.3), (int)(getForeground().getBlue() / 1.3));
            g.setColor(c);
            g.fillRect(getX(), getY(), ((int) Math.round((((double) (nowState - minValue)) / ((double) (maxValue - minValue))) * ((double) getWidth()))), 1);
            
            c = new Color((int)(getForeground().getRed() / 1.1), (int)(getForeground().getGreen() / 1.1), (int)(getForeground().getBlue() / 1.1));
            g.setColor(c);
            g.fillRect(getX(), getY() + 1, ((int) Math.round((((double) (nowState - minValue)) / ((double) (maxValue - minValue))) * ((double) getWidth()))), 2);
            
            c = getForeground();
            g.setColor(c);
            g.fillRect(getX(), getY() + 3, ((int) Math.round((((double) (nowState - minValue)) / ((double) (maxValue - minValue))) * ((double) getWidth()))), getHeight() - 6);
            
            c = new Color((int)(getForeground().getRed() / 1.1), (int)(getForeground().getGreen() / 1.1), (int)(getForeground().getBlue() / 1.1));
            g.setColor(c);
            g.fillRect(getX(), getY() + getHeight() - 3, ((int) Math.round((((double) (nowState - minValue)) / ((double) (maxValue - minValue))) * ((double) getWidth()))), 2);
            
            c = new Color((int)(getForeground().getRed() / 1.3), (int)(getForeground().getGreen() / 1.3), (int)(getForeground().getBlue() / 1.3));
            g.setColor(c);
            g.fillRect(getX(), getY() + getHeight() - 1, ((int) Math.round((((double) (nowState - minValue)) / ((double) (maxValue - minValue))) * ((double) getWidth()))), 1);
        }
        else
        {
            g.setColor(getForeground());
            g.fillRect(getX(), getY(), ((int) Math.round((((double) (nowState - minValue)) / ((double) (maxValue - minValue))) * ((double) getWidth()))), getHeight());
        }
    }
    
    
    public void setValue(int n)
    {
        nowState = n;
        repaint();
    }

    public int getNowState()
    {
        return nowState;
    }

    public void setNowState(int nowState)
    {
        setValue(nowState);
    }

    public int getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public int getMinValue()
    {
        return minValue;
    }

    public void setMinValue(int minValue)
    {
        this.minValue = minValue;
    }

    public boolean isUseGradient()
    {
        return useGradient;
    }

    public void setUseGradient(boolean useGradient)
    {
        this.useGradient = useGradient;
    }
    
    public List<String> availables() {
        return PublicMethodOpenedClass.getAvailableMethods(this);
    }
}
