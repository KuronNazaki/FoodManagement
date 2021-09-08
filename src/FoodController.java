/**
 * Author: Huynh Hoang Huy aka Victor
 * RollNo: SE160046
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FoodController {
	private final String FILE_NAME = "food.dat";
	private List<Food> foods = null;
	private static FoodController controller = null;

	private final Scanner userScanner = new Scanner(System.in);
	private Scanner scanner = null;
	private FileWriter fileWriter = null;
	private BufferedWriter bufferedWriter = null;
	private PrintWriter printWriter = null;

	private FoodController() {
		foods = getFromFile();
	}

	public static FoodController getInstance() {
		if (controller == null)
			controller = new FoodController();

		return controller;
	}

	public Optional<Food> find(String id) {
		return foods.isEmpty() ? Optional.empty() : foods.stream().filter(food -> food.getId().equals(id)).findFirst();
	}

	public List<Food> findAllByName(String name) {
		return foods.size() == 0 ? null
				: foods.stream().filter(food -> food.getName().matches("(?i).*" + name + ".*")).collect(Collectors.toList());
	}

	private boolean isExisted(String id) {
		if (foods.isEmpty()) {
			return false;
		}

		for (Food food : foods) {
			if (food.getId().equals(id)) {
				System.out.println("ID is existed");
				return true;
			}
		}

		return false;
	}

	public void add() {
		String id = null, name = null, type = null, place = null, inputString = null;
		int weight = 0;
		Date expiredDate = null;

		System.out.println("\n--ADD NEW FOOD--");

		do {
			System.out.print("Enter ID: ");
			id = userScanner.nextLine();
		} while (isExisted(id) || isEmptyString(id));

		do {
			System.out.print("Enter name: ");
			name = userScanner.nextLine();
		} while (isEmptyString(name));

		do {
			try {
				System.out.print("Enter weight in gram: ");
				weight = Integer.parseInt(userScanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Invalid number format");
			}
		} while (weight == 0);

		do {
			System.out.print("Enter type: ");
			type = userScanner.nextLine();
		} while (isEmptyString(type));

		do {
			System.out.print("Enter place (cool or freeze): ");
			place = userScanner.nextLine();
		} while (!place.matches("(?i)(cool|freeze)"));

		do {
			try {
				System.out.print("Enter expired date (dd/mm/yyyy): ");
				inputString = userScanner.nextLine();

				// Check date format
				if (!inputString.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$") || !isValidDate(inputString)) {
					System.out.println("ERROR: Invalid date");
					continue;
				}

				expiredDate = new SimpleDateFormat("dd/MM/yyyy").parse(inputString);
				if (!isValidExpiredDate(expiredDate)) {
					System.out.println("ERROR: Expired food");
					expiredDate = null;
				}
			} catch (ParseException e) {
				System.out.println("ERROR: Parsing unsuccessfully");
			}
		} while (expiredDate == null);

		Food food = new Food(id, refineText(name), weight, refineText(type), refineText(place), expiredDate);
		foods.add(food);
		System.out.println("New food with ID = " + id + " has been added successfully");
	}

	public void delete() {
		String id = null;
		String wantToDelete = null;

		System.out.println("\n--Remove food item--");
		System.out.print("Enter food's ID to remove: ");
		id = userScanner.nextLine();
		Optional<Food> foodOptional = find(id);
		Food food = foodOptional.isPresent() ? foodOptional.get() : null;

		if (food != null) {
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
			foods.remove(food);
			System.out.println("Food item with ID = " + food.getId() + " has been deleted successfully");
		} else {
			System.out.println("Food item with ID = " + id + " is not existed");
		}
	}

	public void search() {
		String name = null;
		System.out.println("\n--Search food item--");
		System.out.print("Enter food's name to search: ");
		name = userScanner.nextLine();
		List<Food> foodList = findAllByName(name);

		if (foodList != null) {
			printList(foodList);
		} else {
			System.out.println("Not found");
		}
	}

	private Food create(String data) {
		String[] tokens = data.split("\\|");
		int weight = Integer.parseInt(tokens[2]);
		Date expiredDate = null;
		try {
			expiredDate = new SimpleDateFormat("dd/MM/yyyy").parse(tokens[5]);
		} catch (ParseException e) {
			System.out.println("ERROR: Parsing unsuccessfully");
		}
		Food food = new Food(tokens[0], tokens[1], weight, tokens[3], tokens[4], expiredDate);
		return food;
	}

	// File operation module
	public List<Food> getFromFile() {
		List<Food> foods = new ArrayList<>();
		boolean isReadMode = true;
		open(isReadMode);

		if (foods.size() != 0) {
			foods.clear();
		}

		while (scanner.hasNext()) {
			foods.add(create(scanner.nextLine()));
		}

		close();
		return foods;
	}

	public void saveToFile() {
		boolean isReadMode = false;
		open(isReadMode);

		foods.stream().forEach(food -> printWriter.println(toWritableString(food)));

		close();
	}

	private void open(boolean isReadMode) {
		if (isReadMode) {
			try {
				scanner = new Scanner(Paths.get(FILE_NAME), "UTF-8");
			} catch (IOException e) {
				System.out.println("ERROR: Connecting to file failed");
			}
		} else {
			try {
				fileWriter = new FileWriter(FILE_NAME, false);
				bufferedWriter = new BufferedWriter(fileWriter);
				printWriter = new PrintWriter(bufferedWriter);
			} catch (IOException e) {
				System.out.println("ERROR: Connecting to file failed");
			}
		}
	}

	private void close() {
		if (scanner != null) {
			scanner.close();
		}

		if (fileWriter != null && bufferedWriter != null && printWriter != null) {
			try {
				printWriter.close();
				bufferedWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("ERROR: Closing file failed");
			}
		}
	}

	// Printing-out module
	public void printSortedList() {
		System.out.println("+--------------------------------Sorted Food List-----------------------------------+");
		System.out.format("|%8s|%20s|%13s|%15s|%10s|%12s|\n", "ID", "Name", "Weight", "Type", "Place", "Expired By");
		System.out.println("+-----------------------------------------------------------------------------------+");
		foods.stream().sorted((first, second) -> first.getExpiredDate().compareTo(second.getExpiredDate()))
				.forEach(FoodController::printFormattedItem);
		System.out.println("+-----------------------------------------------------------------------------------+");
	}

	public void printList() {
		printList(foods);
	}

	private void printList(List<Food> foodList) {
		System.out.println("+-----------------------------------Food List---------------------------------------+");
		System.out.format("|%8s|%20s|%13s|%15s|%10s|%12s|\n", "ID", "Name", "Weight", "Type", "Place", "Expired By");
		System.out.println("+-----------------------------------------------------------------------------------+");
		foodList.stream().forEach(FoodController::printFormattedItem);
		System.out.println("+-----------------------------------------------------------------------------------+");
	}

	private static void printFormattedItem(Food food) {
		System.out.format("|%8s|%20s|%10d(g)|%15s|%10s|%12s|\n", food.getId(), food.getName(), food.getWeight(),
				food.getType(), food.getPlace(), toSimpleDateString(food.getExpiredDate()));
	}

	// Data validation
	private static boolean isValidDate(String date) {
		String[] tokens = date.split("/");
		int day = Integer.parseInt(tokens[0]);
		int month = Integer.parseInt(tokens[1]);
		int year = Integer.parseInt(tokens[2]);
		boolean isLeapYear = false;

		if (day <= 0 || month <= 0 || year <= 0) {
			return false;
		}

		if (year % 100 == 0) {
			isLeapYear = (year % 400 == 0) ? true : false;
		} else {
			isLeapYear = (year % 4 == 0) ? true : false;
		}

		if (month > 12 || day > 31) {
			return false;
		}
		switch (month) {
			case 2:
				if (isLeapYear) {
					if (day > 29) {
						return false;
					}
				} else {
					if (day > 28) {
						return false;
					}
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				if (day > 30) {
					return false;
				}
				break;
		}

		return true;
	}

	private static boolean isValidExpiredDate(Date expiredDate) {
		Date now = new Date();
		int compare = expiredDate.compareTo(now);
		return compare <= 0 ? false : true;
	}

	private static boolean isEmptyString(String data) {
		if (data.equals("")) {
			System.out.println("ERROR: String can't be empty");
			return true;
		} else {
			return false;
		}
	}

	private static String refineText(String data) {
		data = data.trim().toLowerCase();
		data = data.replaceAll("\\s+", " ");
		String[] temp = data.split(" ");

		data = "";
		for (int i = 0; i < temp.length; i++) {
			data += String.valueOf(temp[i].charAt(0)).toUpperCase() + temp[i].substring(1);
			if (i < temp.length - 1) {
				data += " ";
			}
		}

		return data;
	}

	//Utilities
	private static String toSimpleDateString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date).toString();
	}

	private static String toWritableString(Food food) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return food.getId() + "|" + food.getName() + "|" + food.getWeight() + "|" + food.getType() + "|" + food.getPlace()
				+ "|" + dateFormat.format(food.getExpiredDate());
	}
}