package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class TextToSpeech {
	
	public static final String TTSSPEEDFILE = "GameData/Setting/TTS";
	private static int ttsSpeed = 160;
	public static int testSpeed = 160;
	private static Process previous = null;
	
	public static Process say(String msg)  {
		if (previous != null) {
			previous.destroy();
		}
		Process process = null;
		try {
			msg = msg.replace("\'", "");
			String refinedMsg = "\"" + msg + "\"";

			double duration = 160.0/ttsSpeed;
			String command = "echo $'(Parameter.set `Duration_Stretch "
			+ duration + ")\n(SayText " + refinedMsg + ")' |  festival --pipe";
			
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			
			process = pb.start();
			previous = process;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return process;
	}
	
	public static void test()  {
		ArrayList<String> testPrompts = new ArrayList<String>();
		testPrompts.add("Testing! one two three!");
		testPrompts.add("I am a robot! beep boop!");
		testPrompts.add("Do you like this speed?");
		if (testSpeed > 240) {
			testPrompts.add("Fast and Furious!");
			testPrompts.add("You like it quick!");
		}
		else if (testSpeed < 110) {
			testPrompts.add("Slow mode");
			testPrompts.add("Talking very slowly");
		}
		Collections.shuffle(testPrompts);
		
		String msg = testPrompts.get(0);
		int temp = ttsSpeed;
		ttsSpeed = testSpeed;
		TextToSpeech.say(msg);
		ttsSpeed = temp;
	}
	
	public static void correct() {
		ArrayList<String> correctPrompts = new ArrayList<String>();
		correctPrompts.add("Nice one!");
		correctPrompts.add("Awesome!");
		correctPrompts.add("Correct!");
		correctPrompts.add("Well done!");
		correctPrompts.add("Right!");
		correctPrompts.add("Wow!");
		correctPrompts.add("Very Clever!");
		Collections.shuffle(correctPrompts);
		
		String msg = correctPrompts.get(0);
		TextToSpeech.say(msg);
	}
	
	public static void incorrect() {
		ArrayList<String> incorrectPrompts = new ArrayList<String>();
		incorrectPrompts.add("Objection!");
		incorrectPrompts.add("Incorrect!");
		incorrectPrompts.add("Not quite right!");
		incorrectPrompts.add("Needs more practice!");
		incorrectPrompts.add("Sorry, Wrong answer!");
		incorrectPrompts.add("Almost!");
		Collections.shuffle(incorrectPrompts);
		
		String msg = incorrectPrompts.get(0);
		TextToSpeech.say(msg);
	}
		
	
	public static void save() {
		ttsSpeed = testSpeed;
		try {
			PrintWriter saveSpeed = new PrintWriter(new FileWriter(TTSSPEEDFILE));
			saveSpeed.println(ttsSpeed + "");
			saveSpeed.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static int getSpeed() {
		return ttsSpeed;
	}
	
	public static void setup() {
		try {
			BufferedReader getSpeed = new BufferedReader(new FileReader(TTSSPEEDFILE));
			String speedLine = getSpeed.readLine();
			getSpeed.close();
			if (speedLine != null) {
				ttsSpeed = Integer.parseInt( speedLine );
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}


