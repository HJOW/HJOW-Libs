package org.duckdns.hjow.commons.vdp.elements;

import java.util.Vector;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.differential.*;
import org.duckdns.hjow.commons.vdp.setting.DebugOnlySetting;
import org.duckdns.hjow.commons.vdp.tracker.*;
import org.duckdns.hjow.commons.vdp.translator.Translator;
public class MultipleSection extends Section
{
	private static final long serialVersionUID = -314575354548161132L;
	public Section[] contents;
	
	
	public MultipleSection(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public String toBasicString() 
	{
		String sum = String.valueOf(this.previousOperation);
		if(coefficient.data != 1.0) sum = sum + coefficient.toBasicString();
		sum = sum + "(";
		String temp2 = "";
		if(contents == null || contents.length == 0 || contents[0] == null)
		{
			return null;
		}
		for(int i=0; i<contents.length; i++)
		{
			temp2 = contents[i].toBasicString();
			sum = sum + " " + temp2;			
		}
		sum = sum + " )";
		if(exponent.data != 1.0)
		{
			sum = sum + "^" + exponent.toBasicString();
		}
		return sum;
	}
	
	@Override
	public void setPreviousOperation(char op)
	{		
		if(contents != null)
		{
			if(contents.length == 1)
			{
				contents[0].setPreviousOperation(op);
			}
			else
			{
				if(op == '-')
				{			
					for(int i=0; i<contents.length; i++)
					{
						if(contents[i].previousOperation == '+' || contents[i].previousOperation == '-')
						{
							contents[i].coefficient.data *= -1.0;
						}
					}
					previousOperation = '+';
				}
				else this.previousOperation = op;
			}
		}
	}
	
	

	@Override
	public int getSectionType() 
	{
		return 2;
	}

	@Override
	public Section differential() 
	{
		if(this.exponent.data == 1.0)
		{
			//if(DebugOnlySetting.print_test_message >= 1) System.out.println("다항식 미분 대상 : " + this.toBasicString());
			//Section trimmed_first;
			MultipleSection trimmed;
			
			/*
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("변수 준비");
			Translator translator = new Translator();
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("번역기 준비");
			trimmed_first = translator.trim((MultipleSection)this.clone());
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("간단화 완료");
			if(trimmed_first.getSectionType() == Section.MULTIPLE)
			{
				trimmed = (MultipleSection) trimmed_first;
			}
			else
			{
				if(DebugOnlySetting.print_test_message >= 1) System.out.println("간단화 재호출");
				return trimmed_first.differential();
			}		
			*/
			
			trimmed = (MultipleSection) this.trim();
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("1차 trim 완료");
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "1차 Trim 완료 , " + trimmed.toBasicString());
			
			if(contents != null && contents.length >= 1)
			{
				if(DebugOnlySetting.print_test_message >= 1) System.out.println("전체가 상수인지 검사");
				boolean allZero = true;
				for(int i=0; i<contents.length; i++)
				{
					if(contents[i] != null)
					{
						if(contents[i].getSectionType() != Section.CONSTANT)
							allZero = false;
					}
					else allZero = false;
				}
				if(allZero) return Section.initialize(Section.ZERO);
			}
			
			if(trimmed.contents.length == 1)
			{
				if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "배열 크기가 0이므로 미분 결과 나옴 : " + trimmed.contents[0].differential().toBasicString());
				return trimmed.contents[0].differential();
			}
			
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("간단화 결과 : " + trimmed.toBasicString());
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("길이 : " + trimmed.contents.length);
			
			Vector<DifferentialBlock> lists = new Vector<DifferentialBlock>(); // 덧셈 기준으로 나눔
			DifferentialBlock temp = new DifferentialBlock(tracker);
			
			for(int i=0; i<trimmed.contents.length; i++)
			{
				if(DebugOnlySetting.print_test_message >= 1) System.out.println(" " + i + " 번째 처리 대상 : " + trimmed.contents[i].toBasicString());
				if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + " 곱셈, 나눗셈을 묶음 , " + trimmed.contents[i].toBasicString());
				if((trimmed.contents[i].previousOperation == '*' || trimmed.contents[i].previousOperation == '/') || i == 0)
				{
					if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "  곱셈, 나눗셈임");
					temp.sections.add(trimmed.contents[i]);
				}
				else if(trimmed.contents[i].previousOperation == '+' || trimmed.contents[i].previousOperation == '-')
				{				
					if(DebugOnlySetting.print_test_message >= 1) System.out.println("      처리 대상 입력 시도 : " + temp.toBasicString());
					if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "  덧셈, 뺄셈임, 기존 값 리스트에 추가 : " + temp.toBasicString() + ", 임시 저장소 초기화");
					if(temp.sections.size() != 0)
					{					
						lists.add(temp);					
					}
					temp = new DifferentialBlock(tracker);
					
					temp.previousOperation = trimmed.contents[i].previousOperation;
					temp.sections.add(trimmed.contents[i]);
				}
				
			}
			if(temp != null && temp.sections != null && temp.sections.size() != 0)
			{				
				lists.add(temp);
				
				if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "  곱셈, 나눗셈 묶기 최종 단계로 잔류내용 추가 : " + temp.toBasicString());
				if(DebugOnlySetting.print_test_message >= 1) System.out.println("   처리 후 입력 : " + temp.toBasicString() + ", lists의 크기 : " + lists.size());
			}
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("  미분 전, 내용 크기 : " + lists.size());
			
			Vector<DifferentialBlock> result_list = new Vector<DifferentialBlock>(); // 미분 결과들이 들어감
			
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "미분 시작" + temp.toBasicString());
			for(int i=0; i<lists.size(); i++)
			{
				if(DebugOnlySetting.print_test_message >= 1) System.out.println("    삽입됨 : " + lists.get(i).toBasicString());
				if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + " 미분해서 추가됨" + lists.get(i).differential().toBasicString());
				
				result_list.add(lists.get(i).differential());
			}
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("  미분 후, 내용 삽입 이전 리스트 크긴 : " + result_list.size());
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "미분됨, 크기 : " + result_list.size());
			
			MultipleSection newOne = (MultipleSection) Section.initialize(Section.MULTIPLE);		
			newOne.contents = new Section[result_list.size()];
			
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("  미분 후, 내용 삽입 이전 : " + newOne.toBasicString());
			
			Translator translator = new Translator(tracker);
			
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "묶음에서 식으로 전환");
			
			for(int i=0; i<result_list.size(); i++)
			{
				MultipleSection partOne = (MultipleSection) Section.initialize(Section.MULTIPLE);
				partOne.contents = new Section[result_list.get(i).sections.size()];
				for(int j=0; j<result_list.get(i).sections.size(); j++)
				{	
					partOne.contents[j] = result_list.get(i).sections.get(j);	
				}
				newOne.contents[i] = partOne;
				
			}
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("  미분 결과의 간단화 이전 : " + newOne.toBasicString());
			/*
			newOne.contents = new Section[(result_list.size() * 2) - 1];		
			for(int i=0; i<((result_list.size() * 2) - 1); i+=2)
			{
				MultipleSection partOne = (MultipleSection) Section.initialize(Section.MULTIPLE);
				partOne.contents = new Section[result_list.get(i/2).sections.size()];
				for(int j=0; j<result_list.get(i/2).sections.size(); j++)
				{	
					partOne.contents[j] = result_list.get(i/2).sections.get(j);	
				}
				newOne.contents[i] = partOne;			
			}
			for(int i=1; i<((result_list.size() * 2) - 2); i+=2)
			{
				newOne.contents[i] = Section.initialize(Section.OPERATION);
			}*/
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("2차 trim 호출");
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "현재 : " + newOne.toBasicString());
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "2차 trim 호출");
			
			Section result = newOne.trim();
			
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("최종 trim 호출");
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "현재 : " + newOne.toBasicString());
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "최종 trim 호출");
			
			result = translator.trim(result);
			
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("  미분 결과 : " + result.toBasicString());
			if(DebugOnlySetting.collect_tracking >= 1) addThis(true, "differential()" + "결과 : " + result.toBasicString());
			result.coefficient.multiply(this.coefficient);
			return result;
		}
		else
		{
			MultipleSection original = (MultipleSection) this.clone();
			original.coefficient.data = 1.0;
			original.exponent.data = this.exponent.data - 1.0;
			original.previousOperation = '+';
			Section temp = (Section) this.clone();
			temp.coefficient.data = 1.0;
			temp.exponent.data = 1.0;
			temp = temp.differential();
			temp.previousOperation = '*';
			MultipleSection result = (MultipleSection) Section.initialize(Section.MULTIPLE);
			result.previousOperation = this.previousOperation;
			result.contents = new Section[2];
			result.coefficient.data = this.coefficient.data * this.exponent.data;
			result.contents[0] = original;
			result.contents[1] = temp;
			
			return result;
		}
	}
	@Override
	public boolean isDifferentiable() 
	{
		boolean isDifferentiable = true;
		for(int i=0; i<contents.length; i++)
		{
			if(! contents[i].isDifferentiable()) isDifferentiable = false;
		}
		return isDifferentiable;
	}
	public Section getContent(int index)
	{
		return contents[index];
	}
	public int getContentCount()
	{
		return contents.length;
	}

	@Override
	public Elements clone() 
	{
		MultipleSection newone = (MultipleSection) Section.initialize(Section.MULTIPLE);
		newone.coefficient.data = this.coefficient.data;
		newone.exponent.data = this.exponent.data;
		newone.inside = (Data) this.inside.clone();
		newone.previousOperation = this.previousOperation;
		newone.contents = new Section[this.contents.length];
		for(int i=0; i<this.contents.length; i++)
		{
			newone.contents[i] = (Section) this.contents[i].clone();
		}
		return newone;
	}

	@Override
	public String getName()
	{
		return "";
	}

	@Override
	public DoubleData calculate(DoubleData value)
	{
		DoubleData newOne = new DoubleData(tracker);
		boolean operationExist = false;
		char operation = '+';
		for(int i=0; i<contents.length; i++)
		{
			if(contents[i].getSectionType() == Section.OPERATION)
			{
				operation = contents[i].previousOperation;
				operationExist = true;
				continue;
			}
			else
			{
				if(operationExist)
				{
					if(contents[i].previousOperation == '-' && operation == '+')
					{
						contents[i].previousOperation = '-';
					}
					if(contents[i].previousOperation == '+' && operation == '-')
					{
						contents[i].previousOperation = '-';
					}
					else if(contents[i].previousOperation == '-' && operation == '-')
					{
						contents[i].previousOperation = '+';
					}
					else if(operation == '*' || operation == '/')
					{
						if(contents[i].previousOperation == '*' || contents[i].previousOperation == '/')
						{
							if(operation == '*' && contents[i].previousOperation == '/')
							{
								contents[i].previousOperation = '*';
							}
							else if(operation == '/' && contents[i].previousOperation == '*')
							{
								contents[i].previousOperation = '/';
							}
						}
						else contents[i].previousOperation = operation;
					}
					operationExist = false;
				}
				switch(contents[i].previousOperation)
				{
				case '+':
					newOne.data += contents[i].calculate(value).data;
					break;
				case '-':
					newOne.data -= contents[i].calculate(value).data;
					break;
				case '*':
					newOne.data *= contents[i].calculate(value).data;
					break;
				case '/':
					newOne.data /= contents[i].calculate(value).data;
					break;
				}
				
			}
		}
		if(exponent.data != 1.0)
		{
			newOne.data = Math.pow(newOne.data, exponent.data);
		}
		if(coefficient.data != 1.0)
		{
			newOne.data = newOne.data * coefficient.data;
		}
		return newOne;
	}
	
	@Override
	public Section trim()
	{
		Section newOne;
		if(DebugOnlySetting.print_test_message >= 1) System.out.println("===Trim 호출===");
		
		
		if(this.contents != null && this.contents.length >= 1)
		{
			Vector<Section> temp = new Vector<Section>();
			for(int i=0; i<this.contents.length; i++)
			{
				if(this.contents[i] != null)
				{
					if(this.contents[i].getSectionType() == Section.CONSTANT 
							&& (! (this.contents[i].previousOperation == '*' || this.contents[i].previousOperation == '/')) 
							&&  ((DoubleData)this.contents[i].inside).data == 0.0)
					{
						
					}
					else if(this.contents[i].getSectionType() == Section.CONSTANT 
							&& (! (this.contents[i].previousOperation == '+' || this.contents[i].previousOperation == '-')) 
							&&  ((DoubleData)this.contents[i].inside).data == 1.0)
					{
						
					}
					else
					{
						temp.add(this.contents[i].trim());
					}
				}
					
			}
			this.contents = new Section[temp.size()];
			for(int i=0; i<this.contents.length; i++)
			{
				this.contents[i] = temp.get(i);
			}
		}
		
		
		if(this.contents != null && this.contents.length > 1)
		{
			newOne = (Section) this.clone();
		}
		else if(this.contents != null && this.contents.length == 1)
		{
			newOne = (Section) this.contents[0].clone();
		}		
		else newOne = (Section) this.clone();
		
		if(newOne.getSectionType() == Section.MULTIPLE)
		{
			MultipleSection newMultiples = (MultipleSection) newOne;
			if(newMultiples.contents.length == 1)
			{
				newOne = newOne.trim();
			}
		}
		
		// null 항 없애기
		if(DebugOnlySetting.print_test_message >= 1) System.out.println("null 판정 이전 : " + newOne.toBasicString());
		Vector<Section> resultList = new Vector<Section>();
		if(newOne.getSectionType() == Section.MULTIPLE)
		{
			if(((MultipleSection) newOne).contents != null)
			{
				for(int i=0; i<((MultipleSection) newOne).contents.length; i++)
				{
					if(((MultipleSection) newOne).contents[i] != null)
					{
						if(DebugOnlySetting.print_test_message >= 1) System.out.println("판정 대상 :" + ((MultipleSection) newOne).contents[i] + ", 판정 결과 : " + Translator.isEliminatable(((MultipleSection) newOne).contents[i]));
						if(! Translator.isEliminatable(((MultipleSection) newOne).contents[i])) resultList.add(((MultipleSection) newOne).contents[i]);
					}
				}
			}
		}
		else resultList.add(newOne);
		
		if(resultList.size() == 0)
		{
			if(DebugOnlySetting.print_test_message >= 1) System.out.println("null 판정 최종 결과 : " + newOne.toBasicString());
			return Section.initialize(Section.NULL);
		}
		else if(resultList.size() == 1) return resultList.get(0);
		else
		{
			MultipleSection newOne2 = (MultipleSection) Section.initialize(Section.MULTIPLE);
			newOne2.previousOperation = newOne.previousOperation;
			newOne2.coefficient = newOne.coefficient;
			newOne2.exponent = newOne.exponent;
			newOne2.inside = newOne.inside;
			newOne2.contents = new Section[resultList.size()];
			for(int i=0; i<resultList.size(); i++)
			{
				if(DebugOnlySetting.print_test_message >= 1) System.out.println("null 판정 결과 삽입 : " + resultList.get(i).toBasicString());
				newOne2.contents[i] = resultList.get(i);
			}
			newOne = newOne2;
		}		
		//if(DebugOnlySetting.print_test_message >= 1) System.out.println("  MultipleSection Trim : " + newOne.toBasicString());
		if(DebugOnlySetting.print_test_message >= 1) System.out.println("null 판정 최종 결과 : " + newOne.toBasicString());
		return newOne;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_MULTIPLE_SECTION;
	}
}
