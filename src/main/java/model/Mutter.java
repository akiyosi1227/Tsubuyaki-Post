package model;
import java.io.Serializable;
import java.sql.Timestamp;

public class Mutter implements Serializable {
	private int id;
	private String userName;
	private String text;
	private Timestamp createdAt;
	
	public Mutter() {}
	public Mutter(String userName, String text) {
		this.userName = userName;
		this.text = text;
	}
	public Mutter(int id, String userName, String text) {
		this.id = id;
		this.userName = userName;
		this.text = text;
	}
	public Mutter(int id, String userName, String text, Timestamp createdAt) {
		this.id = id;
		this.userName = userName;
		this.text = text;
		this.createdAt = createdAt;
	}
	
	public int getID() { return id; }
	public String getUserName() { return userName; }
	public String getText() { return text; }
	public Timestamp getCreatedAt() { return createdAt; }
}
