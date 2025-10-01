package org.duckdns.hjow.commons.vdp.setting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.duckdns.hjow.commons.vdp.language.Language;
import org.duckdns.hjow.commons.vdp.ui.OutputManager;

public class Setting implements Serializable
{
	private static final long serialVersionUID = -9186610412311670759L;
	
	public int graph_domain_max = 100;
	public int graph_domain_min = -100;
	public boolean need_grid = true;
	public boolean save_setting_when_exit = false;
	
	public int version_main = 0;
	public int version_sub = 1; // 3자리
	public String version_date = "2013.11.12";
	public String lookAndFeel = null;
	
	
	public String getVersion()
	{
		String sub = String.valueOf(version_sub);
		if(version_sub < 100)
		{
			sub = "0" + sub;
		}
		if(version_sub < 10)
		{
			sub = "0" + sub;
		}
		return "  v" + String.valueOf(version_main) + "." + sub + " (" + version_date + ")";
	}
	public static Setting readSetting(String file_path, OutputManager manager) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file_path));
		Setting reads = (Setting) obj.readObject();		
		obj.close();
		if(manager != null) manager.print(manager.getLanguage().getText(Language.TEXT_MENU_TOOL_SET) + " " + manager.getLanguage().getText(Language.TEXT_LOAD) + " : " + file_path);
		return reads;
	}
	public static void writeSetting(String file_path, Setting setting, OutputManager manager) throws FileNotFoundException, IOException
	{
		ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file_path));
		obj.writeObject(setting);
		if(manager != null) manager.print(manager.getLanguage().getText(Language.TEXT_MENU_TOOL_SET) + " " + manager.getLanguage().getText(Language.TEXT_SAVE) + " : " + file_path);
		obj.close();
	}
	public void writeSetting(String file_path, OutputManager manager) throws FileNotFoundException, IOException
	{
		ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file_path));
		obj.writeObject(this);
		if(manager != null) manager.print(manager.getLanguage().getText(Language.TEXT_MENU_TOOL_SET) + " " + manager.getLanguage().getText(Language.TEXT_SAVE) + " : " + file_path);
		obj.close();
	}
}
