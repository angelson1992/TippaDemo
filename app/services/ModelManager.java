package services;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.impl.SelectorImpl;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueNode;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueString;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;
import com.hp.hpl.jena.util.FileManager;
import play.Logger;

import javax.inject.Singleton;
import java.util.Iterator;

import static com.hp.hpl.jena.ontology.OntModelSpec.OWL_MEM;
import static com.hp.hpl.jena.ontology.OntModelSpec.OWL_MEM_TRANS_INF;

/**
 * Created by John on 6/5/2017.
 */
@Singleton
public class ModelManager {

  public final String FILE_LOCATION = "C:/Users/John/Desktop/eoUsers.rdf";
  public final String NS = "http://carryncare.com/eoUsers.rdf#";
  public final String NS_owl = "http://www.w3.org/2002/07/owl#";
  public final String NS_rdfs = "http://www.w3.org/2000/01/rdf-schema#";
  public final String NS_xsd = "http://www.w3.org/2001/XMLSchema#";
  public final String NS_edu = "http://www.carryncare.com/EducationOntology#";
  public final String NS_eoUser ="<http://carryncare.com/eoUsers.rdf#";
  public final String NS_foaf = "http://xmlns.com/foaf/0.1/";
  public final String NS_rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

  public OntModel base;
  public OntModel inf;

  public String test(){

//    OntClass testClass = inf.getOntClass(NS + "InformationalKeyIdeasAndDetailsKindergarten");

//    for (Iterator<OntClass> i = testClass.listSuperClasses(); i.hasNext(); ){
//      System.out.println(testClass.getLabel("") + " is subClass for " + i.next().getLabel(""));
//    }

//    SimpleSelector sel = new SimpleSelector(null, inf.getObjectProperty(NS+"hasApplicableGrade"), inf.getOntClass(NS+"FourthGrade"));
//
//    for (Iterator it = inf.listStatements(sel); it.hasNext(); ){
//      System.out.println(it.next());
//    }

    for(NsIterator ns = base.listNameSpaces(); ns.hasNext();){
      System.out.println(ns.nextNs());
    }
    System.out.println();

//    String queryString =
//      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
//      "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
//      "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
//      "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
//      "PREFIX edu: <http://www.carryncare.com/EducationOntology#>" +
//      "SELECT ?course ?grade ?qcontent ?pacontent ?cacontent " +
//      "WHERE { " +
//      "?course rdfs:subClassOf* edu:Geometry ." +
//      "?course edu:hasApplicableGrade ?grade ." +
//      "?course edu:hasQuestion ?question . " +
//      "?question edu:hasContent ?qcontent ." +
//      "?question edu:hasPossibleAnswer ?panswer ." +
//      "?panswer edu:hasContent ?pacontent ." +
//      "OPTIONAL {?question edu:hasCorrectAnswer ?canswer ." +
//                "?canswer edu:hasContent ?cacontent } ." +
//      "}";
//
//    Query query = QueryFactory.create(queryString);
//
//    QueryExecution qe = QueryExecutionFactory.create(query, base);
//    ResultSet results = qe.execSelect();
//
//
//
//    ResultSetFormatter.out(System.out, results, query);
//    qe.close();

//    OntClass student = base.getOntClass(NS + "Student");
//    Individual studentJohnDoe = student.createIndividual();
//    studentJohnDoe.addLabel("John Doe", null);
//    studentJohnDoe.addProperty(base.getObjectProperty("http://xmlns.com/foaf/0.1/mbox"), "johndoe2000@gmail.com");
//    studentJohnDoe.addProperty(base.getDatatypeProperty("http://xmlns.com/foaf/0.1/age"), "17");
//    studentJohnDoe.addProperty(base.getDatatypeProperty("http://xmlns.com/foaf/0.1/firstName"), "John");
//    studentJohnDoe.addProperty(base.getDatatypeProperty("http://xmlns.com/foaf/0.1/lastName"), "Doe");
//    studentJohnDoe.addProperty(base.getDatatypeProperty(NS + "password"), "password");
//
//    OntClass courseKnowledge = base.getOntClass(NS + "CourseKnowledge");
//    Individual knowledge = courseKnowledge.createIndividual();
//    knowledge.addProperty(base.getObjectProperty(NS + "hasAssociatedCourse"), base.getIndividual("http://www.carryncare.com/EducationOntology#ProblemSolvingGeometry"));
//    knowledge.addProperty(base.getObjectProperty(NS + "hasUnderstanding"), "7");
//
//    studentJohnDoe.addProperty(base.getObjectProperty(NS + "hasCourseKnowledge"), knowledge);
//


//    String courseTestQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//      "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
//      "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
//      "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
//      "PREFIX edu: <http://www.carryncare.com/EducationOntology.rdf#>\n" +
//      "PREFIX eoUser: <http://carryncare.com/eoUsers.rdf#>\n" +
//      "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
//      "SELECT ?firstName ?lastName ?courseLabel ?score\n" +
//      "WHERE { " +
//      "?student eoUser:hasCourseKnowledge ?knowledge ." +
//      "?student foaf:firstName ?firstName ." +
//      "?student foaf:lastName ?lastName ." +
//      "?knowledge eoUser:hasAssociatedCourse ?course ." +
//      "?course rdfs:label ?courseLabel ." +
//      "?knowledge eoUser:hasUnderstanding ?score" +
//      "}";

//    Triple pattern = Triple.create(Var.alloc("s"), Var.alloc("p"), Var.alloc("o"));
    ElementTriplesBlock block = new ElementTriplesBlock();
    Node studentVar = Var.alloc("student");
    Node knowledgeVar = Var.alloc("knowledge");
    Node courseVar = Var.alloc("course");
    block.addTriple(Triple.create(studentVar, base.getProperty(NS + "hasCourseKnowledge").asNode(), knowledgeVar));
    block.addTriple(Triple.create(studentVar, base.getProperty(NS_foaf + "firstName").asNode(), Var.alloc("firstName")));
    block.addTriple(Triple.create(studentVar, base.getProperty(NS_foaf + "lastName").asNode(), Var.alloc("lastName")));
    block.addTriple(Triple.create(knowledgeVar, base.getProperty(NS + "hasAssociatedCourse").asNode(), courseVar));
    block.addTriple(Triple.create(courseVar, base.getProperty(NS_rdfs + "label").asNode(), Var.alloc("courseLabel")));
    block.addTriple(Triple.create(knowledgeVar, base.getProperty(NS + "hasUnderstanding").asNode(), Var.alloc("score")));


    ElementGroup body = new ElementGroup();
    body.addElement(block);

    Query courseTestQuery = QueryFactory.make();
    courseTestQuery.setQueryPattern(body);
    courseTestQuery.setQuerySelectType();
    courseTestQuery.addResultVar("firstName");
    courseTestQuery.addResultVar("lastName");
    courseTestQuery.addResultVar("courseLabel");
    courseTestQuery.addResultVar("score");



    Individual studentJohnDoe = insertNewStudent("John", "Doe", "johndoe2020@gmail.com", "passpass");
    insertNewTeacher("Seymore", "Buts", "jokenames@yahoo.com", "sans");
    studentHasTeacher("johndoe2020@gmail.com", "jokenames@yahoo.com");
    studentHasKnowledge("johndoe2020@gmail.com", base.getResource(NS_edu + "ShapeDrawingFourthGrade"), "7");
    studentHasKnowledge("johndoe2020@gmail.com", base.getResource(NS_edu + "OperationsFourthGrade"), "4");

    for(StmtIterator itit = base.getIndividual(NS_edu + "OperationsFourthGrade").listProperties(); itit.hasNext();){
      System.out.println(itit.nextStatement());
    }
    System.out.println();



    //Query courseTestQuery = QueryFactory.create(courseTestQueryString);
    QueryExecution execution = QueryExecutionFactory.create(courseTestQuery, base);
    ResultSet resultSet = execution.execSelect();

    ResultSetFormatter.out(System.out, resultSet);
    ResultSetFormatter.out(System.out, queryStudentAccountCourseKnowledge("johndoe2020@gmail.com", "passpass"));
    ResultSetFormatter.out(System.out, queryAccountCourseQuestions("johndoe2020@gmail.com", "passpass"));

    return "SUCCESS!";

  }

  public void load(){

    String result = "";

    //create the base model
    base = ModelFactory.createOntologyModel(OWL_MEM);
    FileManager.get().readModel(base, FILE_LOCATION);
    base.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");


    //create the inference model by using the base
    inf = ModelFactory.createOntologyModel(OWL_MEM_TRANS_INF, base);

  }

  public void save(){

  }

  private Individual insertNewPerson(OntClass personClass, String firstName, String lastName, String emailAddress, String password) {
    Individual johnDoe = personClass.createIndividual();
    johnDoe.addLabel(firstName + " " + lastName, null);
    johnDoe.addProperty(base.getProperty(NS_foaf + "mbox"), emailAddress);
    johnDoe.addProperty(base.getProperty(NS_foaf + "firstName"), firstName);
    johnDoe.addProperty(base.getProperty(NS_foaf + "lastName"), lastName);
    johnDoe.addProperty(base.getProperty(NS + "password"), password);
    return johnDoe;
  }

  public Individual insertNewStudent(String firstName, String lastName, String emailAddress, String password){
    OntClass student = base.getOntClass(NS + "Student");
    return insertNewPerson(student, firstName, lastName, emailAddress, password);
  }

  public Individual insertNewTeacher(String firstName, String lastName, String emailAddress, String password){
    OntClass teacher = base.getOntClass(NS + "Teacher");
    return insertNewPerson(teacher, firstName, lastName, emailAddress, password);
  }

  public Individual insertNewParent(String firstName, String lastName, String emailAddress, String password){
    OntClass parent = base.getOntClass(NS + "Parent");
    return insertNewPerson(parent, firstName, lastName, emailAddress, password);
  }

  private Resource getAgentThroughMbox(String mbox){
    ElementTriplesBlock agent1QueryBlock = new ElementTriplesBlock();
    agent1QueryBlock.addTriple(Triple.create(Var.alloc("agent1"), base.getProperty(NS_foaf + "mbox").asNode(), NodeValue.makeNodeString(mbox).asNode()));

    ElementGroup agent1QueryBody = new ElementGroup();
    agent1QueryBody.addElement(agent1QueryBlock);
    Query agent1Query = QueryFactory.make();
    agent1Query.setQueryPattern(agent1QueryBody);
    agent1Query.addResultVar("agent1");
    agent1Query.setDistinct(true);
    agent1Query.setQuerySelectType();

    QueryExecution agent1QueryExecution = QueryExecutionFactory.create(agent1Query, base);

    ResultSet agent1Results = agent1QueryExecution.execSelect();

    if(!agent1Results.hasNext()){
      Logger.error("The  email: " + mbox + " could not be found");
      return null;
    }
    return agent1Results.next().getResource("agent1");
  }

  private void connectPeopleThroughMbox(String mbox1, String mbox2, Property connection){
    Resource agent1Node = getAgentThroughMbox(mbox1);
    Resource agent2Node = getAgentThroughMbox(mbox2);

    agent1Node.addProperty(connection, agent2Node);
  }

  public void studentHasTeacher(String studentEmail, String teacherEmail) {
    connectPeopleThroughMbox(studentEmail, teacherEmail, base.getProperty(NS + "hasTeacher"));
    connectPeopleThroughMbox(teacherEmail, studentEmail, base.getProperty(NS + "hasStudent"));

  }

  public void studentHasParent(String studentEmail, String parentEmail) {
    connectPeopleThroughMbox(studentEmail, parentEmail, base.getProperty(NS + "hasParent"));
    connectPeopleThroughMbox(parentEmail, studentEmail, base.getProperty(NS + "hasChild"));
  }

  public void studentHasKnowledge(String studentEmail, Resource course, String confidenceLevel) {
    OntClass courseKnowledge = base.getOntClass(NS + "CourseKnowledge");
    Individual knowledge = courseKnowledge.createIndividual();
    knowledge.addProperty(base.getObjectProperty(NS + "hasAssociatedCourse"), course);
    knowledge.addProperty(base.getObjectProperty(NS + "hasUnderstanding"), confidenceLevel);

    Resource studentJohnDoe = getAgentThroughMbox(studentEmail);
    studentJohnDoe.addProperty(base.getObjectProperty(NS + "hasCourseKnowledge"), knowledge);
  }

  public ResultSet queryStudentAccountCourseKnowledge(String mbox, String password){
    Resource student = getAgentThroughMbox(mbox);
    return this.queryStudentAccountCourseKnowledge(student);
  }

  public ResultSet queryStudentAccountCourseKnowledge(Resource student){
    ElementTriplesBlock block = new ElementTriplesBlock();
    Node knowledge = Var.alloc("knowledge");
    Node course = Var.alloc("course");

    block.addTriple(Triple.create(student.asNode(), base.getProperty(NS_rdf + "type").asNode(), base.getResource(NS + "Student").asNode()));
    block.addTriple(Triple.create(student.asNode(), base.getProperty(NS + "hasCourseKnowledge").asNode(), knowledge));
    block.addTriple(Triple.create(knowledge, base.getProperty(NS + "hasAssociatedCourse").asNode(), course));
    block.addTriple(Triple.create(knowledge, base.getProperty(NS + "hasUnderstanding").asNode(), Var.alloc("understanding")));

    ElementGroup queryBody = new ElementGroup();
    queryBody.addElement(block);
    Query query = QueryFactory.make();
    query.setQueryPattern(block);
    query.addResultVar("understanding");
    query.addResultVar("course");
    query.setQuerySelectType();

    QueryExecution exe = QueryExecutionFactory.create(query, base);
    return exe.execSelect();
  }

  public ResultSet queryCourseQuestions(OntClass course){

    ElementTriplesBlock block = new ElementTriplesBlock();
    Node question = Var.alloc("question");
    Node courseNode = Var.alloc("courseNode");

    block.addTriple(Triple.create(courseNode, base.getProperty(NS_rdfs + "subClassOf").asNode(), course.asNode()));
    block.addTriple(Triple.create(courseNode, base.getProperty(NS_edu + "hasQuestion").asNode(), question));
    block.addTriple(Triple.create(question, base.getProperty(NS_edu + "hasPossibleAnswer").asNode(), Var.alloc("possibleAnswer")));
    block.addTriple(Triple.create(question, base.getProperty(NS_edu + "hasCorrectAnswer").asNode(), Var.alloc("correctAnswer")));


    ElementGroup queryBody = new ElementGroup();
    queryBody.addElement(queryBody);
    Query query = QueryFactory.make();
    query.setQueryPattern(block);
    query.addResultVar("courseNode");
    query.addResultVar("question");
    query.addResultVar("possibleAnswer");
    query.addResultVar("correctAnswer");
    query.setQuerySelectType();


    QueryExecution exe = QueryExecutionFactory.create(query, inf);
    return exe.execSelect();
  }

  public ResultSet queryAccountCourseQuestions(String mbox, String password, OntClass course){

    ElementTriplesBlock block = new ElementTriplesBlock();
    Node student = Var.alloc("student");
    Node knowledge = Var.alloc("knowledge");
    Node questionNode = Var.alloc("questionNode");
    Node courseNode = Var.alloc("courseNode");
    Node possibleAnswerNode = Var.alloc("possibleAnswerNode");
    Node correctAnswerNode = Var.alloc("correctAnswerNode");
    Node gradeLevelNode = Var.alloc("gradeLevelNode");

    block.addTriple(Triple.create(student, base.getProperty(NS_foaf + "mbox").asNode(), NodeValue.makeNodeString(mbox).asNode()));
    block.addTriple(Triple.create(student, base.getProperty(NS + "password").asNode(), NodeValue.makeNodeString(password).asNode()));
    block.addTriple(Triple.create(student, base.getProperty(NS + "hasCourseKnowledge").asNode(), knowledge));
    block.addTriple(Triple.create(knowledge, base.getProperty(NS + "hasAssociatedCourse").asNode(), courseNode));
    block.addTriple(Triple.create(courseNode, base.getProperty(NS_rdfs + "label").asNode(), Var.alloc("courseLabel")));
    block.addTriple(Triple.create(courseNode, base.getProperty(NS_edu + "hasApplicableGrade").asNode(), gradeLevelNode));
    block.addTriple(Triple.create(gradeLevelNode, base.getProperty(NS_rdfs + "label").asNode(), Var.alloc("gradeLevelLabel")));
    block.addTriple(Triple.create(knowledge, base.getProperty(NS + "hasUnderstanding").asNode(), Var.alloc("understanding")));
    block.addTriple(Triple.create(courseNode, base.getProperty(NS_rdfs + "subClassOf").asNode(), course.asNode()));
    block.addTriple(Triple.create(courseNode, base.getProperty(NS_edu + "hasQuestion").asNode(), questionNode));
    block.addTriple(Triple.create(questionNode, base.getProperty(NS_edu + "hasContent").asNode(), Var.alloc("question")));
    block.addTriple(Triple.create(questionNode, base.getProperty(NS_edu + "hasPossibleAnswer").asNode(), possibleAnswerNode));
    block.addTriple(Triple.create(possibleAnswerNode, base.getProperty(NS_edu + "hasContent").asNode(), Var.alloc("possibleAnswer")));
    block.addTriple(Triple.create(questionNode, base.getProperty(NS_edu + "hasCorrectAnswer").asNode(), correctAnswerNode));
    block.addTriple(Triple.create(correctAnswerNode, base.getProperty(NS_edu + "hasContent").asNode(), Var.alloc("correctAnswer")));

    ElementGroup queryBody = new ElementGroup();
    queryBody.addElement(block);
    Query query = QueryFactory.make();
    query.setQueryPattern(block);
    query.addResultVar("courseLabel");
    query.addResultVar("understanding");
    query.addResultVar("question");
    query.addResultVar("possibleAnswer");
    query.addResultVar("correctAnswer");
    query.addResultVar("gradeLevelLabel");
    query.setQuerySelectType();

    QueryExecution exe = QueryExecutionFactory.create(query, inf);
    return exe.execSelect();

  }

  public ResultSet queryAccountCourseQuestions(String mbox, String password){

    return this.queryAccountCourseQuestions(mbox, password, base.getOntClass(NS_edu + "Course"));

  }

}