package org.duckdns.hjow.commons.vdp.elements;

public class SectionList
{
	public static int count = 8;
	public SectionList()
	{
		
	}
	public static int[] getSectionNumberList()
	{
		int[] list = new int[count];
		list[0] = Section.CONSTANT;
		list[1] = Section.BASIC;
		list[2] = Section.SINE;
		list[3] = Section.COSINE;
		list[4] = Section.TANGENT;
		list[5] = Section.LN;
		list[6] = Section.LOG;
		list[7] = Section.EXP;
		return list;
	}
	public static String[] getList()
	{
		String[] list = new String[count];
		for(int i=0; i<list.length; i++)
		{
			list[i] = Section.initialize(getSectionNumberList()[i]).getName();
		}
		return list;
	}
	public static Section[] getSectionList()
	{
		Section[] list = new Section[count];
		for(int i=0; i<list.length; i++)
		{
			list[i] = Section.initialize(getSectionNumberList()[i]);
		}		
		return list;
	}	
	public static String getSectionName(byte code)
	{
		//String newone = "";
		return Section.initialize(code).getName();
	}
}
