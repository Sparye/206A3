import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuestionSelector {
	public static void copyRandomCategories(String QUESTIONBANKFILE, String GAMEQUESTIONSFILE)  {
		try {
			
			ArrayList<String> categoryArray = QuestionSelector.getCategoriesInFile(QUESTIONBANKFILE);
			// choose five at random
			Random randomCategory = new Random();
			Collections.shuffle(categoryArray);
			while (categoryArray.size() > 5) {
				categoryArray.remove(randomCategory.nextInt(categoryArray.size()));
				Collections.shuffle(categoryArray);
			}
			
			// for each category
			PrintWriter copyQuestions = new PrintWriter(new FileWriter(GAMEQUESTIONSFILE));
			for (int i = 0 ; i < categoryArray.size() ; i++) {
				
				// get list of questions in category
				ArrayList<String> questionArray = new ArrayList<String>();
				String line;
				BufferedReader readQuestions = new BufferedReader(new FileReader(QUESTIONBANKFILE));
				while((line = readQuestions.readLine()) != null) {
					if (line.equals(categoryArray.get(i))) {
						break;
					}
				}
				while(((line = readQuestions.readLine()) != null) && (line.indexOf('(') >= 0 )) {
					questionArray.add( line );
				}
				readQuestions.close();
				
				// choose five questions at random
				Random randomQuestions = new Random();
				Collections.shuffle(questionArray);
				while (questionArray.size() > 5) {
					questionArray.remove(randomQuestions.nextInt(questionArray.size()));
					Collections.shuffle(questionArray);
				}
				
				int value = 100;
				// copy category and questions
				copyQuestions.println(categoryArray.get(i));
				for (int j = 0; j < questionArray.size() ; j++) {
					copyQuestions.println(value + questionArray.get(j));
					value += 100;
				}
				
				copyQuestions.println("");
			}
			copyQuestions.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static boolean deleteLinesContaining(String string, String GAMEQUESTIONSFILE)  {
		File tempFile = new File(GAMEQUESTIONSFILE + ".tmp");
		File questionFile = new File(GAMEQUESTIONSFILE);
		try {
			BufferedReader readLines = new BufferedReader(new FileReader(GAMEQUESTIONSFILE));
			PrintWriter writeLines = new PrintWriter(new FileWriter(tempFile));
			
			String currentLine;
			while((currentLine = readLines.readLine()) != null ) {
				if (currentLine.contains(string)) continue;
				writeLines.println(currentLine);
			}
			readLines.close();
			writeLines.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ( tempFile.renameTo(questionFile) );
	}

	public static String[] getQuestionSetFromValue(String value, String category, String GAMEQUESTIONSFILE)  {
		String question = "Couldn't find a question (What is) disappointment/sorry";
		try {
			BufferedReader readLines = new BufferedReader(new FileReader(GAMEQUESTIONSFILE));
			
			String currentLine;
			while((currentLine = readLines.readLine()) != null ) {
				if (currentLine.equals(category)) {
					break;
				}
			}
			while((currentLine = readLines.readLine()) != null ) {
				if (currentLine.startsWith(value)) {
					question = currentLine.split(value,2)[1];
					break;
				}
			}
			readLines.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return LineToVar.toVarSet(question);
	}
	
	public static ArrayList<String> getCategoriesInFile(String CATEGORYFILE)  {
		ArrayList<String> categoryArray = new ArrayList<String>();
		try {
				String lineToRead;
				// Add category names to array list
				BufferedReader readCategories = new BufferedReader(new FileReader(CATEGORYFILE));
				while((lineToRead = readCategories.readLine()) != null) {
					if (lineToRead.indexOf('(') == -1 ) {
						if (!lineToRead.isBlank()) {
							categoryArray.add(lineToRead);
						}
					}
				}
				readCategories.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryArray;
	}
	
	public static ArrayList<Integer> getQuestionsRemainingCount(String QUESTIONFILE) {
		ArrayList<String> categoryArray = QuestionSelector.getCategoriesInFile(QUESTIONFILE);
		ArrayList<Integer> questionCountArray = new ArrayList<Integer>();
		try {
		for (int i = 0 ; i < categoryArray.size() ; i++) {
			
			String line;
			Integer questionCount = 0;
			BufferedReader readQuestions = new BufferedReader(new FileReader(QUESTIONFILE));
			while((line = readQuestions.readLine()) != null) {
				if (line.equals(categoryArray.get(i))) {
					break;
				}
			}
			while(((line = readQuestions.readLine()) != null) && (line.indexOf('(') >= 0 )) {
				questionCount++;
			}
				readQuestions.close();
			questionCountArray.add(questionCount);
		
	}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return questionCountArray;
	}
}
