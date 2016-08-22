
public class InsufficientCreditException extends RuntimeException{
	
	private String productName;
	
	public InsufficientCreditException(String productName) {
		this.productName = productName;
	}
	
	public void printMessage() {
		System.out.println("Insufficient funds for " + productName);
	}
}