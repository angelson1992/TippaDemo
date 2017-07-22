package controllers;

import play.mvc.Result;
import services.ModelManager;
import services.TwilioManager;
import services.Question;

import javax.inject.Inject;
import javax.inject.Singleton;

import static play.mvc.Results.ok;

/**
 * Created by John on 7/7/2017.
 */

public class TwilioController {

  private final ModelManager model;
  private final TwilioManager twi;
  private final QuizController quiz;

  @Inject
  public TwilioController(ModelManager model, TwilioManager twi, QuizController quiz){this.model = model; this.twi = twi; this.quiz = quiz;}

  public Result textQuiz(String email, String password){

    Question question = quiz.collectQuestions(email, password).get(0);
    twi.sendMessage("4049817493", question.getQuestion() + "\n" + question.getCorrectAnswer() + "\nor\n" + question.getIncorrectAnswers().get(0));
    return ok();
  }

}
