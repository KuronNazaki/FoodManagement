package com.victor.controller;

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

import com.victor.entity.Food;

@SuppressWarnings("serial")
public class FoodController {
	private final String FILE_NAME = "food.dat";
	private final List<Food> foods = new ArrayList<>();
	private static FoodController controller = null;

	private final Scanner userScanner = new Scanner(System.in);
	private Scanner scanner = null;
	private FileWriter fileWriter = null;
	private BufferedWriter bufferedWriter = null;
	private PrintWriter printWriter = null;

	private FoodController() {

	}

	public static FoodController getInstance() {
		if (controller == null)
			controller = new FoodController();

		return controller;
	}

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

	private String toWritableString(Food food) {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		return food.getId() + "|" + food.getName() + "|" + food.getWeight() + "|" + food.getType() + "|" + food.getPlace() + "|" + dateFormat.format(food.getExpiredDate());
	}

	public Optional<Food> find(String id) {
		return foods.isEmpty() ? Optional.empty() : foods.stream().filter(food -> food.getId().equals(id)).findFirst();
	}

	public void add() {
		String id = null, name = null, type = null, place = null;
		int weight = 0;
		Date expiredDate = null;

		System.out.println("\n\n--ADD NEW FOOD--\n");

		System.out.print("Enter ID: ");
		do {
			id = userScanner.nextLine(); // need ID validation
		} while (isExisted(id));

		System.out.print("Enter name: ");
		name = userScanner.nextLine();

		do {
			try {
				System.out.print("Enter weight in gram: ");
				weight = Integer.parseInt(userScanner.nextLine()); // need number validation
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Invalid number format");
			}
		} while (weight != 0);

		System.out.print("Enter type: ");
		type = userScanner.nextLine(); // need a menu to choose between various types

		System.out.print("Enter place (cool or freeze): ");
		place = userScanner.nextLine(); // need to validate whether it is cool or freeze or not

		do {
			try {
				System.out.print("Enter expired date (dd.mm.yyyy): ");
				expiredDate = new SimpleDateFormat("dd.MM.yyyy").parse(scanner.nextLine()); // need date validation
			} catch (ParseException e) {
				System.out.println("ERROR: Parsing unsuccessfully");
			}
		} while (expiredDate != null);

		Food food = new Food(id, name, weight, type, place, expiredDate);
		foods.add(food);
		System.out.println("New food with id=" + food.getId() + " has been added successfully");

	}

	public void delete() {
		String id = null;
		System.out.print("Enter food's ID to remove: ");
		id = userScanner.nextLine();
		Optional<Food> foodOptional = find(id);
		Food food = foodOptional.isPresent() ? foodOptional.get() : null;

		if (food != null) {
			foods.remove(food);
			System.out.println("Food item with ID = " + food.getId() + " has been deleted successfully");
		} else {
			System.out.println("Food item with ID = " + id + " is not existed");
		}
	}

	public void search() {
		String id = null;
		System.out.print("Enter food's ID to search: ");
		id = userScanner.nextLine();
		Optional<Food> foodOptional = find(id);
		Food foundFood = foodOptional.isPresent() ? foodOptional.get() : null;

		if (foundFood != null) {
			System.out.println("Found: " + foundFood.toString());
		} else {
			System.out.println("Not found");
		}
	}

	public boolean isExisted(String id) {
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

	public Food create(String data) {
		String[] tokens = data.split("\\|");
		int weight = Integer.parseInt(tokens[2]);
		Date expiredDate = null;
		try {
			expiredDate = new SimpleDateFormat("dd.MM.yyyy").parse(tokens[5]);
		} catch (ParseException e) {
			System.out.println("ERROR: Parsing unsuccessfully");
		}
		Food food = new Food(tokens[0], tokens[1], weight, tokens[3], tokens[4], expiredDate);
		return food;
	}

	public void open(boolean isReadMode) {
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

	public void close() {
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
}
