package pt.iscte.poo.sokobanstarter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManagement {

	/**
	 * Reads the floor scheme for the current level from a file and populates the
	 * floorScheme array.
	 * This method opens a file corresponding to the current level and reads its
	 * contents line by line.
	 * Each line represents a row in the level's floor scheme, and each character in
	 * a line represents a different element or tile.
	 */
	protected static char[][] getFloorSchema() {
		GameEngine instance = GameEngine.getInstance();
		char[][] floorScheme = new char[instance.getGridWidth()][instance.getGridHeight()];

		File file = new File("./levels/level" + instance.getLevel() + ".txt");
		try {
			Scanner sc = new Scanner(file);
			int row = 0;
			while (sc.hasNextLine() && row != 10) {
				floorScheme[row] = sc.nextLine().toCharArray();
				row++;
			}
			sc.close();

		} catch (FileNotFoundException e) {
			// Handle the case where the level file is not found
			e.printStackTrace();
		}
		return floorScheme;
	}

	/**
	 * Recursively determines the number of the last level available in the game.
	 * This method checks for the existence of level files in a sequence,
	 * incrementing the level number until a file is not found.
	 * It's used to identify how many levels are present in the game.
	 *
	 * @param level The starting level number from which to begin the search.
	 *              Usually starts with 1 or the lowest level number.
	 * @return The number of the last level available. If no file is found for the
	 *         current level, it returns one less than the current level.
	 */
	protected static int getLastLevelNumber(int level) {
		File file = new File("./levels/level" + level + ".txt");

		if (file.exists()) {
			return getLastLevelNumber(level + 1);
		} else {
			return level - 1;
		}
	}

	/**
	 * Registers the level scores to a file.
	 * This method checks if the current score is better than the best score
	 * recorded and updates the scores file accordingly.
	 */
	public static void registerLevels() {
		GameEngine instance = GameEngine.getInstance();

		int bestScore = Integer.parseInt(getBestLevelScores().split(":", 2)[1]);
		if (bestScore < instance.getBattery()) {
			File file = new File("pontuacao.txt");
			try {
				file.createNewFile();

				try (BufferedWriter output = new BufferedWriter(new FileWriter(file, true))) {
					output.write("Username:" + instance.getUsername() + "| Level:" + instance.getLevel() + "| Score:"
							+ instance.getBattery());
					output.newLine(); // Adds a new line for each entry
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads a file and returns its content as a list of string arrays.
	 * Each line in the file is split into parts and added to the list.
	 * 
	 * @param filePath The path of the file to read.
	 * @return A list of string arrays, where each array represents a line in the
	 *         file.
	 * @throws FileNotFoundException if the file is not found.
	 */
	private static List<String[]> readFile(String filePath) throws FileNotFoundException {
		List<String[]> lines = new ArrayList<>();
		try (Scanner fileScanner = new Scanner(new File(filePath))) {
			while (fileScanner.hasNextLine()) {
				String[] parts = fileScanner.nextLine().split("\\|", 3);
				if (parts.length == 3) {
					lines.add(parts);
				}
			}
		}
		return lines;
	}

	/**
	 * Retrieves the best score for the current level.
	 * This method scans through the scores file and finds the best score for the
	 * user at the current level.
	 * 
	 * @return A string representing the best score of the user at the current
	 *         level.
	 */
	public static String getBestLevelScores() {
		GameEngine instance = GameEngine.getInstance();
		String currentUsername = instance.getUsername();
		int bestScore = 0;
		try {
			for (String[] parts : readFile("pontuacao.txt")) {
				String username = parts[0].split(":", 2)[1].trim();
				if (username.equals(currentUsername)) {
					int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
					int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
					if (currentLevel == instance.getLevel() && currentScore > bestScore) {
						bestScore = currentScore;
					}
				}
			}
		} catch (FileNotFoundException e) {
			return "Error: Scores file not found.";
		}
		return currentUsername + ":" + bestScore;
	}

	/**
	 * Retrieves the best scores of all users for the current level.
	 * This method scans through the scores file and compiles a list of the best
	 * scores at the current level.
	 * 
	 * @return A formatted string representing the best scores of all users at the
	 *         current level.
	 */
	public static String getAllBestLevelScores() {
		GameEngine instance = GameEngine.getInstance();
		StringBuilder usersBestScore = new StringBuilder("Best Scores of level " + instance.getLevel() + "\n");
		try {
			for (String[] parts : readFile("pontuacao.txt")) {
				int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
				if (currentLevel == instance.getLevel()) {
					String username = parts[0].split(":", 2)[1].trim();
					int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
					usersBestScore.append("User(").append(username).append("): ").append(currentScore).append("\n");
				}
			}
		} catch (FileNotFoundException e) {
			return "Error: Scores file not found.";
		}
		return usersBestScore.append("\nPress N to continue").toString();
	}

	/**
	 * Retrieves the best scores for the current user for all levels.
	 * This method scans through the scores file and compiles a list of the best
	 * scores of the current user across all levels.
	 * 
	 * @return A formatted string representing the best scores of the current user
	 *         for each level.
	 */
	public static String getUsersBestScores() {
		GameEngine instance = GameEngine.getInstance();
		List<String> userScores = new ArrayList<>();
		try {
			for (String[] parts : readFile("pontuacao.txt")) {
				String username = parts[0].split(":", 2)[1].trim();
				int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
				if (currentLevel == instance.getLevel()) {
					int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
					userScores.add(username + ": " + currentScore);
				}
			}
		} catch (FileNotFoundException e) {
			return "Error: Scores file not found.";
		}

		List<String> userBestScores = new ArrayList<>();
		for (String scoreInfo : userScores) {
			String[] parts = scoreInfo.split(": ");
			String username = parts[0];
			int score = Integer.parseInt(parts[1]);

			boolean found = false;
			for (String bestScoreInfo : userBestScores) {
				if (bestScoreInfo.startsWith(username + ":")) {
					int existingScore = Integer.parseInt(bestScoreInfo.split(": ")[1]);
					if (score > existingScore) {
						userBestScores.set(userBestScores.indexOf(bestScoreInfo), scoreInfo);
					}
					found = true;
					break;
				}
			}

			if (!found) {
				userBestScores.add(scoreInfo);
			}
		}

		StringBuilder result = new StringBuilder("Sokoban Game Best Scores:\n");
		for (String bestScore : userBestScores) {
			result.append(bestScore).append("\n");
		}
		return result.append("\nPress N to continue").toString();
	}
}
