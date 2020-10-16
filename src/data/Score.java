package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Score {
    public static Integer getSumAndSave(String selectedPoint) {
    	int currentWinning = Integer.parseInt(selectedPoint);
    	File file =new File("./GameData/Game/.Score");
    	Scanner scan;
		try {
			scan = new Scanner(file);
			currentWinning = Integer.parseInt(scan.nextLine());
    		currentWinning = currentWinning + Integer.parseInt(selectedPoint);
    		scan.close();
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(currentWinning);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return currentWinning;

    }
    
    public static void reset(String GAMESCOREFILE) {
    	PrintWriter resetScore;
		try {
			resetScore = new PrintWriter(new FileWriter(GAMESCOREFILE));
			resetScore.println("0");
			resetScore.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
