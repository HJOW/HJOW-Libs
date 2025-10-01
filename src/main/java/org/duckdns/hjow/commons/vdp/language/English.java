package org.duckdns.hjow.commons.vdp.language;

public class English extends Language
{
	private static final long serialVersionUID = 3513260299969520903L;

	public English()
	{
		s = "'s";
		to = "to";
		exit = "exit";
		close = "close";
		constant = "constant";
		differential = "differential";
		equation = "equation";
		act = "act";
		do_not_ask_again = "Do not show this again";
		function = "function";
		graph = "graph";
		input = "input";
		integral = "integral";
		distance = "difference";
		average = "average";
		stdev = "standard deviation";
		domain = "domain";
		range = "range";
		degree_of_equal = "Probability of equal";
		f_range = "range";
		load = "load";
		output = "output";
		polynomial = "polynomial";
		save = "save";
		section = "section";
		solve = "solve";
		result = "result";
		impossible = "impossible";
		possible = "possible";
		insert_value = "value substitution";
		up = "up";
		down = "down";
		left = "left";
		right = "right";
		modified = "modified";
		min = "min";
		max = "max";
		grid = "grid";
		not_exist = "Not exist";
		from_file = "Load";
		from_parent = "Get";
		theme = "Theme";
		
		menu_file = "File";
		menu_file_exit = "Exit";
		menu_file_log_save = "Save log";
		menu_file_load = "Load the function";
		menu_file_load_1 = "On primary";
		menu_file_load_2 = "On secondary";
		menu_file_save = "Save the function";
		menu_file_save_1 = "Primary";
		menu_file_save_2 = "Secondary";
		menu_tool = "Tool";
		menu_tool_set = "Setting";
		menu_tool_language_creator = "Language creator";
		menu_view = "View";
		menu_view_compare = "Function compare helper";
		menu_help = "Help";
		menu_help_basic = "Basic helps";
		menu_help_ver = "About...";
		
		accept = "accept";
		cancel = "cancel";
		text_console_help = "Act console helps\n==================================\n function [Function]\n \tInput the function.\n \t\tf [Function]\n \tis also available.\n input [Value]\n \tInput a value and get the result.\n \tFunction should inserted first.\n \t\t= [Value]\n \tis also available.\n differential\n \tGet differential function.\n \tFunction should inserted first.\n \tOn console, differential function will replace inserted function.\n \tOn GUI mode, differential function will saved another place.\n \t\t'\n \tis also available.\n up\n \tOnly for GUI mode.\n \tMove secondary equation to the primary equation field.\n graph\n \tOnly for GUI mode.\n \tDraw the graph.\n==================================\n";
		
		error_cannot_translate = "Cannot translate strings to the function.";
		error_unknown_coefficient = "Cannot read coefficient value.";
		error_unknown_exponent = "Cannot read exponential value.";
		error_wrong_value = "Cannot read number value.";
		
		console_select = "Select the menu : ";
		console_menu = new String[4];
		console_menu[0] = "Input the function";
		console_menu[1] = "Differential";
		console_menu[2] = "Input value and get result";
		console_menu[3] = "Exit";
		console_input_please = "Please input numbers : ";
		console_input_function = "Please input equation : ";
		
		tooltips = new String[23];
		tooltips[0] = "Input the value to inserted function.";
		tooltips[1] = "Run inserted commands.";
		tooltips[2] = "Input the function.";
		tooltips[3] = "Draw a graph.";
		tooltips[4] = "Differential inserted function.";
		tooltips[5] = "Draw a graph of secondary function.";
		tooltips[6] = "Move the secondary function box to primary function box.";
		tooltips[7] = "Close this program.";
		tooltips[8] = "Save logs as a text file.";
		tooltips[9] = "Open the option dialog.";
		tooltips[10] = "Open the language creator to modify this program's labels.";
		tooltips[11] = "The menu includes such as saving files, and closing the program.";
		tooltips[12] = "The menu includes such as creating language pack, and opening the option window.";
		tooltips[13] = "Load a function from the file.";
		tooltips[14] = "Save a function as file.";
		tooltips[15] = "The menu includes many dialogs for your works.";
		tooltips[16] = "Help you to compare two functions.";
		tooltips[17] = "The menu includes many acts.";
		tooltips[18] = "Differential inserted function.";
		tooltips[19] = "Move the secondary function box to primary function box.";
		tooltips[20] = "Draw graphs.";
		tooltips[21] = "Draw a graph.";
		tooltips[22] = "Draw a graph of secondary function.";
		
		tutorials = new String[100];
		tutorials[0] = "이 창을 닫으면 프로그램을 이용하실 수 있습니다.";
		tutorials[1] = "반드시 아래의 내용을 숙지하신 후 이용하시기 바랍니다.";
		tutorials[2] = "";
		tutorials[3] = "VDP 기본적인 사용 방법입니다.";
		tutorials[4] = "";
		tutorials[5] = "모든 함수(방정식)은 y = f(x) 에서 f(x) 부분만을 입력합니다.";
		tutorials[6] = "가령, \"y = x^5 + 3x^4\" 을 입력하고 싶다면 \"x^5 + 3x^4\" 만을 입력합니다.";
		tutorials[7] = "";
		tutorials[8] = "모든 함수(방정식)은, 하나 혹은 여러 항으로 구성됩니다.";
		tutorials[9] = "항에는, 직전 연산자, 계수, 해당 함수, 내부의 항, 그리고 지수 이렇게 5가지 요소를 포함합니다.";
		tutorials[10] = "";
		tutorials[11] = "VDP에 입력하실 때는 반드시, 항의 5가지 요소를 붙여서 입력하셔야 항으로 인식합니다.";
		tutorials[12] = "지수가 1이라면 지수 부분은 생략해도 되며, 지수가 1이 아니라면 ^ 기호를 뒤에 붙여 적고 그 뒤에 지수값을 붙여 적습니다.";
		tutorials[13] = "";
		tutorials[14] = "예 : 5.29x^4.19 (O) , 10.1x ^ 5.7 (X) , 17.7x ^2.8 (X)";
		tutorials[15] = "";
		tutorials[16] = "여러 항으로 구성된 함수인 경우, 띄어쓰기로 각 항을 구분해 입력하셔야 합니다.";
		tutorials[17] = "";
		tutorials[18] = "예 : 5x^5 + 7x^4 + x + 35 (O) , 5x^5+7x^4+x+35 (X)";
		tutorials[19] = "";
		tutorials[20] = "다항식을 제외한 \"여러 항으로 구성된\" 함수를 아직 지원하지 않습니다.";
		tutorials[21] = "단항식의 경우, sin, cos, tan 도 인식합니다.";
		tutorials[22] = "";
		tutorials[23] = "그러면 즐거운 시간 되시기 바랍니다.";
		
		themes = new String[3];
		themes[0] = "Basic";
		themes[1] = "Nimbus";
		themes[2] = "Windows";
		
		title = "VDP";
		welcome = "Welcome to use VDP v0.1\nMade by dankook university group named MAVA\n2013 MAVA";
		maker = "2013 MAVA, Dankook Univ.";
		bye = "Thanks for using VDP.\n" + maker +"\n";
	}	
	public int getLanguageType()
	{
		return LANGUAGE_ENGLISH;
	}
}
