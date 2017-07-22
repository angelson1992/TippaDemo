package services;

import java.util.ArrayList;

/**
 * Created by John on 7/6/2017.
 */
public class Question {

  private String gradeLevel;
  private String course;
  private String question;
  private String correctAnswer;
  private ArrayList<String> incorrectAnswers;

  public Question(){
    incorrectAnswers = new ArrayList<>();
  }

  public String getGradeLevel() {
    return gradeLevel;
  }

  public void setGradeLevel(String gradeLevel) {
    this.gradeLevel = gradeLevel;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getCorrectAnswer() {
    return correctAnswer;
  }

  public void setCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
  }

  public ArrayList<String> getIncorrectAnswers() {
    return incorrectAnswers;
  }
}
