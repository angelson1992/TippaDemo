package controllers;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import play.mvc.Result;
import static play.mvc.Results.*;
import services.ModelManager;
import services.Question;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

/**
 * Created by John on 7/4/2017.
 */
@Singleton
public class QuizController {

  public final ModelManager model;

  @Inject
  public QuizController(ModelManager model){this.model = model;}

  public Result quizPage(String email, String password){

    ArrayList<Question> questions = collectQuestions(email, password);

    return ok(quiz.render(questions));
  }

  public int findQuestion(ArrayList<Question> questions, String question){
    int result = -1;

    for(int i = 0; i < questions.size(); i++){

      if(questions.get(i).getQuestion().compareTo(question) == 0){
        result = i;
      }

    }

    return result;
  }

  public ArrayList<Question> collectQuestions(String email, String password){

    ArrayList<Question> questions = new ArrayList<>();

    for(ResultSet results = model.queryAccountCourseQuestions(email, password);results.hasNext();){

      QuerySolution line = results.next();

      int questionID = findQuestion(questions, line.getLiteral("question").getString());
      if(questionID == -1){
        Question tempQuestion = new Question();
        tempQuestion.setQuestion(line.getLiteral("question").getString());
        tempQuestion.setCourse(line.getLiteral("courseLabel").getString());
        tempQuestion.setGradeLevel(line.getLiteral("gradeLevelLabel").getString());
        tempQuestion.setCorrectAnswer(line.getLiteral("correctAnswer").getString());
        if(line.getLiteral("possibleAnswer").getString().compareTo(line.getLiteral("correctAnswer").getString())!=0){
          //System.out.println("          Added possible Answer " + line.getLiteral("possibleAnswer").getString() + " to " + tempQuestion.getQuestion());
          tempQuestion.getIncorrectAnswers().add(line.getLiteral("possibleAnswer").getString());
        }
        questions.add(tempQuestion);
        //System.out.println("          Added new question" + line.getLiteral("question").getString());
      }else{
        Question foundQuestion = questions.get(questionID);
        if(!foundQuestion.getIncorrectAnswers().contains(line.getLiteral("possibleAnswer").getString()) && !(foundQuestion.getCorrectAnswer().compareTo(line.getLiteral("possibleAnswer").getString())==0)){
          foundQuestion.getIncorrectAnswers().add(line.getLiteral("possibleAnswer").getString());
          //System.out.println("          Added possible Answer " + line.getLiteral("possibleAnswer").getString() + " to " + foundQuestion.getQuestion());
        }
      }

      //System.out.println(line);

    }

    return questions;

  }

}
