/**
 * Author: Huynh Hoang Huy aka Victor
 * RollNo: SE160046
 */
import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		view();
	}

	private static void view() {
		int choice = 0;
		Scanner scanner = new Scanner(System.in);
		FoodController controller = FoodController.getInstance();
		String wantToContinue = null;

		do {
			System.out.println("\n\nWelcome to Food Management - @2021 by Huynh Hoang Huy SE160046");
			System.out.println("Select the options below: ");
			System.out.println("1. Add a new food");
			System.out.println("2. Search a food by its name");
			System.out.println("3. Remove a food by its ID");
			System.out.println("4. Print the food list in the descending order of expired date");
			System.out.println("5. Quit");
			System.out.print("Your choice: ");

			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Invalid number format");
				continue;
			}
			System.out.println();

			switch (choice) {
				case 1:
					do {
						controller.add();
						do {
							System.out.print("Do you want to add another food (Y/N)? ");
							wantToContinue = scanner.nextLine();
							if (!wantToContinue.matches("[YyNn]")) {
								wantToContinue = null;
							}
						} while (wantToContinue == null);
					} while (wantToContinue.toUpperCase().equals("Y"));
					break;
				case 2:
					do {
						controller.search();
						do {
							System.out.print("Do you want to search another food (Y/N)? ");
							wantToContinue = scanner.nextLine();
							if (!wantToContinue.matches("[YyNn]")) {
								wantToContinue = null;
							}
						} while (wantToContinue == null);
					} while (wantToContinue.toUpperCase().equals("Y"));
					break;
				case 3:
					controller.delete();
					break;
				case 4:
					controller.printSortedList();
					break;
				case 6:
					controller.printList();
					break;
				case 5:
					System.out.println("Bye. Thank you for using me UwU");
					break;
				default:
					System.out.println("ERROR: Option is not available");
			}
		} while (choice != 5);

		controller.saveToFile();
		scanner.close();
	}
}
