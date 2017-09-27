package a4q2;

import java.util.LinkedList;
import java.util.Set;
import java.util.*;
import java.util.Set;
import java.util.HashMap;


public class ShapeGraph extends Graph<Shape> { 
  
  public ShapeGraph() {
  }
  
  public void resetVisited() {
    Set<String>  vertexKeySet =  vertexMap.keySet(); 
    for( String key : vertexKeySet ){
      vertexMap.get(key).visited = false;
    }    
  }
  
  
  /**
   * Returns a list of lists, each inner list is a path to a node that can be reached from a given node
   * if the total area along the path to that node is greater than the threshold.
   * Your solution must be a recursive, depth first implementation for a graph traversal.
   * The Strings in the returned list of lists should be the vertex labels (keys).
   */
  
  public LinkedList<LinkedList<String>> traverseFrom(String key, float threshold){
    
    LinkedList<LinkedList<String>> masterList = new LinkedList<LinkedList<String>>();
    
    //   ADD YOUR CODE HERE.  (IF YOU WISH TO ADD A HELPER METHOD, THEN ADD IT AFTER THIS METHOD.)
    
    
    Vertex<Shape> start = this.vertexMap.get(key);
    
    masterList = helper(start, threshold, 0, masterList);
      
    resetVisited();
    
    return masterList;
  } 
  
 
  
  
  private Stack<String> cur = new Stack<String>();
   
  private LinkedList<String> contains = new LinkedList<String>();         // if vertex is on the path store it here
  
  
  
  //Helper method 
  
  private LinkedList<LinkedList<String>> helper(Vertex<Shape> vertex, float threshold, float area, LinkedList<LinkedList<String>> masterList){
   
    
    vertex.setVisited(true);
 
    cur.push(vertex.getKey());
    
    contains.add(vertex.getKey());
    
    
    if(area + vertex.element.getArea() > threshold){
      
      Stack<String> dup = (Stack<String>) cur.clone();  
      
      Stack<String> stack = new Stack<String>();
      
      LinkedList<String> paths = new LinkedList<String>();
      
      while(!dup.isEmpty())
        stack.push(dup.pop());
     
      while(!stack.isEmpty()){       
        paths.add(stack.pop());
      }
      masterList.add(paths);
    }
    
    
    for(Edge edge : vertex.adjList){
      if(!contains.contains(edge.getEndVertex().getKey()) && edge.getEndVertex().getVisited() == false){
        helper(edge.getEndVertex(), threshold, area + vertex.element.getArea(), masterList); // recursion
      }
    }
    
    cur.pop();
    
    contains.remove(contains.indexOf(vertex.getKey()));
    
    return masterList;
  }
}







