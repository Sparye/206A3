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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.LineToVar;
import questions.TextToSpeech;

public class PracticeQuestionScene {
	
	public static final String QUESTIONBANKFILE = "categories";
	public static final String PRACTICEATTEMPTFILE = "GameData/Practice/Attempt";
	public static final String PRACTICEQUESTIONFILE = "GameData/Practice/Question";
	
	private String[] practiceQuestionSet;
	private int attemptsRemaining;
	boolean newQuestion;
	private String practiceCategory = "";
	
	Button practiceLockInButton,hearButton,newQuestionButton,menuButton;
	
	Stage guiStage;
	
	public PracticeQuestionScene(String[] inQset,int inARemaining,boolean inNewQuestion,String inPracticeCategory,Button inMenuButton,Stage inGuiStage) {
		practiceQuestionSet=inQset;
		attemptsRemaining = inARemaining;
		newQuestion=inNewQuestion;
		practiceCategory=inPracticeCategory;
		menuButton=inMenuButton;
		guiStage=inGuiStage;
		
		practiceLockInButton = new Button( "I'm Sure!" );
		practiceLockInButton.setLayoutX( Quinzical.buttonXPos );
		practiceLockInButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 );
		practiceLockInButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
		practiceLockInButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");
		
		hearButton = new Button( "Hear Question" );
		hearButton.setLayoutX(Quinzical.buttonXPos + 350);
		hearButton.setLayoutY(Quinzical.buttonYStart + Quinzical.buttonYOffset );
		hearButton.setPrefSize( Quinzical.buttonXScale/2 , Quinzical.buttonYScale/2 );
		hearButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.00em; -fx-text-fill: white; ");
		hearButton.setOnAction(e-> {
			TextToSpeech.say(PracticeScene.practiceQuestionSet[0]);
		});
		
	}
	
	public Scene getPracticeQuestionScene() {
		// practice question scene
				Group practiceQuestionRoot = new Group();
				Scene practiceQuestionScene = new Scene( practiceQuestionRoot );

				StackPane practiceQuestionBackground = new StackPane();
				Canvas practiceQuestionCanvas = new Canvas( Quinzical.width, Quinzical.height );
				practiceQuestionBackground.setStyle( Quinzical.backgroundStyle );

				// practice module title
				GraphicsContext categoryTitlePrompt = practiceQuestionCanvas.getGraphicsContext2D();
				categoryTitlePrompt.setFill( Color.PURPLE );
				categoryTitlePrompt.setStroke( Color.BLACK );
				categoryTitlePrompt.setLineWidth(2);
				categoryTitlePrompt.setFont( Quinzical.titleFont );
				categoryTitlePrompt.fillText( "Practice Module", 270, 100 );
				categoryTitlePrompt.strokeText( "Practice Module", 270, 100 );

				// displays the question
				Text practiceQuestionPrompt = new Text( practiceQuestionSet[0] );
				practiceQuestionPrompt.setWrappingWidth( 800 );
				practiceQuestionPrompt.setStyle("-fx-font-size: 2.5em; ");
				practiceQuestionPrompt.setTextAlignment(TextAlignment.CENTER);
				practiceQuestionPrompt.setLayoutY(Quinzical.buttonYStart);
				practiceQuestionPrompt.setLayoutX(200);

				// user answer field
				TextField answerField = new TextField("Type Response Here!");
				answerField.setLayoutX( Quinzical.buttonXPos - 75 ) ;
				answerField.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset );
				answerField.setPrefSize( 400 , 50 );
				answerField.setStyle( Quinzical.buttonStyle );

				if (attemptsRemaining == 1) {
					// make field text red and display the first letter
					answerField.setStyle( Quinzical.buttonStyle + " -fx-text-fill: #B43757;");
					String firstLetter = "c"; // Placeholder
					answerField.setText(practiceQuestionSet[1]+ " " + firstLetter.toUpperCase());
				} 

				// display remaining attempts
				Text displayAttempts = new Text(attemptsRemaining + "\nAttempts Remaining");
				displayAttempts.setLayoutX( 50 ) ;
				displayAttempts.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 4 );
				displayAttempts.setTextAlignment(TextAlignment.CENTER);
				displayAttempts.setStyle("-fx-background-color: #d0e7ff; -fx-font-size: 1.75em; ");

				// make lock in button appear when answer field text is changed
				answerField.textProperty().addListener(
						(ObservableValue<? extends String> ov, String old_val, String new_val) -> {
							practiceLockInButton.setVisible(true);
						});
				
				newQuestionButton = new Button("Another Category");
				newQuestionButton.setLayoutX( Quinzical.buttonXPos );
				newQuestionButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 3 );
				newQuestionButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
				newQuestionButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #B43757; -fx-font-size: 2em; ");
				newQuestionButton.setOnAction(e->{
					PracticeScene ps = new PracticeScene(menuButton, guiStage);
					guiStage.setScene(ps.getPracticeScene());
				});
				
				practiceQuestionBackground.getChildren().add( practiceQuestionCanvas );
				practiceQuestionRoot.getChildren().add( practiceQuestionBackground );
				practiceQuestionRoot.getChildren().add( practiceQuestionPrompt );
				practiceQuestionRoot.getChildren().add( answerField );
				practiceQuestionRoot.getChildren().add( practiceLockInButton ); 
				practiceQuestionRoot.getChildren().add( displayAttempts ); 
				practiceQuestionRoot.getChildren().add( hearButton );
				practiceLockInButton.setVisible(false);
							
				practiceLockInButton.setOnAction(e->{
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
								answerField.setStyle( Quinzical.buttonStyle + " -fx-text-fill: #B43757;");
								String firstLetter = "" + practiceQuestionSet[2].charAt(0);
								answerField.setText(practiceQuestionSet[1]+ " " + firstLetter.toUpperCase());
							}
							displayAttempts.setText(attemptsRemaining + "\nAttempts Remaining");
						}
					}

					Attempt.save(attemptsRemaining, PRACTICEATTEMPTFILE);
				});
				
				
				if(newQuestion==true) {
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

					practiceQuestionRoot.getChildren().add(newQuestionButton);
			//		guiStage.setScene( practiceQuestionScene );
				}else {
					practiceQuestionRoot.getChildren().add(newQuestionButton);
					answerField.setText(practiceQuestionSet[1]+ " ");
					TextToSpeech.say(practiceQuestionSet[0]);

					if (attemptsRemaining == 1) {
						String firstLetter = "" + practiceQuestionSet[2].charAt(0);
						answerField.setText(practiceQuestionSet[1]+ " " + firstLetter.toUpperCase());
					}
					//guiStage.setScene( practiceQuestionScene );
				}

		return practiceQuestionScene;
		
	}
}
