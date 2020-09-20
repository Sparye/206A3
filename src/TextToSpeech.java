import java.io.File;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextToSpeech {
	public static void toSpeech(String msg)  {
		File file = new File("./GameData/Setting/TTS");
		String speed="100";
		Scanner scan;
		try {
			scan = new Scanner(file);
			speed = scan.nextLine();
			System.out.println(speed);
			scan.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block

			e1.printStackTrace();
		}
		
		try {
			String refinedMsg = "\"" + msg + "\"";
		//	String command = "espeak "+refinedMsg+" -s 100";
		//String command = "espeak "+msg;
			String command = "espeak "+refinedMsg+" -s "+speed;

		//	System.out.println(command);
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			
			Process process = pb.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void toTestSpeech(String speed)  {
		String msg = "This is the speed of speaking";
		try {
			String refinedMsg = "\"" + msg + "\"";
		//	String command = "espeak "+refinedMsg+" -s 100";
		//String command = "espeak "+msg;
			String command = "espeak "+refinedMsg+" -s "+speed;

		//	System.out.println(command);
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			
			Process process = pb.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
