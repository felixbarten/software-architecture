package src.main.java.impl;

import java.util.List;
import src.main.java.Task;
import static org.junit.Assert.assertEquals;


public class EventHandlerImpl implements EventHandler{
	private int property;
	
	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}

	public List<Task> getProperties() {
		return properties;
	}

	public void setProperties(List<Task> properties) {
		this.properties = properties;
	}

	private List<Task> properties;
	
	@Override
	public void fireEvent() {
		
		assertEquals(property, properties.size());

	}

}
