package edu.utdallas.taskExecutorImpl;

import java.util.ArrayList;
import java.util.List;

import edu.utdallas.blockingFIFO.BlockFIFO;
import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;

public class TaskExecutorImpl implements TaskExecutor
{

	BlockFIFO blockFifo = new BlockFIFO(100);
	List<TaskRunner> taskRunner = new ArrayList<>();

	public TaskExecutorImpl(int numberOfThreads)
	{
		for(int i=0;i<numberOfThreads;i++)
		{
			taskRunner.add(new TaskRunner(blockFifo,"TaskThread"+i));
		}
		for(TaskRunner tr : taskRunner)
		{
			tr.start();
		}
	}
	@Override
	public void addTask(Task task)
	{
		try{
			blockFifo.append(task);
			
		}catch(Exception E)
		{
			System.out.println(E.getMessage());
		}
	}

}
