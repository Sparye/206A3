package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Timer implements Runnable {
	public static final String TIMERLENGTHFILE = "GameData/Setting/Timer";
	private static Integer timerLength = 15;
	public static Integer tempLength = 15;
	private Timeline timeline;
	public static Label timerLabel = new Label();
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(timerLength);
	
	public Timer() {
		
	}
	
	@Override
	public void run() {
		timerLabel.textProperty().bind(timeSeconds.asString());
		timeSeconds.set(timerLength);
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(timerLength + 1),
						new KeyValue(timeSeconds,0)));
		timerLabel.setVisible(true);
		timeline.playFromStart();
	}
	
	public static void setup() {
		// timer label format
		Timer.timerLabel.setLayoutX(900);
		Timer.timerLabel.setLayoutY(Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 + 40);
		Timer.timerLabel.setStyle("-fx-font-size: 10em");
		try {
			BufferedReader getLength = new BufferedReader(new FileReader(TIMERLENGTHFILE));
			String lengthLine = getLength.readLine();
			getLength.close();
			if (lengthLine != null) {
				timerLength = Integer.parseInt( lengthLine );
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static int getLength() {
		return timerLength;
	}
	
	public static void save() {
		timerLength = tempLength;
		try {
			PrintWriter saveLength = new PrintWriter(new FileWriter(TIMERLENGTHFILE));
			saveLength.println(timerLength + "");
			saveLength.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
