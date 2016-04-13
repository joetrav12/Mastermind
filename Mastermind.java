import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;

public class Mastermind {

	protected static int lengthOfList;
	protected static String [] arrayDoubled;

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		//Greet the user, explain rules:
		System.out.println("Hello! Welcome to Mastermind! In this game, you will have to create a secret combination consiting of different colors in different locations.");
		System.out.println("Once you've successfully entered your code, the computer will try to crack your code in the fewest possible guesses.");
		System.out.println("Of course, the computer does not know your code, but it does recieve feedback for every one if its guesses.");
		System.out.println("The bs represent black pegs. The number of bs that appear after each guess corresponds with the number of the elements in the computers guess that are the correct color and in the correct position.");
		System.out.println("The ws represent white pegs. The number of ws that appear after each guess correspons with the number of elements in the computer's guess that are the correct color but are in the incorrect position.");
		System.out.println();
		System.out.println("> Press '1' to begin a new game.");
		System.out.println("> Press any other key to quit.");
		String play = scan.nextLine();

		//if user enters 1, go to method newGame()
		if (play.equals("1")) {
			newGame();
		}
		//if user enters anything else, quit
		else {
			System.out.println();
			System.out.println("Goodbye!");
			System.exit(0);
		}

		scan.close();
	}

	public static void newGame() {
		Random rando = new Random();
		Scanner scan = new Scanner(System.in);

		System.out.println();

		int numberOfColors = 0;
		int x = 0;
		while (x == 0) {
			//catching errors; if user enters non-integer or a number less than 1 or greater than 10, re-prompts user
			try {
				int y = 0;

				while (y == 0) {
					System.out.println("Input number of colors (anywhere from 1 - 10):");
					int input = scan.nextInt();

					if (input < 1 || input > 10) {
						System.out.println("Number not between 1 and 10.");
						y = 0;
					}
					else {
						numberOfColors = input;
						break;
					}
				}

				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Not an integer.");
				x = 0;
				scan.nextLine();
			}
		}

		//all possible colors:
		String [] colors = {"R", "G", "Y", "B", "O", "P", "M", "S", "W", "C"};
		//names of possible colors
		String [] colorNames = {"Red", "Green", "Yellow", "Black", "Orange", "Purple", "Magenta", "Silver", "White", "Cyan"};
		//have to create new array of colors because the size depends on user's input
		String [] array = new String[numberOfColors];
		arrayDoubled = new String[numberOfColors*2];

		System.out.println();
		//showing user the colors that are allowed
		System.out.println("Colors Allowed:");
		for (int i = 0; i < numberOfColors; i++) {
			array[i] = colors[i];
			System.out.println(colorNames[i] + " (" + colors[i] + ")");
		}

		List<String> list0 = new ArrayList<String>();
		int j = 0;
		for (int i = 0; i < numberOfColors*2; i++) {
			list0.add(array[j]);
			j++;

			if (j == numberOfColors) {
				j = 0;
			}
		}
		arrayDoubled = list0.toArray(arrayDoubled);

		System.out.println();

		int length = 0;
		int z = 0;
		while (z == 0) {
			//catching error; if user inputs something that is not an integer, will re-prompt user
			try {
				System.out.println("Input length of code:");
				length = scan.nextInt();
				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Not an integer.");
				z = 0;
				scan.nextLine();
			}
		}
		lengthOfList = length;

		System.out.println();
		//user inputs their secret combination here:
		System.out.println("For each place in the code, input the letter that represents an allowed color.");
		String correctOrder = "";
		for (int i = 0; i < lengthOfList; i++) {
			System.out.println("Color " + (i + 1) + ": ");
			String input2 = scan.next();
			input2 = input2.toUpperCase();

			if (Arrays.asList(array).contains(input2)) {
				correctOrder += input2;
			}
			//catch error; if color is not allowed, user is re-prompted
			else {
				System.out.println("Not the letter of an allowed color.");
				i--;
			}
		}
		//convert lower-case letters to upper case letters
		correctOrder = correctOrder.toUpperCase();
		String initialCorrectOrder = correctOrder;

		//generate all possible combinations
		array = combinations(array, lengthOfList);

		System.out.println("Correct Order: " + correctOrder);

		//generate random first guess
		int index = rando.nextInt(array.length);
		String firstGuess = array[index];

		List<String> list = new ArrayList<String>();
		for (String s : array) {
			list.add(s);
		}

		String gameOver = "";
		for (int i = 0; i < lengthOfList; i++) {
			gameOver += "b";
		}

		System.out.println();

		nextMove(correctOrder, initialCorrectOrder, firstGuess, gameOver, list);
		
		scan.close();
	}

	public static String[] combinations(String[] array, int lengthOfList) {
		//initialize our returned list with the number of elements calculated above
		String [] allLists = new String[(int)Math.pow(array.length, lengthOfList)];

		//if lenght of 1, then just original element
		if (lengthOfList == 1) {
			return array; 
		}
		else {
			//getting all lists, beginning with list with a length of "lengthOfList", then lengthOfList - 1, etc...
			String[] allSublists = combinations(array, lengthOfList - 1);

			//appending sublists to each element
			int index = 0;

			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < allSublists.length; j++) {
					//add new combination to allLists
					allLists[index] = array[i] + allSublists[j];
					index++;
				}
			}

			return allLists;
		}
	}

	public static String response(String correctOrder, String firstGuess) {
		StringBuilder firstGuess2 = null;
		StringBuilder correctOrder2 = null;
		boolean b = false;
		String correctOrder3 = "";
		String firstGuess3 = "";
		StringBuilder firstGuess4 = null;
		StringBuilder correctOrder4 = null;
		String feedback = "";

		correctOrder2 = new StringBuilder(correctOrder);
		firstGuess2 = new StringBuilder(firstGuess);

		for (int i = 0; i < lengthOfList; i++) {
			//checking for number of black pegs
			if (correctOrder2.charAt(i) == firstGuess2.charAt(i)) {
				//replace corresponding characters with Xs and Zs so that they are not counting when checking for white pegs
				correctOrder2.setCharAt(i, 'X');
				firstGuess2.setCharAt(i, 'Z');

				correctOrder3 = correctOrder2.toString();
				firstGuess3 = firstGuess2.toString();
				b = true;
				feedback += "b";
			}
		}

		if (b == false) {
			correctOrder3 = correctOrder;
			firstGuess3 = firstGuess;
		}

		//must use arrayDoubled; duplicates will be eliminated. So if the correct code is BBOG and the computer's guess is RYBB, only white peg will be returned (which is wrong). So must use an array that contains each color twice to avoid this error
		for (String color : arrayDoubled) {
			//checking for white pegs
			if (correctOrder3.contains(color) && firstGuess3.contains(color)) {
				int indexCO = correctOrder3.indexOf(color);
				int indexFG = firstGuess3.indexOf(color);

				correctOrder4 = new StringBuilder(correctOrder3);
				firstGuess4 = new StringBuilder(firstGuess3);

				//replacing corresponding characters with @s and $s so that they are not counted again
				correctOrder4.setCharAt(indexCO, '@');
				firstGuess4.setCharAt(indexFG, '$');

				correctOrder3 = correctOrder4.toString();
				firstGuess3 = firstGuess4.toString();

				feedback += "w";
			}
		}

		return feedback;
	}
	
	public static void nextMove(String correctOrder, String initialCorrectOrder, String firstGuess, String gameOver, List<String> list) {
		Random rando = new Random();
		
		//max number of guesses: Integer.MAX_VAlUE
		for (int i = 0; i < Integer.MAX_VALUE;) {
			String result = response(correctOrder, firstGuess);
			System.out.println("Guess " + (i + 1) + ": " + firstGuess);
			System.out.println("Feedback: " + result);

			//if computer cracks code:
			if (result.equals(gameOver)) {
				System.out.println();
				System.out.println("The computer has cracked your code in " + (i + 1) + " turns! Would you like to play again?");
				System.out.println();
				System.out.println("> Press '1' to begin a new game.");
				System.out.println("> Press any other key to quit.");
				Scanner scan2 = new Scanner(System.in);
				String play2 = scan2.nextLine();

				//reset game:
				if (play2.equals("1")) {
					newGame();
					break;
				}
				else {
					System.out.println();
					System.out.println("Goodbye!");
					System.exit(0);
				}

				scan2.close();
				break;
			}
			else {
				System.out.println();

				//adjust list based on feedback:
				for (ListIterator<String>iterator = list.listIterator(); iterator.hasNext();) {
					String s = iterator.next();

					if (!result.equals(response(firstGuess, s)) || s.equals(firstGuess)) {
						iterator.remove();
					}
				}

				if (list.size() == 0) {
					result.equals(gameOver);
				}

				//reset correctOrder, get computer's next guess from remaining possible combinations:
				correctOrder = initialCorrectOrder;
				String [] array2 = new String[list.size()];
				array2 = list.toArray(array2);
				if (array2.length == 0) {}
				else {
					int index2 = rando.nextInt(array2.length);
					firstGuess = array2[index2];
				}

				i++;
			}

		}

	}

}