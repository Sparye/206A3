import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
	
	@Override
	public void start(Stage guiStage)
	{
		
		guiStage.setResizable(false);
		guiStage.setTitle( "Quinzical" );
		
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
		gameButton.setStyle(buttonStyle);
		
		Button practiceButton = new Button( "Practice" );
		practiceButton.setLayoutX( buttonXPos );
		practiceButton.setLayoutY( buttonYStart + buttonYOffset );
		practiceButton.setPrefSize( buttonXScale , buttonYScale );
		practiceButton.setStyle(buttonStyle);
		
		Button settingsButton = new Button( "Settings" );
		settingsButton.setLayoutX( buttonXPos );
		settingsButton.setLayoutY( buttonYStart + buttonYOffset * 2 );
		settingsButton.setPrefSize( buttonXScale , buttonYScale );
		settingsButton.setStyle(buttonStyle);
		
		Button resetButton = new Button( "Reset Progress" );
		resetButton.setLayoutX( buttonXPos );
		resetButton.setLayoutY( buttonYStart + buttonYOffset * 3 );
		resetButton.setPrefSize( buttonXScale , buttonYScale );
		resetButton.setStyle(buttonStyle);
		
		// return to menu button (used for other scenes)
		Button menuButton = new Button( "Back to Menu" );
		menuButton.setLayoutX( buttonXPos );
		menuButton.setLayoutY( buttonYStart + buttonYOffset * 3 );
		menuButton.setPrefSize( buttonXScale , buttonYScale );
		menuButton.setStyle(buttonStyle);
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
				
				// PRACTICE LOGIC HERE ~ TODO
				
				guiStage.setScene( practiceScene );
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

	