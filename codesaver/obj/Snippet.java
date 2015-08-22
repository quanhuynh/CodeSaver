package obj;

public class Snippet {
	private String name;
	private String category;
	private String code;
	private String comment;
	private String syntax;
	
	public Snippet(String name, String category, String code, String comment, String syntax) {
		this.name = name;
		this.category = category;
		this.code = code;
		this.comment = comment;
		this.syntax = syntax;
	}
	
	public String name() {
		return name;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public String category() {
		return category;
	}
	
	public void setCategory(String newCat) {
		category = newCat;
	}
	
	public String code() {
		return code;
	}
	
	public void setCode(String newCode) {
		code = newCode;
	}
	
	public String comment() {
		return comment;
	}
	
	public void setComment(String newComm) {
		comment = newComm;
	}
	
	public String syntax() {
		return syntax;
	}
	
	public void setSyntax(String newSyn) {
		syntax = newSyn;
	}
	
	public int hashCode() {
		return name.hashCode();
	}
	
	public boolean equals(Snippet s) {
		return name.equals(s.name());
	}
}
