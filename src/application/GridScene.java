package application;



import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import data.Attempt;
import data.Score;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.QuestionSelector;

public class GridScene{
	Button restartButton,menuButton,gameLockInButton,hearGameButton,dontKnowButton;

	Stage guiStage;

	public GridScene(Button inRestartButton,Button inMenuButton,Button inGameLockInButton,Button inHearGameButton,Button inDontKnowButton,Stage inGuiStage) {
		restartButton = inRestartButton;
		menuButton = inMenuButton;
		gameLockInButton = inGameLockInButton;
		hearGameButton = inHearGameButton;
		dontKnowButton = inDontKnowButton;
		guiStage = inGuiStage;
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

	
	Group gameRoot = new Group();
	Scene gameScene = new Scene( gameRoot );

	StackPane gameBackground = new StackPane();

	Canvas gameCanvas = new Canvas( width, height );
	GridPane gameGrid = new GridPane();
	// create new question set when game is reset
	/**
	 * 
	 * 	
	 * 
	 */



	public Scene getGridScene() {
		gameBackground.setStyle( backgroundStyle );

		GridPane gameGrid = new GridPane();
		// create new question set when game is reset
		if(QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).size() < 1) {
			QuestionSelector.copyRandomCategories(QUESTIONBANKFILE, GAMEQUESTIONSFILE);
		}

		gameGrid.setVgap(10);
		gameGrid.setHgap(30);		

		int checktotal=0;
		for(int c=0;c<QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).size();c++) {
			checktotal += QuestionSelector.getQuestionsRemainingCount(GAMEQUESTIONSFILE).get(c);
		}

		if(checktotal==0) {
			// display the question
			Text WinningPrompt=new Text();;
			try {
				WinningPrompt.setText( "You have completed all questions!\nYour final score is: "+ Score.getSumAndSave("0") );
			} catch (FileNotFoundException ev) {
				ev.printStackTrace();
			}
			WinningPrompt.setWrappingWidth( 800 );
			WinningPrompt.setStyle("-fx-font-size: 2.5em; ");
			WinningPrompt.setTextAlignment(TextAlignment.CENTER);
			WinningPrompt.setLayoutY(buttonYStart);
			WinningPrompt.setLayoutX(200);


			gameGrid.setLayoutX( buttonXPos -250);
			gameGrid.setLayoutY(buttonYStart);
			gameBackground.getChildren().add( gameCanvas );
			gameRoot.getChildren().add( gameBackground );
			gameRoot.getChildren().add(menuButton);
			gameRoot.getChildren().add(restartButton);
			gameRoot.getChildren().add(WinningPrompt);
			//	gameRoot.getChildren().add(gameGrid);

			guiStage.setScene( gameScene );
		}else {
			//buttons for money grid
			for(int j=0;j<QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).size();j++) {
				//Labels for displaying categories in game module
				Text catLabel = new Text(QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(j));
				catLabel.setStyle("-fx-font-size: 1.25em; -fx-font-weight: bold");
				catLabel.setWrappingWidth(125);
				catLabel.setTextAlignment(TextAlignment.CENTER);
				GridPane.setConstraints(catLabel,j,0);
				gameGrid.getChildren().add(catLabel);
				int numQues = QuestionSelector.getQuestionsRemainingCount(GAMEQUESTIONSFILE).get(j);
				for(int r=1;r<6;r++) {
					Button moneyButton = new Button(r+"00");
					moneyButton.setId(Integer.toString(j*10+r));
					moneyButton.setPrefSize(buttonXScale/2, buttonYScale/2);
					moneyButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.75em; -fx-text-fill: white; -fx-font-weight: bold");
					moneyButton.setOnAction(eve->{
						dontKnowButton.setText("Don't know");
						//	System.out.println(QuestionSelector.getQuestionSetFromValue(moneyButton.getText(), QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10), GAMEQUESTIONSFILE)[0]);
						gameQuestionSet=QuestionSelector.getQuestionSetFromValue(moneyButton.getText(), QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10), GAMEQUESTIONSFILE);	
						QuestionSelector.deleteLinesContaining(gameQuestionSet[0], GAMEQUESTIONSFILE);
						//TextToSpeech.say(gameQuestionSet[0]);
						QuestionScene qs = new QuestionScene(restartButton, menuButton, gameLockInButton, hearGameButton, dontKnowButton, guiStage, gameQuestionSet, moneyButton);
						guiStage.setScene(qs.getQuestionScene());
				/**		TimedQuestion sayQuestion = new TimedQuestion(gameQuestionSet[0]);
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

						guiStage.setScene(gameQuestionScene);**/

					});
					if((numQues+r)==6) {
						GridPane.setConstraints(moneyButton,j,r);
						gameGrid.getChildren().add(moneyButton);
					}else {
						moneyButton.setDisable(true);
						GridPane.setConstraints(moneyButton,j,r);
						gameGrid.getChildren().add(moneyButton);
					}

				}

			}

			gameGrid.setLayoutX( buttonXPos -250);
			gameGrid.setLayoutY(buttonYStart);
			gameBackground.getChildren().add( gameCanvas );
			gameRoot.getChildren().add( gameBackground );
			gameRoot.getChildren().add(menuButton);
			gameRoot.getChildren().add(gameGrid);


		}
		return gameScene;
	}
}

