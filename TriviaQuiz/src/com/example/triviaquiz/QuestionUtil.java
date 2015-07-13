package com.example.triviaquiz;

import java.util.ArrayList;

public class QuestionUtil {
	static public class QuestionParser {
		static ArrayList<String> completeText;
		static ArrayList<Question> questions;
		static Question question;
		static ArrayList<String> options;

		static public ArrayList<Question> parseQuestion(
				ArrayList<String> completeText) {
			String[] temp;
			questions = new ArrayList<Question>();
			for (String qn : completeText) {
				temp = qn.split("[;]");
				question = new Question();

				if (temp[0] != "") {
					question.setQuestion(temp[0]);
					if (temp[temp.length - 1] != "") {
						question.setCorrectAnswerIndex(Integer
								.parseInt(temp[temp.length - 1]));
					}

					if (temp[temp.length - 2] != "") {
						question.setImageURL(temp[temp.length - 2]);
					}
					options = new ArrayList<String>();
					for (int i = 1; i < temp.length - 2; i++) {

						options.add(temp[i]);
					}
					question.setAnswerOptions(options);
					temp = null;

					if (question.getQuestion().length() != 0) {
						questions.add(question);
					}
				}
			}
			return questions;
		}
	}
}
