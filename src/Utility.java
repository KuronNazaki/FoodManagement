/**
 * Author: Huynh Hoang Huy
 * RollNo: SE160046
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public static boolean isValidExpiredDate(Date expiredDate) {
		Date now = new Date();
		int compare = expiredDate.compareTo(now);
		return compare <= 0 ? false : true;
	}

	public static boolean isEmptyString(String data) {
		if (data.equals("")) {
			System.err.println("ERROR: String can't be empty");
			return true;
		} else {
			return false;
		}
	}

	public static String refineText(String data) {
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

	public static String toSimpleDateString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date).toString();
	}

	public static String toWritableString(Food food) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return food.getId() + "|" + food.getName() + "|" + food.getWeight() + "|" + food.getType() + "|" + food.getPlace()
				+ "|" + dateFormat.format(food.getExpiredDate());
	}
}
