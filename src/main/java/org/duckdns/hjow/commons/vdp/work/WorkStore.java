package org.duckdns.hjow.commons.vdp.work;

import java.util.Vector;

public class WorkStore
{
	private Vector<Work> works;
	private int pointer = 0;
	public WorkStore()
	{
		works = new Vector<Work>();
	}
	
	public synchronized void add(Work work)
	{
		works.add(work);
	}
	public synchronized int getSize()
	{
		return works.size();
	}
	public synchronized int getLeftWorkCount()
	{
		int count = 0;
		for(int i=0; i<works.size(); i++)
		{
			if(! works.get(i).isDone()) count++;
		}
		return count;
	}
	public synchronized Work takeOut()
	{
		findPointer();
		if(works.size() >= 1)
		{
			Work gets = works.get(pointer);
			works.remove(pointer);
			return gets;
		}
		else return null;
	}
	public synchronized Work take()
	{
		findPointer();
		if(works.size() >= 1)
		{
			Work gets = works.get(pointer);
			return gets;
		}
		else return null;
	}
	public synchronized Work take(int index)
	{
		findPointer();
		if(works.size() >= 1)
		{
			Work gets = works.get(index);
			return gets;
		}
		else return null;
	}
	public synchronized void clear()
	{
		works.clear();
	}
	public synchronized void setPointer(int loc)
	{
		pointer = loc;
	}
	public synchronized void findPointer()
	{
		if(works.size() >= 1)
		{
			for(int i=0; i<works.size(); i++)
			{
				if(! works.get(i).isDone())
				{
					pointer = i;
					break;
				}
			}
			pointer = works.size() - 1;
		}
		else pointer = 0;
		
	}
}
