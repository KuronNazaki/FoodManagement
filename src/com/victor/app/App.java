package com.victor.app;

import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		view();
	}

	private static void view() {
		int choice = 0;
		Scanner scanner = new Scanner(System.in);

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
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					System.out.println("Bye. Thank you for using me UwU");
					break;
				default:
					System.out.println("ERROR: Option is not available");
			}
		} while (choice != 5);

		scanner.close();
	}
}
