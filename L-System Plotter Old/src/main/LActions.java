package main;

public enum LActions {
	FORWARD ("Forward"),
	NOTHING ("Nothing"),
	ROTATECW ("Rotate CW"),
	ROTATECC ( "Rotate CC"),
	PUSHSTATE ("Push State"),
	POPSTATE ("Pop State");
	
	public final String dispString;
	LActions(String name) {
		this.dispString = name;
	}
	
	
	public String toString() {
		return dispString;
	}
	
}
