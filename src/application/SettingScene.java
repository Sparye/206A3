package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.TextToSpeech;
import questions.Timer;

public class SettingScene {
	Button testSpeedButton = new Button( "Test Speed" );
	Button saveSettingsButton = new Button( "Save" );
	Stage guiStage;
	Button menuButton;

	public SettingScene(Button inMenuButton,Stage inGuiStage) {
		menuButton = inMenuButton;
		guiStage = inGuiStage;
		
		testSpeedButton.setLayoutX(Quinzical.buttonXPos + 400);
		testSpeedButton.setLayoutY(Quinzical.buttonYStart - 10 );
		testSpeedButton.setPrefSize( Quinzical.buttonXScale/2 , Quinzical.buttonYScale/2 );
		testSpeedButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.25em; -fx-text-fill: white; ");
		testSpeedButton.setOnAction(e-> {
			TextToSpeech.test();
		});
		
		saveSettingsButton.setLayoutX( Quinzical.buttonXPos );
		saveSettingsButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 );
		saveSettingsButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
		saveSettingsButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");
		saveSettingsButton.setOnAction(e->{
			saveSettingsButton.setVisible(false);
			// save TTS speed
			TextToSpeech.save();
			Timer.save();
		});
	}
	
	public Scene getSettingScene() {
		Group settingsRoot = new Group();
		Scene settingsScene = new Scene( settingsRoot );
		
		TextToSpeech.testSpeed = TextToSpeech.getSpeed();

		StackPane settingsBackground = new StackPane();
		Canvas settingsCanvas = new Canvas( Quinzical.width, Quinzical.height );
		settingsBackground.setStyle( Quinzical.backgroundStyle );

		GraphicsContext settingsTitle = settingsCanvas.getGraphicsContext2D();
		// settings sub-menu title
		settingsTitle.setFill( Color.PURPLE );
		settingsTitle.setStroke( Color.BLACK );
		settingsTitle.setLineWidth(2);
		settingsTitle.setFont( Quinzical.titleFont );
		settingsTitle.fillText( "Settings", 425, 100 );
		settingsTitle.strokeText( "Settings", 425, 100 );

		// tts speed slider label
		Text speedText = new Text(String.format("Talking Speed: x%.2f",((double)(TextToSpeech.getSpeed()) / 160.0)));
		speedText.setStyle("-fx-font-size: 1.5em; ");
		speedText.setTextAlignment(TextAlignment.CENTER);
		speedText.setLayoutY( Quinzical.buttonYStart );
		speedText.setLayoutX( Quinzical.buttonXPos );

		// TTS speed slider
		Slider speedSlider = new Slider( 40 , 320 , TextToSpeech.getSpeed() );
		speedSlider.setLayoutX(Quinzical.buttonXPos - 130 );
		speedSlider.setLayoutY(Quinzical.buttonYStart + 10);
		speedSlider.setPrefWidth(Quinzical.buttonXScale * 2);
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
		timerText.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset );
		timerText.setLayoutX( Quinzical.buttonXPos );
		
		// timer length slider
		Slider timerSlider = new Slider( 5 , 30 , Timer.getLength() );
		timerSlider.setLayoutX(Quinzical.buttonXPos - 130 );
		timerSlider.setLayoutY(Quinzical.buttonYStart + 10 + Quinzical.buttonYOffset);
		timerSlider.setPrefWidth(Quinzical.buttonXScale * 2);
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

	//	guiStage.setScene( settingsScene );
		return settingsScene;
		
	}
}
