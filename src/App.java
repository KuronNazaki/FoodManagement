/**
 * Author: Huynh Hoang Huy
 * RollNo: SE160046
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {
	private final static Scanner userScanner = new Scanner(System.in);

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
				System.err.println("ERROR: Invalid number format");
				continue;
			}
			System.out.println();

			switch (choice) {
				case 1:
					do {
						add(controller);
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
						searchExpiredDate(controller);
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
					delete(controller);
					break;
				case 4:
					controller.printSortedList();
					break;
				case 5:
					System.out.println("Bye. Thank you for using me UwU");
					break;
				default:
					System.err.println("ERROR: Option is not available");
			}
		} while (choice != 5);

		controller.saveToFile();
		scanner.close();
	}

	public static void add(FoodController controller) {
		String id = null, name = null, type = null, place = null, inputString = null;
		int weight = 0;
		Date expiredDate = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);

		System.out.println("\n-----ADD NEW FOOD-----");

		do {
			System.out.print("Enter ID: ");
			id = userScanner.nextLine();
		} while (controller.isExisted(id) || Utility.isEmptyString(id));

		do {
			System.out.print("Enter name: ");
			name = userScanner.nextLine();
		} while (Utility.isEmptyString(name));

		do {
			try {
				System.out.print("Enter weight in gram: ");
				weight = Integer.parseInt(userScanner.nextLine());
				if (weight == 0) {
					System.err.println("Weight can't be 0");
				} else if (weight < 0) {
					System.err.println("Weight can't be negative");
					weight = 0;
				} else if (weight > FoodController.MAX_WEIGHT) {
					System.err.println("Weight can't be greater than 10,000 grams");
					weight = 0;
				}
			} catch (NumberFormatException e) {
				System.err.println("ERROR: Invalid number format");
			}
		} while (weight == 0);

		do {
			System.out.print("Enter type: ");
			type = userScanner.nextLine();
		} while (Utility.isEmptyString(type));

		do {
			System.out.print("Enter place (cool or freeze): ");
			place = userScanner.nextLine();
		} while (!place.matches("(?i)(cool|freeze)"));

		do {
			try {
				System.out.print("Enter expired date (dd/mm/yyyy): ");
				inputString = userScanner.nextLine();

				if (!inputString.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")) {
					System.err.println("ERROR: Invalid date");
					continue;
				}

				expiredDate = dateFormat.parse(inputString);
				if (!Utility.isValidExpiredDate(expiredDate)) {
					System.err.println("ERROR: Expired food");
					expiredDate = null;
				}
			} catch (ParseException e) {
				System.err.println("ERROR: Invalid date");
			}
		} while (expiredDate == null);

		Food food = new Food(id, Utility.refineText(name), weight, Utility.refineText(type), Utility.refineText(place),
				expiredDate);
		controller.add(food);
		System.out.println("New food with ID = " + id + " has been added successfully");
	}

	public static void delete(FoodController controller) {
		String id = null;
		String wantToDelete = null;
		Food food = null;

		do {
			System.out.println("\n-----Remove food item-----");
			System.out.print("Enter food's ID to remove: ");
			id = userScanner.nextLine();
		} while (Utility.isEmptyString(id));

		try {
			food = controller.find(id);
		} catch (NoSuchElementException e) {
			System.out.println("Food item with ID = " + id + " is not existed");
			return;
		}

		do {
			System.out.print("Do you want to delete this food (Y/N)? ");
			wantToDelete = userScanner.nextLine();
			if (!wantToDelete.matches("[YyNn]")) {
				wantToDelete = null;
			}
		} while (wantToDelete == null);
		if (wantToDelete.toUpperCase().equals("N")) {
			System.out.print("Don't worry. I don't remove that");
			return;
		}
		controller.remove(food);
		System.out.println("Food item with ID = " + food.getId() + " has been deleted successfully");
	}

	public static void search(FoodController controller) {
		String name = null;
		
		do {
			System.out.println("\n-----Search food item-----");
			System.out.print("Enter food's name to search: ");
			name = userScanner.nextLine();
		} while (Utility.isEmptyString(name));
		
		List<Food> foodList = controller.findAllByName(name);
		if (foodList.size() != 0) {
			controller.printList(foodList);
		} else {
			System.err.println("No item is found");
		}
	}

	public static void searchExpiredDate(FoodController controller) {
		String inputString = null;
		Date expiredDate = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		do {
			try {
				System.out.print("Enter expired date (dd/mm/yyyy): ");
				inputString = userScanner.nextLine();

				if (!inputString.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")) {
					System.err.println("ERROR: Invalid date");
					continue;
				}

				expiredDate = dateFormat.parse(inputString);
			} catch (ParseException e) {
				System.err.println("ERROR: Invalid date");
			}
		} while (expiredDate == null);

		List<Food> foodList = controller.findAllByDate(expiredDate);
		if (foodList.size() != 0) {
			controller.printList(foodList);
		} else {
			System.err.println("No item is found");
		}
	}
}
