package ir.assignments.three;

public class MainStatistics {
	public static void main(String[] args) {
		DocumentStorage documentStorage = new DocumentStorage("docStorage.yap");
		try {
			AnswerQuestions.Answer(documentStorage);
		}
		finally {
			documentStorage.close();
		}
	}
}
