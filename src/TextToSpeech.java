import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextToSpeech {
	public static void toSpeech(String msg)  {
		File file = new File("./GameData/Setting/TTS");
		String speed="160";
		Scanner scan;
		try {
			scan = new Scanner(file);
			speed = scan.nextLine();
			//System.out.println(speed);
			scan.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block

			e1.printStackTrace();
		}
		
		try {
			String refinedMsg = "\"" + msg + "\"";
		//	String command = "espeak "+refinedMsg+" -s 100";
		//String command = "espeak "+msg;
		//	String command = "espeak "+refinedMsg+" -s "+speed;

		//	System.out.println(command);
			
			double duration = 160.0/(Integer.parseInt(speed));
			String command = "echo $'(Parameter.set `Duration_Stretch " + duration
					+ ")\n(SayText " + refinedMsg + ")' |  festival --pipe";
			
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			
			pb.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void toTestSpeech(int speed)  {
		String msg = "This is the speed of speaking";
		try {
			String refinedMsg = "\"" + msg + "\"";
			
			//	String command = "espeak "+refinedMsg+" -s 100";
			//String command = "espeak "+msg;
			//String command = "espeak "+refinedMsg;
			//System.out.println(command);
			
			double duration = 160.0/speed;
			String command = "echo $'(Parameter.set `Duration_Stretch " + duration
					+ ")\n(SayText " + refinedMsg + ")' |  festival --pipe";

			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			
			pb.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void save(int speed, String TTSSPEEDFILE) {
		try {
			PrintWriter saveSpeed = new PrintWriter(new FileWriter(TTSSPEEDFILE));
			saveSpeed.println(speed + "");
			saveSpeed.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
