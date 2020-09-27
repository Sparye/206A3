import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Score {
    public static Integer readAndWriteScore(String selectedPoint) throws FileNotFoundException {
    	File file =new File("./GameData/Game/.Score");
    	Scanner scan = new Scanner(file);
    	int currentWinning = Integer.parseInt(scan.nextLine());


    	currentWinning = currentWinning + Integer.parseInt(selectedPoint);


    	scan.close();
    	try {
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(currentWinning);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return currentWinning;

    }
}
