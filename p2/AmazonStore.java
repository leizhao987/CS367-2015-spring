import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;


public class AmazonStore {

	//Store record of users and products
	private static ListADT<Product> products = new DLinkedList<Product>();
	private static ListADT<User> users = new DLinkedList<User>();
	private static User currentUser = null;//current user logged in

	//scanner for console input
	public static final Scanner stdin= new Scanner(System.in);


	//main method
	public static void main(String args[]) {

		//Populate the two lists using the input files: Products.txt User1.txt User2.txt ... UserN.txt
		if (args.length < 2) {
			System.out.println("Usage: java AmazonStore [PRODUCT_FILE] [USER1_FILE] [USER2_FILE] ...");
			System.exit(0);
		}

		//load store products
		loadProducts(args[0]);

		//load users one file at a time
		for(int i=1; i<args.length; i++)
			loadUser(args[i]);

		//User Input for login
		boolean done = false;
		while (!done) 
		{
			System.out.print("Enter username : ");
			String username = stdin.nextLine();
			System.out.print("Enter password : ");
			String passwd = stdin.nextLine();

			if(login(username, passwd)!=null)
			{
				//generate random items in stock based on this user's wish list
				ListADT<Product> inStock=currentUser.generateStock();
				//show user menu
				userMenu(inStock);
			}
			else
				System.out.println("Incorrect username or password");

			System.out.println("Enter 'exit' to exit program or anything else to go back to login");
			if(stdin.nextLine().equals("exit"))
				done = true;
		}

	}

	/**
	 * Tries to login for the given credentials. Updates the currentUser if successful login
	 * 
	 * @param username name of user
	 * @param passwd password of user
	 * @returns the currentUser 
	 */
	public static User login(String username, String passwd){
		if (username == null || passwd == null) 
			throw new IllegalArgumentException();
		
		int userNum = users.size();
		for (int id = 0; id < userNum; id++) {
			if (users.get(id).checkLogin(username, passwd)) {
				currentUser = users.get(id);
				break;
			}
		}
		return currentUser;
	}

	/**
	 * Reads the specified file to create and load products into the store.
	 * Every line in the file has the format: <NAME>#<CATEGORY>#<PRICE>#<RATING>
	 * Create new products based on the attributes specified in each line and insert them into the products list
	 * Order of products list should be the same as the products in the file
	 * For any problem in reading the file print: 'Error: Cannot access file'
	 * 
	 * @param fileName name of the file to read
	 */
	public static void loadProducts(String fileName){
		if (fileName == null) 
			throw new IllegalArgumentException();
		
		try {
			File productFile = new File(fileName);
			Scanner pf = new Scanner(productFile);
        
			while (pf.hasNext()) {
				String product = pf.nextLine();
				String[] attribs = product.split("#");
				products.add(new Product(attribs[0],attribs[1], // check for irregular product
						Integer.parseInt(attribs[2]),
						Float.parseFloat(attribs[3])));
			}
			pf.close();
		} catch (FileNotFoundException e) { // consider other exceptions
			System.out.println("Error: Cannot access file");
		}
	}

	/**
	 * Reads the specified file to create and load a user into the store.
	 * The first line in the file has the format:<NAME>#<PASSWORD>#<CREDIT>
	 * Every other line after that is a name of a product in the user's wishlist, format:<NAME>
	 * For any problem in reading the file print: 'Error: Cannot access file'
	 * 
	 * @param fileName name of the file to read
	 */
	public static void loadUser(String fileName){
		if (fileName == null) 
			throw new IllegalArgumentException();
		
		try {
			File userFile = new File(fileName);
			Scanner uf = new Scanner(userFile);
			
			String user = uf.nextLine();
			String[] userInfo = user.split("#");
			
			User newUser = new User(userInfo[0],userInfo[1], 
					Integer.parseInt(userInfo[2]));
			users.add(newUser);
						
			while (uf.hasNext()) {
				String productName = uf.nextLine();
				
				int i, numproducts = products.size();
				for (i = 0; i < numproducts; i++) {
					Product product = products.get(i);
					if (product.getName().equals(productName)) { // get the "first" product with the same "productName" ??
						newUser.addToWishList(product);
					}
				}
			}
			uf.close();
		} catch (FileNotFoundException e) { // consider other exceptions
			System.out.println("Error: Cannot access file");
		}
	}

	/**
	 * See sample outputs
     * Prints the entire store inventory formatted by category
     * The input text file for products is already grouped by category, use the same order as given in the text file 
     * format:
     * <CATEGORY1>
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * ...
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * 
     * <CATEGORY2>
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * ...
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     */
	public static void printByCategory(){
		int i, numProducts = products.size();
		String category = null;
		for (i = 0; i < numProducts; i++) {
			if (!products.get(i).getCategory().equals(category)) {
				category = products.get(i).getCategory();
				System.out.println("\n" + category + ":");
			}
			System.out.println(products.get(i).toString());
		}
		System.out.println();
	}

	
	/**
	 * Interacts with the user by processing commands
	 * 
	 * @param inStock list of products that are in stock
	 */
	public static void userMenu(ListADT<Product> inStock){
		if (inStock == null) 
			throw new IllegalArgumentException();

		boolean done = false;
		while (!done) 
		{
			boolean notFound = true;
			int i, numProducts = products.size(),numinStock = inStock.size();
			
			System.out.print("Enter option : ");
			String input = stdin.nextLine();

			//only do something if the user enters at least one character
			if (input.length() > 0) 
			{
				String[] commands = input.split(":");//split on colon, because names have spaces in them
				if(commands[0].length()>1)
				{
					System.out.println("Invalid Command");
					continue;
				}
				switch(commands[0].charAt(0)){
				case 'v':
					switch(commands[1]) {
					case "all":
						printByCategory();
						break;
						
					case "wishlist":
						PrintStream ps = new PrintStream(System.out);
						currentUser.printWishList(ps);
						// ps.flush();
						break;
						
					case "instock":
						for (i = 0; i < numinStock; i++) {
							System.out.println(inStock.get(i).toString());
						}
						break;
						
					default:  //a command with no argument
						System.out.println("Invalid Command");
						break;
					}
					break;

				case 's':
					if (input.length() > 2 && commands.length == 2){
						for (i = 0; i < products.size(); i++) {
							String[] productName = products.get(i).getName().split(" ");
							// print a product if it contains the word exactly matches the String
							for (int j = 0; j < productName.length; j++){
								if (productName[j].equals(commands[1])){
									System.out.println(products.get(i).toString());
									break;
								}
							}
						}
					} else {
						System.out.println("Invalid Command");
					}
					break;

				case 'a':
					for (i = 0; i < numProducts; i++) {
						if (products.get(i).getName().equals(commands[1])) {
							currentUser.addToWishList(products.get(i));
							System.out.println("Added to wishlist");
							notFound = false;
						}
					}
					
					if (notFound) {
						System.out.println("Product not found");
					}
					break;

				case 'r':
					if (input.length() > 2 && commands.length == 2){
						Product removed = currentUser.removeFromWishList(commands[1]);
						if (removed == null) {
							System.out.println("Product not found");
						} else {
							System.out.println("Removed from wishlist");
						}
					} else {
						System.out.println("Invalid Command");
					}
					break;

				case 'b':
					for (i = 0; i < numinStock; i++) {
						try {
							if (currentUser.buy(inStock.get(i).getName()))
								System.out.println("Bought " + inStock.get(i).getName());
						} catch (InsufficientCreditException e) {
							e.printMessage();
						}
					}
					break;

				case 'c':
					System.out.println("$" + currentUser.getCredit());
					break;

				case 'l':
					done = true;
					currentUser = null;
					System.out.println("Logged Out");
					break;

				default:  //a command with no argument
					System.out.println("Invalid Command");
					break;
				}
			}
		}
	}

}
