package Plan.Process.Task.Operations;

import eu.robopartner.ps.planner.planninginputmodel.TASK;

import Plan.WorkingArea;

import java.util.ArrayList;

import Elements.Elements;
import Elements.Parts.Parts;
import Plan.Process.Task.Operations.Actions.*;

public class Operations extends TASK {

  private Parts basepart;

  private Parts matingpart;
  
  private Elements elements;

  public WorkingArea workingplace;

  ArrayList<Actions> actions;
              
}