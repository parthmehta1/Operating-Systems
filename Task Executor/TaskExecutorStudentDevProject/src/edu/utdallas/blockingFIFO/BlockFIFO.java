package edu.utdallas.blockingFIFO;

import edu.utdallas.taskExecutor.Task;

public class BlockFIFO 
{
	int count;
	int size;
	int nextIn, nextOut;
	Object ob1 = new Object();
	Object ob2 = new Object();
	Task[] blockingFifo;
	
	public BlockFIFO(int size)
	{
		if(size<0)
		{
			throw new IllegalArgumentException();
		}
		this.size = size;
		this.blockingFifo = new Task[size];
	}
	
	public void append(Task x) throws Exception
	{
		while(true)
		{
			synchronized (ob1)
			{
				if(count == size)
				{
					ob1.wait();
				}
			}
			synchronized(ob2)
			{
				if(count!=size)
				{
					blockingFifo[nextIn] = x;
					nextIn = (nextIn+1)%100;
					count++;
					ob2.notifyAll();
					break;
				}
			}
		}
	}
	public Task Take() throws Exception
	{
		while(true)
		{
			synchronized(ob2)
			{
				if(count ==0)
				{
					ob2.wait();
				}
			}
			synchronized(ob1)
			{
				if(count!=0)
				{
					Task task = blockingFifo[nextOut];
					nextOut = (nextOut+1)%100;
					count--;
					ob1.notifyAll();
					return task;
				}
			}
		}
	}
}
