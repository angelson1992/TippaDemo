package controllers;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Resource;
import play.mvc.Result;
import services.ModelManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.io.InputStream;

import static play.mvc.Results.*;

/**
 * Created by John on 6/28/2017.
 */

public class CozmoController {

  private final ModelManager manager;

  @Inject
  public CozmoController(ModelManager manager){this.manager = manager;}

  public Result runRequest(String email, String password, String request){

    if(request.compareTo("chooseQuestion") == 0){
      return ok(chooseQuestion(email, password));
    }
    return badRequest("Command not found");
  }

  private String chooseQuestion(String email, String password){


    ResultSet courseKnowledge = manager.queryAccountCourseQuestions(email, password);

    return ResultSetFormatter.asXMLString(courseKnowledge);
  }

}
