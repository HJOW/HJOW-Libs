package org.duckdns.hjow.commons.vdp.language;

import java.io.Serializable;

public class Language implements Serializable
{
	private static final long serialVersionUID = -4779079240731702886L;
	public int version_main = 0;	
	public int version_sub = 1;
	
	public static final byte ENGLISH = 0;
	public static final byte KOREAN = 1;
	
	public static final int TEXT_EXIT = 1;
	public static final int TEXT_SAVE = 2;
	public static final int TEXT_LOAD = 3;
	public static final int TEXT_ACCEPT = 4;
	public static final int TEXT_CANCEL = 5;	
	public static final int TEXT_ACT = 6;
	public static final int TEXT_CLOSE = 7;
	
	public static final int TEXT_TITLE = 10;
	
	public static final int TEXT_FROMPARENT = 101;
	public static final int TEXT_FROMFILE = 102;
	
	public static final int TEXT_EQUATION = 1001;
	public static final int TEXT_SECTION = 1002;
	public static final int TEXT_FUNCTION = 1003;
	public static final int TEXT_CONSTANT = 1004;
	public static final int TEXT_POLYNOMIAL = 1005;
	public static final int TEXT_SOLVE = 2001;
	public static final int TEXT_DIFFERENTIAL = 2002;
	public static final int TEXT_INTEGRAL = 2003;
	public static final int TEXT_RANGE = 2004;
	public static final int TEXT_DOMAIN = 2005;
	public static final int TEXT_F_RANGE = 2006;
	public static final int TEXT_MIN = 2010;
	public static final int TEXT_MAX = 2011;
	public static final int TEXT_DISTANCE = 2012;
	public static final int TEXT_STDEV = 2013;
	public static final int TEXT_DEGREE_OF_EQUAL = 2014;
	public static final int TEXT_AVERAGE = 2015;
	
	public static final int TEXT_GRAPH = 10001;
	public static final int TEXT_INPUT = 10002;
	public static final int TEXT_OUTPUT = 10003;
	public static final int TEXT_GRID = 10004;
	
	public static final int TEXT_RESULT = 20001;
	public static final int TEXT_IMPOSSIBLE = 20002;
	public static final int TEXT_POSSIBLE = 20003;
	
	public static final int TEXT_INSERT_VALUE = 30001;
	
	public static final int TEXT_UP = 40001;
	public static final int TEXT_DOWN = 40002;
	public static final int TEXT_LEFT = 40003;
	public static final int TEXT_RIGHT = 40004;	
	public static final int TEXT_NOTEXIST = 40005;
	
	public static final int TEXT_MODIFIED = 50001;
	public static final int TEXT_INPUT_CONSOLE_HELP = 50100;
	
	public static final int TEXT_ERROR_WRONG_VALUE = 60001;
	public static final int TEXT_ERROR_UNKNOWN_COEFFICIENT = 60002;
	public static final int TEXT_ERROR_UNKNOWN_EXPONENT = 60003;
	public static final int TEXT_ERROR_CANNOT_TRANSLATE = 60004;	
	
	public static final int TEXT_WELCOME = 100001;
	public static final int TEXT_BYE = 100002;
	public static final int TEXT_MAKER = 100003;
	public static final int TEXT_DONOTASKAGAIN = 100004;
	
	public static final int TEXT_MENU_FILE = 200100;
	public static final int TEXT_MENU_FILE_EXIT = 200101;
	public static final int TEXT_MENU_FILE_LOG_SAVE = 200102;
	public static final int TEXT_MENU_FILE_LOAD = 200110;
	public static final int TEXT_MENU_FILE_LOAD_1 = 200111;
	public static final int TEXT_MENU_FILE_LOAD_2 = 200112;
	public static final int TEXT_MENU_FILE_SAVE = 200120;
	public static final int TEXT_MENU_FILE_SAVE_1 = 200121;
	public static final int TEXT_MENU_FILE_SAVE_2 = 200122;
	public static final int TEXT_MENU_TOOL = 200200;
	public static final int TEXT_MENU_TOOL_SET = 200201;
	public static final int TEXT_MENU_TOOL_LANGUAGE_CREATOR = 200202;
	public static final int TEXT_MENU_VIEW = 200300;
	public static final int TEXT_MENU_VIEW_COMPARE = 200301;
	public static final int TEXT_MENU_HELP = 200400;
	public static final int TEXT_MENU_HELP_BASIC = 200401;
	public static final int TEXT_MENU_HELP_VER = 200402;
	
	public static final int TEXT_CONSOLE_SELECT = 300001;
	public static final int TEXT_CONSOLE_INPUT_PLEASE = 300002;
	public static final int TEXT_CONSOLE_INPUT_FUNCTION = 300003;
	
	public static final int TEXT_CONSOLE_MENU_ = 300100;
	
	public static final int TEXT_S = 900001;
	public static final int TEXT_TO = 900002;
	public static final int TEXT_THEME = 90010;
	public static final int TEXT_THEME_ = 900101;
	
	public static final int TEXT_TOOLTIPS_ = 1000000;
	
	public static final int TEXT_TUTORIALS_ = 2000000;
	
	public static final int LANGUAGE = 0;
	public static final int LANGUAGE_KOREAN = 1;
	public static final int LANGUAGE_ENGLISH = 2;
	public static final int LANGUAGE_USER_DEFINED = 1000;
	
	
	protected String s, theme, to, exit, close, save, load, accept, cancel, do_not_ask_again, act, title, from_file, from_parent, equation, section, function, constant, polynomial, solve, differential, integral, graph, degree_of_equal, distance, stdev, average, input, output, result, impossible, possible, text_console_help, insert_value, up, down, left, right, modified, welcome, domain, f_range, range, min, max, grid, menu_file, menu_file_log_save, menu_file_exit, menu_file_load, menu_file_load_1, menu_file_load_2, menu_file_save, menu_file_save_1, menu_file_save_2, menu_tool, menu_tool_set, menu_tool_language_creator, menu_view, menu_view_compare, menu_help, menu_help_basic, menu_help_ver, not_exist, error_wrong_value, error_unknown_coefficient, error_unknown_exponent, error_cannot_translate , console_select, console_input_please, console_input_function, bye, maker;
	protected String[] console_menu, tooltips, tutorials, themes;
	
	public String getText(int index)
	{
		for(int i=0; i<console_menu.length; i++)
		{
			if(index == TEXT_CONSOLE_MENU_ + i)
			{
				return console_menu[i];
			}
		}
		for(int i=0; i<tooltips.length; i++)
		{
			if(index == TEXT_TOOLTIPS_ + i)
			{
				return tooltips[i];
			}
		}
		for(int i=0; i<tutorials.length; i++)
		{
			if(index == TEXT_TUTORIALS_ + i)
			{
				return tutorials[i];
			}
		}
		for(int i=0; i<themes.length; i++)
		{
			if(index == TEXT_THEME_ + i)
			{
				return themes[i];
			}
		}
		switch(index)
		{
		case TEXT_S:
			return s;
		case TEXT_TO:
			return to;
		case TEXT_CONSTANT:
			return constant;
		case TEXT_DIFFERENTIAL:
			return differential;
		case TEXT_EQUATION:
			return equation;
		case TEXT_EXIT:
			return exit;
		case TEXT_CLOSE:
			return close;
		case TEXT_FUNCTION:
			return function;
		case TEXT_GRAPH:
			return graph;
		case TEXT_INPUT:
			return input;
		case TEXT_ACT:
			return act;
		case TEXT_INTEGRAL:
			return integral;
		case TEXT_LOAD:
			return load;
		case TEXT_OUTPUT:
			return output;
		case TEXT_POLYNOMIAL:
			return polynomial;
		case TEXT_SAVE:
			return save;
		case TEXT_DONOTASKAGAIN:
			return do_not_ask_again;
		case TEXT_SECTION:
			return section;
		case TEXT_SOLVE:
			return solve;
		case TEXT_RESULT:
			return result;
		case TEXT_FROMFILE:
			return from_file;
		case TEXT_FROMPARENT:
			return from_parent;
		case TEXT_IMPOSSIBLE:
			return impossible;
		case TEXT_POSSIBLE:
			return possible;
		case TEXT_INSERT_VALUE:
			return insert_value;
		case TEXT_DEGREE_OF_EQUAL:
			return degree_of_equal;
		case TEXT_DISTANCE:
			return distance;
		case TEXT_STDEV:
			return stdev;
		case TEXT_AVERAGE:
			return average;
		case TEXT_UP:
			return up;
		case TEXT_DOWN:
			return down;
		case TEXT_LEFT:
			return left;
		case TEXT_RIGHT:
			return right;
		case TEXT_MODIFIED:
			return modified;
		case TEXT_TITLE:
			return title;
		case TEXT_WELCOME:
			return welcome;
		case TEXT_DOMAIN:
			return domain;
		case TEXT_F_RANGE:
			return f_range;
		case TEXT_RANGE:
			return range;
		case TEXT_MENU_FILE:
			return menu_file;
		case TEXT_MENU_FILE_EXIT:
			return menu_file_exit;
		case TEXT_MENU_FILE_LOG_SAVE:
			return menu_file_log_save;
		case TEXT_MENU_FILE_LOAD:
			return menu_file_load;
		case TEXT_MENU_FILE_LOAD_1:
			return menu_file_load_1;
		case TEXT_MENU_FILE_LOAD_2:
			return menu_file_load_2;
		case TEXT_MENU_FILE_SAVE:
			return menu_file_save;
		case TEXT_MENU_FILE_SAVE_1:
			return menu_file_save_1;
		case TEXT_MENU_FILE_SAVE_2:
			return menu_file_save_2;
		case TEXT_MENU_TOOL:
			return menu_tool;
		case TEXT_MENU_TOOL_SET:
			return menu_tool_set;
		case TEXT_MENU_TOOL_LANGUAGE_CREATOR:
			return menu_tool_language_creator;
		case TEXT_MENU_VIEW:
			return menu_view;
		case TEXT_MENU_VIEW_COMPARE:
			return menu_view_compare;
		case TEXT_MENU_HELP:
			return menu_help;
		case TEXT_MENU_HELP_BASIC:
			return menu_help_basic;
		case TEXT_MENU_HELP_VER:
			return menu_help_ver;
		case TEXT_INPUT_CONSOLE_HELP:
			return text_console_help;
		case TEXT_ACCEPT:
			return accept;
		case TEXT_CANCEL:
			return cancel;
		case TEXT_MIN:
			return min;
		case TEXT_MAX:
			return max;
		case TEXT_GRID:
			return grid;
		case TEXT_NOTEXIST:
			return not_exist;
		case TEXT_BYE:
			return bye;
		case TEXT_ERROR_WRONG_VALUE:
			return error_wrong_value;
		case TEXT_ERROR_UNKNOWN_COEFFICIENT:
			return error_unknown_coefficient;
		case TEXT_ERROR_UNKNOWN_EXPONENT:
			return error_unknown_exponent;
		case TEXT_ERROR_CANNOT_TRANSLATE:
			return error_cannot_translate;
		case TEXT_CONSOLE_SELECT:
			return console_select;
		case TEXT_CONSOLE_INPUT_PLEASE:
			return console_input_please;
		case TEXT_CONSOLE_INPUT_FUNCTION:
			return console_input_function;
		case TEXT_MAKER:
			return maker;
		case TEXT_THEME:
			return theme;
		default:
			return "";	
		}
	}
	public void setText(int index, String text)
	{
		for(int i=0; i<console_menu.length; i++)
		{
			if(index == TEXT_CONSOLE_MENU_ + i)
			{
				console_menu[i] = text;
				return;
			}
		}		
		for(int i=0; i<tooltips.length; i++)
		{
			if(index == TEXT_TOOLTIPS_ + i)
			{
				tooltips[i] = text;
				return;
			}
		}
		for(int i=0; i<tutorials.length; i++)
		{
			if(index == TEXT_TUTORIALS_ + i)
			{
				tutorials[i] = text;
				return;
			}
		}
		for(int i=0; i<themes.length; i++)
		{
			if(index == TEXT_THEME_ + i)
			{
				themes[i] = text;
				return;
			}
		}
		switch(index)
		{
		case TEXT_S:
			s = text;
			break;
		case TEXT_TO:
			to = text;
			break;
		case TEXT_CONSTANT:
			constant = text;
			break;
		case TEXT_DIFFERENTIAL:
			differential = text;
			break;
		case TEXT_EQUATION:
			equation = text;
			break;
		case TEXT_EXIT:
			exit = text;
			break;
		case TEXT_CLOSE:
			close = text;
			break;
		case TEXT_ACT:
			act = text;
			break;
		case TEXT_FUNCTION:
			function = text;
			break;
		case TEXT_GRAPH:
			graph = text;
			break;
		case TEXT_DEGREE_OF_EQUAL:
			degree_of_equal = text;
			break;
		case TEXT_DISTANCE:
			distance = text;
			break;
		case TEXT_STDEV:
			stdev = text;
			break;
		case TEXT_AVERAGE:
			average = text;
			break;
		case TEXT_INPUT:
			input = text;
			break;
		case TEXT_INTEGRAL:
			integral = text;
			break;
		case TEXT_LOAD:
			load = text;
			break;
		case TEXT_OUTPUT:
			output = text;
			break;
		case TEXT_POLYNOMIAL:
			polynomial = text;
			break;
		case TEXT_SAVE:
			save = text;
			break;
		case TEXT_DONOTASKAGAIN:
			do_not_ask_again = text;
			break;
		case TEXT_SECTION:
			section = text;
			break;
		case TEXT_SOLVE:
			solve = text;
			break;
		case TEXT_RESULT:
			result = text;
			break;
		case TEXT_FROMFILE:
			from_file = text;
			break;
		case TEXT_FROMPARENT:
			from_parent = text;
			break;
		case TEXT_INPUT_CONSOLE_HELP:
			text_console_help = text;
			break;
		case TEXT_IMPOSSIBLE:
			impossible = text;
			break;
		case TEXT_POSSIBLE:
			possible = text;
			break;
		case TEXT_INSERT_VALUE:
			insert_value = text;
			break;
		case TEXT_UP:
			up = text;
			break;
		case TEXT_DOWN:
			down = text;
			break;
		case TEXT_LEFT:
			left = text;
			break;
		case TEXT_RIGHT:
			right = text;
			break;
		case TEXT_MODIFIED:
			modified = text;
			break;
		case TEXT_TITLE:
			title = text;
			break;
		case TEXT_WELCOME:
			welcome = text;
			break;
		case TEXT_BYE:
			bye = text;
			break;
		case TEXT_DOMAIN:
			domain = text;
			break;
		case TEXT_RANGE:
			range = text;
			break;
		case TEXT_F_RANGE:
			f_range = text;
			break;
		case TEXT_MENU_FILE:
			menu_file = text;
			break;
		case TEXT_MENU_FILE_EXIT:
			menu_file_exit = text;
			break;
		case TEXT_MENU_FILE_LOG_SAVE:
			menu_file_log_save = text;
			break;
		case TEXT_MENU_TOOL:
			menu_tool = text;
			break;
		case TEXT_MENU_TOOL_SET:
			menu_tool_set = text;
			break;
		case TEXT_MENU_TOOL_LANGUAGE_CREATOR:
			menu_tool_language_creator = text;
			break;
		case TEXT_MENU_FILE_LOAD:
			menu_file_load = text;
			break;
		case TEXT_MENU_FILE_LOAD_1:
			menu_file_load_1 = text;
			break;
		case TEXT_MENU_FILE_LOAD_2:
			menu_file_load_2 = text;
			break;
		case TEXT_MENU_FILE_SAVE:
			menu_file_save = text;
			break;
		case TEXT_MENU_FILE_SAVE_1:
			menu_file_save_1 = text;
			break;
		case TEXT_MENU_FILE_SAVE_2:
			menu_file_save_2 = text;
			break;
		case TEXT_MENU_VIEW:
			menu_view = text;
			break;
		case TEXT_MENU_VIEW_COMPARE:
			menu_view_compare = text;
			break;
		case TEXT_ACCEPT:
			accept = text;
			break;
		case TEXT_CANCEL:
			cancel = text;
			break;
		case TEXT_MAX:
			max = text;
			break;
		case TEXT_MIN:
			min = text;
			break;
		case TEXT_GRID:
			grid = text;
			break;
		case TEXT_NOTEXIST:
			not_exist = text;
			break;
		case TEXT_ERROR_CANNOT_TRANSLATE:
			error_cannot_translate = text;
			break;
		case TEXT_ERROR_UNKNOWN_COEFFICIENT:
			error_unknown_coefficient = text;
			break;
		case TEXT_ERROR_UNKNOWN_EXPONENT:
			error_unknown_exponent = text;
			break;
		case TEXT_ERROR_WRONG_VALUE:
			error_wrong_value = text;
			break;
		case TEXT_CONSOLE_SELECT:
			console_select = text;
			break;
		case TEXT_CONSOLE_INPUT_PLEASE:
			console_input_please = text;
			break;
		case TEXT_CONSOLE_INPUT_FUNCTION:
			console_input_function = text;
			break;
		case TEXT_MAKER:
			maker = text;
			break;
		case TEXT_THEME:
			theme = text;
			break;
		}
	}
	public int[] getIndexList()
	{
		int[] list = new int[69 + console_menu.length + tooltips.length + tutorials.length + themes.length];
		list[0] = TEXT_EXIT;
		list[1] = TEXT_SAVE;
		list[2] = TEXT_LOAD;
		list[3] = TEXT_EQUATION;
		list[4] = TEXT_SECTION;
		list[5] = TEXT_FUNCTION;
		list[6] = TEXT_CONSTANT;
		list[7] = TEXT_POLYNOMIAL;
		list[8] = TEXT_SOLVE;
		list[9] = TEXT_DIFFERENTIAL;
		list[10] = TEXT_INTEGRAL;
		list[11] = TEXT_GRAPH;
		list[12] = TEXT_INPUT;
		list[13] = TEXT_OUTPUT;
		list[14] = TEXT_RESULT;
		list[15] = TEXT_IMPOSSIBLE;
		list[16] = TEXT_POSSIBLE;
		list[17] = TEXT_INSERT_VALUE;
		list[18] = TEXT_UP;
		list[19] = TEXT_DOWN;
		list[20] = TEXT_LEFT;
		list[21] = TEXT_RIGHT;
		list[22] = TEXT_MODIFIED;
		list[23] = TEXT_TITLE;
		list[24] = TEXT_WELCOME;
		list[25] = TEXT_BYE;
		list[26] = TEXT_DOMAIN;
		list[27] = TEXT_RANGE;
		list[28] = TEXT_F_RANGE;
		list[29] = TEXT_MENU_FILE;
		list[30] = TEXT_MENU_FILE_EXIT;
		list[31] = TEXT_MENU_TOOL;
		list[32] = TEXT_MENU_TOOL_SET;
		list[33] = TEXT_ACCEPT;
		list[34] = TEXT_CANCEL;
		list[35] = TEXT_MAX;
		list[36] = TEXT_MIN;
		list[37] = TEXT_GRID;
		list[38] = TEXT_NOTEXIST;
		list[39] = TEXT_CONSOLE_SELECT;
		list[40] = TEXT_CONSOLE_INPUT_PLEASE;
		list[41] = TEXT_CONSOLE_INPUT_FUNCTION;
		list[42] = TEXT_ERROR_CANNOT_TRANSLATE;
		list[43] = TEXT_ERROR_UNKNOWN_COEFFICIENT;
		list[44] = TEXT_ERROR_UNKNOWN_EXPONENT;
		list[45] = TEXT_ERROR_WRONG_VALUE;	
		list[46] = TEXT_MENU_TOOL_LANGUAGE_CREATOR;
		list[47] = TEXT_MENU_FILE_LOG_SAVE;
		list[48] = TEXT_ACT;
		list[49] = TEXT_INPUT_CONSOLE_HELP;
		list[50] = TEXT_MENU_FILE_LOAD;
		list[51] = TEXT_MENU_FILE_LOAD_1;
		list[52] = TEXT_MENU_FILE_LOAD_2;
		list[53] = TEXT_MENU_FILE_SAVE;
		list[54] = TEXT_MENU_FILE_SAVE_1;
		list[55] = TEXT_MENU_FILE_SAVE_2;
		list[56] = TEXT_FROMFILE;
		list[57] = TEXT_FROMPARENT;
		list[58] = TEXT_DEGREE_OF_EQUAL;
		list[59] = TEXT_MENU_VIEW;
		list[60] = TEXT_MENU_VIEW_COMPARE;
		list[61] = TEXT_CLOSE;
		list[62] = TEXT_DISTANCE;
		list[63] = TEXT_STDEV;
		list[64] = TEXT_S;
		list[65] = TEXT_TO;
		list[66] = TEXT_MAKER;
		list[67] = TEXT_DONOTASKAGAIN;
		list[68] = TEXT_THEME;
		
		for(int i=0; i<console_menu.length; i++)
		{
			list[list.length - tooltips.length - console_menu.length - tutorials.length - themes.length + i] = TEXT_CONSOLE_MENU_ + i;
		}
		for(int i=0; i<tooltips.length; i++)
		{
			list[list.length - tooltips.length - tutorials.length - themes.length + i] = TEXT_TOOLTIPS_ + i;
		}
		for(int i=0; i<tutorials.length; i++)
		{
			list[list.length - tutorials.length - themes.length + i] = TEXT_TUTORIALS_ + i;
		}
		for(int i=0; i<themes.length; i++)
		{
			list[list.length - themes.length + i] = TEXT_THEME_ + i;
		}
		return list;
	}
	public int getTutorialLineCount()
	{
		return tutorials.length;
	}
	public Language clone()
	{
		Language newOne = new Language();
		for(int i=0; i<this.getIndexList().length; i++)
		{
			newOne.setText(this.getIndexList()[i], this.getText(this.getIndexList()[i]));
		}
		return newOne;
	}
	public int getLanguageType()
	{
		return LANGUAGE;
	}
}
