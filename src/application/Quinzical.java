package application;


import data.Attempt;
import data.Score;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.QuestionSelector;

public class Quinzical extends Application
{
	public static void main(String[] args) 
	{
		// launch GUI
		launch(args);
	}

	// GUI window size
	public static int width = 1200;
	public static int height = 800;

	// file names to use
	public static final String QUESTIONBANKFILE = "categories";
	public static final String PRACTICEATTEMPTFILE = "GameData/Practice/Attempt";
	public static final String PRACTICEQUESTIONFILE = "GameData/Practice/Question";
	public static final String GAMEQUESTIONSFILE = "GameData/Game/Questions";
	public static final String GAMESCOREFILE = "GameData/Game/.Score";

	Scene menuScene, gameScene, practiceScene, settingsScene, resetScene;


	// title settings
	String menuTitle = "Main Menu";
	public static final Font titleFont = Font.font("Arial", FontWeight.BOLD, 72);
	final double titleXPos = 375;

	// background settings
	public static final String backgroundStyle = "-fx-background-color: #e4bbde; ";

	// button settings
	public static final double buttonXPos = 480;
	public static final double buttonYStart = 160;
	public static final double buttonYOffset = 150;
	public static final double buttonXScale = 250;
	public static final double buttonYScale = 100;
	public static final String buttonStyle = "-fx-background-color: #d0e7ff; -fx-font-size: 2em; ";

	// default data
	String practiceCategory = "";
	int attemptsRemaining = 0; 
	int currentScore = 0;

	@Override
	public void start(Stage guiStage)
	{

		guiStage.setResizable(false);
		guiStage.setTitle( "Quinzical" );

		// fetch data
		TextToSpeech.setup();
		Timer.setup();
		QuestionSelector.setupLock();

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
		gameButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");

		Button practiceButton = new Button( "Practice" );
		practiceButton.setLayoutX( buttonXPos );
		practiceButton.setLayoutY( buttonYStart + buttonYOffset );
		practiceButton.setPrefSize( buttonXScale , buttonYScale );
		practiceButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #7c5295; -fx-font-size: 2em; ");

		Button settingsButton = new Button( "Settings" );
		settingsButton.setLayoutX( buttonXPos );
		settingsButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		settingsButton.setPrefSize( buttonXScale , buttonYScale );
		settingsButton.setStyle("-fx-background-color: #EC9706; -fx-font-size: 2em; ");

		Button resetButton = new Button( "Reset Progress" );
		resetButton.setLayoutX( buttonXPos );
		resetButton.setLayoutY( buttonYStart + buttonYOffset * 3 );
		resetButton.setPrefSize( buttonXScale , buttonYScale );
		resetButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #B43757; -fx-font-size: 2em; ");

		// button used to reset data (in reset submenu)
		Button resetConfirmButton = new Button( "Confirm Reset" );
		resetConfirmButton.setLayoutX( buttonXPos );
		resetConfirmButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		resetConfirmButton.setPrefSize( buttonXScale , buttonYScale );
		resetConfirmButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #4E4C58; -fx-font-size: 2em; ");

		// button used to confirm category selection in practice module
		Button practiceConfirmButton = new Button( "Practice This!" );
		practiceConfirmButton.setLayoutX( buttonXPos );
		practiceConfirmButton.setLayoutY( buttonYStart + buttonYOffset * 1 );
		practiceConfirmButton.setPrefSize( buttonXScale , buttonYScale );
		practiceConfirmButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");

		// button used to return to in-progress practice question
		Button practiceReturnButton = new Button( "Return to Question" );
		practiceReturnButton.setLayoutX( buttonXPos );
		practiceReturnButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		practiceReturnButton.setPrefSize( buttonXScale , buttonYScale );
		practiceReturnButton.setStyle("-fx-background-color: #EC9706; -fx-font-size: 1.8em; ");

		// button used to lock in a practice question attempt
		Button practiceLockInButton = new Button( "I'm Sure!" );
		practiceLockInButton.setLayoutX( buttonXPos );
		practiceLockInButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		practiceLockInButton.setPrefSize( buttonXScale , buttonYScale );
		practiceLockInButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");

		// button used to lock in a practice question attempt
		Button saveSettingsButton = new Button( "Save" );
		saveSettingsButton.setLayoutX( buttonXPos );
		saveSettingsButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		saveSettingsButton.setPrefSize( buttonXScale , buttonYScale );
		saveSettingsButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");

		// return to menu button (used for other scenes)
		Button menuButton = new Button( "Back to Menu" );
		menuButton.setLayoutX( buttonXPos );
		menuButton.setLayoutY( buttonYStart + buttonYOffset * 3 );
		menuButton.setPrefSize( buttonXScale , buttonYScale );
		menuButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #B43757; -fx-font-size: 2em; ");
		menuButton.setOnAction(e-> guiStage.setScene(menuScene));

		//button used to test speed in setting
		Button testSpeedButton = new Button( "Test Speed" );
		testSpeedButton.setLayoutX(buttonXPos + 400);
		testSpeedButton.setLayoutY(buttonYStart - 10 );
		testSpeedButton.setPrefSize( buttonXScale/2 , buttonYScale/2 );
		testSpeedButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.25em; -fx-text-fill: white; ");
		testSpeedButton.setOnAction(e-> {
			TextToSpeech.test();
		});

		//button to speak the question
		Button hearButton = new Button( "Hear Question" );
		hearButton.setLayoutX(buttonXPos + 350);
		hearButton.setLayoutY(buttonYStart + buttonYOffset );
		hearButton.setPrefSize( buttonXScale/2 , buttonYScale/2 );
		hearButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.00em; -fx-text-fill: white; ");
		hearButton.setOnAction(e-> {
			TextToSpeech.say(PracticeScene.practiceQuestionSet[0]);
		});

		//button to speak the game question
		Button hearGameButton = new Button( "Hear Question" );
		hearGameButton.setLayoutX(buttonXPos + 350);
		hearGameButton.setLayoutY(buttonYStart + buttonYOffset );
		hearGameButton.setPrefSize( buttonXScale/2 , buttonYScale/2 );
		hearGameButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.00em; -fx-text-fill: white; ");
		hearGameButton.setOnAction(e-> {
			TextToSpeech.say(QuestionScene.gameQuestionSet[0]);
		});

		// button used to lock in a game question attempt
		Button gameLockInButton = new Button( "I'm Sure!" );
		gameLockInButton.setLayoutX( buttonXPos );
		gameLockInButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		gameLockInButton.setPrefSize( buttonXScale , buttonYScale );
		gameLockInButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");
		
		// restart game module button
		Button restartButton = new Button( "New Game" );
		restartButton.setLayoutX( buttonXPos );
		restartButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		restartButton.setPrefSize( buttonXScale , buttonYScale );
		restartButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");
		restartButton.setOnAction(e-> {
			Score.reset(GAMESCOREFILE);
			QuestionSelector.reset(GAMEQUESTIONSFILE);
			GridScene gs = new GridScene(restartButton, menuButton, gameLockInButton, hearGameButton, guiStage);
			guiStage.setScene(gs.getGridScene());
		});

		root.getChildren().add(gameButton);
		root.getChildren().add(practiceButton);
		root.getChildren().add(settingsButton);
		root.getChildren().add(resetButton);

		guiStage.setScene( menuScene );

		// button handlers
		gameButton.setOnAction(e -> {
			GridScene gs = new GridScene(restartButton, menuButton, gameLockInButton, hearGameButton,  guiStage);
			guiStage.setScene(gs.getGridScene());
		}
				
);

		practiceButton.setOnAction(e -> {
			PracticeScene ps = new PracticeScene(practiceConfirmButton, practiceReturnButton, menuButton, practiceLockInButton, hearButton, guiStage);
			guiStage.setScene(ps.getPracticeScene());
		});

		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Group settingsRoot = new Group();
				Scene settingsScene = new Scene( settingsRoot );

				TextToSpeech.testSpeed = TextToSpeech.getSpeed();

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
				Text speedText = new Text(String.format("Talking Speed: x%.2f",((double)(TextToSpeech.getSpeed()) / 160.0)));
				speedText.setStyle("-fx-font-size: 1.5em; ");
				speedText.setTextAlignment(TextAlignment.CENTER);
				speedText.setLayoutY( buttonYStart );
				speedText.setLayoutX( buttonXPos );

				// TTS speed slider
				Slider speedSlider = new Slider( 40 , 320 , TextToSpeech.getSpeed() );
				speedSlider.setLayoutX(buttonXPos - 130 );
				speedSlider.setLayoutY(buttonYStart + 10);
				speedSlider.setPrefWidth(buttonXScale * 2);
				speedSlider.setStyle("-fx-control-inner-background: #4E4C58; -fx-color: #50C878;");
				speedSlider.valueProperty().addListener((observable, oldvalue, newvalue) ->
				{
					TextToSpeech.testSpeed = newvalue.intValue();
					saveSettingsButton.setVisible(true);
					speedText.setText(String.format("Talking Speed: x%.2f",((double)(TextToSpeech.testSpeed) / 160.0)));
				});
				
				// timer slider label
				Timer.tempLength = Timer.getLength();
				Text timerText = new Text(String.format("Timer Length: %d secs", Timer.getLength()));
				timerText.setStyle("-fx-font-size: 1.5em; ");
				timerText.setTextAlignment(TextAlignment.CENTER);
				timerText.setLayoutY( buttonYStart + buttonYOffset );
				timerText.setLayoutX( buttonXPos );
				
				// timer length slider
				Slider timerSlider = new Slider( 5 , 30 , Timer.getLength() );
				timerSlider.setLayoutX(buttonXPos - 130 );
				timerSlider.setLayoutY(buttonYStart + 10 + buttonYOffset);
				timerSlider.setPrefWidth(buttonXScale * 2);
				timerSlider.setStyle("-fx-control-inner-background: #4E4C58; -fx-color: #50C878;");
				timerSlider.valueProperty().addListener((observable, oldvalue, newvalue) ->
				{
					Timer.tempLength = newvalue.intValue();
					saveSettingsButton.setVisible(true);
					timerText.setText(String.format("Timer Length: %d secs", Timer.tempLength));
				});



				saveSettingsButton.setVisible(false);
				settingsBackground.getChildren().add( settingsCanvas );
				settingsRoot.getChildren().add( settingsBackground );
				settingsRoot.getChildren().add( speedText );
				settingsRoot.getChildren().add( speedSlider );
				settingsRoot.getChildren().add( menuButton );
				settingsRoot.getChildren().add( saveSettingsButton );
				settingsRoot.getChildren().add( testSpeedButton );
				settingsRoot.getChildren().add( timerSlider );
				settingsRoot.getChildren().add( timerText );

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
				QuestionSelector.lock();

				resetConfirmButton.setDisable(true);
			}
		});

		saveSettingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				saveSettingsButton.setVisible(false);
				// save TTS speed
				TextToSpeech.save();
				Timer.save();

			}
		});
		guiStage.show();
	}


}

