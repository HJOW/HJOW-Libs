package org.duckdns.hjow.commons.vdp.elements;

import java.io.Serializable;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
public class Equation extends Elements implements Serializable
{
	private static final long serialVersionUID = -4292519123724702392L;
	public Section[] contents;
	public Variable variable;
	public String name = "";
	public Equation(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public String toBasicString()
	{
		String sum = "(";
		String temp2 = "";
		for(int i=0; i<contents.length; i++)
		{
			temp2 = contents[i].toBasicString();
			sum = sum + " " + temp2;			
		}
		sum = sum + " )";
		return sum;
	}
	@Override
	public String getName()
	{
		return name;
	}
	@Override
	public Elements clone()
	{
		Equation newOne = new Equation(tracker);
		newOne.contents = new Section[this.contents.length];
		for(int i=0; i<newOne.contents.length; i++)
		{
			newOne.contents[i] = (Section) this.contents[i].clone();
		}
		newOne.variable = (Variable) this.variable.clone();
		newOne.name = this.name;
		return newOne;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_EQUATION;
	}
}
