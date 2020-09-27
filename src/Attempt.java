import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Attempt {
	public static boolean isCorrect (String userAttempt, String[] questionSet) {
		
		// A "/" indicates multiple valid answers
		String[] answerSet = questionSet[2].split("/", 0);
		for (int i = 0; i<answerSet.length; i++) {
			if (userAttempt.equalsIgnoreCase(questionSet[1]+ " " + answerSet[i].strip())
				|| userAttempt.equalsIgnoreCase(answerSet[i].strip())	) {
				return true;
			}
		}
		return false;
	}
	
	public static void save (int attemptsRemaining, String PRACTICEATTEMPTFILE) {
		try {
			PrintWriter saveAttempts = new PrintWriter(new FileWriter(PRACTICEATTEMPTFILE));
			saveAttempts.println(attemptsRemaining + "");
			saveAttempts.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}