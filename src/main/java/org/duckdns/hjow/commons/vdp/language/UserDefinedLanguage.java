package org.duckdns.hjow.commons.vdp.language;

public class UserDefinedLanguage extends Language
{
	private static final long serialVersionUID = 3513260299969520903L;

	public UserDefinedLanguage()
	{
		s = "";
		to = "";
		exit = "";
		close = "";
		constant = "";
		differential = "";
		distance = "";
		stdev = "";
		average = "";
		equation = "";
		act = "";
		function = "";
		graph = "";
		input = "";
		integral = "";
		domain = "";
		range = "";
		degree_of_equal = "";
		f_range = "";
		load = "";
		output = "";
		polynomial = "";
		save = "";
		section = "";
		solve = "";
		result = "";
		impossible = "";
		possible = "";
		insert_value = "";
		up = "";
		down = "";
		left = "";
		right = "";
		modified = "";
		min = "";
		max = "";
		grid = "";
		from_file = "";
		from_parent = "";
		
		accept = "";
		cancel = "";
		not_exist = "";
		text_console_help = "";
		
		error_cannot_translate = "";
		error_unknown_coefficient = "";
		error_unknown_exponent = "";
		error_wrong_value = "";
		
		menu_file = "";
		menu_file_exit = "";
		menu_file_log_save = "";
		menu_file_load = "";
		menu_file_load_1 = "";
		menu_file_load_2 = "";
		menu_file_save = "";
		menu_file_save_1 = "";
		menu_file_save_2 = "";
		menu_tool = "";
		menu_tool_set = "";
		menu_tool_language_creator = "";
		menu_view = "";
		menu_view_compare = "";
		
		console_select = "";
		console_menu = new String[4];
		console_menu[0] = "";
		console_menu[1] = "";
		console_menu[2] = "";
		console_menu[3] = "";
		console_input_please = "";
		console_input_function = "";
		
		tooltips = new String[23];
		tooltips[0] = "";
		tooltips[1] = "";
		tooltips[2] = "";
		tooltips[3] = "";
		tooltips[4] = "";
		tooltips[5] = "";
		tooltips[6] = "";		
		tooltips[7] = "";
		tooltips[8] = "";
		tooltips[9] = "";
		tooltips[10] = "";
		tooltips[11] = "";
		tooltips[12] = "";
		tooltips[13] = "";
		tooltips[14] = "";
		tooltips[15] = "";
		tooltips[16] = "";
		tooltips[17] = "";
		tooltips[18] = "";
		tooltips[19] = "";
		tooltips[20] = "";
		tooltips[21] = "";
		tooltips[22] = "";
		
		theme = "";
		themes = new String[3];
		themes[0] = "";
		themes[1] = "";
		themes[2] = "";
		
		tutorials = new String[100];
		
		title = "";
		welcome = "";
		bye = "";
	}	
	public int getLanguageType()
	{
		return LANGUAGE_USER_DEFINED;
	}
}