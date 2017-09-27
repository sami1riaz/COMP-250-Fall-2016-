package a2posted;

/**
 * This static class provides the isStatement() method to parse a sequence 
 * of tokens and to decide if it forms a valid statement
 * You are provided with the helper methods isBoolean() and isAssignment().
 * 
 * - You may add other methods as you deem necessary.
 * - You may NOT add any class fields.
 */
public class LanguageParser {
  
  /**
   * Returns true if the given token is a boolean value, i.e.
   * if the token is "true" or "false".
   * 
   * DO NOT MODIFY THIS METHOD.
   */
  
  private static boolean isBoolean (String token) {
    
    return (token.equals("true") || token.equals("false"));
    
  }
  
  /**
   * Returns true if the given token is an assignment statement of the
   * type "variable=value", where the value is a non-negative integer.
   * 
   * DO NOT MODIFY THIS METHOD.
   */
  private static boolean isAssignment (String token) {
    
    // The code below uses Java regular expressions. You are NOT required to
    // understand Java regular expressions, but if you are curious, see:
    // <http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html>
    //
    //   This method returns true iff 
    //   the token matches the following structure:
    //   one or more letters (a variable), followed by
    //   an equal sign '=', followed by
    //   one or more digits.
    //   However, the variable cannot be a keyword ("if", "then", "else", 
    //   "true", "false")
    
    if (token.matches("if=\\d+") || token.matches("then=\\d+") ||
        token.matches("else=\\d+") || token.matches("end=\\d+") ||
        token.matches("true=\\d+") || token.matches("false=\\d+"))
      return false;
    else
      return token.matches("[a-zA-Z]+=\\d+");
    
  }
  
  /**
   * Given a sequence of tokens through a StringSplitter object,
   * this method returns true if the tokens can be parsed according
   * to the rules of the language as specified in the assignment.
   */
  
  public static boolean  isStatement(StringSplitter splitter) {
    
    StringStack stack = new StringStack();
    int count = splitter.countTokens();
    String token;
    
    if (count == 0) // if there is no token i.e nothing entered
      return false;
    
    
    //   ADD YOUR CODE HERE          //
    
    
    if ( (count == 1) && ( isAssignment(splitter.nextToken())) ){
      return true;
    } else if ( (count == 1) && ( !isAssignment(splitter.nextToken()))){
      return false;
    }
    
    // push tokens to the stack until you reach first "end"
    // pop until you reach the its correlated "if"
    // pop "if"
    
    
    token = splitter.nextToken();         
    stack.push(token);
    
    // if all tokens are in the correct order push all of them on the stack
    
    while ( splitter.hasMoreTokens() ){
      token = splitter.nextToken();         
      
      if ( (stack.peek()).equals("if") && ( isBoolean(token))) { // if "If" is on top of stack, push next token if it is "Boolean" or "Assignment"
        stack.push(token);
      }
      else if ( isBoolean(stack.peek()) && token.equals("then")){ // if "Boolean" is on top of stack, push next token if it is "Then"
        stack.push(token);
      }
      else if ( ( stack.peek() ).equals("then") && ( isAssignment(token) || ( token.equals("if")))){ // if "Then" is on top of stack, push next token if it is "If" or "Assignment"
        stack.push(token);
      }
      else if ( isAssignment(stack.peek()) && ( token.equals("else") || token.equals("end") ) ){ // if "Assignment" is on top of stack, push next token if it is "Else" or "End"
        stack.push(token);
      }
      else if ( (stack.peek()).equals("else") && ( isAssignment(token) || token.equals("if"))){ // if "Else" is on top of stack, push next token if it is "If" or "Assignment"
        stack.push(token);
      }
      else if ( (stack.peek()).equals("end") && ( token.equals("end") || token.equals("else") ) ){ // if "End" is on top of stack, push next token if it is "End" or "assignment"
        stack.push(token);
      }
      
      
      else {              // if tokens do not follow above pattern return false
        return false;
      }
    }
    
    int numOfIfs = 0;
    int numOfEnds = 0;
    int numOfElse = 0;
    
//pop everything off stack, and count number of ifs, ends and elses popped
    
    if ( (stack.peek()).equals("end")){
      while ( !stack.isEmpty() ){
        
        if(stack.peek().equals("end"))  {
          numOfEnds++;
        }
        else if (stack.peek().equals("if")) {
          numOfIfs++;
        }
        else if (stack.peek().equals("else")) {
          numOfElse++;
        }
        
        stack.pop();
      }
    }
    else {
      //if stack top doesnt have end, then its wrong statement
      return false;
      
    }
    
    if (stack.isEmpty() && (numOfIfs == numOfEnds) && (numOfIfs == numOfElse)){ // if number of ifs, ends and elses is equal then statement is valid
      return true;
    }
    
    return false;
    
  }
} 

