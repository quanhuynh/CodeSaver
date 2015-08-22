package obj;

import java.util.HashSet;
import java.util.Set;

import org.jdesktop.swingx.JXTaskPane;

public class Category {
	private String name;
	private JXTaskPane taskPane;
	private Set<Snippet> snippets;
	
	public Category(String name) {
		this.name = name;
		snippets = new HashSet<Snippet>();
	}
	
	public String name() {
		return name;
	}
	
	public Set<Snippet> snippets() {
		return snippets;
	}
	
	public JXTaskPane taskPane() {
		return taskPane;
	}
	
	public void setTaskPane(JXTaskPane tp) {
		taskPane = tp;
	}
	
	public void addSnippet(Snippet s) {
		snippets.add(s);
	}
	
	public void removeSnippet(Snippet s) {
		snippets.remove(s);
	}
	
	public boolean equals(Category c) {
		return name.equals(c.name());
	}
	
	public int hashCode() {
		return name.hashCode();
	}
}
