

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.application.Application;
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

public class GUI extends Application
{
	public static void main(String[] args) 
	{
		// launch GUI
		launch(args);
	}
	
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
	
	Scene menuScene, gameScene, practiceScene, settingsScene, resetScene;
	
	
	// title settings
	String menuTitle = "Main Menu";
	Font titleFont = Font.font("Arial", FontWeight.BOLD, 72);
	double titleXPos = 375;
	
	// background settings
	String backgroundStyle = "-fx-background-color: #e4bbde; ";
	
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
	int currentScore = 0;

	@Override
	public void start(Stage guiStage)
	{
		
		guiStage.setResizable(false);
		guiStage.setTitle( "Quinzical" );
		
		// fetch data
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
			// get tts speed
			BufferedReader getSpeed = new BufferedReader(new FileReader(TTSSPEEDFILE));
			String speedLine = getSpeed.readLine();
			getSpeed.close();
			if (speedLine != null) {
				ttsSpeed = Integer.parseInt( speedLine );
				testSpeed = ttsSpeed;
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
		//  Main Menu setup
		Group root = new Group();
		Scene menuScene = new Scene( root, width, height );
		
		StackPane background = new StackPane();
		Canvas canvas = new Canvas( width, height);
		background.getChildren().add( canvas );
		root.getChildren().add( background );
		
		// Main Menu title
		GraphicsContext menuText = canvas.getGraphicsContext2D();
		
		background.setStyle(backgroundStyle);
		menuText.setFill( Color.PURPLE );
		menuText.setStroke( Color.BLACK );
		menuText.setLineWidth(2);
		menuText.setFont( titleFont );
		menuText.fillText( menuTitle, titleXPos, 100 );
		menuText.strokeText( menuTitle, titleXPos, 100 );

		// main menu buttons
		Button gameButton = new Button( "Play Game" );
		gameButton.setLayoutX( buttonXPos );
		gameButton.setLayoutY( buttonYStart );
		gameButton.setPrefSize( buttonXScale , buttonYScale );
		gameButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 1.75em; ");
		
		Button practiceButton = new Button( "Practice" );
		practiceButton.setLayoutX( buttonXPos );
		practiceButton.setLayoutY( buttonYStart + buttonYOffset );
		practiceButton.setPrefSize( buttonXScale , buttonYScale );
		practiceButton.setStyle(buttonStyle);
		
		Button settingsButton = new Button( "Settings" );
		settingsButton.setLayoutX( buttonXPos );
		settingsButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		settingsButton.setPrefSize( buttonXScale , buttonYScale );
		settingsButton.setStyle("-fx-background-color: #EC9706; -fx-font-size: 1.75em; ");
		
		Button resetButton = new Button( "Reset Progress" );
		resetButton.setLayoutX( buttonXPos );
		resetButton.setLayoutY( buttonYStart + buttonYOffset * 3 );
		resetButton.setPrefSize( buttonXScale , buttonYScale );
		resetButton.setStyle("-fx-background-color: #B43757; -fx-font-size: 1.75em; ");
		
		// button used to reset data (in reset submenu)
		Button resetConfirmButton = new Button( "Confirm Reset" );
		resetConfirmButton.setLayoutX( buttonXPos );
		resetConfirmButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		resetConfirmButton.setPrefSize( buttonXScale , buttonYScale );
		resetConfirmButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #4E4C58; -fx-font-size: 1.75em; ");
		
		// button used to confirm category selection in practice module
		Button practiceConfirmButton = new Button( "Practice This!" );
		practiceConfirmButton.setLayoutX( buttonXPos );
		practiceConfirmButton.setLayoutY( buttonYStart + buttonYOffset * 1 );
		practiceConfirmButton.setPrefSize( buttonXScale , buttonYScale );
		practiceConfirmButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 1.75em; ");
		
		// button used to return to in-progress practice question
		Button practiceReturnButton = new Button( "Return to Question" );
		practiceReturnButton.setLayoutX( buttonXPos );
		practiceReturnButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		practiceReturnButton.setPrefSize( buttonXScale , buttonYScale );
		practiceReturnButton.setStyle("-fx-background-color: #EC9706; -fx-font-size: 1.75em; ");
		
		// button used to lock in a practice question attempt
		Button practiceLockInButton = new Button( "I'm Sure!" );
		practiceLockInButton.setLayoutX( buttonXPos );
		practiceLockInButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		practiceLockInButton.setPrefSize( buttonXScale , buttonYScale );
		practiceLockInButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 1.75em; ");
		
		// button used to lock in a practice question attempt
		Button saveSettingsButton = new Button( "Save" );
		saveSettingsButton.setLayoutX( buttonXPos );
		saveSettingsButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		saveSettingsButton.setPrefSize( buttonXScale , buttonYScale );
		saveSettingsButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 1.75em; ");
				
		// return to menu button (used for other scenes)
		Button menuButton = new Button( "Back to Menu" );
		menuButton.setLayoutX( buttonXPos );
		menuButton.setLayoutY( buttonYStart + buttonYOffset * 3 );
		menuButton.setPrefSize( buttonXScale , buttonYScale );
		menuButton.setStyle("-fx-background-color: #B43757; -fx-font-size: 1.75em; ");
		menuButton.setOnAction(e-> guiStage.setScene(menuScene));
		
		//button used to test speed in setting
		Button testSpeedButton = new Button( "Test Speed" );
		testSpeedButton.setLayoutX(buttonXPos + 400);
		testSpeedButton.setLayoutY(buttonYStart - 10 );
		testSpeedButton.setPrefSize( buttonXScale/2 , buttonYScale/2 );
		testSpeedButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.25em; -fx-text-fill: white; ");
		testSpeedButton.setOnAction(e-> {
			TextToSpeech.toTestSpeech(testSpeed);
		});
		
		//button to speak the question
		Button hearButton = new Button( "Hear Question" );
		hearButton.setLayoutX(buttonXPos + 350);
		hearButton.setLayoutY(buttonYStart + buttonYOffset );
		hearButton.setPrefSize( buttonXScale/2 , buttonYScale/2 );
		hearButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.00em; -fx-text-fill: white; ");
		hearButton.setOnAction(e-> {
			TextToSpeech.toSpeech(practiceQuestionSet[0]);
		});
		
		//button to speak the game question
		Button hearGameButton = new Button( "Hear Question" );
		hearGameButton.setLayoutX(buttonXPos + 350);
		hearGameButton.setLayoutY(buttonYStart + buttonYOffset );
		hearGameButton.setPrefSize( buttonXScale/2 , buttonYScale/2 );
		hearGameButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.00em; -fx-text-fill: white; ");
		hearGameButton.setOnAction(e-> {
			TextToSpeech.toSpeech(gameQuestionSet[0]);
		});
		
		// button used to lock in a game question attempt
		Button gameLockInButton = new Button( "I'm Sure!" );
		gameLockInButton.setLayoutX( buttonXPos );
		gameLockInButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		gameLockInButton.setPrefSize( buttonXScale , buttonYScale );
		gameLockInButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 1.75em; ");
		
		// I dont know button
		Button dontKnowButton = new Button( "I don't know" );
		dontKnowButton.setLayoutX( buttonXPos );
		dontKnowButton.setLayoutY( buttonYStart + buttonYOffset * 3 );
		dontKnowButton.setPrefSize( buttonXScale , buttonYScale );
		dontKnowButton.setStyle("-fx-background-color: #B43757; -fx-font-size: 1.75em; ");
		dontKnowButton.setOnAction(e-> guiStage.setScene(menuScene));
		
		
		
		root.getChildren().add(gameButton);
		root.getChildren().add(practiceButton);
		root.getChildren().add(settingsButton);
		root.getChildren().add(resetButton);
		
		guiStage.setScene( menuScene );
		
		// button handlers
		gameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Group gameRoot = new Group();
				Scene gameScene = new Scene( gameRoot );
				
				StackPane gameBackground = new StackPane();
				Canvas gameCanvas = new Canvas( width, height );
				gameBackground.setStyle( backgroundStyle );
				
				GridPane gameGrid = new GridPane();
				// create new question set when game is reset
				if(QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).size() < 1) {
					QuestionSelector.copyRandomCategories(QUESTIONBANKFILE, GAMEQUESTIONSFILE);
				}

				gameGrid.setVgap(10);
				gameGrid.setHgap(30);


				
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
						moneyButton.setOnAction(e->{
						//	System.out.println(QuestionSelector.getQuestionSetFromValue(moneyButton.getText(), QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10), GAMEQUESTIONSFILE)[0]);
							gameQuestionSet=QuestionSelector.getQuestionSetFromValue(moneyButton.getText(), QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10), GAMEQUESTIONSFILE);	
							QuestionSelector.deleteLinesContaining(gameQuestionSet[0], GAMEQUESTIONSFILE);
							TextToSpeech.toSpeech(gameQuestionSet[0]);
							try {
								currentScore=Score.getSumAndSave("0");
							} catch (FileNotFoundException ev) {
								// TODO Auto-generated catch block
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
							categoryTitlePrompt.fillText( "Game Module", 270, 100 );
							categoryTitlePrompt.strokeText( "Game Module", 270, 100 );
							
							// display the question
							Text gameQuestionPrompt = new Text( gameQuestionSet[0] );
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
							
							gameQuestionBackground.getChildren().add( gameQuestionCanvas );
							gameQuestionRoot.getChildren().add( gameQuestionBackground );
							gameQuestionRoot.getChildren().add( gameQuestionPrompt );
							gameQuestionRoot.getChildren().add( answerField );
							gameQuestionRoot.getChildren().add( gameLockInButton );
							gameQuestionRoot.getChildren().add( displayScore );
							gameQuestionRoot.getChildren().add( hearGameButton );
							gameQuestionRoot.getChildren().add( dontKnowButton );
							
							
							
							
							guiStage.setScene(gameQuestionScene);
							
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

				guiStage.setScene( gameScene );
				
				

			}
		});
			
		practiceButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				Group practiceRoot = new Group();
				Scene practiceScene = new Scene( practiceRoot );
				
				StackPane practiceBackground = new StackPane();
				Canvas practiceCanvas = new Canvas( width, height );
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
				
				guiStage.setScene( practiceScene );
				
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
						TextToSpeech.toSpeech(practiceQuestionSet[0]);
						
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
						TextToSpeech.toSpeech(practiceQuestionSet[0]);
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
							TextToSpeech.toSpeech("Correct!");
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
								TextToSpeech.toSpeech("Incorrect! The answer was " + practiceQuestionSet[1]+ " " + practiceQuestionSet[2]);
								
							} else {
								TextToSpeech.toSpeech("Incorrect!");
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
				
				
			}
		});
		
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Group settingsRoot = new Group();
				Scene settingsScene = new Scene( settingsRoot );
				
				testSpeed = ttsSpeed;
				
				StackPane settingsBackground = new StackPane();
				Canvas settingsCanvas = new Canvas( width, height );
				settingsBackground.setStyle( backgroundStyle );
				
				GraphicsContext settingsTitle = settingsCanvas.getGraphicsContext2D();
				// settings sub-menu title
				settingsTitle.setFill( Color.PURPLE );
				settingsTitle.setStroke( Color.BLACK );
				settingsTitle.setLineWidth(2);
				settingsTitle.setFont( titleFont );
				settingsTitle.fillText( "Settings", 425, 100 );
				settingsTitle.strokeText( "Settings", 425, 100 );
				
				// tts speed slider label
				Text speedText = new Text(String.format("Talking Speed: x%.2f",(ttsSpeed / (double)(DEFAULTSPEED))));
				speedText.setStyle("-fx-font-size: 1.5em; ");
				speedText.setTextAlignment(TextAlignment.CENTER);
				speedText.setLayoutY( buttonYStart );
				speedText.setLayoutX( buttonXPos );
				
				// TTS speed slider
				Slider speedSlider = new Slider( 40 , 320 , ttsSpeed );
				speedSlider.setLayoutX(buttonXPos - 130 );
				speedSlider.setLayoutY(buttonYStart + 10);
				speedSlider.setPrefWidth(buttonXScale * 2);
				speedSlider.setStyle("-fx-control-inner-background: #4E4C58; -fx-color: #50C878;");
				speedSlider.valueProperty().addListener((observable, oldvalue, newvalue) ->
				{
					testSpeed = newvalue.intValue();
					saveSettingsButton.setVisible(true);
					speedText.setText(String.format("Talking Speed: x%.2f",(testSpeed / (double)(DEFAULTSPEED))));
				});
				
		
				
				saveSettingsButton.setVisible(false);
				settingsBackground.getChildren().add( settingsCanvas );
				settingsRoot.getChildren().add( settingsBackground );
				settingsRoot.getChildren().add( speedText );
				settingsRoot.getChildren().add( speedSlider );
				settingsRoot.getChildren().add( menuButton );
				settingsRoot.getChildren().add( saveSettingsButton );
				settingsRoot.getChildren().add( testSpeedButton );
				
				
				guiStage.setScene( settingsScene );
			}
		});
		
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				// reset sub-menu scene
				Group resetRoot = new Group();
				Scene resetScene = new Scene( resetRoot );
				
				StackPane resetBackground = new StackPane();
				Canvas resetCanvas = new Canvas( width, height );
				resetBackground.setStyle( backgroundStyle );
				
				GraphicsContext resetTitle = resetCanvas.getGraphicsContext2D();
				// reset sub-menu title
				resetTitle.setFill( Color.PURPLE );
				resetTitle.setStroke( Color.BLACK );
				resetTitle.setLineWidth(2);
				resetTitle.setFont( titleFont );
				resetTitle.fillText( "Reset Progress", 300, 100 );
				resetTitle.strokeText( "Reset Progress", 300, 100 );
				
				String resetExplanation = 
				"If you reset, you will lose:\n"
				+ "   > In-progress Games\n"
				+ "   > In-progress Practice Questions\n"
				+ "   > Any unlockables or achievements\n\n"
				+ "             Settings will be retained.";
				Text resetText = new Text( resetExplanation );
				resetText.setWrappingWidth( 800 );
				resetText.setStyle("-fx-font-size: 3em; ");
				//resetText.setTextAlignment(TextAlignment.CENTER);
				resetText.setLayoutY(buttonYStart + 30);
				resetText.setLayoutX(200);
				
				resetConfirmButton.setDisable(false);
				
				resetBackground.getChildren().add( resetCanvas );
				resetRoot.getChildren().add( resetBackground );
				resetRoot.getChildren().add( resetConfirmButton );
				resetRoot.getChildren().add( resetText );
				resetRoot.getChildren().add(menuButton);
				
				guiStage.setScene( resetScene );
			}
		});
		
		resetConfirmButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				attemptsRemaining = 0;
				currentScore = 0;
				
				Attempt.save(attemptsRemaining, PRACTICEATTEMPTFILE);
				QuestionSelector.reset(GAMEQUESTIONSFILE);
				Score.reset(GAMESCOREFILE);
				
				resetConfirmButton.setDisable(true);
			}
		});
		
		saveSettingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				saveSettingsButton.setVisible(false);
				// save tts speed
					ttsSpeed = testSpeed;
					TextToSpeech.save(ttsSpeed,TTSSPEEDFILE);
				
			}
		});
		guiStage.show();
	}
	
	
}

	