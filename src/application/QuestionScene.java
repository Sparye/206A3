package application;

import java.io.FileNotFoundException;

import data.Attempt;
import data.Score;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.QuestionSelector;

public class QuestionScene {
	Button restartButton,menuButton,gameLockInButton,hearGameButton,dontKnowButton,moneyButton;

	Stage guiStage;
	
	
	public QuestionScene(Button inRestartButton,Button inMenuButton,Button inGameLockInButton,Button inHearGameButton,Button inDontKnowButton,Stage inGuiStage,String[] inQuestionSet,Button inMoneyButton) {
		restartButton = inRestartButton;
		menuButton = inMenuButton;
		gameLockInButton = inGameLockInButton;
		hearGameButton = inHearGameButton;
		dontKnowButton = inDontKnowButton;
		guiStage = inGuiStage;
		gameQuestionSet = inQuestionSet;
		moneyButton = inMoneyButton;
	}
	//carried over default values
		String backgroundStyle = "-fx-background-color: #e4bbde; ";


		Font titleFont = Font.font("Arial", FontWeight.BOLD, 72);
		// GUI window size
		int width = 1200;
		int height = 800;



		// file names to use
		public static final String QUESTIONBANKFILE = "categories";
		public static final String PRACTICEATTEMPTFILE = "GameData/Practice/Attempt";
		public static final String PRACTICEQUESTIONFILE = "GameData/Practice/Question";
		public static final String GAMEQUESTIONSFILE = "GameData/Game/Questions";
		public static final String GAMESCOREFILE = "GameData/Game/.Score";
		public static final String TTSSPEEDFILE = "GameData/Setting/TTS";

		// button settings
		double buttonXPos = 480;
		double buttonYStart = 160;
		double buttonYOffset = 150;
		double buttonXScale = 250;
		double buttonYScale = 100;
		String buttonStyle = "-fx-background-color: #d0e7ff; -fx-font-size: 1.75em; ";

		// default data
		public static final int DEFAULTSPEED = 160;
		int ttsSpeed = DEFAULTSPEED;
		int testSpeed = ttsSpeed;
		String practiceCategory = "";
		int attemptsRemaining = 0; 
		String[] practiceQuestionSet = {"Don't edit the game files! :(","","sorry"};
		String[] gameQuestionSet = {"Don't edit the game files! :(","","sorry"};
		int currentScore=0;
		
		public Scene getQuestionScene() {
			TimedQuestion sayQuestion = new TimedQuestion(gameQuestionSet[0]);
			sayQuestion.start();
			try {
				currentScore=Score.getSumAndSave("0");
			} catch (FileNotFoundException ev) {
				ev.printStackTrace();
			}
			//game question scene
			Group gameQuestionRoot = new Group();
			Scene gameQuestionScene = new Scene(gameQuestionRoot);

			StackPane gameQuestionBackground = new StackPane();
			Canvas gameQuestionCanvas = new Canvas( width, height );
			gameQuestionBackground.setStyle( backgroundStyle );

			// game module title
			GraphicsContext categoryTitlePrompt = gameQuestionCanvas.getGraphicsContext2D();
			categoryTitlePrompt.setFill( Color.PURPLE );
			categoryTitlePrompt.setStroke( Color.BLACK );
			categoryTitlePrompt.setLineWidth(2);
			categoryTitlePrompt.setFont( titleFont );
			categoryTitlePrompt.fillText( "Game Module", 330, 100 );
			categoryTitlePrompt.strokeText( "Game Module", 330, 100 );

			// display the question
			Text gameQuestionPrompt = new Text( QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10) );
			gameQuestionPrompt.setWrappingWidth( 800 );
			gameQuestionPrompt.setStyle("-fx-font-size: 2.5em; ");
			gameQuestionPrompt.setTextAlignment(TextAlignment.CENTER);
			gameQuestionPrompt.setLayoutY(buttonYStart);
			gameQuestionPrompt.setLayoutX(200);

			// user answer field
			TextField answerField = new TextField(gameQuestionSet[1]+ " ");
			answerField.setLayoutX( buttonXPos - 75 ) ;
			answerField.setLayoutY( buttonYStart + buttonYOffset );
			answerField.setPrefSize( 400 , 50 );
			answerField.setStyle( buttonStyle );

			//display current score
			Text displayScore = new Text("Score:\n"+currentScore);
			displayScore.setLayoutX( 50 );
			displayScore.setLayoutY( buttonYStart + buttonYOffset * 4 );
			displayScore.setTextAlignment(TextAlignment.CENTER);
			displayScore.setStyle("-fx-background-color: #d0e7ff; -fx-font-size: 1.75em; ");
			
			answerField.textProperty().addListener(
					(ObservableValue<? extends String> ov, String old_val, String new_val) -> {
						gameLockInButton.setVisible(true);
					}
					);
			
			ChangeListener<String> changeListener = new ChangeListener<>() {
				@Override
				public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
					if (new_val.equals("0")) {
						Timer.timerLabel.textProperty().removeListener(this);
						gameQuestionRoot.getChildren().remove(Timer.timerLabel);
						displayScore.setText("Score:\n"+currentScore);
						gameQuestionRoot.getChildren().remove(answerField);
						gameQuestionRoot.getChildren().remove(gameLockInButton);
						gameQuestionRoot.getChildren().remove(hearGameButton);
						dontKnowButton.setText("Question Grid");
						String answer="\n\nUnfortunately, the correct answer is "+gameQuestionSet[2]+".";
						gameQuestionPrompt.setText(answer);
						TextToSpeech.say("Sorry, No more time!");
					}
				}
			};
			
			Timer.timerLabel.textProperty().addListener(changeListener);

			gameLockInButton.setOnAction(ev -> {
				gameQuestionRoot.getChildren().remove(Timer.timerLabel);
				if(Attempt.isCorrect(answerField.getText(), gameQuestionSet)) {

					try {
						currentScore=Score.getSumAndSave(moneyButton.getText());
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}

					displayScore.setText("Score:\n"+currentScore);
					gameQuestionRoot.getChildren().remove(answerField);
					gameQuestionRoot.getChildren().remove(gameLockInButton);
					gameQuestionRoot.getChildren().remove(hearGameButton);
					dontKnowButton.setText("Question Grid");
					String answer="\n\nCorrect Answer!\nCongratulations! You gained "+moneyButton.getText()+" points!";
					gameQuestionPrompt.setText(answer);
					TextToSpeech.correct();
				}else {
					displayScore.setText("Score:\n"+currentScore);
					gameQuestionRoot.getChildren().remove(answerField);
					gameQuestionRoot.getChildren().remove(gameLockInButton);
					gameQuestionRoot.getChildren().remove(hearGameButton);
					dontKnowButton.setText("Question Grid");
					String answer="\n\nUnfortunately, the correct answer is "+gameQuestionSet[2]+".";
					gameQuestionPrompt.setText(answer);
					TextToSpeech.incorrect();
				}
			});

			gameQuestionBackground.getChildren().add( gameQuestionCanvas );
			gameQuestionRoot.getChildren().add( gameQuestionBackground );
			gameQuestionRoot.getChildren().add( gameQuestionPrompt );
			gameQuestionRoot.getChildren().add( answerField );
			gameQuestionRoot.getChildren().add( gameLockInButton );
			gameQuestionRoot.getChildren().add( displayScore );
			gameQuestionRoot.getChildren().add( hearGameButton );
			gameQuestionRoot.getChildren().add( dontKnowButton );
			gameQuestionRoot.getChildren().add( Timer.timerLabel );

			//guiStage.setScene(gameQuestionScene);
			return gameQuestionScene;
			
		}
		
		
}
