package org.duckdns.hjow.commons.vdp.language;

public class Korean extends Language
{
	private static final long serialVersionUID = 3513260299969520903L;

	public Korean()
	{
		s = "의";
		to = "로";
		exit = "종료";
		close = "닫기";
		constant = "상수";
		differential = "미분";
		distance = "차이";
		stdev = "표준편차";
		average = "평균";
		equation = "식";
		act = "작업";
		do_not_ask_again = "다시 보지 않기";
		function = "함수";
		graph = "그래프";
		input = "입력";
		integral = "적분";
		domain = "정의역";
		range = "범위";
		degree_of_equal = "두 함수 동일할 확률";
		f_range = "치역";
		load = "불러오기";
		output = "출력";
		polynomial = "다항함수";
		save = "저장";
		section = "항";
		solve = "해석";
		result = "결과";
		impossible = "불가능";
		possible = "가능";
		insert_value = "값 대입";
		up = "위";
		down = "아래";
		left = "왼쪽";
		right = "오른쪽";
		modified = "수정된";
		min = "최소";
		max = "최대";
		grid = "축";
		from_file = "파일로부터...";
		from_parent = "가져오기";
		theme = "테마";
		
		accept = "확인";
		cancel = "취소";
		not_exist = "존재하지 않음";
		text_console_help = "작업 콘솔 도움말\n==================================\n function [식]\n \t식을 입력합니다.\n \t\tf [식]\n \t위의 형태로도 사용할 수 있습니다.\n input [값]\n \t입력된 식에 값을 입력합니다.\n \t식이 먼저 입력되어 있어야 합니다.\n \t\t= [값]\n \t으로도 사용할 수 있습니다.\n differential\n \t입력된 식을 미분합니다.\n \t식이 먼저 입력되어 있어야 합니다.\n \t콘솔 모드인 경우 입력된 식을 미분된 식이 대체합니다.\n \tGUI 모드인 경우 조정된 식 란에 따로 입력됩니다.\n \t\t'\n \t으로도 사용할 수 있습니다.\n up\n \tGUI 모드에서만 유효합니다.\n \t조정된 식 란에 저장된 식을 식 란으로 올립니다.\n graph\n \tGUI 모드에서만 유효합니다.\n \t입력된 식으로 그래프를 그립니다.\n==================================\n";
		
		error_cannot_translate = "함수로 변환하지 못했습니다.";
		error_unknown_coefficient = "계수 인식에 실패하였습니다.";
		error_unknown_exponent = "지수 인식에 실패하였습니다.";
		error_wrong_value = "값을 인식하지 못했습니다.";
		
		menu_file = "파일";
		menu_file_exit = "종료";
		menu_file_log_save = "로그 저장";
		menu_file_load = "함수 파일 불러오기";
		menu_file_load_1 = "메인 함수란에...";
		menu_file_load_2 = "수정된 함수 란에...";
		menu_file_save = "함수 파일로 저장";
		menu_file_save_1 = "메인 함수란에...";
		menu_file_save_2 = "수정된 함수 란에...";
		menu_tool = "도구";
		menu_tool_set = "환경 설정";
		menu_tool_language_creator = "언어 파일 생성";
		menu_view = "보기";
		menu_view_compare = "함수 비교 도우미";
		menu_help = "도움말";
		menu_help_basic = "기본 사용 방법";
		menu_help_ver = "이 프로그램은...";
		
		console_select = "메뉴를 선택하세요 : ";
		console_menu = new String[4];
		console_menu[0] = "식 입력";
		console_menu[1] = "미분";
		console_menu[2] = "값 대입";
		console_menu[3] = "종료";
		console_input_please = "대입할 값을 입력해 주세요 : ";
		console_input_function = "식을 입력해 주세요 : ";
		
		tooltips = new String[23];
		tooltips[0] = "왼쪽의 값을 식에 대입하여 결과를 오른쪽에 보여줍니다.";
		tooltips[1] = "콘솔 명령을 동작합니다.";
		tooltips[2] = "왼쪽의 식을 입력합니다.";
		tooltips[3] = "입력된 식을 그래프로 그립니다.";
		tooltips[4] = "입력된 식을 미분하여 그 결과를 수정된 식 항에 입력합니다.";
		tooltips[5] = "수정된 식 항에 입력된 식을 그래프로 그립니다.";
		tooltips[6] = "수정된 식 항에 입력된 식을 입력된 식 항으로 옮깁니다.";		
		tooltips[7] = "프로그램을 종료합니다.";
		tooltips[8] = "로그 내역을 텍스트 파일로 저장합니다.";
		tooltips[9] = "환경 설정 창을 엽니다.";
		tooltips[10] = "이 프로그램의 언어를 변경할 수 있는 생성기를 엽니다.";
		tooltips[11] = "파일 저장, 프로그램 종료 등이 포함된 메뉴입니다.";
		tooltips[12] = "언어 생성, 환경 설정 등이 포함된 메뉴입니다.";
		tooltips[13] = "파일로부터 함수를 불러옵니다.";
		tooltips[14] = "함수를 파일로 저장합니다.";
		tooltips[15] = "작업에 도움이 되는 창을 열 수 있는 메뉴입니다.";
		tooltips[16] = "현재 입력된 함수 또는 임의의 함수를 불러와 비교할 수 있는 기능을 제공합니다.";
		tooltips[17] = "여러 작업들이 포함된 메뉴입니다.";
		tooltips[18] = "입력된 식을 미분하여 그 결과를 수정된 식 항에 입력합니다.";
		tooltips[19] = "수정된 식 항에 입력된 식을 입력된 식 항으로 옮깁니다.";
		tooltips[20] = "그래프를 그립니다.";
		tooltips[21] = "입력된 식을 그래프로 그립니다.";
		tooltips[22] = "수정된 식 항에 입력된 식을 그래프로 그립니다.";
		
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
		welcome = "VDP 사용을 환영합니다.\n이 프로그램은 단국대학교 수학과 그룹 MAVA가 만들었습니다.\n2013 MAVA";
		maker = "2013 MAVA, Dankook Univ.";
		bye = "VDP 를 사용해 주셔서 감사합니다.\n" + maker +"\n";
		
	}	
	public int getLanguageType()
	{
		return LANGUAGE_KOREAN;
	}
}