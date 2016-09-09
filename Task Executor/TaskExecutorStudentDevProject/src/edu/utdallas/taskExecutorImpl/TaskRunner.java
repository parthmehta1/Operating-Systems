package edu.utdallas.taskExecutorImpl;
import edu.utdallas.blockingFIFO.*;
import edu.utdallas.taskExecutor.Task;

public class TaskRunner extends Thread
{
	BlockFIFO blockFifo;
	String threadName;
	public TaskRunner(BlockFIFO blockFifo, String threadName)
	{
		this.blockFifo = blockFifo;
		this.threadName = threadName;
	}

	@Override 
	public void run()
	{
		while(true)
		{
			try{
			Thread.currentThread().setName(threadName);
			Task newTask = blockFifo.Take();
			newTask.execute();
			}
			catch(Throwable th)
			{
				System.out.println(th.getMessage());

			}
		}
	}

}
