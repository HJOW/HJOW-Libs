package org.duckdns.hjow.commons.vdp.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.UIManager;
import org.duckdns.hjow.commons.vdp.elements.MultipleSection;
import org.duckdns.hjow.commons.vdp.elements.Section;
import org.duckdns.hjow.commons.vdp.exceptions.IsNotNumberException;
import org.duckdns.hjow.commons.vdp.language.Language;
import org.duckdns.hjow.commons.vdp.setting.DebugOnlySetting;
import org.duckdns.hjow.commons.vdp.setting.Setting;
import org.duckdns.hjow.commons.vdp.tracker.Tracker;
import org.duckdns.hjow.commons.vdp.translator.Translator;
import org.duckdns.hjow.commons.vdp.work.DifferentToResultWork;
import org.duckdns.hjow.commons.vdp.work.DifferentialWork;
import org.duckdns.hjow.commons.vdp.work.InputWork;
import org.duckdns.hjow.commons.vdp.work.InsertValueWork;
import org.duckdns.hjow.commons.vdp.work.ResultToGraphWork;
import org.duckdns.hjow.commons.vdp.work.WorkStore;

public class Kernal implements Runnable
{
	private String inputFieldString = "";
	private String outputFieldString = "";
	private OutputManager manager;
	private Language lang;
	private boolean threadSwitch = false;
	private Thread thread;
	private WorkStore works;
	private Section equation;
	private Section modified_equation;
	private Translator translator;
	public String default_path = "";
	public String system_os = "";
	public Setting setting;
	public boolean firstTime = true;
	private boolean consoleMode = false;
	private Scanner scanner;
	private boolean sectionInputMode = false;
	private boolean valueInputMode = false;
	private boolean openMode;
	private boolean trackMode;
	private boolean testMode;
	private static Tracker tracker;
	
	public Kernal(byte outputManagerType, Tracker tracker, Language _language, String looks)
	{				
		if(tracker != null) Kernal.tracker = tracker;
		initKernal(outputManagerType, _language, looks);
		//equation = (MultipleSection) Section.initialize(Section.MULTIPLE);
		//modified_equation = (MultipleSection) Section.initialize(Section.MULTIPLE);
		equation = Section.initialize();
		modified_equation = Section.initialize();		
		
		if(consoleMode) 
		{
			threadSwitch = true;
			thread.start();
		}
	}
	public Kernal(byte outputManagerType, Tracker tracker, Language _language, String inputedSection, String looks)
	{
		if(tracker != null) Kernal.tracker = tracker;
		initKernal(outputManagerType, _language, looks);
		//equation = (MultipleSection) Section.initialize(Section.MULTIPLE);		
		//((MultipleSection) equation).contents[0] = translator.stringToSection(inputedSection);
		modified_equation = Section.initialize();
		equation = translator.stringToSection(inputedSection);
		//modified_equation = (MultipleSection) Section.initialize(Section.MULTIPLE);
		if(manager.getType() == OutputManager.OUTPUT_FRAME)
		{
			// ((FrameOutput) manager).inputField.setText(inputedSection);
			// ((FrameOutput) manager).inputResultField.setText(inputedSection);
		}
		
		if(consoleMode) 
		{
			threadSwitch = true;
			thread.start();
		}
	}	
	private void initKernal(byte outputManagerType, Language _language, String looks)
	{
		try
		{
			system_os = System.getProperty("os.name");
			default_path = System.getProperty("user.home");
			String filePathDelimiter = "/";
			if(system_os.startsWith("Windows") || system_os.startsWith("windows"))
			{
				filePathDelimiter = "\\";
			}
			else
			{
				filePathDelimiter = "/";
			}
			default_path = default_path + filePathDelimiter + "vdp" + filePathDelimiter;
			setting = Setting.readSetting(default_path + "setting.vdps", manager);
			if(looks == null) looks = setting.lookAndFeel;
			firstTime = false;
		}
		catch(FileNotFoundException e)
		{
			firstTime = true;
			setting = new Setting();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			setting = new Setting();
		}
		
		this.lang = _language;
		
		if(looks != null)
		{
			try
			{
				UIManager.setLookAndFeel(looks);
			} 
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		switch(outputManagerType)
		{
		case OutputManager.OUTPUT_CONSOLE:
			this.manager = new ConsoleOutput(this, tracker, lang);
			consoleMode = true;
			break;
		case OutputManager.OUTPUT_FRAME:
			// this.manager = new FrameOutput(this, tracker, lang);
			// break;
		case OutputManager.OUTPUT_CLASSIC_FRAME:
			// this.manager = new ClassicFrame(this, tracker, lang);
			// break;
		default:
			this.manager = new ConsoleOutput(this, tracker, lang);
			consoleMode = true;
			break;
		}
		thread = new Thread(this);
		works = new WorkStore();
		translator = new Translator(tracker);		
		equation = new MultipleSection(tracker);
		equation = Section.initialize();
		
		if(firstTime && manager.getType() == OutputManager.OUTPUT_FRAME)
		{
			// ((FrameOutput) manager).openTutorial(true);
		}
		
		readyMessage();
		if(manager.getType() == OutputManager.OUTPUT_FRAME)
		{
			// ((FrameOutput) manager).inputFieldRequest(0);
		}
	}
	public void saveEquation(int where, File file) throws FileNotFoundException, IOException
	{
		switch(where)
		{
		case 0:
			saveEquation(equation, file);
			break;
		case 1:
			saveEquation(modified_equation, file);
			break;
		}
	}
	public void saveEquation(Section sect, File file) throws FileNotFoundException, IOException
	{
		ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream(file));
		objout.writeObject(sect);
		objout.close();
		manager.print(lang.getText(Language.TEXT_SAVE) + " : " + file.getAbsolutePath() + "\n");
	}
	public void loadEquation(int where, File file) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream objin = new ObjectInputStream(new FileInputStream(file));
		switch(where)
		{
		case 0:
			equation = (Section) objin.readObject();
			break;
		case 1:
			modified_equation = (Section) objin.readObject();
			break;
		}
		objin.close();
		if(manager.getType() == OutputManager.OUTPUT_FRAME)
		{
			switch(where)
			{
			case 0:
				// ((FrameOutput)manager).inputResultField.setText(equation.toBasicString());
				break;
			case 1:
				// ((FrameOutput)manager).differentResultField.setText(modified_equation.toBasicString());
				break;
			}
		}
		switch(where)
		{
		case 0:
			manager.print(lang.getText(Language.TEXT_LEFT) + " : " + equation + "\n");
			break;
		case 1:
			manager.print(lang.getText(Language.TEXT_LEFT) + " : " + modified_equation + "\n");
			break;
		}
	}
	public void inputOrder(String order)
	{
		char[] chars = order.toCharArray();
		Vector<String> blocks = new Vector<String>();
		boolean quotationInside = false;
		sectionInputMode = false;
		valueInputMode = false;
		openMode = false;
		trackMode = false;
		testMode = false;
		String buffer = "";
		for(int i=0; i<chars.length; i++)
		{			
			if((! quotationInside) && chars[i] == '\"')
			{
				quotationInside = true;
			}
			else if((! quotationInside) && chars[i] == ' ')
			{
				blocks.add(buffer);
				buffer = "";
			}
			else if(quotationInside && chars[i] == '\"')
			{
				quotationInside = false;
			}
			else
			{
				buffer = buffer + String.valueOf(chars[i]);
			}
		}
		if(! buffer.equals(""))
		{
			blocks.add(buffer);
		}
		for(int i=0; i<blocks.size(); i++)
		{
			orderOperate(blocks.get(i));
		}
		manager.inputFieldClear();
	}
	private void orderOperate(String block)
	{		
		if(DebugOnlySetting.print_test_message >= 1) System.out.println("콘솔 입력 : " + block);
		// FrameOutput mn = null;
		// if(manager.getType() == OutputManager.OUTPUT_FRAME) mn = (FrameOutput) manager;
		if(works == null) works = new WorkStore();
		if(thread == null) thread = new Thread(this);
		if(sectionInputMode && (! block.equals("")))
		{
			equation = translator.stringToSection(block);
			manager.inputString(equation.toBasicString());
			sectionInputMode = false;
			if(DebugOnlySetting.print_test_message >= 1) System.out.println(" 식 입력됨 : " + equation.toBasicString());
		}		
		else if(openMode)
		{
			if(block.equalsIgnoreCase("compare"))
			{
				// mn.compareManager.open();
			}
			else if(block.equalsIgnoreCase("set"))
			{
				// mn.settingFrame.open(setting);
			}
			openMode = false;
		}	
		else if(trackMode)
		{
			if(block.equalsIgnoreCase("all"))
			{
				manager.print("추적 내역 전체 출력\n");
				if(tracker.getLine() >= 1)
				{
					for(int i=0; i<tracker.getLine(); i++)
					{
						manager.print(" 추적 내역 " + tracker.getPoint(i) + "번째, " + tracker.getTrackableClassName(tracker.getPoint(i).object.getTrackableClassType()) + " 의 클래스 인스턴스에서 : " + tracker.getPoint(i).message + "\n");					
					}
				}
				else manager.print(" 추적 내역 없음 \n");
			}	
			else if(block.equalsIgnoreCase("on"))
			{
				DebugOnlySetting.collect_tracking++;
			}
			else if(block.equalsIgnoreCase("off"))
			{
				if(DebugOnlySetting.collect_tracking >= 1) DebugOnlySetting.collect_tracking--;
			}
			else
			{
				long index = 0;
				try
				{
					index = Integer.parseInt(block);
					manager.print(" 추적 내역 " + index + "번째, " + tracker.getTrackableClassName(tracker.getPoint(index).object.getTrackableClassType()) + " 의 클래스 인스턴스에서 : " + tracker.getPoint(index).message + "\n");
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					manager.print(" 해당 인덱스 번호가 없습니다.\n");
				}
				catch(NumberFormatException e)
				{
					throw new IsNotNumberException("오류 Error : 숫자로의 변환에 실패하였습니다.");
				}
			}
			trackMode = false;
		}
		else if(testMode)
		{
			 if(block.equalsIgnoreCase("on"))
			{
				DebugOnlySetting.print_test_message++;
			}
			else if(block.equalsIgnoreCase("off"))
			{
				if(DebugOnlySetting.print_test_message >= 1) DebugOnlySetting.print_test_message--;
			}
		}
		else if(block.equalsIgnoreCase("function") || block.equalsIgnoreCase("f"))
		{
			sectionInputMode = true;
		}
		else if(block.equalsIgnoreCase("input") || block.equalsIgnoreCase("="))
		{
			valueInputMode = true;
		}
		else if(block.equalsIgnoreCase("open"))
		{
			openMode = true;
		}
		else if(block.equalsIgnoreCase("exit"))
		{
			exit();
		}
		else if(block.equalsIgnoreCase("track") && DebugOnlySetting.collect_tracking >= 1)
		{
			trackMode = true;
		}
		else if(block.equalsIgnoreCase("testmode"))
		{
			testMode = true;
		}
		else if(block.equalsIgnoreCase("points"))
		{
			// if(mn != null)
			// {
			// 	Point[] points = mn.getGraphPoints();
			// 	for(int i=0; i<points.length; i++)
			// 	{
			// 		manager.print(i + ", x : "+ points[i].getX() + ", y : " + points[i].getY() + "\n");
			// 	}
			// }
		}
		else if(block.equalsIgnoreCase("help") || block.equalsIgnoreCase("?"))
		{
			manager.print(lang.getText(Language.TEXT_INPUT_CONSOLE_HELP) + "\n");
		}
		else if(block.equalsIgnoreCase("compare") && (! openMode))
		{
			// mn.compareManager.open();
			// mn.compareManager.insert(equation, modified_equation);
		}
		else
		{
			boolean workAdded = false;
			if(block.equalsIgnoreCase("differential") || block.equalsIgnoreCase("\'") || block.equalsIgnoreCase("d"))
			{
				works.add(new DifferentialWork());
				workAdded = true;
			}
			else if(block.equalsIgnoreCase("graph") || block.equalsIgnoreCase("g"))
			{
				works.add(new ResultToGraphWork());
				workAdded = true;
			}
			else if(block.equalsIgnoreCase("up") || block.equalsIgnoreCase("u"))
			{
				works.add(new DifferentToResultWork());
				workAdded = true;
			}				
			else if(valueInputMode && (! block.equals("")))
			{
				if(manager.getType() == OutputManager.OUTPUT_FRAME)
				{
					// ((JTextComponent) ((FrameOutput) manager).getField(FrameOutput.FIELD_VALUE_INPUT)).setText(block);
					// works.add(new InsertValueWork());
					workAdded = true;
				}
				valueInputMode = false;
			}
			if((! threadSwitch) && workAdded)
			{
				threadSwitch = true;
				try
				{
					thread.run();
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}		
	}
	public void exit()
	{
		if(setting.save_setting_when_exit)
		{
			try
			{
				File checker = new File(default_path);
				if(! checker.exists())
				{
					checker.mkdir();
				}
				Setting.writeSetting(default_path + "setting.vdps", setting, manager);
			}
			catch(Exception e)
			{
				
			}
		}
		if(consoleMode)
		{
			manager.print("\n");
		}
		System.exit(0);
	}
	public void setSetting(Setting newOne)
	{
		setting = newOne;
		if(manager.getType() == OutputManager.OUTPUT_FRAME)
		{
			// FrameOutput frame = (FrameOutput) manager;
			// frame.mainGraph.setSetting(newOne);
		}			
	}
	public void setSetting(Setting newOne, boolean user_defined)
	{
		newOne.save_setting_when_exit = true;
		setSetting(newOne);
	}
	private void readyMessage()
	{
		manager.print(lang.getText(Language.TEXT_WELCOME));
		manager.print(setting.getVersion() + "\n\n");
	}
	public synchronized void start()
	{
		threadSwitch = true;
		if(! thread.isAlive()) thread.start();
	}
	public synchronized void stop()
	{
		threadSwitch = false;
	}
	public synchronized void setManager(OutputManager manager)
	{
		this.manager = manager;
	}
	public synchronized void refreshText()
	{
		int type = manager.getType();
		if(type == OutputManager.OUTPUT_FRAME)
		{
			// FrameOutput mn = (FrameOutput) manager;
			// inputFieldString = mn.inputField.getText();
			// equation = translator.stringToSection(inputFieldString);
			// outputFieldString = new String(equation.toBasicString());
			// mn.setResultFieldText(outputFieldString);
		}
		else
		{
			while(true)
			{
				manager.print("\n" + lang.getText(Language.TEXT_CONSOLE_INPUT_FUNCTION));
				try
				{
					if(scanner == null) scanner = new Scanner(System.in);
					//BufferedReader bfrs = new BufferedReader(new InputStreamReader(System.in));
					//String eq = bfrs.readLine();
					//bfrs.close();					
					
					scanner.reset();
					String eq = scanner.nextLine();
											
					if(eq == null || eq.equals(""))
					{
						eq = scanner.nextLine();
					}
					
					equation = translator.stringToSection(eq);
					break;
				}
				catch(NumberFormatException e)
				{
					manager.print(lang.getText(Language.TEXT_ERROR_WRONG_VALUE));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					break;
				}
			}
		}
	}
	public void addInputWork()
	{
		works.add(new InsertValueWork());
		if(! threadSwitch) 
		{
			thread = new Thread(this);
			start();
		}
	}
	public Section getSection(int index)
	{
		switch(index)
		{
		case 0:
			return (Section) equation.clone();
		case 1:
			return (Section) modified_equation.clone();
		default:
			return (Section) equation.clone();
		}
	}
	public synchronized String getInputFieldText()
	{
		return inputFieldString;
	}
	public synchronized String getOutputFieldText()
	{
		return outputFieldString;
	}
	public synchronized void setInputFieldText(String text)
	{
		inputFieldString = new String(text);
	}
	public synchronized void setOutputFieldText(String text)
	{
		outputFieldString = new String(text);
	}
	public void open()
	{
		manager.open();
	}
	public void menuSelected(int index)
	{
		switch(index)
		{
		case ConsoleOutput.CONSOLE_INPUT_SECTION:
			works.add(new InputWork());
			break;
		case ConsoleOutput.CONSOLE_DIFFERENTIAL:
			works.add(new DifferentialWork());
			break;
		case ConsoleOutput.CONSOLE_INPUT_VALUE:
			works.add(new InsertValueWork());
			break;
		case ConsoleOutput.CONSOLE_EXIT:
			threadSwitch = false;
		}
		if(! threadSwitch) 
		{
			thread = new Thread(this);
			start();
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		// Object ob = e.getSource();
		if(manager.getType() == OutputManager.OUTPUT_FRAME)
		{
			// FrameOutput mn = (FrameOutput) manager;
			// if(ob == mn.getButton(FrameOutput.BUTTON_INPUT) || ob == mn.getField(FrameOutput.FIELD_INPUT))
			// {
			// 	works.add(new InputWork());
			// }
			// else if(ob == mn.getButton(FrameOutput.BUTTON_DIFFERENT) || ob == mn.getMenuItem(FrameOutput.MENU_ACT_DIFFERENTIAL))
			// {
			// 	works.add(new DifferentialWork());
			// }
			// else if(ob == mn.getButton(FrameOutput.BUTTON_INSERT_VALUE) || ob == mn.getField(FrameOutput.FIELD_TEXT_ACT))
			// {
			// 	works.add(new InsertValueWork());
			// }
			// else if(ob == mn.getButton(FrameOutput.BUTTON_DIFFERENT_GRAPH) || ob == mn.getMenuItem(FrameOutput.MENU_ACT_GRAPH_2))
			// {
			// 	works.add(new DifferentToGraphWork());
			// }
			// else if(ob == mn.getButton(FrameOutput.BUTTON_RESULT_GRAPH) || ob == mn.getMenuItem(FrameOutput.MENU_ACT_GRAPH_1))
			// {
			// 	works.add(new ResultToGraphWork());
			// }
			// else if(ob == mn.getButton(FrameOutput.BUTTON_DIFFERENT_UP) || ob == mn.getMenuItem(FrameOutput.MENU_ACT_UP))
			// {
			// 	works.add(new DifferentToResultWork());
			// }
			if(! threadSwitch) 
			{
				thread = new Thread(this);
				start();
			}
		}
	}
	public Tracker getTracker()
	{
		return tracker;
	}
	// private void onThread(GUIOutput mn)
	// {					
	// 	if(consoleMode)
	// 	{
	// 		int leftWork = works.getLeftWorkCount();				
	// 		int selectedMenu = 999;
	// 		if(threadSwitch == false)
	// 		{
	// 			return;
	// 		}
	// 		else if(leftWork == 0)
	// 		{
	// 			while (true) 
	// 			{
	// 				String eqs = "";						
	// 				if(equation == null) eqs = lang.getText(Language.TEXT_NOTEXIST);
	// 				else if(equation.getSectionType() == Section.MULTIPLE)
	// 				{
	// 					if(((MultipleSection) equation).contents == null)
	// 					{
	// 						eqs = lang.getText(Language.TEXT_NOTEXIST);
	// 					}
	// 					else eqs = equation.toBasicString();
	// 				}						
	// 				else eqs = equation.toBasicString();
	// 				manager.print("===========================================\n");
	// 				manager.print(lang.getText(Language.TEXT_INPUT) + lang.getText(Language.TEXT_EQUATION) + " : " + eqs + "\n");
	// 				for(int i=0; i<4; i++)
	// 				{
	// 					manager.print(String.valueOf(i) + ". " + lang.getText(Language.TEXT_CONSOLE_MENU_ + i) + "\n");
	// 				}
	// 				manager.print(lang.getText(Language.TEXT_CONSOLE_SELECT));
	// 				try
	// 				{
	// 					if(scanner == null) scanner = new Scanner(System.in);
	// 					scanner.reset();
	// 					String selectedString = scanner.next();							
	// 					if(selectedString == null || selectedString.equals(""))
	// 					{
	// 						selectedString = scanner.next();
	// 					}
	// 					selectedMenu = Integer.parseInt(selectedString);
	// 				}
	// 				catch(NumberFormatException e)
	// 				{
	// 					manager.print("\n");
	// 				}
	// 				catch(Exception e)
	// 				{
	// 					e.printStackTrace();
	// 					break;
	// 				}
	// 				if(selectedMenu >= 0 && selectedMenu <= 4)
	// 				{
	// 					menuSelected(selectedMenu);
	// 					break;
	// 				}
	// 				manager.print("===========================================\n");
	// 			}
	// 		}
	// 	}
	// 	
	// 	try
	// 	{
	// 		if(works.getSize() >= 1 && works.getLeftWorkCount() >= 1)
	// 		{
	// 			Work nowWork = works.take();
	// 			manager.print(lang.getText(Language.TEXT_INPUT) + " : " + (nowWork.getDescription()));
	// 			switch(nowWork.getType())
	// 			{
	// 			case Work.WORK_INPUT:
	// 				refreshText();						
	// 				break;
	// 			case Work.WORK_DIFFERENTIAL:
	// 				if(DebugOnlySetting.print_test_message >= 1) System.out.println("미분 전 : " + equation.toBasicString() + ", 타입 : " + equation.getSectionType());
	// 				modified_equation = (Section) equation.differential().trim();
	// 				if(manager.getType() == OutputManager.OUTPUT_FRAME)
	// 				{
	// 					((FrameOutput) mn).setDifferentFieldText(modified_equation.toBasicString());
	// 					if(DebugOnlySetting.print_test_message >= 1) System.out.println("미분 결과의 타입 : " + modified_equation.getSectionType());
	// 					if(DebugOnlySetting.print_test_message >= 1) System.out.println("미분 결과 : " + modified_equation.toBasicString());
	// 				}
	// 				else
	// 				{
	// 					equation = (Section) modified_equation.clone();
	// 				}
	// 				break;
	// 			case Work.WORK_UPDATE_GRAPH:
	// 				if(manager.getType() == OutputManager.OUTPUT_FRAME)
	// 				{
	// 					GraphPanel canvas = mn.getCanvas();
	// 					canvas.clean();
	// 					DoubleData newOne = new DoubleData(tracker);
	// 					for(int i=graph_domain_min; i<graph_domain_max; i++)
	// 					{
	// 						newOne.data = (double)i;
	// 						canvas.add(new Point(i, (int)(equation.calculate(newOne).data)));
	// 					}
	// 					canvas.repaint();
	// 				}
	// 				break;
	// 			case Work.WORK_INSERT_VALUE:
	// 				DoubleData newOne, insertOne;
	// 				insertOne = new DoubleData(tracker);						
	// 				try
	// 				{
	// 					if(manager.getType() == OutputManager.OUTPUT_FRAME) insertOne.data = Double.parseDouble(mn.getValueFieldText());
	// 					else
	// 					{
	// 						manager.print("\n" + lang.getText(Language.TEXT_CONSOLE_INPUT_PLEASE));
	// 						String values;
	// 						scanner.reset();
	// 						values = scanner.next();
	// 						if(values == null || values.equals("")) values = scanner.next();
	// 						insertOne.data = Double.parseDouble(values);
	// 						manager.print("\n");
	// 					}
	// 				} 
	// 				catch (NumberFormatException e)
	// 				{
	// 					throw new IsNotNumberException("오류 Error : 대입할 값을 제대로 입력하십시오. Please input numbers.");
	// 				}
	// 				catch (NullPointerException e)
	// 				{
	// 					throw new IsNotFunctionException("오류 Error : 식을 입력한 후 사용하십시오. Use this button after you input the function.");
	// 				}
	// 				
	// 				try
	// 				{
	// 					newOne = equation.calculate(insertOne);
	// 				}
	// 				catch (NullPointerException e)
	// 				{
	// 					throw new IsNotFunctionException("오류 Error : 식을 입력한 후 사용하십시오. Use this button after you input the function.");
	// 				}
	// 				if(manager.getType() == OutputManager.OUTPUT_FRAME)
	// 				{							
	// 					mn.setValueResultFieldText(newOne.toBasicString());
	// 				}
	// 				manager.print(lang.getText(Language.TEXT_RESULT) + " : " + newOne.toBasicString() +"\n");
	// 				break;
	// 			case Work.WORK_DIFFERENT_TO_RESULT:
	// 				equation = ((Section) modified_equation.clone()).trim();
	// 				if(manager.getType() == OutputManager.OUTPUT_FRAME)
	// 				{
	// 					mn.setResultFieldText(equation.toBasicString());
	// 				}
	// 				break;
	// 			case Work.WORK_RESULT_TO_GRAPH:
	// 				if(manager.getType() == OutputManager.OUTPUT_FRAME)
	// 				{
	// 					GraphPanel canvas = mn.getCanvas();
	// 					canvas.clean();
	// 					DoubleData newOne2 = new DoubleData(tracker);
	// 					for(int i=graph_domain_min; i<graph_domain_max; i++)
	// 					{
	// 						newOne2.data = (double)i;
	// 						canvas.add(new Point(i, (int)(equation.calculate(newOne2).data)));
	// 					}
	// 					canvas.repaint();
	// 				}
	// 				break;
	// 			case Work.WORK_DIFFERENT_TO_GRAPH:
	// 				if(manager.getType() == OutputManager.OUTPUT_FRAME)
	// 				{
	// 					GraphPanel canvas = mn.getCanvas();
	// 					canvas.clean();
	// 					DoubleData newOne2 = new DoubleData(tracker);
	// 					for(int i=-100; i<100; i++)
	// 					{
	// 						newOne2.data = (double)i;
	// 						canvas.add(new Point(i, (int)(modified_equation.calculate(newOne2).data)));
	// 					}
	// 					canvas.repaint();
	// 				}
	// 				break;
	// 			}		
	// 			manager.print("\n");
	// 			nowWork.done();
	// 		}
	// 		else threadSwitch = false;
	// 		manager.setProgrssState(works.getLeftWorkCount(), works.getSize());
	// 		if(! threadSwitch) return;
	// 	} 
	// 	catch (Exception e)
	// 	{
	// 		manager.print(e.getMessage() + "\n");
	// 		e.printStackTrace();
	// 		threadSwitch = false;
	// 	}
	// 	
	// 	
	// }
	@Override
	public void run()
	{
		// GUIOutput mn = null;
		// if(manager.isGUI())
		// {
		// 	mn = (GUIOutput) manager;
		// 	mn.pause();
		// }
		
		
		// while(threadSwitch)
		// {
		// 	onThread(mn);
		// 	if(! threadSwitch) break;
		// }	
		// 
		// if(manager.isGUI())
		// {
		// 	mn.resume();
		// }
		works.clear();
		if(consoleMode && (! threadSwitch))
		{
			exit();
		}
	}
}
