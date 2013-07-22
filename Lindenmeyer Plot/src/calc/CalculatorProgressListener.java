package calc;

public interface CalculatorProgressListener {
	public void calculatorFinish(String pattern, int segLength);
	public void calculatorIterDone(double progress);
	public void calculatorCharDone(double progress);
	public void calculatorError(Exception e);
}
