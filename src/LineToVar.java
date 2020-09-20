
public class LineToVar {
	public static String[] toVarSet(String line) {
		String[] firstSplit=line.split("\\(");
		
		//clean up question
		String question=firstSplit[0].trim();
		while(question.endsWith(",")||question.endsWith(".")) {
			question=question.substring(0, question.length()-1);
		}
		question=question.trim();

		//clean up answer prefix
		String[] secondSplit=firstSplit[1].split("\\)");
		String answerPrefix = secondSplit[0];
		answerPrefix=answerPrefix.trim();
		
		//clean up answer
		String answer = secondSplit[1].trim();
		while(answer.endsWith(",")||answer.endsWith(".")) {
			answer=answer.substring(0, answer.length()-1);
		}
		answer=answer.trim();

		String[] varSet= {question,answerPrefix,answer};

		return varSet;
		
	}
}
