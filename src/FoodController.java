/**
 * Author: Huynh Hoang Huy
 * RollNo: SE160046
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FoodController {
	private final String FILE_NAME = "food.dat";
	public static final int MAX_WEIGHT = 10000;
	private List<Food> foods = null;
	private static FoodController controller = null;

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

	public Food find(String id) {
		return foods.isEmpty() ? null : foods.stream().filter(food -> food.getId().equals(id)).findFirst().get();
	}

	public List<Food> findAllByName(String name) {
		return foods.size() == 0 ? null
				: foods.stream().filter(food -> food.getName().matches("(?i).*" + name + ".*")).collect(Collectors.toList());
	}

	public List<Food> findAllByDate(Date date) {
		return foods.size() == 0 ? null : foods.stream().filter(food -> food.getExpiredDate().compareTo(date) <= 0).collect(Collectors.toList());
	}

	public boolean isExisted(String id) {
		if (foods.isEmpty()) {
			return false;
		}

		for (Food food : foods) {
			if (food.getId().equals(id)) {
				System.err.println("ID is existed");
				return true;
			}
		}

		return false;
	}

	public void add(Food food) {
		foods.add(food);
	}

	public void remove(Food food) {
		foods.remove(food);
	}

	private Food create(String data) {
		String[] tokens = data.split("\\|");
		Food food = null;

		try {
			food = new Food(tokens[0], tokens[1], Integer.parseInt(tokens[2]), tokens[3], tokens[4],
					new SimpleDateFormat("dd/MM/yyyy").parse(tokens[5]));
		} catch (ParseException e) {
			System.err.println("ERROR: Parsing unsuccessfully");
		}

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

		foods.stream().forEach(food -> printWriter.println(Utility.toWritableString(food)));

		close();
	}

	private void open(boolean isReadMode) {
		if (isReadMode) {
			try {
				scanner = new Scanner(Paths.get(FILE_NAME), "UTF-8");
			} catch (IOException e) {
				System.err.println("ERROR: Connecting to file failed");
			}
		} else {
			try {
				fileWriter = new FileWriter(FILE_NAME, false);
				bufferedWriter = new BufferedWriter(fileWriter);
				printWriter = new PrintWriter(bufferedWriter);
			} catch (IOException e) {
				System.err.println("ERROR: Connecting to file failed");
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
				System.err.println("ERROR: Closing file failed");
			}
		}
	}

	// Printing-out module
	public void printSortedList() {
		System.out.println("+--------------------------------Sorted Food List-----------------------------------+");
		System.out.format("|%8s|%20s|%13s|%15s|%10s|%12s|\n", "ID", "Name", "Weight", "Type", "Place", "Expired By");
		System.out.println("+-----------------------------------------------------------------------------------+");
		foods.stream().sorted((first, second) -> second.getExpiredDate().compareTo(first.getExpiredDate()))
				.forEach(FoodController::printFormattedItem);
		System.out.println("+-----------------------------------------------------------------------------------+");
	}

	public void printList() {
		printList(foods);
	}

	public void printList(List<Food> foodList) {
		System.out.println("+-----------------------------------Food List---------------------------------------+");
		System.out.format("|%8s|%20s|%13s|%15s|%10s|%12s|\n", "ID", "Name", "Weight", "Type", "Place", "Expired By");
		System.out.println("+-----------------------------------------------------------------------------------+");
		foodList.stream().forEach(FoodController::printFormattedItem);
		System.out.println("+-----------------------------------------------------------------------------------+");
	}

	private static void printFormattedItem(Food food) {
		System.out.format("|%8s|%20s|%10d(g)|%15s|%10s|%12s|\n", food.getId(), food.getName(), food.getWeight(),
				food.getType(), food.getPlace(), Utility.toSimpleDateString(food.getExpiredDate()));
	}
}