package com.victor.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.victor.connection.FileConnection;
import com.victor.dao.DAO;
import com.victor.entity.Food;

@SuppressWarnings("serial")
public class FoodController extends ArrayList<Food> implements DAO<Food, String>, FileConnection<Food> {
	private final String FILE_NAME = "food.dat";
	private final Scanner scanner = new Scanner(System.in);
	private FoodController controller;

	private FoodController() {

	}

	public FoodController getInstance() {
		if (controller != null)
			controller = new FoodController();

		return controller;
	}

	@Override
	public List<Food> getFromFile() {
		return null;
	}

	@Override
	public boolean saveToFile() {
		return false;
	}

	@Override
	public Optional<Food> find(String id) {
		return null;
	}

	@Override
	public List<Food> findAll() {
		return null;
	}

	@Override
	public boolean add(Food o) {
		this.add(o);
		return true;
	}

	public void add() {
		String id = null, name = null, type = null, place = null;
		int weight = 0;
		Date expiredDate = null;

		System.out.println("\n\n--ADD NEW FOOD--\n");
		System.out.print("Enter ID: ");
		id = scanner.nextLine(); // need ID validation
		System.out.print("Enter name: ");
		name = scanner.nextLine();
		System.out.print("Enter weight in gram: ");
		weight = Integer.parseInt(scanner.nextLine()); // need number validation
		System.out.print("Enter type: ");
		type = scanner.nextLine(); // need a menu to choose between various types
		System.out.print("Enter place (cool or freeze): ");
		place = scanner.nextLine(); // need to validate whether it is cool or freeze or not
		try {
			System.out.print("Enter expired date (dd/mm/yyyy): ");
			expiredDate = new SimpleDateFormat("dd.MM.yyyy").parse(scanner.nextLine()); // need date validation
		} catch (ParseException e) {
			System.out.println("ERROR: Parsing unsuccessfully");
		}

		Food food = new Food(id, name, weight, type, place, expiredDate);
		add(food);
	}

	@Override
	public boolean delete(Food o) {
		return false;
	}

	public boolean validateId(String id) {
		return false;
	}
}
