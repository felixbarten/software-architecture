package src.main.java;

public class Task {
	final String taskName;
	final int priority;
	
	public Task(String task, int prio ) {
		this.taskName= task;
		this.priority = prio;
	}
	public Task() {
		this.taskName = "";
		this.priority=1;
	}
	public int getPriority() {
		return this.priority;
	}
	public String getName() {
		return this.taskName;
	}
	
	@Override
	public String toString() {
		return "Task \nName: " + this.getName() + " \nPriority: " + this.getPriority();
	}
}
