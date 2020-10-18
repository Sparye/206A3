package application;

import data.Attempt;
import data.Score;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import questions.QuestionSelector;

public class ResetScene {
	Button resetConfirmButton,menuButton;
	
	public static final String PRACTICEATTEMPTFILE = "GameData/Practice/Attempt";
	public static final String GAMEQUESTIONSFILE = "GameData/Game/Questions";
	public static final String GAMESCOREFILE = "GameData/Game/.Score";
	
	public ResetScene(Button inMenuButton) {
		menuButton = inMenuButton;
		
		resetConfirmButton = new Button( "Confirm Reset" );
		resetConfirmButton.setLayoutX( Quinzical.buttonXPos );
		resetConfirmButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 );
		resetConfirmButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
		resetConfirmButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #4E4C58; -fx-font-size: 2em; ");
		resetConfirmButton.setOnAction(e->{
			Attempt.save(0, PRACTICEATTEMPTFILE);
			QuestionSelector.reset(GAMEQUESTIONSFILE);
			Score.reset(GAMESCOREFILE);
			QuestionSelector.lock();

			resetConfirmButton.setDisable(true);
		});
	}
	
	public Scene getResetScene() {
		// reset sub-menu scene
		Group resetRoot = new Group();
		Scene resetScene = new Scene( resetRoot );

		StackPane resetBackground = new StackPane();
		Canvas resetCanvas = new Canvas( Quinzical.width, Quinzical.height );
		resetBackground.setStyle( Quinzical.backgroundStyle );

		GraphicsContext resetTitle = resetCanvas.getGraphicsContext2D();
		// reset sub-menu title
		resetTitle.setFill( Color.PURPLE );
		resetTitle.setStroke( Color.BLACK );
		resetTitle.setLineWidth(2);
		resetTitle.setFont( Quinzical.titleFont );
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
		resetText.setLayoutY(Quinzical.buttonYStart + 30);
		resetText.setLayoutX(200);

		resetConfirmButton.setDisable(false);

		resetBackground.getChildren().add( resetCanvas );
		resetRoot.getChildren().add( resetBackground );
		resetRoot.getChildren().add( resetConfirmButton );
		resetRoot.getChildren().add( resetText );
		resetRoot.getChildren().add(menuButton);

		return resetScene;
		
	}
}
