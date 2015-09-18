package src.main.java;

public class Task {
	final String taskName;
	final int priority;
	public enum State  { 
			IDLE, STARTED, STOPPED, FINISHED, PAUSED, INITIALIZED 
	}
	State state;
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Task(String task, int prio, State state) {
		this.taskName= task;
		this.priority = prio;
		this.state = state;
	}
	
	public Task(String task, int prio ) {
		this.taskName= task;
		this.priority = prio;
		this.state = State.INITIALIZED;
	}
	public Task() {
		this.taskName = "";
		this.priority=1;
		this.state = State.INITIALIZED;
	}
	public int getPriority() {
		return this.priority;
	}
	public String getName() {
		return this.taskName;
	}
	
	@Override
	public String toString() {
		return "Task \nName: " + this.getName() + " \nPriority: " + this.getPriority() + "\nState: " + this.getState();
	}
}
