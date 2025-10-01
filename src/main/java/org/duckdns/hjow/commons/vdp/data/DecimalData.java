package org.duckdns.hjow.commons.vdp.data;
import java.math.BigDecimal;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.differential.*;
import org.duckdns.hjow.commons.vdp.exceptions.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
import org.duckdns.hjow.commons.vdp.work.*;
import org.duckdns.hjow.commons.vdp.elements.*;
public class DecimalData extends NumberData 
{
	private static final long serialVersionUID = -3147040026320675891L;
	public BigDecimal data;
	public DecimalData(Tracker tracker)
	{
		super(tracker);
		data = new BigDecimal("0.0");
	}
	@Override
	public byte getDataType() 
	{
		return Data.DECIMAL;
	}
	public static DecimalData getNewDecimalData(double number)
	{
		DecimalData newone = new DecimalData(tracker);
		newone.data = new BigDecimal(number);
		return newone;
	}
	public static DecimalData getNewDecimalData(long number)
	{
		DecimalData newone = new DecimalData(tracker);
		newone.data = new BigDecimal(number);
		return newone;
	}
	public static DecimalData getNewDecimalData(String number)
	{
		DecimalData newone = new DecimalData(tracker);
		newone.data = new BigDecimal(number);
		return newone;
	}
	public static DecimalData getNewDecimalData(BigDecimal number)
	{
		DecimalData newone = new DecimalData(tracker);
		newone.data = new BigDecimal(String.valueOf(number));
		return newone;
	}
	@Override
	public String toBasicString() 
	{
		return String.valueOf(data);
	}
	@Override
	public Section differential() 
	{
		ConstantSection newone = new ConstantSection(tracker);
		newone.coefficient = new DoubleData(tracker);
		newone.coefficient.data = 0.0;
		DoubleData newInside = new DoubleData(tracker);
		newInside.data = 0.0;
		newone.inside = newInside;
		newone.exponent = new DoubleData(tracker);
		newone.exponent.data = 1.0;
		return newone;
	}
	@Override
	public Data clone()
	{
		DecimalData datas = new DecimalData(tracker);
		datas.data = this.data; 
		return datas;
	}
	@Override
	public String getName()
	{
		return String.valueOf(data);
	}
	@Override
	public void plus(NumberData data)
	{		
		BigDecimal newOne = new BigDecimal(0.0);
		newOne.add(this.data);
		newOne.add(((DecimalData)data).data);
		this.data = newOne;
	}
	@Override
	public void minus(NumberData data)
	{
		BigDecimal newOne = new BigDecimal(0.0);
		newOne.add(this.data);
		newOne.subtract(((DecimalData)data).data);
		this.data = newOne;
	}
	@Override
	public void multiply(NumberData data)
	{
		BigDecimal newOne = new BigDecimal(0.0);
		newOne.add(this.data);
		newOne.multiply(((DecimalData)data).data);
		this.data = newOne;
	}
	@Override
	public void divide(NumberData data)
	{
		BigDecimal newOne = new BigDecimal(0.0);
		newOne.add(this.data);
		newOne.divide(((DecimalData)data).data);
		this.data = newOne;
	}
	@Override
	public void abs()
	{
		this.data = this.data.abs();		
	}
	@Override
	public byte compareTo(NumberData data)
	{
		DecimalData newOne = new DecimalData(tracker);
		newOne.data = new BigDecimal(0.0);
		newOne.data.add(this.data);
		return (byte) newOne.data.compareTo(((DecimalData)data).data);
	}
	@Override
	public double getDouble()
	{
		return data.doubleValue();
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_DECIMAL_DATA;
	}
}
