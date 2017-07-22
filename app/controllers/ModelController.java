package controllers;

import play.mvc.Controller;
import services.ModelManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by John on 6/19/2017.
 */
@Singleton
public class ModelController extends Controller {

  private final ModelManager model;

  @Inject
  public ModelController(ModelManager model){this.model = model;}

}
