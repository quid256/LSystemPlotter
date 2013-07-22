package calc;

@SuppressWarnings("serial")
public class UnknownCharacterException extends Exception{
	public String character;
	public UnknownCharacterException(String badchar) {
		super("Unknown character found: " + badchar);
		this.character = badchar;
	}
}
