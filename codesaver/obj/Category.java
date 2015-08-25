package obj;

import org.jdesktop.swingx.JXTaskPane;

public class Category {
	private String name;
	private JXTaskPane taskPane;
	
	public Category(String name) {
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
	
	public JXTaskPane taskPane() {
		return taskPane;
	}
	
	public void setTaskPane(JXTaskPane tp) {
		taskPane = tp;
	}
	
	public boolean equals(Category c) {
		return name.equals(c.name());
	}
	
	public int hashCode() {
		return name.hashCode();
	}
}
