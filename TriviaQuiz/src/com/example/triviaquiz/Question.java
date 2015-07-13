package com.example.triviaquiz;

import java.util.ArrayList;

public class Question {
	String question;
	String imageURL;
	ArrayList<String> answerOptions;
	int correctAnswerIndex;

	

	

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public ArrayList<String> getAnswerOptions() {
		return answerOptions;
	}

	public void setAnswerOptions(ArrayList<String> answerOptions) {
		this.answerOptions = answerOptions;
	}

	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

	public void setCorrectAnswerIndex(int correctAnswerIndex) {
		this.correctAnswerIndex = correctAnswerIndex;
	}
}
