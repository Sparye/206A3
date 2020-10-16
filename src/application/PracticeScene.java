package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import data.Attempt;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.LineToVar;

public class PracticeScene {
	Button practiceConfirmButton,practiceReturnButton,menuButton,practiceLockInButton,hearButton;

	Stage guiStage;
	
	public PracticeScene(Button inPracticeConfirmButton,Button inPracticeReturnButton, Button inMenuButton,Button inPracticeLockInButton,Button inHearButton,Stage inGuiStage) {
		practiceConfirmButton=inPracticeConfirmButton;
		practiceReturnButton=inPracticeReturnButton;
		menuButton=inMenuButton;
		practiceLockInButton=inPracticeLockInButton;
		hearButton=inHearButton;
		guiStage=inGuiStage;
		
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
	
	
	Group practiceRoot = new Group();
	Scene practiceScene = new Scene( practiceRoot );

	StackPane practiceBackground = new StackPane();
	Canvas practiceCanvas = new Canvas( width, height );
	
	
	
	
	public Scene getPracticeScene() {
		try {
			// get practice attempts
			BufferedReader getAttempts = new BufferedReader(new FileReader(PRACTICEATTEMPTFILE));
			String attemptLine = getAttempts.readLine();
			if (attemptLine == null) {
				Attempt.save(attemptsRemaining, PRACTICEATTEMPTFILE);
			} else {
				attemptsRemaining = Integer.parseInt( attemptLine );
			}
			getAttempts.close();
			if (attemptsRemaining > 0) {
				// get practice question
				BufferedReader getPracticeQuestion = new BufferedReader(new FileReader(PRACTICEQUESTIONFILE));
				String practiceQuestionLine = getPracticeQuestion.readLine();
				getPracticeQuestion.close();
				if (practiceQuestionLine != null) {
					String practiceQuestionWhole = practiceQuestionLine;
					practiceQuestionSet = LineToVar.toVarSet(practiceQuestionWhole);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		practiceBackground.setStyle( backgroundStyle );

		practiceBackground.getChildren().add( practiceCanvas );
		practiceRoot.getChildren().add( practiceBackground );
		practiceRoot.getChildren().add(menuButton);

		GraphicsContext selectCategoryPrompt = practiceCanvas.getGraphicsContext2D();
		// select category prompt
		selectCategoryPrompt.setFill( Color.PURPLE );
		selectCategoryPrompt.setStroke( Color.BLACK );
		selectCategoryPrompt.setLineWidth(2);
		selectCategoryPrompt.setFont( titleFont );
		selectCategoryPrompt.fillText( "Select Practice Category", 100, 100 );
		selectCategoryPrompt.strokeText( "Select Practice Category", 100, 100 );

		// make buttons invisible
		practiceConfirmButton.setVisible(false);
		practiceReturnButton.setVisible(false);

		// drop-down menu to choose category
		ChoiceBox<String> categoryDropDown = new ChoiceBox<String>();
		categoryDropDown.setLayoutX(buttonXPos - 30);
		categoryDropDown.setLayoutY(buttonYStart);
		categoryDropDown.setPrefSize(buttonXScale + 60, buttonYScale);
		categoryDropDown.setStyle( buttonStyle );

		try {
			String line;
			// Add category names to drop-down
			BufferedReader readCategories = new BufferedReader(new FileReader(QUESTIONBANKFILE));
			while((line = readCategories.readLine()) != null) {
				if (line.indexOf('(') == -1 ) {
					if (!line.isBlank()) {
						categoryDropDown.getItems().add(line);
					}
				}
			}
			readCategories.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		practiceRoot.getChildren().add(practiceConfirmButton);
		practiceRoot.getChildren().add(practiceReturnButton);
		practiceRoot.getChildren().add( categoryDropDown );
		// confirmation button appears when category selected
		categoryDropDown.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					practiceCategory = new_val;
					practiceConfirmButton.setVisible(true);
				});

		// another button appears if the user had an in-progress question from previous session
		if (attemptsRemaining > 0) {
			practiceReturnButton.setVisible(true);
		}

	//	guiStage.setScene( practiceScene );

		// practice question scene
		Group practiceQuestionRoot = new Group();
		Scene practiceQuestionScene = new Scene( practiceQuestionRoot );

		StackPane practiceQuestionBackground = new StackPane();
		Canvas practiceQuestionCanvas = new Canvas( width, height );
		practiceQuestionBackground.setStyle( backgroundStyle );

		// practice module title
		GraphicsContext categoryTitlePrompt = practiceQuestionCanvas.getGraphicsContext2D();
		categoryTitlePrompt.setFill( Color.PURPLE );
		categoryTitlePrompt.setStroke( Color.BLACK );
		categoryTitlePrompt.setLineWidth(2);
		categoryTitlePrompt.setFont( titleFont );
		categoryTitlePrompt.fillText( "Practice Module", 270, 100 );
		categoryTitlePrompt.strokeText( "Practice Module", 270, 100 );

		// displays the question
		Text practiceQuestionPrompt = new Text( practiceQuestionSet[0] );
		practiceQuestionPrompt.setWrappingWidth( 800 );
		practiceQuestionPrompt.setStyle("-fx-font-size: 2.5em; ");
		practiceQuestionPrompt.setTextAlignment(TextAlignment.CENTER);
		practiceQuestionPrompt.setLayoutY(buttonYStart);
		practiceQuestionPrompt.setLayoutX(200);

		// user answer field
		TextField answerField = new TextField("Type Response Here!");
		answerField.setLayoutX( buttonXPos - 75 ) ;
		answerField.setLayoutY( buttonYStart + buttonYOffset );
		answerField.setPrefSize( 400 , 50 );
		answerField.setStyle( buttonStyle );

		if (attemptsRemaining == 1) {
			// make field text red and display the first letter
			answerField.setStyle( buttonStyle + " -fx-text-fill: #B43757;");
			String firstLetter = "c"; // Placeholder
			answerField.setText(practiceQuestionSet[1]+ " " + firstLetter.toUpperCase());
		} 

		// display remaining attempts
		Text displayAttempts = new Text(attemptsRemaining + "\nAttempts Remaining");
		displayAttempts.setLayoutX( 50 ) ;
		displayAttempts.setLayoutY( buttonYStart + buttonYOffset * 4 );
		displayAttempts.setTextAlignment(TextAlignment.CENTER);
		displayAttempts.setStyle("-fx-background-color: #d0e7ff; -fx-font-size: 1.75em; ");

		// make lock in button appear when answer field text is changed
		answerField.textProperty().addListener(
				(ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					practiceLockInButton.setVisible(true);
				});


		practiceQuestionBackground.getChildren().add( practiceQuestionCanvas );
		practiceQuestionRoot.getChildren().add( practiceQuestionBackground );
		practiceQuestionRoot.getChildren().add( practiceQuestionPrompt );
		practiceQuestionRoot.getChildren().add( answerField );
		practiceQuestionRoot.getChildren().add( practiceLockInButton ); 
		practiceQuestionRoot.getChildren().add( displayAttempts ); 
		practiceQuestionRoot.getChildren().add( hearButton );

		practiceLockInButton.setVisible(false);

		practiceConfirmButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				// make an array of questions
				ArrayList<String> questionArray = new ArrayList<String>();
				try {
					String line;
					BufferedReader readQuestions = new BufferedReader(new FileReader(QUESTIONBANKFILE));
					while((line = readQuestions.readLine()) != null) {
						// get to category line
						if (line.equals(practiceCategory)) {
							break;
						}
					}
					while(((line = readQuestions.readLine()) != null) && (line.indexOf('(') >= 0 )) {
						questionArray.add( line );
					}
					readQuestions.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// choose one question randomly
				Collections.shuffle(questionArray);
				Random chooseRandomQuestion = new Random();
				String practiceQuestionWhole = questionArray.get(chooseRandomQuestion.nextInt(questionArray.size()));
				practiceQuestionSet = LineToVar.toVarSet(practiceQuestionWhole);
				answerField.setText(practiceQuestionSet[1]+ " ");
				TextToSpeech.say(practiceQuestionSet[0]);
				
				// reset attempts
				attemptsRemaining = 3;
				try {
					PrintWriter resetAttempts = new PrintWriter(new FileWriter(PRACTICEATTEMPTFILE));
					resetAttempts.println(attemptsRemaining + "");
					resetAttempts.close();
					// save new question
					PrintWriter saveQuestion = new PrintWriter(new FileWriter(PRACTICEQUESTIONFILE));
					saveQuestion.println(practiceQuestionWhole);
					saveQuestion.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				displayAttempts.setText(attemptsRemaining + "\nAttempts Remaining");
				practiceQuestionPrompt.setText( practiceQuestionSet[0] );

				practiceQuestionRoot.getChildren().add(menuButton);
				guiStage.setScene( practiceQuestionScene );

			}
		});

		practiceReturnButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				practiceQuestionRoot.getChildren().add(menuButton);
				answerField.setText(practiceQuestionSet[1]+ " ");
				TextToSpeech.say(practiceQuestionSet[0]);
				guiStage.setScene( practiceQuestionScene );
				if (attemptsRemaining == 1) {
					String firstLetter = "" + practiceQuestionSet[2].charAt(0);
					answerField.setText(practiceQuestionSet[1]+ " " + firstLetter.toUpperCase());
				}
			}
		});

		practiceLockInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				if (Attempt.isCorrect(answerField.getText(), practiceQuestionSet)) {
					// User answered correctly
					practiceQuestionRoot.getChildren().remove(answerField);
					practiceQuestionRoot.getChildren().remove(practiceLockInButton);
					practiceQuestionRoot.getChildren().remove(displayAttempts);
					practiceQuestionRoot.getChildren().remove(hearButton);

					String answer = "\n\nCorrect Answer!!\n\nYou Answered:\n" + practiceQuestionSet[1] + " " + practiceQuestionSet[2];
					practiceQuestionPrompt.setText(practiceQuestionSet[0] + answer);
					TextToSpeech.correct();
					attemptsRemaining = 0;
				} else {
					// User answered incorrectly
					attemptsRemaining--;
					if (attemptsRemaining == 0) {
						practiceQuestionRoot.getChildren().remove(answerField);
						practiceQuestionRoot.getChildren().remove(practiceLockInButton);
						practiceQuestionRoot.getChildren().remove(displayAttempts);
						practiceQuestionRoot.getChildren().remove(hearButton);

						String answer = "\n\nNo more attempts!\n\nAnswer:\n" + practiceQuestionSet[1] + " " + practiceQuestionSet[2];
						practiceQuestionPrompt.setText(practiceQuestionSet[0] + answer);
						TextToSpeech.say("Sorry! The answer was " + practiceQuestionSet[1]+ " " + practiceQuestionSet[2]);

					} else {
						TextToSpeech.incorrect();
						answerField.setText(practiceQuestionSet[1]+ " ");
						if (attemptsRemaining == 1) {
							// make text red and display the first letter
							answerField.setStyle( buttonStyle + " -fx-text-fill: #B43757;");
							String firstLetter = "" + practiceQuestionSet[2].charAt(0);
							answerField.setText(practiceQuestionSet[1]+ " " + firstLetter.toUpperCase());
						}
						displayAttempts.setText(attemptsRemaining + "\nAttempts Remaining");
					}
				}

				Attempt.save(attemptsRemaining, PRACTICEATTEMPTFILE);

			}
		});
		return practiceScene;
		
		
		
	}
}
