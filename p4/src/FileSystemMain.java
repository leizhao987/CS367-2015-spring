import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class FileSystemMain {

	// store the file system
	private static SimpleFileSystem fSys;
	
	// Scanner object on input stream
	private static final Scanner scnr = new Scanner(System.in);

	/**
	 * 
	 * @param args
	 */
	public static void loadFileSystem(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException();
		}
		
		User adminUser = new User("admin");
		ArrayList<User> users = new ArrayList<User>();
		users.add(adminUser);
		
		try {
			File inFile = new File(fileName);
			Scanner f = new Scanner(inFile);
			
			SimpleFolder root = new SimpleFolder(f.nextLine().trim().toLowerCase(),"",null,adminUser);
			
			String[] usrs = f.nextLine().split(",");
			for (int i = 0; i < usrs.length; i++) {
				users.add(new User(usrs[i].trim().toLowerCase()));
			}
			
			fSys = new SimpleFileSystem(root, users);
			
			while (f.hasNext()) {
				String newln = f.nextLine();
				if (newln.lastIndexOf("/") < 0) continue;
				newln = "/" + newln;				
				fSys.moveLoc(newln.substring(0, newln.lastIndexOf("/")));
				String fileFolder = newln.substring(newln.lastIndexOf("/")+1).trim();
				
				if (fileFolder.indexOf(".") > 0) {					
					String[] sn = fileFolder.split(" ", 2);				
					fSys.addFile(sn[0].toLowerCase(), (sn.length == 2) ? sn[1] : null);
					for (User u : users) {
						if (!u.getName().equals("admin"))
							fSys.addUser(sn[0].split("\\.")[0], u.getName(), 'r');
					}
				}
				else {
					fSys.mkdir(fileFolder.toLowerCase());
					for (User u : users) {
						if (!u.getName().equals("admin"))
							fSys.addUser(fileFolder.toLowerCase(), u.getName(), 'r');
					}
				}
				fSys.reset();
			}
			
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: Cannot load file system");
		}
		
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	private static boolean validName(String objName) {
		String name = objName.toLowerCase();
		if (!Character.isLetter(name.charAt(0)))
			return false;
		for (int i = 1; i < name.length(); i++) {
			if (!Character.isLetter(name.charAt(i)) && !Character.isDigit(name.charAt(i)))
				return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void processMenu() {
		boolean execute = true;
		
		while (execute) {
			String prompt = fSys.getCurrUser().getName() + "@CS367$ ";
			System.out.print(prompt);
			String[] cmds = scnr.nextLine().trim().split(" ", 3);
			
			switch (cmds[0].toLowerCase()) {
			case "reset":
				if (cmds.length == 1) {
					fSys.reset();
					System.out.println(fSys.getCurrUser().getName() + "@CS367$ " + "Reset done");
				}
				else
					System.out.println(prompt + "No Argument Needed");
				break;
			case "pwd":
				if (cmds.length == 1)
					System.out.println(fSys.getPWD());
				else
					System.out.println(prompt + "No Argument Needed");
				break;
			case "ls":
				if (cmds.length == 1)
					fSys.printAll();
				else
					System.out.println(prompt + "No Argument Needed");
				break;
			case "u":
				if (cmds.length != 2)
					System.out.println(prompt + "One Argument Needed");
				else if (!fSys.setCurrentUser(cmds[1].toLowerCase()))
					System.out.println(prompt + "user " + cmds[1] + " does not exist");
				break;
			case "uinfo":
				if (cmds.length != 1)
					System.out.println(prompt + "No Argument Needed");
				else if (!fSys.printUsersInfo())
					System.out.println(prompt + "Insufficient privileges");
				break;
			case "cd":
				if (cmds.length != 2)
					System.out.println(prompt + "One Argument Needed");
				else if (!fSys.moveLoc(cmds[1].toLowerCase()))
					System.out.println(prompt + "Invalid location passed");
				break;
			case "rm":
				if (cmds.length != 2)
					System.out.println(prompt + "One Argument Needed");
				else if (!fSys.containsFileFolder(cmds[1].toLowerCase()))
					System.out.println(prompt + "Invalid name");
				else if (fSys.remove(cmds[1].toLowerCase()))
					System.out.println(prompt + cmds[1].toLowerCase() + " removed");
				else
					System.out.println(prompt + "Insufficient privilege");
				break;
			case "mkdir":
				if (cmds.length != 2)
					System.out.println(prompt + "One Argument Needed");
				else if (validName(cmds[1])) {
					fSys.mkdir(cmds[1].toLowerCase());
					System.out.println(prompt + cmds[1] + " added");
				}
				else
					System.out.println(prompt + "Invalid foldername");
				break;
			case "mkfile":
				if (cmds.length == 2 || cmds.length == 3) {
					String[] fname = cmds[1].toLowerCase().split("\\.", 2);
					if (validName(fname[0]) && (fname[1].equals("txt") || 
						fname[1].equals("doc") || fname[1].equals("rtf"))) {
					    fSys.addFile(cmds[1].toLowerCase(), (cmds.length == 3) ? cmds[2] : null);
					    System.out.println(prompt + cmds[1] + " added");
					}
					else
						System.out.println(prompt + "Invalid filename");
				}
				else
					System.out.println(prompt + "One Argument Needed");
				break;
			case "sh":
				if (cmds.length != 3 || cmds[2].split(" ").length != 2) {
					System.out.println(prompt + "Four Arguments Needed");
					break;
				}
				String[] sts = cmds[2].split(" ");
				if (sts[1].charAt(0) != 'w' && sts[1].charAt(0) != 'r')
					System.out.println(prompt + "Invalid permission type");
				else if (fSys.containsUser(sts[0].toLowerCase()) == null)
					System.out.println(prompt + "Invalid user");
				else if (!fSys.containsFileFolder(cmds[1].toLowerCase()))
					System.out.println(prompt + "Invalid file/folder name");
				else if (fSys.addUser(cmds[1].toLowerCase(), sts[0].toLowerCase(), sts[1].charAt(0)))
					System.out.println(prompt + "Privilege granted");
				else
					System.out.println(prompt + "Insufficient privilege");
				break;
			case "x":
				execute = false;
				break;
			default:
					System.out.println(prompt + "Invalid command");
			}
		}
	}
	
	public static void main(String[] args) {

		// check the command-line argument
		if (args.length != 1) {
			System.out.println("Usage: java FileSystemMain FileName");
			System.exit(0);
		}
		
		// load the file system
		loadFileSystem(args[0]);
		
		processMenu();
	}	
	
	
}