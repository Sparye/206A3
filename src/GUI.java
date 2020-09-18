import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
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
	
	// Practice data
	String practiceCategory = "";
	int attemptsRemaining = 0; // placeholder
	String practiceQuestion = ""; // placeholder
	
	@Override
	public void start(Stage guiStage)
	{
		
		guiStage.setResizable(false);
		guiStage.setTitle( "Quinzical" );
		
		// fetch data
		try {
			// get practice attempts
			BufferedReader getAttempts = new BufferedReader(new FileReader(PRACTICEATTEMPTFILE));
			attemptsRemaining = Integer.parseInt(getAttempts.readLine());
			getAttempts.close();
			if (attemptsRemaining > 0) {
			// get practice question
			BufferedReader getPracticeQuestion = new BufferedReader(new FileReader(PRACTICEQUESTIONFILE));
			practiceQuestion = getPracticeQuestion.readLine();
			getPracticeQuestion.close();
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
				
		// return to menu button (used for other scenes)
		Button menuButton = new Button( "Back to Menu" );
		menuButton.setLayoutX( buttonXPos );
		menuButton.setLayoutY( buttonYStart + buttonYOffset * 3 );
		menuButton.setPrefSize( buttonXScale , buttonYScale );
		menuButton.setStyle("-fx-background-color: #B43757; -fx-font-size: 1.75em; ");
		menuButton.setOnAction(e-> guiStage.setScene(menuScene));
		
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
				
				gameBackground.getChildren().add( gameCanvas );
				gameRoot.getChildren().add( gameBackground );
				gameRoot.getChildren().add(menuButton);
				
				// GAME LOGIC HERE ~ TODO
				
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
				Text practiceQuestionPrompt = new Text( practiceQuestion );
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
				
				// correct answer (appears when out of attempts)
				Text actualAnswer = new Text();
				actualAnswer.setLayoutX( buttonXPos - 75 ) ;
				actualAnswer.setLayoutY( buttonYStart + buttonYOffset );
				practiceQuestionPrompt.setWrappingWidth( 800 );
				practiceQuestionPrompt.setTextAlignment(TextAlignment.CENTER);
				actualAnswer.setStyle( buttonStyle + "-fx-font-size: 4em; " );
				
				if (attemptsRemaining == 1) {
					// make field text red and display the first letter
					answerField.setStyle( buttonStyle + " -fx-text-fill: #B43757;");
					String firstLetter = "c"; // Placeholder
					answerField.setText(firstLetter.toUpperCase());
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

				practiceLockInButton.setVisible(false);
				
				practiceConfirmButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO ~ New Practice Question Logic
						
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
						Random chooseRandomQuestion = new Random();
						practiceQuestion = questionArray.get(chooseRandomQuestion.nextInt(questionArray.size()));
						
						// reset attempts
						attemptsRemaining = 3;
						try {
						PrintWriter resetAttempts = new PrintWriter(new FileWriter(PRACTICEATTEMPTFILE));
						resetAttempts.println(attemptsRemaining + "");
						resetAttempts.close();
						// save new question
						PrintWriter saveQuestion = new PrintWriter(new FileWriter(PRACTICEQUESTIONFILE));
						saveQuestion.println(practiceQuestion);
						saveQuestion.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						displayAttempts.setText(attemptsRemaining + "\nAttempts Remaining");
						practiceQuestionPrompt.setText( practiceQuestion );
						
						practiceQuestionRoot.getChildren().add(menuButton);
						guiStage.setScene( practiceQuestionScene );
						
					}
				});
				
				practiceReturnButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO ~ Return to Practice Question Logic
						practiceQuestionRoot.getChildren().add(menuButton);
						guiStage.setScene( practiceQuestionScene );
					}
				});
				
				practiceLockInButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO ~ VERIFY ATTEMPT LOGIC
						attemptsRemaining--;
						try {
							PrintWriter saveAttempts = new PrintWriter(new FileWriter(PRACTICEATTEMPTFILE));
							saveAttempts.println(attemptsRemaining + "");
							saveAttempts.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						if (attemptsRemaining == 0) {
							practiceQuestionRoot.getChildren().remove(answerField);
							practiceQuestionRoot.getChildren().remove(practiceLockInButton);
							practiceQuestionRoot.getChildren().remove(displayAttempts);
							
							String answer = "womp womp :(";
							actualAnswer.setText(answer);
							practiceQuestionRoot.getChildren().add(actualAnswer);
							
						} else {
							if (attemptsRemaining == 1) {
								// make text red and display the first letter
								answerField.setStyle( buttonStyle + " -fx-text-fill: #B43757;");
								String firstLetter = "c"; // Placeholder
								answerField.setText(firstLetter.toUpperCase());
							}
							displayAttempts.setText(attemptsRemaining + "\nAttempts Remaining");
						}
					}
				});
				
				
			}
		});
		
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Group settingsRoot = new Group();
				Scene settingsScene = new Scene( settingsRoot );
				
				StackPane settingsBackground = new StackPane();
				Canvas settingsCanvas = new Canvas( width, height );
				settingsBackground.setStyle( backgroundStyle );
				
				settingsBackground.getChildren().add( settingsCanvas );
				settingsRoot.getChildren().add( settingsBackground );
				settingsRoot.getChildren().add(menuButton);
				
				// SETTINGS LOGIC HERE ~ TODO

				guiStage.setScene( settingsScene );
			}
		});
		
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Group resetRoot = new Group();
				Scene resetScene = new Scene( resetRoot );
				
				StackPane resetBackground = new StackPane();
				Canvas resetCanvas = new Canvas( width, height );
				resetBackground.setStyle( backgroundStyle );
				
				resetBackground.getChildren().add( resetCanvas );
				resetRoot.getChildren().add( resetBackground );
				resetRoot.getChildren().add(menuButton);
				
				// RESET LOGIC HERE ~ TODO
				
				guiStage.setScene( resetScene );
			}
		});
		
		guiStage.show();
	}
}

	