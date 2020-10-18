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
import javafx.collections.FXCollections;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.LineToVar;
import questions.QuestionSelector;
import questions.TextToSpeech;

public class PracticeScene {
	Button practiceConfirmButton,practiceReturnButton,menuButton;
	public static String[] practiceQuestionSet = {"Do not edit the game files!","","sorry"};
	Stage guiStage;
	
	public PracticeScene(Button inMenuButton,Stage inGuiStage) {

		menuButton=inMenuButton;
		guiStage=inGuiStage;
		
		// button used to return to in-progress practice question
		practiceReturnButton = new Button( "Return to Question" );
		practiceReturnButton.setLayoutX( Quinzical.buttonXPos );
		practiceReturnButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 );
		practiceReturnButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
		practiceReturnButton.setStyle("-fx-background-color: #EC9706; -fx-font-size: 1.8em; ");
		
		// button used to confirm category selection in practice module
		practiceConfirmButton = new Button( "Practice This!" );
		practiceConfirmButton.setLayoutX( Quinzical.buttonXPos );
		practiceConfirmButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 1 );
		practiceConfirmButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
		practiceConfirmButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");
		
	}

	// file names to use
	public static final String QUESTIONBANKFILE = "categories";
	public static final String PRACTICEATTEMPTFILE = "GameData/Practice/Attempt";
	public static final String PRACTICEQUESTIONFILE = "GameData/Practice/Question";

	String practiceCategory = "";
	int attemptsRemaining = 0; 
	
	Group practiceRoot = new Group();
	Scene practiceScene = new Scene( practiceRoot );

	StackPane practiceBackground = new StackPane();
	Canvas practiceCanvas = new Canvas( Quinzical.width, Quinzical.height );
		
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
		
		practiceBackground.setStyle( Quinzical.backgroundStyle );
		practiceBackground.getChildren().add( practiceCanvas );
		practiceRoot.getChildren().add( practiceBackground );
		practiceRoot.getChildren().add(menuButton);

		GraphicsContext selectCategoryPrompt = practiceCanvas.getGraphicsContext2D();
		// select category prompt
		selectCategoryPrompt.setFill( Color.PURPLE );
		selectCategoryPrompt.setStroke( Color.BLACK );
		selectCategoryPrompt.setLineWidth(2);
		selectCategoryPrompt.setFont( Quinzical.titleFont );
		selectCategoryPrompt.fillText( "Select Practice Category", 100, 100 );
		selectCategoryPrompt.strokeText( "Select Practice Category", 100, 100 );

		// make buttons invisible
		practiceConfirmButton.setVisible(false);
		practiceReturnButton.setVisible(false);

		// drop-down menu to choose category
		ChoiceBox<String> categoryDropDown = new ChoiceBox<String>(FXCollections.observableArrayList(
				QuestionSelector.getCategoriesInFile(QUESTIONBANKFILE)));
		categoryDropDown.setLayoutX(Quinzical.buttonXPos - 30);
		categoryDropDown.setLayoutY(Quinzical.buttonYStart);
		categoryDropDown.setPrefSize(Quinzical.buttonXScale + 60, Quinzical.buttonYScale);
		categoryDropDown.setStyle( Quinzical.buttonStyle );

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

		practiceConfirmButton.setOnAction(e->{
			PracticeQuestionScene pqs = new PracticeQuestionScene(practiceQuestionSet, attemptsRemaining, true, practiceCategory, menuButton, guiStage);
			guiStage.setScene(pqs.getPracticeQuestionScene());
		});

		practiceReturnButton.setOnAction(e->{
			PracticeQuestionScene pqs = new PracticeQuestionScene(practiceQuestionSet, attemptsRemaining, false, practiceCategory, menuButton, guiStage);
			guiStage.setScene(pqs.getPracticeQuestionScene());
		});
		
		return practiceScene;
		
	}
}
