package application;
import data.Score;
import data.Trophies;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import questions.QuestionSelector;
import questions.TextToSpeech;

public class GridScene{
	Button menuButton;

	Stage guiStage;

	public GridScene(Button inMenuButton,Stage inGuiStage) {

		menuButton = inMenuButton;
	//	gameLockInButton = inGameLockInButton;
		guiStage = inGuiStage;
		

		
	}

	// file names to use
	public static final String QUESTIONBANKFILE = "categories";
	public static final String GAMEQUESTIONSFILE = "GameData/Game/Questions";
	public static final String GAMESCOREFILE = "GameData/Game/.Score";

	// default data
	int currentScore=0;

	
	Group gameRoot = new Group();
	Scene gameScene = new Scene( gameRoot );

	StackPane gameBackground = new StackPane();

	Canvas gameCanvas = new Canvas( Quinzical.width, Quinzical.height );
	GridPane gameGrid = new GridPane();
	
	//Label for displaying score
	Label displayScore;
	Label scoreHeading = new Label("SCORE");
	
	Button newGameButton = new Button( "New Game" );
	Button continueButton = new Button( "Question Grid" );


	public Scene getGridScene() {
		gameBackground.setStyle( Quinzical.backgroundStyle );
		
		newGameButton.setLayoutX( Quinzical.buttonXPos + 380);
		newGameButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 3 );
		newGameButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
		newGameButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");
		newGameButton.setOnAction(e-> {
			Score.reset(GAMESCOREFILE);
			QuestionSelector.reset(GAMEQUESTIONSFILE);
			GridScene gs = new GridScene( menuButton, guiStage);
			guiStage.setScene(gs.getGridScene());
		});
//		GridPane gameGrid = new GridPane();
		// create new question set when game is reset
		if(QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).size() < 1) {
			QuestionSelector.copyRandomCategories(QUESTIONBANKFILE, GAMEQUESTIONSFILE);
		}

		gameGrid.setVgap(10);
		gameGrid.setHgap(30);		

		int checktotal=0;
		int emptyCategories = 0;
		for(int c=0;c<QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).size();c++) {
			int count = QuestionSelector.getQuestionsRemainingCount(GAMEQUESTIONSFILE).get(c);
			checktotal += count;
			if (count == 0)
				emptyCategories++;
		}
		
		if (emptyCategories >= 2 && !QuestionSelector.isUnlocked()) {
			QuestionSelector.unlock();
			
			continueButton.setLayoutX( Quinzical.buttonXPos );
			continueButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 3 );
			continueButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
			continueButton.setStyle("-fx-text-fill: #D4D4D4; -fx-background-color: #B43757; -fx-font-size: 1.75em; ");
			continueButton.setOnAction(e->{
				GridScene gs = new GridScene( menuButton, guiStage);
				guiStage.setScene(gs.getGridScene());
			});
			
			Text WinningPrompt=new Text();;
			WinningPrompt.setText( "You have unlocked the International Section!\n"
					+ " You can restart to play games with International questions,"
					+ " or continue with the current game.");
			WinningPrompt.setWrappingWidth( 800 );
			WinningPrompt.setStyle("-fx-font-size: 2.5em; ");
			WinningPrompt.setTextAlignment(TextAlignment.CENTER);
			WinningPrompt.setLayoutY(Quinzical.buttonYStart);
			WinningPrompt.setLayoutX(200);

			newGameButton.setLayoutX( Quinzical.buttonXPos );
			newGameButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 );
			newGameButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
			
			gameGrid.setLayoutX( Quinzical.buttonXPos -250);
			gameGrid.setLayoutY(Quinzical.buttonYStart);
			gameBackground.getChildren().add( gameCanvas );
			gameRoot.getChildren().add( gameBackground );
		//	gameRoot.getChildren().add( menuButton );
			gameRoot.getChildren().add( continueButton );
			gameRoot.getChildren().add(newGameButton);
			gameRoot.getChildren().add(WinningPrompt);
			//	gameRoot.getChildren().add(gameGrid);

			//guiStage.setScene( gameScene );
			return gameScene;
		}

		if(checktotal==0) {
			// display the question
			Text WinningPrompt=new Text();;
			int score = Score.getSumAndSave("0");
			WinningPrompt.setText( "You have completed all questions!\nYour final score is: "+ score + Trophies.get());
			WinningPrompt.setWrappingWidth( 800 );
			WinningPrompt.setStyle("-fx-font-size: 2.5em; ");
			WinningPrompt.setTextAlignment(TextAlignment.CENTER);
			WinningPrompt.setLayoutY(Quinzical.buttonYStart);
			WinningPrompt.setLayoutX(200);
			
			newGameButton.setLayoutX( Quinzical.buttonXPos );
			newGameButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 );
			newGameButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );

			gameGrid.setLayoutX( Quinzical.buttonXPos -250);
			gameGrid.setLayoutY(Quinzical.buttonYStart);
			gameBackground.getChildren().add( gameCanvas );
			gameRoot.getChildren().add( gameBackground );
			gameRoot.getChildren().add(menuButton);
			gameRoot.getChildren().add(newGameButton);
			gameRoot.getChildren().add(WinningPrompt);

			return gameScene;
		}else {
			currentScore=Score.getSumAndSave("0");
			displayScore = new Label(" "+currentScore);
			displayScore.setLayoutX( 77 );
			displayScore.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 + 170 );
			displayScore.setTextAlignment(TextAlignment.CENTER);
			displayScore.setStyle("-fx-font-size: 5em; ");
			

			scoreHeading.setLayoutX( 77 );
			scoreHeading.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 2 + 150 );
			scoreHeading.setTextAlignment(TextAlignment.CENTER);
			scoreHeading.setStyle("-fx-font-size: 2em; ");
			
			

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
					moneyButton.setPrefSize(Quinzical.buttonXScale/2, Quinzical.buttonYScale/2);
					moneyButton.setStyle("-fx-background-color: #003399; -fx-font-size: 1.75em; -fx-text-fill: white; -fx-font-weight: bold");
					moneyButton.setOnAction(eve->{
						//	System.out.println(QuestionSelector.getQuestionSetFromValue(moneyButton.getText(), QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10), GAMEQUESTIONSFILE)[0]);
						QuestionScene.gameQuestionSet=QuestionSelector.getQuestionSetFromValue(moneyButton.getText(), QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10), GAMEQUESTIONSFILE);	
						QuestionSelector.deleteLinesContaining(QuestionScene.gameQuestionSet[0], GAMEQUESTIONSFILE);
						QuestionScene qs = new QuestionScene( menuButton,  guiStage, QuestionScene.gameQuestionSet, moneyButton);
						guiStage.setScene(qs.getQuestionScene());
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


			gameGrid.setLayoutX( Quinzical.buttonXPos -250);
			gameGrid.setLayoutY(Quinzical.buttonYStart);
			gameBackground.getChildren().add( gameCanvas );
			gameRoot.getChildren().add( gameBackground );
			gameRoot.getChildren().add( displayScore );
			gameRoot.getChildren().add( scoreHeading );
			gameRoot.getChildren().add(newGameButton);
			gameRoot.getChildren().add(menuButton);
			gameRoot.getChildren().add(gameGrid);


		}
		return gameScene;
	}
}

