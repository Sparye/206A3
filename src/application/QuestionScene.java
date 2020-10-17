package application;

import data.Attempt;
import data.Score;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.QuestionSelector;
import questions.TextToSpeech;
import questions.TimedQuestion;
import questions.Timer;

public class QuestionScene {
	Button restartButton,menuButton,gameLockInButton,hearGameButton,dontKnowButton,moneyButton;

	Stage guiStage;
	
	public static String[] gameQuestionSet = {"Do not edit the game files!","","sorry"};
	
	public QuestionScene(Button inRestartButton,Button inMenuButton,Button inGameLockInButton,Button inHearGameButton,Stage inGuiStage,String[] inQuestionSet,Button inMoneyButton) {
		restartButton = inRestartButton;
		menuButton = inMenuButton;
		gameLockInButton = inGameLockInButton;
		hearGameButton = inHearGameButton;
		guiStage = inGuiStage;
		gameQuestionSet = inQuestionSet;
		moneyButton = inMoneyButton;
	}
		// file names to use
		public static final String QUESTIONBANKFILE = "categories";
		public static final String GAMEQUESTIONSFILE = "GameData/Game/Questions";
		public static final String GAMESCOREFILE = "GameData/Game/.Score";

		// default data
		int currentScore=0;
		
		public Scene getQuestionScene() {
			TimedQuestion sayQuestion = new TimedQuestion(gameQuestionSet[0]);
			sayQuestion.start();
			currentScore=Score.getSumAndSave("0");
			//game question scene
			Group gameQuestionRoot = new Group();
			Scene gameQuestionScene = new Scene(gameQuestionRoot);

			StackPane gameQuestionBackground = new StackPane();
			Canvas gameQuestionCanvas = new Canvas( Quinzical.width, Quinzical.height );
			gameQuestionBackground.setStyle( Quinzical.backgroundStyle );

			// game module title
			GraphicsContext categoryTitlePrompt = gameQuestionCanvas.getGraphicsContext2D();
			categoryTitlePrompt.setFill( Color.PURPLE );
			categoryTitlePrompt.setStroke( Color.BLACK );
			categoryTitlePrompt.setLineWidth(2);
			categoryTitlePrompt.setFont( Quinzical.titleFont );
			categoryTitlePrompt.fillText( "Game Module", 330, 100 );
			categoryTitlePrompt.strokeText( "Game Module", 330, 100 );

			// display the question
			Text gameQuestionPrompt = new Text( QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10) );
			gameQuestionPrompt.setWrappingWidth( 800 );
			gameQuestionPrompt.setStyle("-fx-font-size: 2.5em; ");
			gameQuestionPrompt.setTextAlignment(TextAlignment.CENTER);
			gameQuestionPrompt.setLayoutY(Quinzical.buttonYStart);
			gameQuestionPrompt.setLayoutX(200);

			// user answer field
			TextField answerField = new TextField(gameQuestionSet[1]+ " ");
			answerField.setLayoutX( Quinzical.buttonXPos - 75 ) ;
			answerField.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset );
			answerField.setPrefSize( 400 , 50 );
			answerField.setStyle( Quinzical.buttonStyle );

			//display current score
			Label displayScore = new Label(" "+currentScore);
			displayScore.setLayoutX( 77 );
			displayScore.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 + 170 );
			displayScore.setTextAlignment(TextAlignment.CENTER);
			displayScore.setStyle("-fx-font-size: 5em; ");
			
			Label scoreHeading = new Label("SCORE");
			scoreHeading.setLayoutX( 77 );
			scoreHeading.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 + 150 );
			scoreHeading.setTextAlignment(TextAlignment.CENTER);
			scoreHeading.setStyle("-fx-font-size: 2em; ");
			
			answerField.textProperty().addListener(
					(ObservableValue<? extends String> ov, String old_val, String new_val) -> {
						gameLockInButton.setVisible(true);
					}
					);
			
			ChangeListener<String> changeListener = new ChangeListener<>() {
				@Override
				public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
					if (new_val.equals("0")) {
						Timer.timerLabel.textProperty().unbind();
						Timer.timerLabel.setText("");
						Timer.timerLabel.textProperty().removeListener(this);
						gameQuestionRoot.getChildren().remove(Timer.timerLabel);
						displayScore.setText(" "+currentScore);
						gameQuestionRoot.getChildren().remove(answerField);
						gameQuestionRoot.getChildren().remove(gameLockInButton);
						gameQuestionRoot.getChildren().remove(hearGameButton);
						dontKnowButton.setText("Question Grid");
						String answer="\n\nUnfortunately, the correct answer is "+gameQuestionSet[2]+".";
						gameQuestionPrompt.setText(answer);
						TextToSpeech.outOfTime();
					}
				}
			};
			
			Timer.timerLabel.textProperty().addListener(changeListener);
			dontKnowButton = new Button("Don't know");
			dontKnowButton.setLayoutX( Quinzical.buttonXPos );
			dontKnowButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 3 );
			dontKnowButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
			dontKnowButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #B43757; -fx-font-size: 1.75em; ");
			dontKnowButton.setOnAction(e-> {
				Timer.timerLabel.textProperty().unbind();
				Timer.timerLabel.setText("");
				Timer.timerLabel.textProperty().removeListener(changeListener);
				gameQuestionRoot.getChildren().remove(Timer.timerLabel);
				GridScene gs = new GridScene(restartButton, menuButton, gameLockInButton,
						hearGameButton, guiStage);
				guiStage.setScene(gs.getGridScene());});

			gameLockInButton.setOnAction(ev -> {
				Timer.timerLabel.textProperty().unbind();
				Timer.timerLabel.setText("");
				Timer.timerLabel.textProperty().removeListener(changeListener);
				gameQuestionRoot.getChildren().remove(Timer.timerLabel);
				
				if(Attempt.isCorrect(answerField.getText(), gameQuestionSet)) {
					currentScore=Score.getSumAndSave(moneyButton.getText());
					
					displayScore.setText(" "+currentScore);
					gameQuestionRoot.getChildren().remove(answerField);
					gameQuestionRoot.getChildren().remove(gameLockInButton);
					gameQuestionRoot.getChildren().remove(hearGameButton);
					dontKnowButton.setText("Question Grid");
					String answer="\n\nCorrect Answer!\nCongratulations! You gained "+moneyButton.getText()+" points!";
					gameQuestionPrompt.setText(answer);
					TextToSpeech.correct();
				}else {
					displayScore.setText(" "+currentScore);
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
			gameQuestionRoot.getChildren().add( scoreHeading );
			gameQuestionRoot.getChildren().add( hearGameButton );
			gameQuestionRoot.getChildren().add( dontKnowButton );
			gameQuestionRoot.getChildren().add( Timer.timerLabel );

			//guiStage.setScene(gameQuestionScene);
			return gameQuestionScene;
			
		}
		
		
}
