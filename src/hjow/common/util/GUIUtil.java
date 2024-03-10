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

package hjow.common.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.FontUIResource;

import hjow.common.core.Core;

/**
 * <p>이 클래스에는 GUI에 관련된 여러 정적 메소드들이 있습니다.</p>
 * 
 * @author HJOW
 *
 */
public class GUIUtil
{
    /**
     * 
     * <p>글꼴 객체들입니다.</p>
     */
    public static Font usingFont, usingFont2, usingFontB, usingFont2B, usingFontP;
    
    /**
     * 
     * <p>기본 글꼴 크기입니다.</p>
     */
    public static int default_fontSize = 12;
    
    /**
     * 
     * <p>기본 글꼴 이름입니다.</p>
     */
    public static String usingFontName = null;
    
    /**
     * 
     * <p>글꼴을 불러옵니다.</p>
     */
    public static void prepareFont() 
    {
    	prepareFont(null);
    }
    
    /**
     * 
     * <p>글꼴을 불러옵니다.</p>
     */
    public static void prepareFont(Core core)
    {
        boolean font_loaded = false;
        String osName = Core.getProperty("os_name");
        String locale = Core.getProperty("user_language");
        int fontSize = default_fontSize;
        
        if(osName == null)
        {
            osName = System.getProperty("os.name");
        }
        if(locale == null)
        {
            locale = System.getProperty("user.language");
        }
        if(locale.equalsIgnoreCase("auto"))
        {
            locale = System.getProperty("user.language");
        }
        
        try
        {
            fontSize = Integer.parseInt(Core.getProperty("fontSize"));
        }
        catch(Throwable e)
        {
            fontSize = default_fontSize;
        }
        
        InputStream infs = null;
        FileInputStream finfs = null;
        ObjectInputStream objs = null;
        
        String fontPath = "";
    	if(core != null && Core.hasPropertyKey("font_path")) fontPath = core.getConfigPath();
    	else fontPath = Core.getProperty("font_path");
        
        if(osName.startsWith("Windows") || osName.startsWith("windows") || osName.startsWith("WINDOWS"))
        {
            try
            {
                File fontFile = new File(fontPath + "basic_font.ttf");
                if(fontFile.exists())
                {
                    finfs = new FileInputStream(fontFile);
                    infs = new BufferedInputStream(finfs);
                    usingFont = Font.createFont(Font.TRUETYPE_FONT, infs);
                    usingFont = usingFont.deriveFont(Font.PLAIN, fontSize);
                    usingFont2 = usingFont.deriveFont(Font.PLAIN, fontSize * 2);
                    usingFontB = usingFont.deriveFont(Font.BOLD, fontSize);
                    usingFont2B = usingFont.deriveFont(Font.BOLD, fontSize * 2);
                    usingFontP = usingFont.deriveFont(Font.BOLD, fontSize - 2);
                    font_loaded = true;
                }
                else font_loaded = false;
            }
            catch(Throwable e)
            {
                e.printStackTrace();
                font_loaded = false;
            }
            finally
            {
                try
                {
                    infs.close();
                }
                catch(Throwable e)
                {
                    
                }
                try
                {
                    finfs.close();
                }
                catch(Throwable e)
                {
                    
                }
            }
        }
        String truetype = "돋움";
        try
        {
            GraphicsEnvironment gr = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontList = gr.getAvailableFontFamilyNames();
            for(int i=0; i<fontList.length; i++)
            {
                if(fontList[i].equals("나눔고딕코딩") || fontList[i].equalsIgnoreCase("NanumGothicCoding"))
                {
                    truetype = fontList[i];
                    break;
                }
            }
        } 
        catch (Exception e1)
        {
            truetype = "돋움";
        }
        
        if(! font_loaded)
        {
            try
            {
                File fontObjectFile = new File(fontPath + "defaultFont.font");
                if(fontObjectFile.exists())
                {
                    finfs = new FileInputStream(fontObjectFile);                
                    objs = new ObjectInputStream(finfs);
                    usingFont = (Font) objs.readObject();
                    usingFont2 = usingFont.deriveFont(Font.PLAIN, usingFont.getSize() * 2);
                    usingFontB = usingFont.deriveFont(Font.BOLD, usingFont.getSize());
                    usingFont2B = usingFont.deriveFont(Font.BOLD, usingFont.getSize() * 2);
                    usingFontP = usingFont.deriveFont(Font.BOLD, fontSize - 2);
                }
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                font_loaded = false;
            }
            finally
            {
                try
                {
                    objs.close();
                }
                catch(Throwable e)
                {
                    
                }
                try
                {
                    finfs.close();
                }
                catch(Throwable e)
                {
                    
                }
            }
        }
        
        if(! font_loaded)
        {
            if(osName.equalsIgnoreCase("Windows Vista") || osName.equalsIgnoreCase("Windows 7")
                    || osName.equalsIgnoreCase("Windows 8")|| osName.equalsIgnoreCase("Windows 8.1"))
            {
                if(locale.startsWith("ko") || locale.startsWith("KO") || locale.startsWith("kr") || locale.startsWith("KR") || locale.startsWith("kor") || locale.startsWith("KOR"))
                    usingFontName = truetype;
                else
                    usingFontName = "Arial";
            }
            else if(osName.startsWith("Windows") || osName.startsWith("windows") || osName.startsWith("WINDOWS"))
            {
                if(osName.endsWith("95") || osName.endsWith("98") || osName.endsWith("me") || osName.endsWith("ME") || osName.endsWith("Me") || osName.endsWith("2000"))
                {
                    if(locale.startsWith("ko") || locale.startsWith("KO") || locale.startsWith("kr") || locale.startsWith("KR") || locale.startsWith("kor") || locale.startsWith("KOR"))
                        usingFontName = "돋움";
                    else
                        usingFontName = "Dialog";
                }
                else
                {
                    if(locale.startsWith("ko") || locale.startsWith("KO") || locale.startsWith("kr") || locale.startsWith("KR") || locale.startsWith("kor") || locale.startsWith("KOR"))
                        usingFontName = truetype;
                    else
                        usingFontName = "Arial";
                }
                
            }
            try
            {
                usingFont = new Font(usingFontName, Font.PLAIN, fontSize);
                usingFontB = new Font(usingFontName, Font.BOLD, fontSize);
                usingFont2 = new Font(usingFontName, Font.PLAIN, fontSize * 2);
                usingFont2B = new Font(usingFontName, Font.BOLD, fontSize * 2);
                usingFontP = usingFont.deriveFont(Font.BOLD, fontSize - 2);
                font_loaded = true;
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                font_loaded = false;
                usingFont = null;
            }
        }
        if(font_loaded)
        {
            try
            {
                UIManager.put("OptionPane.messageFont", new FontUIResource(usingFont));
                UIManager.put("OptionPane.font", new FontUIResource(usingFont));
                UIManager.put("OptionPane.buttonFont", new FontUIResource(usingFont));
                UIManager.put("JOptionPane.font", new FontUIResource(usingFont));
            }
            catch(Throwable e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 
     * <p>컴포넌트의 글꼴을 변경합니다. 해당 컴포넌트에 포함된 하위 컴포넌트 전체에 적용됩니다.</p>
     * 
     * @param comp : 컴포넌트
     * @param font : 폰트 객체
     */
    public static void setFontRecursively(Component comp, Font font)
    {
        setFontRecursively(comp, font, 1000);
    }
    
    /**
     * 
     * <p>컴포넌트의 글꼴을 변경합니다. 해당 컴포넌트에 포함된 하위 컴포넌트 전체에 적용됩니다.</p>
     * 
     * @param comp : 컴포넌트
     * @param font : 폰트 객체
     * @param prevent_infiniteLoop : 무한 반복 방지용 실행 횟수 제한
     */
    public static void setFontRecursively(Component comp, Font font, int prevent_infiniteLoop)
    {
        try
        {
            if(font == null) return;
            try
            {
                comp.setFont(font);
            }
            catch(Throwable e)
            {
                
            }
            int max_limits = prevent_infiniteLoop;
            if(comp instanceof Container)
            {
                Container cont = (Container) comp;
                int ub = cont.getComponentCount();
                for(int  j=0; j<ub; j++)
                {
                    ub = cont.getComponentCount();
                    if(ub > max_limits) ub = max_limits;
                    max_limits--;
                    if(max_limits <= 0) break;
                    setFontRecursively(cont.getComponent(j), font, max_limits);                    
                }
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /** 창을 모니터 가운데에 위치시킵니다. */
    public static void centerWindow(Window window) {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((int)( scrSize.getWidth() / 2 - window.getWidth() / 2 ), (int)( scrSize.getHeight() / 2 - window.getHeight() / 2 ));
    }
    
    /** 창 크기를 모니터 해상도와 비슷하도록 키웁니다. */
    public static void stretchWindow(Window window) {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize((int) (scrSize.getWidth() - 50), (int) (scrSize.getHeight() - 100));
    }
    
    /** 룩앤필을 지정합니다. 이름이나 클래스명을 지정할 수 있습니다. */
    public static void setLookAndFeel(String nameOrClassName) {
        if(DataUtil.isEmpty(nameOrClassName)) {
            nameOrClassName = UIManager.getSystemLookAndFeelClassName();
        }
        nameOrClassName = nameOrClassName.trim();
        
        LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        for(LookAndFeelInfo i : infos) {
            if(nameOrClassName.equals(i.getName()) || nameOrClassName.equals(i.getClassName())) {
                try {
                    UIManager.setLookAndFeel(i.getClassName());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                } 
                break;
            }
        }
    }
    
    /**
     * 문자열로 키보드 문자를 받아 키 코드를 반환합니다.
     */
    public static Integer convertKeyStroke(String shortcutKeyOpt) {
        shortcutKeyOpt = shortcutKeyOpt.trim();
        shortcutKeyOpt = shortcutKeyOpt.toUpperCase();
        
        if(shortcutKeyOpt.equals("F1"))
            return new Integer(KeyEvent.VK_F1);
        if(shortcutKeyOpt.equals("F2"))
            return new Integer(KeyEvent.VK_F2);
        if(shortcutKeyOpt.equals("F3"))
            return new Integer(KeyEvent.VK_F3);
        if(shortcutKeyOpt.equals("F4"))
            return new Integer(KeyEvent.VK_F4);
        if(shortcutKeyOpt.equals("F5"))
            return new Integer(KeyEvent.VK_F5);
        if(shortcutKeyOpt.equals("F6"))
            return new Integer(KeyEvent.VK_F6);
        if(shortcutKeyOpt.equals("F7"))
            return new Integer(KeyEvent.VK_F7);
        if(shortcutKeyOpt.equals("F8"))
            return new Integer(KeyEvent.VK_F8);
        if(shortcutKeyOpt.equals("F9"))
            return new Integer(KeyEvent.VK_F9);
        if(shortcutKeyOpt.equals("F10"))
            return new Integer(KeyEvent.VK_F10);
        if(shortcutKeyOpt.equals("F11"))
            return new Integer(KeyEvent.VK_F11);
        if(shortcutKeyOpt.equals("F12"))
            return new Integer(KeyEvent.VK_F12);
        if(shortcutKeyOpt.equals("A"))
            return new Integer(KeyEvent.VK_A);
        if(shortcutKeyOpt.equals("S"))
            return new Integer(KeyEvent.VK_S);
        if(shortcutKeyOpt.equals("D"))
            return new Integer(KeyEvent.VK_D);
        if(shortcutKeyOpt.equals("F"))
            return new Integer(KeyEvent.VK_F);
        if(shortcutKeyOpt.equals("G"))
            return new Integer(KeyEvent.VK_G);
        if(shortcutKeyOpt.equals("H"))
            return new Integer(KeyEvent.VK_H);
        if(shortcutKeyOpt.equals("J"))
            return new Integer(KeyEvent.VK_J);
        if(shortcutKeyOpt.equals("K"))
            return new Integer(KeyEvent.VK_K);
        if(shortcutKeyOpt.equals("L"))
            return new Integer(KeyEvent.VK_L);
        if(shortcutKeyOpt.equals("Z"))
            return new Integer(KeyEvent.VK_Z);
        if(shortcutKeyOpt.equals("X"))
            return new Integer(KeyEvent.VK_X);
        if(shortcutKeyOpt.equals("C"))
            return new Integer(KeyEvent.VK_C);
        if(shortcutKeyOpt.equals("V"))
            return new Integer(KeyEvent.VK_V);
        if(shortcutKeyOpt.equals("B"))
            return new Integer(KeyEvent.VK_B);
        if(shortcutKeyOpt.equals("N"))
            return new Integer(KeyEvent.VK_N);
        if(shortcutKeyOpt.equals("M"))
            return new Integer(KeyEvent.VK_M);
        if(shortcutKeyOpt.equals("Q"))
            return new Integer(KeyEvent.VK_Q);
        if(shortcutKeyOpt.equals("W"))
            return new Integer(KeyEvent.VK_W);
        if(shortcutKeyOpt.equals("E"))
            return new Integer(KeyEvent.VK_E);
        if(shortcutKeyOpt.equals("R"))
            return new Integer(KeyEvent.VK_R);
        if(shortcutKeyOpt.equals("T"))
            return new Integer(KeyEvent.VK_T);
        if(shortcutKeyOpt.equals("Y"))
            return new Integer(KeyEvent.VK_Y);
        if(shortcutKeyOpt.equals("U"))
            return new Integer(KeyEvent.VK_U);
        if(shortcutKeyOpt.equals("I"))
            return new Integer(KeyEvent.VK_I);
        if(shortcutKeyOpt.equals("O"))
            return new Integer(KeyEvent.VK_O);
        if(shortcutKeyOpt.equals("P"))
            return new Integer(KeyEvent.VK_P);
        if(shortcutKeyOpt.equals("1"))
            return new Integer(KeyEvent.VK_1);
        if(shortcutKeyOpt.equals("2"))
            return new Integer(KeyEvent.VK_2);
        if(shortcutKeyOpt.equals("3"))
            return new Integer(KeyEvent.VK_3);
        if(shortcutKeyOpt.equals("4"))
            return new Integer(KeyEvent.VK_4);
        if(shortcutKeyOpt.equals("5"))
            return new Integer(KeyEvent.VK_5);
        if(shortcutKeyOpt.equals("6"))
            return new Integer(KeyEvent.VK_6);
        if(shortcutKeyOpt.equals("7"))
            return new Integer(KeyEvent.VK_7);
        if(shortcutKeyOpt.equals("8"))
            return new Integer(KeyEvent.VK_8);
        if(shortcutKeyOpt.equals("9"))
            return new Integer(KeyEvent.VK_9);
        if(shortcutKeyOpt.equals("0"))
            return new Integer(KeyEvent.VK_0);
        
        return null;
    }
    
    /** 문자열로 단축키 마스크 (CTRL / ALT / SHIFT) 값을 받아 그 이벤트 코드를 반환합니다. */
    public static Integer convertMaskStroke(String shortcutMaskOpt) {
        shortcutMaskOpt = shortcutMaskOpt.trim();
        shortcutMaskOpt = shortcutMaskOpt.toUpperCase();
        
        if(shortcutMaskOpt.equalsIgnoreCase("CTRL")) {
            return new Integer(InputEvent.CTRL_MASK);
        }
        
        if(shortcutMaskOpt.equalsIgnoreCase("ALT")) {
            return new Integer(InputEvent.ALT_MASK);
        }
        
        if(shortcutMaskOpt.equalsIgnoreCase("SHIFT")) {
            return new Integer(InputEvent.SHIFT_MASK);
        }
        
        return null;
    }
    
    /** Create captcha image and return base64 code */
    public static String createImageCaptchaBase64(String code, int width, int height, int noises, int fontSize, boolean darkMode, List<String> fontFamily) throws IOException {
    	int backr, backg, backb, forer, foreg, foreb;
    	
    	if(darkMode) {
    		backr = 0;
    		backg = 0;
    		backb = 0;
    		forer = 250;
    		foreg = 250;
    		foreb = 250;
    	} else {
    		backr = 250;
    		backg = 250;
    		backb = 250;
    		forer = 0;
    		foreg = 0;
    		foreb = 0;
    	}
    	
    	return createImageCaptchaBase64(code, width, height, noises, fontSize, backr, backg, backb, forer, foreg, foreb, fontFamily);
    }
    
    /** Create captcha image and return base64 code */
    public static String createImageCaptchaBase64(String code, int width, int height, int noises, int fontSize, int backr, int backg, int backb, int forer, int foreg, int foreb, List<String> fontFamily) throws IOException {
    	if(code == null) {
            code = "REFRESH";
        }
        
        if(fontFamily == null) {
        	fontFamily = new ArrayList<String>();
        }
        if(fontFamily.isEmpty()) {
        	fontFamily.add("Serif");
        	fontFamily.add("Arial");
        	fontFamily.add("Helvetica");
        }
        
        BufferedImage image    = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D    graphics = image.createGraphics();
        
        if(backr <    0) backr =   0;
        if(backr >= 255) backr = 255;
        
        if(backg <    0) backg =   0;
        if(backg >= 255) backg = 255;
        
        if(backb <    0) backb =   0;
        if(backb >= 255) backb = 255;

        graphics.setColor(new Color(backr, backg, backb));
        graphics.fillRect(0, 0, width, height);

        FontMetrics metrics = graphics.getFontMetrics();
        int fontWidth = metrics.stringWidth(code);
        int gap       = (width - fontWidth) / (code.length() + 1);
        int x         = gap;
        int y         = (height - metrics.getHeight()) / 2 + metrics.getAscent();
        
        int calcr, calcg, calcb, colorChangeRange, fontRand, fontFamilyCnt, fontStyle;
        
        colorChangeRange = 80;
        fontFamilyCnt = fontFamily.size();
        fontStyle = Font.BOLD;

        // 방해물 출력
        for(int ndx=0; ndx<noises; ndx++) {
            int x1, y1, x2, y2;
            x1 = (int) (Math.random() * width);
            y1 = (int) (Math.random() * height);
            x2 = x1 + (int) (Math.random() * (width  / 2));
            y2 = y1 + (int) (Math.random() * (height / 2));
            
            if(forer >= 128) calcr = forer - (int) (Math.random() * colorChangeRange);
            else             calcr = forer + (int) (Math.random() * colorChangeRange);
            
            if(forer >= 128) calcg = foreg - (int) (Math.random() * colorChangeRange);
            else             calcg = foreg + (int) (Math.random() * colorChangeRange);
            
            if(forer >= 128) calcb = foreb - (int) (Math.random() * colorChangeRange);
            else             calcb = foreb + (int) (Math.random() * colorChangeRange);
            
            while(Math.abs(backr - calcr) <= 15 && Math.abs(backg - calcg) <= 15 && Math.abs(backb - calcb) <= 15) {
            	if(forer >= 128) calcr = forer - (int) (Math.random() * colorChangeRange);
                else             calcr = forer + (int) (Math.random() * colorChangeRange);
                
                if(forer >= 128) calcg = foreg - (int) (Math.random() * colorChangeRange);
                else             calcg = foreg + (int) (Math.random() * colorChangeRange);
                
                if(forer >= 128) calcb = foreb - (int) (Math.random() * colorChangeRange);
                else             calcb = foreb + (int) (Math.random() * colorChangeRange);
            }
            
            if(calcr <    0) calcr =   0;
            if(calcr >= 255) calcr = 255;
            
            if(calcg <    0) calcg =   0;
            if(calcg >= 255) calcg = 255;
            
            if(calcb <    0) calcb =   0;
            if(calcb >= 255) calcb = 255;
            
            graphics.setColor(new Color(calcr, calcg, calcb));
            graphics.drawLine(x1, y1, x2, y2);
        }

        // 글자 출력
        for(int idx=0; idx<code.length(); idx++) {
            char charNow = code.charAt(idx);
            
            if(forer >= 128) calcr = forer - (int) (Math.random() * colorChangeRange);
            else             calcr = forer + (int) (Math.random() * colorChangeRange);
            
            if(forer >= 128) calcg = foreg - (int) (Math.random() * colorChangeRange);
            else             calcg = foreg + (int) (Math.random() * colorChangeRange);
            
            if(forer >= 128) calcb = foreb - (int) (Math.random() * colorChangeRange);
            else             calcb = foreb + (int) (Math.random() * colorChangeRange);
            
            while(Math.abs(backr - calcr) <= 15 && Math.abs(backg - calcg) <= 15 && Math.abs(backb - calcb) <= 15) {
            	if(forer >= 128) calcr = forer - (int) (Math.random() * colorChangeRange);
                else             calcr = forer + (int) (Math.random() * colorChangeRange);
                
                if(forer >= 128) calcg = foreg - (int) (Math.random() * colorChangeRange);
                else             calcg = foreg + (int) (Math.random() * colorChangeRange);
                
                if(forer >= 128) calcb = foreb - (int) (Math.random() * colorChangeRange);
                else             calcb = foreb + (int) (Math.random() * colorChangeRange);
            }
            
            if(calcr <    0) calcr =   0;
            if(calcr >= 255) calcr = 255;
            
            if(calcg <    0) calcg =   0;
            if(calcg >= 255) calcg = 255;
            
            if(calcb <    0) calcb =   0;
            if(calcb >= 255) calcb = 255;
            
            graphics.setColor(new Color(calcr, calcg, calcb));
            
            int nowX = x + metrics.charWidth(charNow) / 2;
            int ang  = ((int) (Math.random() * 41)) - 20;
            
            graphics.rotate(Math.toRadians(ang), nowX, y);
            
            fontRand = (int) Math.floor(Math.random() * fontFamilyCnt);
            if(fontRand >= fontFamilyCnt) fontRand = fontFamilyCnt - 1;
            if(fontRand < 0) fontRand = 0;
            
            if(Math.random() >= 0.5) fontStyle = Font.BOLD;
            else                     fontStyle = Font.BOLD | Font.ITALIC;
            
            graphics.setFont(new Font(fontFamily.get(fontRand), fontStyle, fontSize + ((int) Math.random() * 4) - 2));
            graphics.drawString(String.valueOf(charNow), nowX, y + ((int) ((Math.random() * height) / 3.0)));
            graphics.rotate(Math.toRadians(ang) * (-1), nowX, y);
            
            x += metrics.charWidth(charNow) + gap;
        }

        ByteArrayOutputStream binary = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", binary);
        image    = null;
        graphics = null;

        String bs64str = SecurityUtil.base64String(binary.toByteArray());
        binary = null;
        return bs64str;
    }
    
    /** Create captcha for command line environment. Only space, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 available. */
    public static String createTextCaptcha(String code) {
    	if(code == null) return "";
    	StringBuilder res = new StringBuilder("");
    	
    	int idx, cdx;
    	
    	String nCode = code;
    	int clen = nCode.length();
    	int widths = clen * 7;
    	
    	// Print header
        for(idx=0; idx<2; idx++) {
        	for(cdx=0; cdx<widths + 2; cdx++) {
        		res = res.append("□");
        	}
        	res = res.append("\n");
        }
        
        // Print characters
        // 1st line
        res = res.append("□");
        for(cdx=0; cdx<clen; cdx++) {
        	char charOne = nCode.charAt(cdx);
        	res = res.append("□");
        	switch(charOne) {
        	case '0':
        		res = res.append("□■■■□");
        		break;
        	case '1':
        		res = res.append("□□■□□");
        		break;
        	case '2':
        		res = res.append("□■■■□");
        		break;
        	case '3':
        		res = res.append("□■■■□");
        		break;
        	case '4':
        		res = res.append("□□□■□");
        		break;
        	case '5':
        		res = res.append("■■■■■");
        		break;
        	case '6':
        		res = res.append("□■■■□");
        		break;
        	case '7':
        		res = res.append("■■■■■");
        		break;
        	case '8':
        		res = res.append("□■■■□");
        		break;
        	case '9':
        		res = res.append("□■■■□");
        		break;
        	default:
        		res = res.append("□□□□□");
        	}
        	res = res.append("□");
        }
        res = res.append("□");
        res = res.append("\n");
        // 2nd line
        res = res.append("□");
        for(cdx=0; cdx<clen; cdx++) {
        	char charOne = nCode.charAt(cdx);
        	res = res.append("□");
        	switch(charOne) {
        	case '0':
        		res = res.append("■■□■■");
        		break;
        	case '1':
        		res = res.append("□■■□□");
        		break;
        	case '2':
        		res = res.append("■□□□■");
        		break;
        	case '3':
        		res = res.append("■□□□■");
        		break;
        	case '4':
        		res = res.append("□□□■□");
        		break;
        	case '5':
        		res = res.append("■□□□□");
        		break;
        	case '6':
        		res = res.append("■□□□■");
        		break;
        	case '7':
        		res = res.append("□□□□■");
        		break;
        	case '8':
        		res = res.append("■□□□■");
        		break;
        	case '9':
        		res = res.append("■□□□■");
        		break;
        	default:
        		res = res.append("□□□□□");
        	}
        	res = res.append("□");
        }
        res = res.append("□");
        res = res.append("\n");
        // 3rd line
        res = res.append("□");
        for(cdx=0; cdx<clen; cdx++) {
        	char charOne = nCode.charAt(cdx);
        	res = res.append("□");
        	switch(charOne) {
        	case '0':
        		res = res.append("■□□□■");
        		break;
        	case '1':
        		res = res.append("□□■□□");
        		break;
        	case '2':
        		res = res.append("□□□□■");
        		break;
        	case '3':
        		res = res.append("□□□□■");
        		break;
        	case '4':
        		res = res.append("□□■■□");
        		break;
        	case '5':
        		res = res.append("■■■■□");
        		break;
        	case '6':
        		res = res.append("■□□□□");
        		break;
        	case '7':
        		res = res.append("□□□■□");
        		break;
        	case '8':
        		res = res.append("■□□□■");
        		break;
        	case '9':
        		res = res.append("■□□□■");
        		break;
        	default:
        		res = res.append("□□□□□");
        	}
        	res = res.append("□");
        }
        res = res.append("□");
        res = res.append("\n");
        // 4th line
        res = res.append("□");
        for(cdx=0; cdx<clen; cdx++) {
        	char charOne = nCode.charAt(cdx);
        	res = res.append("□");
        	switch(charOne) {
        	case '0':
        		res = res.append("■□□□■");
        		break;
        	case '1':
        		res = res.append("□□■□□");
        		break;
        	case '2':
        		res = res.append("□□□■□");
        		break;
        	case '3':
        		res = res.append("□□□■□");
        		break;
        	case '4':
        		res = res.append("□■□■□");
        		break;
        	case '5':
        		res = res.append("■□□□■");
        		break;
        	case '6':
        		res = res.append("■■■■□");
        		break;
        	case '7':
        		res = res.append("□□□■□");
        		break;
        	case '8':
        		res = res.append("□■■■□");
        		break;
        	case '9':
        		res = res.append("□■■■■");
        		break;
        	default:
        		res = res.append("□□□□□");
        	}
        	res = res.append("□");
        }
        res = res.append("□");
        res = res.append("\n");
        // 5th line
        res = res.append("□");
        for(cdx=0; cdx<clen; cdx++) {
        	char charOne = nCode.charAt(cdx);
        	res = res.append("□");
        	switch(charOne) {
        	case '0':
        		res = res.append("■□□□■");
        		break;
        	case '1':
        		res = res.append("□□■□□");
        		break;
        	case '2':
        		res = res.append("□□■□□");
        		break;
        	case '3':
        		res = res.append("□□□□■");
        		break;
        	case '4':
        		res = res.append("■■■■■");
        		break;
        	case '5':
        		res = res.append("□□□□■");
        		break;
        	case '6':
        		res = res.append("■□□□■");
        		break;
        	case '7':
        		res = res.append("□□■□□");
        		break;
        	case '8':
        		res = res.append("■□□□■");
        		break;
        	case '9':
        		res = res.append("□□□□■");
        		break;
        	default:
        		res = res.append("□□□□□");
        	}
        	res = res.append("□");
        }
        res = res.append("□");
        res = res.append("\n");
        // 6th line
        res = res.append("□");
        for(cdx=0; cdx<clen; cdx++) {
        	char charOne = nCode.charAt(cdx);
        	res = res.append("□");
        	switch(charOne) {
        	case '0':
        		res = res.append("■■□■■");
        		break;
        	case '1':
        		res = res.append("□□■□□");
        		break;
        	case '2':
        		res = res.append("□■□□□");
        		break;
        	case '3':
        		res = res.append("■□□□■");
        		break;
        	case '4':
        		res = res.append("□□□■□");
        		break;
        	case '5':
        		res = res.append("■□□□■");
        		break;
        	case '6':
        		res = res.append("■□□□■");
        		break;
        	case '7':
        		res = res.append("□□■□□");
        		break;
        	case '8':
        		res = res.append("■□□□■");
        		break;
        	case '9':
        		res = res.append("■□□□■");
        		break;
        	default:
        		res = res.append("□□□□□");
        	}
        	res = res.append("□");
        }
        res = res.append("□");
        res = res.append("\n");
        // 7th line
        res = res.append("□");
        for(cdx=0; cdx<clen; cdx++) {
        	char charOne = nCode.charAt(cdx);
        	res = res.append("□");
        	switch(charOne) {
        	case '0':
        		res = res.append("□■■■□");
        		break;
        	case '1':
        		res = res.append("□□■□□");
        		break;
        	case '2':
        		res = res.append("■■■■■");
        		break;
        	case '3':
        		res = res.append("□■■■□");
        		break;
        	case '4':
        		res = res.append("□□□■□");
        		break;
        	case '5':
        		res = res.append("□■■■□");
        		break;
        	case '6':
        		res = res.append("□■■■□");
        		break;
        	case '7':
        		res = res.append("□□■□□");
        		break;
        	case '8':
        		res = res.append("□■■■□");
        		break;
        	case '9':
        		res = res.append("□■■■□");
        		break;
        	default:
        		res = res.append("□□□□□");
        	}
        	res = res.append("□");
        }
        res = res.append("□");
        res = res.append("\n");
        
        // Print footer
        for(idx=0; idx<2; idx++) {
        	for(cdx=0; cdx<widths + 2; cdx++) {
        		res = res.append("□");
        	}
        	res = res.append("\n");
        }
    	
    	return res.toString();
    }
}
