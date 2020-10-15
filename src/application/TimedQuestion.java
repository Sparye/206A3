package application;

import javafx.application.Platform;

public class TimedQuestion extends Thread {
	
	private String question;
	public TimedQuestion(String question) {
		this.question = question;
	}

	@Override
	public void run() {
		try {
			Timer.timerLabel.setVisible(false);
			Process questionReadout = TextToSpeech.say(question);
			questionReadout.waitFor();
			
			Timer startTimer = new Timer();
			Platform.runLater(startTimer);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
