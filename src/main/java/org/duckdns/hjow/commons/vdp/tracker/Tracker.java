package org.duckdns.hjow.commons.vdp.tracker;
import java.util.Vector;

public class Tracker implements Trackable
{
	long lines = 0;
	Vector<TrackablePoint> points;
	
	public static final int TRACKABLE_TRACKER = 0;
	public static final int TRACKABLE_KERNAL = 1;
	public static final int TRACKABLE_OUTPUT_MANAGER = 101;
	public static final int TRACKABLE_CONSOLE_OUTPUT = 102;
	public static final int TRACKABLE_FRAME_OUTPUT = 103;
	public static final int TRACKABLE_GRAPH_PANEL = 104;
	public static final int TRACKABLE_MENU_LISTENER = 105;
	public static final int TRACKABLE_SETTING_FRAME = 106;
	public static final int TRACKABLE_TRANSLATOR = 201;
	public static final int TRACKABLE_SETTING = 301;
	public static final int TRACKABLE_ELEMENTS = 1000;
	public static final int TRACKABLE_SECTION = 1001;
	public static final int TRACKABLE_NULL_SECTION = 1002;
	public static final int TRACKABLE_OPERATION_SECTION = 1003;
	public static final int TRACKABLE_CONSTANT_SECTION = 1004;
	public static final int TRACKABLE_BASIC_SECTION = 1005;
	public static final int TRACKABLE_SINE_SECTION = 1006;
	public static final int TRACKABLE_COSINE_SECTION = 1007;
	public static final int TRACKABLE_TANGENT_SECTION = 1008;
	public static final int TRACKABLE_LN_SECTION = 1009;
	public static final int TRACKABLE_LOG_SECTION = 1010;
	public static final int TRACKABLE_EXP_SECTION = 1011;
	public static final int TRACKABLE_MULTIPLE_SECTION = 1013;
	public static final int TRACKABLE_EQUATION = 1020;
	public static final int TRACKABLE_DATA = 1100;
	public static final int TRACKABLE_NUMBER_DATA = 1101;
	public static final int TRACKABLE_DOUBLE_DATA = 1102;
	public static final int TRACKABLE_DECIMAL_DATA = 1103;
	public static final int TRACKABLE_SECTION_DATA = 1104;
	public static final int TRACKABLE_VARIABLE = 1105;
	public static final int TRACKABLE_COMPARE_MANAGER = 2000;
	public static final int TRACKABLE_DIFFERENTIAL_BLOCK = 2001;
	public static final int TRACKABLE_LANGUAGE_CREATOR = 3000;
	public static final int TRACKABLE_INDEX = 10000;
	
	public Tracker()
	{
		points = new Vector<TrackablePoint>();
	}
	public void addCount()
	{
		lines += 1;	
	}
	public long getLine()
	{
		return lines;
	}
	public TrackablePoint getPoint(long index)
	{
		return points.get((int) index);
	}
	public void addPoint(Trackable trackable)
	{
		TrackablePoint newOne = new TrackablePoint(this.lines + 1, trackable);
		this.points.add(newOne);
	}
	public void addPoint(Trackable trackable, boolean normal)
	{
		TrackablePoint newOne = new TrackablePoint(this.lines + 1, trackable, normal);
		this.points.add(newOne);
	}
	public void addPoint(Trackable trackable, boolean normal, String message)
	{
		TrackablePoint newOne = new TrackablePoint(this.lines + 1, trackable, normal, message);
		this.points.add(newOne);
	}
	public String getTrackableClassName(int index)
	{
		switch(index)
		{
		case TRACKABLE_BASIC_SECTION:
			return "BasicSection";
		case TRACKABLE_COMPARE_MANAGER:
			return "CompareManager";
		case TRACKABLE_CONSOLE_OUTPUT:
			return "ConsoleOutput";
		case TRACKABLE_CONSTANT_SECTION:
			return "ConstantSection";
		case TRACKABLE_COSINE_SECTION:
			return "CosineSection";
		case TRACKABLE_DATA:
			return "Data";
		case TRACKABLE_DECIMAL_DATA:
			return "DecimalData";
		case TRACKABLE_DIFFERENTIAL_BLOCK:
			return "DifferentialBlock";
		case TRACKABLE_DOUBLE_DATA:
			return "DoubleData";
		case TRACKABLE_ELEMENTS:
			return "Elements";
		case TRACKABLE_EQUATION:
			return "Equation";
		case TRACKABLE_FRAME_OUTPUT:
			return "FrameOutput";
		case TRACKABLE_GRAPH_PANEL:
			return "GraphPanel";
		case TRACKABLE_INDEX:
			return "Index";
		case TRACKABLE_KERNAL:
			return "Kernal";
		case TRACKABLE_LANGUAGE_CREATOR:
			return "LanguageCreator";
		case TRACKABLE_LN_SECTION:
			return "LnSection";
		case TRACKABLE_LOG_SECTION:
			return "LogSection";
		case TRACKABLE_MENU_LISTENER:
			return "MenuListener";
		case TRACKABLE_MULTIPLE_SECTION:
			return "MultipleSection";
		case TRACKABLE_NULL_SECTION:
			return "NullSection";
		case TRACKABLE_NUMBER_DATA:
			return "NumberData";
		case TRACKABLE_OPERATION_SECTION:
			return "OperationSection";
		case TRACKABLE_OUTPUT_MANAGER:
			return "OutputManager";
		case TRACKABLE_SECTION:
			return "Section";
		case TRACKABLE_SECTION_DATA:
			return "SectionData";
		case TRACKABLE_SETTING:
			return "Setting";
		case TRACKABLE_SETTING_FRAME:
			return "SettingFrame";
		case TRACKABLE_SINE_SECTION:
			return "SineSection";
		case TRACKABLE_TANGENT_SECTION:
			return "TangentSection";
		case TRACKABLE_TRACKER:
			return "Tracker";
		case TRACKABLE_TRANSLATOR:
			return "Translator";
		case TRACKABLE_VARIABLE:
			return "Variable";
		default:
			return "";
		}
	}
	@Override
	public void addThis()
	{
		TrackablePoint newOne = new TrackablePoint(this.lines + 1, this);
		this.points.add(newOne);
	}
	@Override
	public void addThis(boolean normal)
	{
		TrackablePoint newOne = new TrackablePoint(this.lines + 1, this, normal);
		this.points.add(newOne);
	}
	@Override
	public void addThis(boolean normal, String message)
	{
		TrackablePoint newOne = new TrackablePoint(this.lines + 1, this, normal, message);
		this.points.add(newOne);
		
	}	
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_TRACKER;
	}
	
}
