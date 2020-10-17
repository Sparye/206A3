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

public class GridScene{
	Button restartButton,menuButton,gameLockInButton,hearGameButton,dontKnowButton;

	Stage guiStage;

	public GridScene(Button inRestartButton,Button inMenuButton,Button inGameLockInButton,Button inHearGameButton,Button inDontKnowButton,Stage inGuiStage) {
		restartButton = inRestartButton;
		menuButton = inMenuButton;
		gameLockInButton = inGameLockInButton;
		hearGameButton = inHearGameButton;
		dontKnowButton = inDontKnowButton;
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
	Label scoreHeading = new Label(" Score");
	
	Button newGameButton = new Button( "New Game" );


	public Scene getGridScene() {
		gameBackground.setStyle( Quinzical.backgroundStyle );
		
		newGameButton.setLayoutX( Quinzical.buttonXPos + 380);
		newGameButton.setLayoutY( Quinzical.buttonYStart + Quinzical.buttonYOffset * 3 );
		newGameButton.setPrefSize( Quinzical.buttonXScale , Quinzical.buttonYScale );
		newGameButton.setStyle("-fx-background-color: #50C878; -fx-font-size: 2em; ");
		newGameButton.setOnAction(e-> {
			Score.reset(GAMESCOREFILE);
			QuestionSelector.reset(GAMEQUESTIONSFILE);
			GridScene gs = new GridScene(restartButton, menuButton, gameLockInButton, hearGameButton, dontKnowButton, guiStage);
			guiStage.setScene(gs.getGridScene());
		});
	//	GridPane gameGrid = new GridPane();
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
			Text WinningPrompt=new Text();;
			WinningPrompt.setText( "You have unlocked the International Section!\n"
					+ " You can restart to play games with International questions,"
					+ " or return to the menu then continue with the current game.");
			WinningPrompt.setWrappingWidth( 800 );
			WinningPrompt.setStyle("-fx-font-size: 2.5em; ");
			WinningPrompt.setTextAlignment(TextAlignment.CENTER);
			WinningPrompt.setLayoutY(Quinzical.buttonYStart);
			WinningPrompt.setLayoutX(200);

			gameGrid.setLayoutX( Quinzical.buttonXPos -250);
			gameGrid.setLayoutY(Quinzical.buttonYStart);
			gameBackground.getChildren().add( gameCanvas );
			gameRoot.getChildren().add( gameBackground );
			gameRoot.getChildren().add( menuButton );
			gameRoot.getChildren().add(restartButton);
			gameRoot.getChildren().add(WinningPrompt);
			//	gameRoot.getChildren().add(gameGrid);

			guiStage.setScene( gameScene );
			
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


			gameGrid.setLayoutX( Quinzical.buttonXPos -250);
			gameGrid.setLayoutY(Quinzical.buttonYStart);
			gameBackground.getChildren().add( gameCanvas );
			gameRoot.getChildren().add( gameBackground );
			gameRoot.getChildren().add(menuButton);
			gameRoot.getChildren().add(restartButton);
			gameRoot.getChildren().add(WinningPrompt);
			//	gameRoot.getChildren().add(gameGrid);

			guiStage.setScene( gameScene );
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
						dontKnowButton.setText("Don't know");
						//	System.out.println(QuestionSelector.getQuestionSetFromValue(moneyButton.getText(), QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10), GAMEQUESTIONSFILE)[0]);
						QuestionScene.gameQuestionSet=QuestionSelector.getQuestionSetFromValue(moneyButton.getText(), QuestionSelector.getCategoriesInFile(GAMEQUESTIONSFILE).get(Integer.parseInt(moneyButton.getId())/10), GAMEQUESTIONSFILE);	
						QuestionSelector.deleteLinesContaining(QuestionScene.gameQuestionSet[0], GAMEQUESTIONSFILE);
						QuestionScene qs = new QuestionScene(restartButton, menuButton, gameLockInButton, hearGameButton, dontKnowButton, guiStage, QuestionScene.gameQuestionSet, moneyButton);
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

