package a1posted;

/*
 *   STUDENT NAME      :  SAMI RIAZ
 *   STUDENT ID        :  260630430
 *   
 *   If you have any issues that you wish the T.A.s to consider, then you
 *   should list them here.   If you discussed on the assignment in depth 
 *   with another student, then you should list that student's name here.   
 *   We insist that you each write your own code.   But we also expect 
 *   (and indeed encourage) that you discuss some of the technical
 *   issues and problems with each other, in case you get stuck.    
 *   
 */

import java.util.LinkedList;

public class NaturalNumber  {
  
  /*
   *  To represent a natural number, we need to define the base and a list of coefficients.
   *  We will use a LinkedList, but we could have written the code with LinkedList instead.
   *  
   *  If the list has N coefficients,  then then the number represented is a polynomial:
   *  coefficients[N-1] base^{N-1} + ...  coefficients[1] base^{1} +  coefficients[0] 
   *  where base has a particular value and the coefficients are in {0, 1, ...  base - 1}
   *  
   *  For any base and any positive integer, the representation of that positive 
   *  integer as a sum of powers of that base is unique.  
   *  
   *  We require that the coefficient of the largest power is non-zero.  
   *  For example,  '354' is a valid representation (which we call "three hundred fifty four") 
   *  but '0354' is not.  
   * 
   */
  
  private int base;       
  
  private LinkedList<Integer>  coefficients;
  
  //  Constructors
  
  NaturalNumber(int base){
    
    //  If no string argument is given, then it constructs an empty list of coefficients.
    
    this.base = base;
    coefficients = new LinkedList<Integer>();
  }
  
  
  /*       
   *   The constructor builds a LinkedList of Integers where the integers need to be in [0,base).
   *   The string only represents a base 10 number when the base is given to be 10 !
   */
  
  
  NaturalNumber(String sBase10,  int base) throws Exception{
    int i;
    this.base = base;
    coefficients = new LinkedList<Integer>();
    if ((base < 2) || (base > 10)){
      System.out.println("constructor error:  base must be between 2 and 10");
      throw new Exception();
    }
    
    int l = sBase10.length();
    for (int indx = 0; indx < l; indx++){  
      i = sBase10.charAt(indx);
      if ( (i >= 48) && (i - 48 < base))   // ascii value of symbol '0' is 48, symbol '1' is 49, etc.
        // e.g.  to get the numerical value of '2',  we subtract 
        // the character value of '0' (48) from the character value of '2' (50)    
        coefficients.addFirst( new Integer(i-48) );
      else{
        System.out.println("constructor error:  all coefficients should be non-negative and less than base");
        throw new Exception();
      }   
    }
  }
  
  /*
   *   Construct a natural number object for a number that has just one digit in [0, base).  
   *   
   *   This constructor acts as a helper.  It is not called from the Tester class.
   */
  
  NaturalNumber(int i,  int base) throws Exception{
    this.base = base;
    coefficients = new LinkedList<Integer>();
    
    if ((i >= 0) && (i < base))
      coefficients.addFirst( new Integer(i) );
    else {
      System.out.println("constructor error: all coefficients should be non-negative and less than base");
      throw new Exception();
    }
  }
  
  /*
   *   The plus method computes this.plus(b) where 'this' is a,  i.e. it computes a+b.
   *   
   *   If you do not know what the Java keyword 'this' is,  then see
   *   https://docs.oracle.com/javase/tutorial/java/javaOO/thiskey.html
   * 
   */
  
  public NaturalNumber plus( NaturalNumber  second) throws Exception{
    
    //  initialize the sum as an empty list of coefficients
    
    NaturalNumber sum = new NaturalNumber( this.base );
    
    if (this.base != second.base){
      System.out.println("ERROR: bases must be the same in an addition");
      throw new Exception();
    }
    
    /*   
     * The plus method shouldn't affect the numbers themselves. 
     * So let's just work  with a copy (a clone) of the numbers. 
     * For example, in your solution, you might want to make the
     * two numbers have the same number digits by padding 0's on 
     * the upper digits of the smaller one.       
     */
    
    NaturalNumber  firstClone  = this.clone();
    NaturalNumber  secondClone = second.clone();
    
    //   If the two numbers have a different polynomial order    
    //   then pad the smaller one with zero coefficients.
    //
    
    int   diff = firstClone.coefficients.size() - second.coefficients.size();
    while (diff < 0){  // second is bigger
      
      firstClone.coefficients.addLast(0);           
      diff++;
    }
    while (diff > 0){  //  this is bigger
      secondClone.coefficients.addLast(0);
      diff--;
    }
    //   ADD YOUR CODE HERE
    
    int carry = 0;
    for(int i = 0; i < firstClone.coefficients.size(); i++){
      /* E.g:
       * 3 7 1 --> (173)
       * +
       * 5 7 5 
       * ------
       * 8 7 6 --> (678)
       */
      int rawResult1 = firstClone.coefficients.get(i); 
      int rawResult2 = secondClone.coefficients.get(i);
      int rawResult = rawResult1 + rawResult2 + carry;
      // 8          =   3    +   5                      (first iteration)
      // 14         =   7    +   7                      (second iteration)
      // 7          =   1    +   5   +    1             (third iteration)
      
      int coeffResult = rawResult % base;  // rawResult could be >base or <base
      carry = rawResult/base;
      
      sum.coefficients.addLast(coeffResult);
    }
    return sum;  
  }
  
  
  /*   
   *    Slow multiplication algorithm, mentioned in lecture 1.
   *    You need to implement the plus algorithm in order for this to work.
   *    
   *    'this' is the multiplicand i.e. a*b = a+a+a+...+a (b times) where a is multiplicand and b is multiplier
   */
  
  public NaturalNumber slowTimes( NaturalNumber  multiplier) throws Exception{
    
    NaturalNumber prod  = new NaturalNumber(0, this.base);
    NaturalNumber one   = new NaturalNumber(1, this.base);
    for (NaturalNumber counter = new NaturalNumber(0, this.base);  counter.compareTo(multiplier) < 0;  counter = counter.plus(one) ){
      prod = prod.plus(this);
    }
    return prod;
  }
  
  
  /*
   *    The multiply method must NOT be the same as what you learned in grade school since 
   *    that method uses a temporary 2D table with space proportional to the square of 
   *    the number of coefficients in the operands i.e. O(N^2).   Instead, you must write a method 
   *    that uses space that is proportional to the number of coefficients i.e. O(N).    
   *    Your algorithm will still take time O(N^2) however.   
   */
  
  /*   The multiply method computes this.multiply(b) where 'this' is a.
   */
  
  public NaturalNumber times( NaturalNumber multiplicand) throws Exception{
    
    //  initialize product as an empty list of coefficients
    
    NaturalNumber product = new NaturalNumber(this.base);
    
    if (this.base != multiplicand.base){
      System.out.println("ERROR: bases must be the same in a multiplication");
      throw new Exception();
    }
    
    //    ADD YOUR CODE HERE
    //E.g 12 * 11
    for(int i = 0; i < multiplicand.coefficients.size(); i++){
      NaturalNumber prod =helperMethod(this, multiplicand.coefficients.get(i));   // 12  // 12
      int zeros = i;   
      while(zeros != 0){
        prod.coefficients.addFirst(0); // 120  (for second iteration) 
        zeros--; 
      }
      product = product.plus(prod);   //132 = 12 + 120 
    }
    return product;
  }
  
  public static NaturalNumber helperMethod(NaturalNumber multiplicand, int digit) {    // multiply each number of the multiplicand with one of the multiplier
    NaturalNumber result = new NaturalNumber(multiplicand.base);
    LinkedList<Integer> coeff = new LinkedList<Integer>();
    int carry = 0;
    for(int i = 0; i < multiplicand.coefficients.size(); i++){    // iterate through the multiplicand
      int product = multiplicand.coefficients.get(i) * digit + carry;   // 2 = 1 * 2 + 0    // 1 * 1 + 0 = 1
      
      coeff.add(product % multiplicand.base);      // 2 % 10 = 2    // 1 % 10 = 1  
      carry = product / multiplicand.base;         // 2 / 10 = 0    // 1 / 10 = 0
    }
    if(carry != 0){    
      coeff.add(carry);   
    }
    result.coefficients = coeff; // 2 , 1    
    return result; 
  } 
  
  /*
   *   The minus method computes this.minus(b) where 'this' is a, and a > b.
   *   If a < b, then it throws an exception.
   * 
   */
  
  public NaturalNumber  minus(NaturalNumber second) throws Exception{
    
    //  initialize the result (difference) as an empty list of coefficients
    
    NaturalNumber  difference = new NaturalNumber(this.base);
    
    if (this.base != second.base){
      System.out.println("ERROR: bases must be the same in a subtraction");
      throw new Exception();
    }
    /*
     *    The minus method is not supposed to change the numbers. 
     *    But the grade school algorithm sometimes requires us to "borrow"
     *    from a higher coefficient to a lower one.   So we work
     *    with a copy (a clone) instead.
     */
    
    NaturalNumber  first = this.clone();
    
    //   You may assume 'this' > second. 
    
    if (this.compareTo(second) < 0){
      System.out.println("Error: the subtraction a-b requires that a > b");
      throw new Exception();
    }
    
    //   ADD YOUR CODE HERE                       //If E.g 21-12      // Else E.g: 25-05
    int borrow = 0;
    int i;
    for (i = 0; i < second.coefficients.size(); i++) {
     if ((first.coefficients.get(i) - borrow - second.coefficients.get(i)) < 0) {    // 1 - 0 - 2   
        difference.coefficients.add((first.coefficients.get(i) + first.base - second.coefficients.get(i)) - borrow); //1 + 10 -2 - 0 = 9
        borrow = 1;
      }
      
      else {
        difference.coefficients.add(first.coefficients.get(i) - second.coefficients.get(i) - borrow);  // 5 - 5 - 0 = 0
        borrow = 0;                            
      }                                  
    }
   
    while(i < first.coefficients.size()){
      difference.coefficients.add(first.coefficients.get(i));
      i++;
    }
    
    /*  
     *  In the case of say  100-98, we will end up with 002.  
     *  Remove all the leading 0's of the result.
     *
     *  We are giving you this code because you could easily neglect
     *  to do this check, which would mess up grading since correct
     *  answers would appear incorrect.
     */
    
    while ((difference.coefficients.size() > 1) & 
           (difference.coefficients.getLast().intValue() == 0)){
      difference.coefficients.removeLast();
    }
    
    
    
    return difference; 
  }
  
  /*   
   *    Slow division algorithm, mention in lecture 1.
   */
  
  
  public NaturalNumber slowDivide( NaturalNumber  divisor) throws Exception{
    
    NaturalNumber quotient = new NaturalNumber(0,base);
    NaturalNumber one = new NaturalNumber(1,base);
    NaturalNumber remainder = this.clone();
    while ( remainder.compareTo(divisor) >= 0 ){
      remainder = remainder.minus(divisor);
      quotient = quotient.plus(one);
    }
    return quotient;
  }
  
  
  /*  
   * The divide method divides 'this' by 'divisor' i.e. this.divide(divisor)
   *   When this method terminates, there is a remainder but it is ignored (not returned).
   *   
   */
  
  public NaturalNumber divide( NaturalNumber  divisor ) throws Exception{
    
    //  initialize quotient as an empty list of coefficients
    
    NaturalNumber  quotient = new NaturalNumber(this.base);
    
    if (this.base != divisor.base){
      System.out.println("ERROR: bases must be the same in an division");
      throw new Exception();
    }
    
    NaturalNumber  remainder = this.clone();
    
    //   ADD YOUR CODE HERE.
    
    
    return quotient;  
  }
  
//   Helper methods
  
  /*
   * The methods you write to add, subtract, multiply, divide 
   * should not alter the two numbers.  If a method require
   * that one of the numbers be altered (e.g. borrowing in subtraction)
   * then you need to clone the number and work with the cloned number 
   * instead of the original. 
   */
  
  @Override
  public NaturalNumber  clone(){
    
    //  For technical reasons that don't interest us now (and perhaps ever), this method 
    //  has to be declared public (not private).
    
    NaturalNumber copy = new NaturalNumber(this.base);
    for (int i=0; i < this.coefficients.size(); i++){
      copy.coefficients.addLast( new Integer( this.coefficients.get(i) ) );
    }
    return copy;
  }
  
  /*
   *  The subtraction method (minus) computes a-b and requires that a>b.   
   *  The a.compareTo(b) method is useful for checking this condition. 
   *  It returns -1 if a < b,  it returns 0 if a == b,  
   *  and it returns 1 if a > b.
   */
  
  private int  compareTo(NaturalNumber second){
    
    //   if  this < other,  return -1  
    //   if  this > other,  return  1  
    //   otherwise they are equal and return 0
    
    //   Assume maximum degree coefficient is non-zero.   Then,  if two numbers
    //   have different maximum degree, it is easy to decide which is larger.
    
    int diff = this.coefficients.size() - second.coefficients.size();
    if (diff < 0)
      return -1;
    else if (diff > 0)
      return 1;
    else { 
      
      //   If two numbers have the same maximum degree,  then it is a bit trickier
      //   to decide which number is larger.   You need to compare the coefficients,
      //   starting from the largest and working toward the smallest until you find
      //   coefficients that are not equal.
      
      boolean done = false;
      int i = this.coefficients.size() - 1;
      while (i >=0 && !done){
        diff = this.coefficients.get(i) - second.coefficients.get(i); 
        if (diff < 0){
          return -1;
        }
        else if (diff > 0)
          return 1;
        else{
          i--;
        }
      }
      return 0;    //   if all coefficients are the same,  so numbers are equal.
    }
  }
  
  /*  computes  'this' * base^n  
   */
  
  private NaturalNumber timesBaseToThePower(int n){
    for (int i=0; i< n; i++){
      this.coefficients.addFirst(new Integer(0));
    }
    return this;
  }
  
//   The following method is invoked by System.out.print.
//   It returns the string with coefficients in the reverse order 
//   which is the natural format for people to reading numbers,
//   i.e..  [ a[N-1], ... a[2], a[1], a[0] ] as in the Tester class. 
//   It does so simply by make a copy of the list with elements in 
//   reversed order, and then printing the list using the LinkedList's
//   toString() method.
  
  @Override
  public String toString(){ 
    String s = new String(); 
    for (Integer i : coefficients)    //  Java enhanced for loop
      s = i.toString() + s ;        //   coefficient i corresponds to degree i
    return "(" + s + ")_" + base;  
  }
  
}

