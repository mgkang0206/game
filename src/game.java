import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.HashMap;
	import java.util.Iterator;
	import java.util.Scanner;

public class game {
	static ArrayList<String> products = new ArrayList();
	static final int EMPTY_HIT = 2;
	static final int OCCUPIED = 3;
	static final int OCCUPIED_HIT = 4;
	static int guesses = 0;
	static String player = "";
	static HashMap<String, String> list = new HashMap();

	public static void main(String[] args) {
		while(true) {
			homeScreen();
			String name = scan();
			if(name.contains("add")) {
				addProduct(name);
			} else if(name.contains("list")) {
				listProduct();
			} else if(name.contains("delete")) {
				removeProduct(name);
			} else if(name.contains("help")) {
				helpDianna();
			} else if(name.contains("play")) {
				if(products.size() > 0) {
					try {
						playGame();
					} catch (IOException var3) {
						var3.printStackTrace();
					}
				} else {
					System.out.println("There are no items in the inventory, please add some items before playing");
				}
			} else if(name.contains("exit")) {
				System.exit(0);
			} else if(name.contains("history")) {
				try {
					System.out.println("-----The Records-----");
					readHistory();
				} catch (IOException var4) {
					var4.printStackTrace();
				}
			}
		}
	}

	public static void addProduct(String userInput) {
		String item;
		if(userInput.length() > 3 && userInput.charAt(3) != 32) {
			item = userInput.substring(3);
			System.out.println(item + " is added to the inventory");
			item = capitalization(item);
			products.add(item);
			Collections.sort(products);
		} else if(userInput.length() > 4) {
			item = userInput.substring(4);
			System.out.println(item + " is added to the inventory");
			item = capitalization(item);
			products.add(item);
			Collections.sort(products);
		} else {
			System.out.println("Try again.");
		}

	}

	public static String capitalization(String itemW) {
		String item = "";
		String[] itemP = itemW.split(" ");

		for(int j = 0; j < itemP.length; ++j) {
			if(itemP[j].length() > 0) {
				item = item + itemP[j].substring(0, 1).toUpperCase() + itemP[j].substring(1).toLowerCase() + " ";
			}
		}

		return item;
	}

	public static void removeProduct(String userInput) {
		String item = "";
		boolean x = false;

		try {
			int x1;
			if(userInput.startsWith("delete ")) {
				item = userInput.substring(7);
				x1 = Integer.parseInt(item);
				if(x1 >= 0 && x1 <= products.size()) {
					System.out.println((String)products.get(x1 - 1) + " is removed from the inventory");
					products.remove(x1 - 1);
				}
			} else if(userInput.startsWith("delete")) {
				item = userInput.substring(6);
				x1 = Integer.parseInt(item);
				if(x1 >= 0 && x1 <= products.size()) {
					System.out.println((String)products.get(x1 - 1) + " is removed from the inventory");
					products.remove(x1 - 1);
				}
			}
		} catch (NumberFormatException var4) {
			System.out.println("This is not a number");
		}

	}

	public static void helpDianna() {
		System.out.println("\'Add _____\' command is used to add a product to your inventory.");
		System.out.println("\'List\' command displays the list of items in your inventory.");
		System.out.println("\'Delete #\' command gets rid of an item from your inventory.");
		System.out.println("\'Play\' command will let you play a fun game of battleship!");
		System.out.println("\'History\' command will show you the game record");
		System.out.println("IF YOU DID NOT GET A CONFIRMATION PROMPT, THE CHANGE DID NOT HAPPEN.");
	}

	public static void listProduct() {
		if(products.isEmpty()) {
			System.out.println("The inventory is empty");
		} else {
			Iterator i$ = products.iterator();

			while(i$.hasNext()) {
				String item = (String)i$.next();
				System.out.println(item);
			}
		}

	}

	public static void homeScreen() {
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.println("Welcome to Dianna\'s Dinosaur & Donut Emporium. How may I help you?");
		System.out.println("Your choices are, \'Add \'item name\'\', \'List\', \'Delete #\', \'Play\', \'History\' and \'Help\'");
		System.out.println("Please type your choice or type \'exit\' to exit out of the program");
		System.out.println("-------------------------------------------------------------------------------------");
	}

	public static String scan() {
		Scanner input = new Scanner(System.in);
		String userInput = input.nextLine();
		return userInput.toLowerCase();
	}

	public static void lines() {
		for(int k = 0; k < 15; ++k) {
			System.out.print('-');
		}

		System.out.println();
	}

	public static void redrawBoard(int[][] board) {
		lines();

		for(int y = 0; y < board.length; ++y) {
			for(int x = 0; x < board[0].length; ++x) {
				if(board[x][y] != 0 && board[x][y] != 3) {
					if(board[x][y] == 4) {
						System.out.print("|O|");
					} else if(board[x][y] == 2) {
						System.out.print("|X|");
					}
				} else {
					System.out.print("|*|");
				}
			}

			System.out.println("");
		}

		lines();
	}

	public static boolean fireAt(int[][] board, int x, int y) {
		return board[x][y] == 3;
	}

	public static int[][] updateBoard(int[][] board, int x, int y) {
		if(board[x][y] == 3) {
			board[x][y] = 4;
		} else if(board[x][y] == 0) {
			board[x][y] = 2;
		}

		return board;
	}

	private static int[][] setup(int height, int width) {
		int[][] board = new int[height][width];
		int i = 0;

		while(i < products.size()) {
			int randWidth = (int)(Math.random() * (double)width);
			int randHeight = (int)(Math.random() * (double)height);
			if(!list.containsKey(randWidth + "," + randHeight)) {
				list.put(randWidth + "," + randHeight, products.get(i));
				board[randWidth][randHeight] = 3;
				++i;
			}
		}

		return board;
	}

	public static void playGame() throws IOException {
		int[][] board = setup(5, 5);
		redrawBoard(board);
		System.out.println("Lets play a game of Battleship with your favorite items!!");
		System.out.println("Please enter the x and y coordinates by \"x,y\"");
		boolean won = false;

		Scanner s3;
		while(!won) {
			System.out.println("Fire to a position:");
			s3 = new Scanner(System.in);
			String e = s3.nextLine();
			if(e.equals("exit")) {
				System.out.println("Thank you for playing");
				break;
			}

			String[] splitAttemptString = e.split(",");
			if(splitAttemptString[0].length() <= 1 && splitAttemptString[1].length() <= 1 && Integer.parseInt(splitAttemptString[0]) <= 4 && Integer.parseInt(splitAttemptString[1]) <= 4 && e.charAt(1) == 44) {
				int attemptX = Integer.parseInt(splitAttemptString[0]);
				int attemptY = Integer.parseInt(splitAttemptString[1]);
				++guesses;
				boolean fi = fireAt(board, attemptX, attemptY);
				board = updateBoard(board, attemptX, attemptY);
				if(fi) {
					System.out.println("YES! YOU HAVE HIT A " + (String)list.get(e));
				} else {
					System.out.println("You have not hit anything please try again");
				}

				redrawBoard(board);
				boolean occupied = false;

				for(int i = 0; i < board.length; ++i) {
					for(int j = 0; j < board[0].length; ++j) {
						if(board[i][j] == 3) {
							occupied = true;
						}
					}
				}

				if(!occupied) {
					won = true;
				}
			} else {
				System.out.println("Please put right coordinate");
			}
		}

		System.out.println("Congrats!!! You have won the game. it took " + guesses + " guesses.");
		System.out.println("Please Enter Your Name");
		s3 = new Scanner(System.in);
		player = capitalization(s3.nextLine());

		try {
			writeHistory();
		} catch (IOException var11) {
			var11.printStackTrace();
		}

	}

	public static void writeHistory() throws IOException {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter("gameHistory.txt", true));
			writer.newLine();
			writer.write(guesses + "     -     " + player);
		} catch (IOException var10) {
			;
		} finally {
			try {
				if(writer != null) {
					writer.close();
				}
			} catch (IOException var9) {
				;
			}

		}

	}

	public static void readHistory() throws IOException {
		ArrayList record = new ArrayList();
		Scanner s4 = new Scanner(new File("gameHistory.txt"));

		while(s4.hasNextLine()) {
			record.add(s4.nextLine());
		}

		Collections.sort(record);

		for(int i = 0; i < record.size(); ++i) {
			System.out.println((String)record.get(i));
		}

	}
}
