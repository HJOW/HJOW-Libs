package org.duckdns.hjow.commons.vdp.translator;

import java.util.Vector;

import org.duckdns.hjow.commons.vdp.data.DoubleData;
import org.duckdns.hjow.commons.vdp.data.SectionData;
import org.duckdns.hjow.commons.vdp.elements.MultipleSection;
import org.duckdns.hjow.commons.vdp.elements.Section;
import org.duckdns.hjow.commons.vdp.elements.SectionList;
import org.duckdns.hjow.commons.vdp.exceptions.CannotTranslateToFunctionException;
import org.duckdns.hjow.commons.vdp.exceptions.IsNotFunctionException;
import org.duckdns.hjow.commons.vdp.exceptions.IsNotNumberException;
import org.duckdns.hjow.commons.vdp.setting.DebugOnlySetting;
import org.duckdns.hjow.commons.vdp.tracker.Tracker;

public class Translator
{
	public static Tracker tracker;
	public Translator(Tracker tracker)
	{
		if(tracker != null) Translator.tracker = tracker;
	}
	public Section stringToPartedSection(String str) // 블록 단위로 잘라져 있다고 가정함, 같은 수준의 괄호가 여러 개인 경우 제대로 불러오지 못함
	{
		Section newOne;
		//Vector<Section> saves = new Vector<Section>();
		char[] characters = str.toCharArray();
		char[] insides;
		String insideString = "";
		String functionName = "";
		String coefficientString = "";
		int frontBlacketLocation = 0, behindBlacketLocation = 0, blacketSize = 0;
		int sectionType = 0;
		boolean blacketExist = false;
		boolean onlyBlacket = false;
		boolean expExist = false;
		for(int i=0; i<characters.length; i++) // 괄호가 있는지 검사
		{
			if(characters[i] == '(' || characters[i] == ')')
			{
				blacketExist = true;
				break; // 하나라도 찾았으면 반복 끝냄
			}
		}		
		
		if(blacketExist) // 괄호 위치 찾기
		{
			// 괄호 위치를 찾기
			for(int i=0; i<characters.length; i++)
			{
				if(characters[i] == '(')
				{
					frontBlacketLocation = i;
					break;
				}			
			}
			for(int i=characters.length - 1; i >= frontBlacketLocation; i--)
			{
				if(characters[i] == ')')
				{
					behindBlacketLocation = i;
					break;
				}			
			}	
		}
		
		if(blacketExist) // 어떤 함수인지 확인
		{
			// 괄호가 시작되기 전까지의 글자들 가져옴	
			for(int i=0; i<frontBlacketLocation; i++)
			{
				if(isNumber(characters[i]))
				{
					coefficientString += String.valueOf(characters[i]);
				}
				else if(isCharacter(characters[i]))
				{					
					functionName += String.valueOf(characters[i]);
				}						
			}
			if(functionName.equals(""))
			{
				onlyBlacket = true;				
			}
		}
		else // 괄호 없는 경우 처리
		{
			if(str.toCharArray().length == 0 && isBasicOperator(str.toCharArray()[0])) // 길이가 1이고 그 내용이 부호일 때
			{
				newOne = Section.initialize(Section.OPERATION);
				newOne.previousOperation = str.toCharArray()[0];
				return newOne;
			}
			else if(isInteger(str) || isDouble(str)) // 문자열 자체를 숫자로 변환할 수 있다면 상수임이 확정
			{
				newOne = Section.initialize(Section.CONSTANT);
				DoubleData ndata = new DoubleData(null);
				ndata.data = Double.parseDouble(str);
				newOne.inside = ndata.clone();
				return newOne;
			}
			else // 다항식(단항)
			{
				//int whereIsX = 0;
				boolean existX = false;
				boolean existExp = false;
				boolean blockFinish = false;
				String constants_before = "";
				String exponent_before = "";
				double constant_data = 1;
				double exponent_data = 1;
				for(int i=0; i<characters.length; i++)
				{
					//System.out.println(i + " : " + characters[i]);
					if(characters[i] == 'x' && (! blockFinish))
					{
						//whereIsX = i;
						existX = true;
					}
					else if((! existX) && (! blockFinish))
					{
						constants_before += String.valueOf(characters[i]);
					}
					else if(characters[i] == '^' && (! blockFinish))
					{
						existExp = true;
					}
					else if(existExp && (! blockFinish))
					{
						exponent_before += String.valueOf(characters[i]);
					}
					else if(characters[i] == ' ') blockFinish = true;
				}
				if(existX)
				{
					newOne = Section.initialize(Section.BASIC);
					try
					{
						if(! constants_before.equals(""))
						{
							constant_data = Double.parseDouble(constants_before);
							newOne.coefficient.data = constant_data;
						}
						if(! exponent_before.equals(""))
						{
							exponent_data = Double.parseDouble(exponent_before);
							newOne.exponent.data = exponent_data;
						}
						return newOne;
					} 
					catch (NumberFormatException e)
					{
						throw new IsNotNumberException("오류 Error : 숫자 인식에 실패하였습니다. Cannot read numbers somewhere.");
					}
				}
				else throw new IsNotFunctionException("오류 Error : 함수로의 변환에 실패하였습니다. Cannot change to function data.");
			}
		} // 이제 더이상 괄호가 없는 경우는 없음
		
		// 괄호 안의 내용 추출 시작
		
		blacketSize = behindBlacketLocation - frontBlacketLocation - 1; // 괄호 내용 분리
		try
		{
			insides = new char[blacketSize];
		} 
		catch (NegativeArraySizeException e1)
		{
			throw new CannotTranslateToFunctionException("오류 Error : 괄호가 불규칙합니다. 쌍따옴표를 사용해 보십시오. Cannot find other blacket. How about using double quotation mark?");
			//throw new IsNotNumberException("오류 Error : " + expPart + " : 지수를 변환하지 못했습니다. Cannot read exponential number.");
		}
		for(int i=frontBlacketLocation + 1; i<behindBlacketLocation; i++)
		{
			insides[i - (frontBlacketLocation + 1)] = characters[i];
		}
		insideString = new String(insides);
		
		// 항으로 변환 시작
		
		if(! onlyBlacket) // 함수가 있는 경우
		{
			for(int i=0; i<SectionList.getSectionNumberList().length; i++) // 함수 이름이 함수 목록에 있는지 확인 후 번호 받아오기
			{
				if(functionName.equals(SectionList.getList()[i]))
				{
					sectionType = SectionList.getSectionNumberList()[i];
					break;
				}
			}
			
			newOne = Section.initialize(sectionType);
			SectionData insideSectionData = new SectionData(tracker);
			insideSectionData.contents = stringToSection(insideString); // 재귀 호출을 통해 괄호 안의 내용 받아오기
			newOne.inside = insideSectionData;						
		}
		else // 괄호만 있는 경우
		{			
			newOne = stringToSection(insideString);		
			//System.out.println("괄호만 있는 경우 : " + newOne.toBasicString());
		}
		
		for(int i=0; i<characters.length; i++) // 지수가 있는지 검사
		{
			if(characters[i] == '^')
			{
				expExist = true;
				break; // 하나라도 찾았으면 반복 끝냄
			}
		}
		
		DoubleData exponentData = new DoubleData(null);
		if(expExist)
		{
			String expPart = "";
			boolean arriveEx = false;
			for(int i=0; i<characters.length; i++)
			{
				//System.out.println("  지수 인식 중 : " + characters[i] + ", ^ 만남 : " + arriveEx);
				if((! arriveEx) && (i >= behindBlacketLocation) && (characters[i] == '^'))
				{
					arriveEx = true;
				}
				else if(arriveEx && (characters[i] == ' ' || isBasicOperator(characters[i])))
				{
					break;
				}
				else if(arriveEx)
				{
					expPart = expPart + String.valueOf(characters[i]);
				}
			}
			if((! expPart.equals("") || expPart.equals(" ")))
			try
			{
				exponentData.data = Double.parseDouble(expPart);
				newOne.exponent = exponentData;
			}
			catch(NumberFormatException e)
			{
				throw new IsNotNumberException("오류 Error : " + expPart + " : 지수를 변환하지 못했습니다. Cannot read exponential number.");
			}
		}
		
		
		try
		{			
			if(! coefficientString.equals("")) newOne.coefficient.data = Double.parseDouble(coefficientString);
			else newOne.coefficient.data = 1.0;
		} 
		catch (NumberFormatException e)
		{
			throw new IsNotNumberException("오류 Error : 계수를 변환하지 못했습니다. Cannot read coefficient.");
		}
		return newOne;
	}
	public static boolean isEliminatable(Section target)
	{
		if(target == null) return true;
		else if(target.getSectionType() == Section.NULL) return true;
		else if(target.getSectionType() == Section.CONSTANT)
		{
			if(target.previousOperation == '*' || target.previousOperation == '/')
			{
				if(((DoubleData) target.inside).data == 1.0)
				{
					return true;
				}
				else return false;
			}
			else
			{
				if(((DoubleData) target.inside).data == 0.0)
				{
					return true;
				}
				else return false;
			}
		}
		else if(target.getSectionType() == Section.MULTIPLE)
		{
			if(((MultipleSection) target).contents == null || (((MultipleSection) target).contents.length == 1 && isEliminatable(((MultipleSection) target).contents[0])))
			{
				return true;
			}
			else if(((MultipleSection) target).contents.length > 1)
			{
				MultipleSection temp = (MultipleSection) target;
				boolean result = true;
				for(int i=0; i<temp.contents.length; i++)
				{
					if(temp.contents[i] != null)
					{
						if(! isEliminatable(temp.contents[i]))
						{
							result = false;
						}
					}
				}
				return result;
			}
			else return false;
		}
		else return false;
	}
	public Section stringToSection(String str) // 블록 단위로 분할하는데, 괄호로 묶여진 것은 한 블록으로 봐서 위의 메소드를 이용해 변환
	{
		 
		
		int infinite_loop_prevent = 0;
		String str_copy = new String(str);
		char[] chars = str_copy.toCharArray();
		boolean[] checked = new boolean[chars.length];
		for(int i=0; i<checked.length; i++)
		{
			checked[i] = false;
		}
		Vector<String> saves = new Vector<String>();		
		Section newOne;
		boolean finished = false; 
		while(! finished)
		{
			String temp = "";
			boolean blacketExist = false;
			int blacketLevel = 0;
			saves.clear();
			for(int i=0; i<chars.length; i++)
			{
				if(isNumber(chars[i]) || isCharacter(chars[i]) || isBasicOperator(chars[i]))
				{
					temp = temp + String.valueOf(chars[i]);
					checked[i] = true;
				}
				else if(isBlacket(chars[i]))
				{
					if(chars[i] == '(')
					{
						if(blacketLevel >= 0) blacketExist = true;
						blacketLevel++;
					}
					else if(chars[i] == ')')
					{						
						blacketLevel--;
						if(blacketLevel <= 0) // 괄호 밖으로 완전히 빠져 나왔을 때
						{
							blacketExist = false;
							blacketLevel = 0; // 혹시 몰라서...
						}
					}
					temp = temp + String.valueOf(chars[i]);
					checked[i] = true;
				}
				else if(isDelimiter(chars[i]))
				{
					if(blacketExist) // 괄호 안에 있을 때 : 공백 자체도 저장해야 함
					{
						temp = temp + String.valueOf(chars[i]);
						checked[i] = true;
					}
					else // 괄호 바깥일 때 : 분리해서 저장하고 임시 공간 초기화
					{
						saves.add(temp);
						temp = "";
						checked[i] = true;
					}
				}
				if(i == chars.length - 1)
				{
					saves.add(temp);
					temp = "";
				}
			}
			if(! temp.equals("")) saves.add(temp);
			
			// 다 읽었는지 확인하기
			finished = true;
			for(int i=0; i<checked.length; i++)
			{
				if(checked[i] == false)
				{
					finished = false;
				}
			}
			if(finished) break;
			infinite_loop_prevent++;
			if(infinite_loop_prevent > 100000) throw new CannotTranslateToFunctionException("함수 인식 중 무한 반복이 발생하였습니다.");
		}
		
		// 임시로 넣은 부호 다음 항에 병합 (가능하면 병합)
		Vector<String> saves_op = new Vector<String>();
		boolean skip = false;
		for(int i=0; i<saves.size(); i++)
		{			
			if(skip)
			{
				skip = false;
				continue;
			}
			if(saves.get(i).toCharArray().length == 1)
			{
				if(isBasicOperator(saves.get(i).toCharArray()[0]) && (i < (saves.size() - 1)))
				{
					char[] nextOne = saves.get(i+1).toCharArray();
					char thisOne = saves.get(i).toCharArray()[0];
					if(nextOne[0] == '-')
					{
						if(thisOne == '+')
						{
							
						}
						else if(thisOne == '-')
						{
							nextOne[0] = '+';
						}
					}	
					else if(nextOne[0] == '+')
					{
						if(thisOne == '+')
						{
							
						}
						else if(thisOne == '-')
						{
							nextOne[0] = '-';
						}
					}
					
					saves_op.add(new String(nextOne));
					skip = true;
				}
				else if(isNumber(saves.get(i).toCharArray()[0]) || saves.get(i).toCharArray()[0] == 'x')
				{
					saves_op.add(saves.get(i));
				}
				else
				{
					throw new CannotTranslateToFunctionException(i + " 번째 블럭을 항으로 변환하지 못했습니다.");
				}
			}
			else
			{
				saves_op.add(saves.get(i));
			}
		}
		// 임시 벡터 내용을 다시 saves로 옮김
		saves.clear();
		for(int i=0; i<saves_op.size(); i++)
		{
			saves.add(saves_op.get(i));
		}		
		/*
		System.out.println("본격적으로 변환, saves 크기 : " + saves.size());
		for(int i=0; i<saves.size(); i++)
		{
			System.out.println(" " + i + " 번째 saves 내용 : " + saves.get(i));
		}		
		*/
		
		if(saves.size() == 1) // 나뉘어지지 않았을 때 : 재귀함수 호출 필요 없음, 바로 변환함, 괄호 내에 분리자 있으면 다시 이 함수 호출하게 되어 있음
		{
			newOne = stringToPartedSection(saves.get(0));
		}
		else // 여러 개를 다 변환해야 함
		{
			Vector<Section> sections = new Vector<Section>();
			
			for(int i=0; i<saves.size(); i++)
			{
				
				sections.add(stringToSection(saves.get(i)));
				
			}
			MultipleSection temp_section = (MultipleSection) Section.initialize(Section.MULTIPLE);
			temp_section.contents = new Section[sections.size()];
			for(int i=0; i<sections.size(); i++)
			{
				temp_section.contents[i] = sections.get(i);
			}			
			newOne = temp_section;
			
			
		}
		return newOne;
	}	
	public Section trim(Section oldOne) // 항 단순화 작업
	{
		int type = oldOne.getSectionType();
		MultipleSection multiples;
		if(type != Section.MULTIPLE)
		{
			return oldOne;
		}
		else
		{
			multiples = (MultipleSection) oldOne;
			Vector<Section> lists_1 = new Vector<Section>();
			
			// 1차 : 부호 저장을 위한 임시 항이 있는지 검사해 합침
			for(int i=0; i<multiples.contents.length; i++)
			{
				if(multiples.contents[i].getSectionType() == Section.OPERATION)
				{
					if(i+1 < multiples.contents.length)
					{
						multiples.contents[i+1].previousOperation = multiples.contents[i].previousOperation;
					}
				}
				else lists_1.add(multiples.contents[i]);
			}
			
			// 1차의 결과 적용
			multiples.contents = new Section[lists_1.size()];
			for(int i=0; i<lists_1.size(); i++)
			{
				multiples.contents[i] = lists_1.get(i);
			}
			lists_1.clear();
			
			
			// 2차 : 상수항이 곱셈으로 연결되어 있는지 검사해 합침
			for(int i=0; i<multiples.contents.length; i++)
			{
				if(DebugOnlySetting.print_test_message >= 1) System.out.println("오류검사 : " + i + ", " + multiples.contents.length);
				if((i+1 < multiples.contents.length) && multiples.contents[i].getSectionType() == Section.CONSTANT &&(multiples.contents[i+1].previousOperation == '*' || multiples.contents[i+1].previousOperation == '/'))
				{
					if(i+1 < multiples.contents.length)
					{
						if(multiples.contents[i+1].previousOperation == '/') // 다음 항, 나누기 연산을 곱셈 연산으로 바꾸고 대신 지수를 역전
						{
							multiples.contents[i+1].previousOperation = '*';
							multiples.contents[i+1].exponent.data *= -1.0;
						}
						if(multiples.contents[i].previousOperation == '-') // 현재 항, 뺄셈 연산을 덧셈 연산으로 변경, 대신 상수를 역전
						{
							multiples.contents[i].previousOperation = '+';
							multiples.contents[i].coefficient.data *= -1.0;
						}
						multiples.contents[i+1].coefficient.data *= multiples.contents[i].coefficient.data; // 계수 적용
					}
				}
				else
				{
					lists_1.add(multiples.contents[i]);
				}
			}			
			
			// 2차의 결과 적용
			multiples.contents = new Section[lists_1.size()];
			for(int i=0; i<multiples.contents.length; i++)
			{
				multiples.contents[i] = lists_1.get(i);
			}
			lists_1.clear();
			
			//Section newOne;
			// 3차 : MultipleSection 여러 겹으로 되어 있는지 확인
			for(int i=0; i<multiples.contents.length; i++)
			{
				if(multiples.contents[i].getSectionType() == Section.MULTIPLE)
				{
					if(hasDuplicateMultiples(((MultipleSection)multiples.contents[i])))
					{
						multiples.contents[i] = removeDuplicateMultiples(((MultipleSection)multiples.contents[i]));
					}
				}
			}
			multiples = removeDuplicateMultiples(multiples);
			
			return multiples;
		}
	}
	public boolean hasDuplicateMultiples(MultipleSection sect)
	{
		if(sect.contents.length == 1)
		{
			if(sect.contents[0].getSectionType() == Section.MULTIPLE)
			{
				MultipleSection target = (MultipleSection) sect.contents[0];
				if(target.contents.length == 1)
				{
					return true;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}
	public MultipleSection removeDuplicateMultiples(MultipleSection oldOne)
	{
		MultipleSection newOne;
		if(hasDuplicateMultiples(oldOne))
		{
			newOne = (MultipleSection) oldOne.contents[0].clone();
		}
		else newOne = oldOne;
		
		return newOne;
	}
	private boolean isInteger(String str)
	{
		try
		{
			Integer.parseInt(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	private boolean isDouble(String str)
	{
		try
		{
			Double.parseDouble(str);
			if(isInteger(str)) return false; // 정수로도 변환이 가능하면 정수이지 실수가 아니다
			else return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	private boolean isNumber(char ch)
	{
		switch(ch)
		{
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		case '.': // 소수점을 위함
			return true;
		default:
			return false;
		}		
	}
	private boolean isBlacket(char ch)
	{
		if(ch == '(' || ch == ')') return true;
		else return false;
	}
	private boolean isDelimiter(char ch)
	{
		if(ch == ' ' || ch == '\n' || ch == '\t') return true;
		else return false;
	}
	private boolean isBasicOperator(char ch)
	{
		switch(ch)
		{
		case '+':
		case '-':
		case '*':
		case '/':
			return true;
		default:
			return false;
		}
	}
	private boolean isCharacter(char ch)
	{
		if(isNumber(ch) || isBlacket(ch) || isDelimiter(ch) || isBasicOperator(ch)) return false;
		else return true;
	}
}
