
/** 

* Program class for an SRPN calculator. 

*/

//Imports necessary libraries

import java.util.*;

import java.lang.Math;

public class SRPN {

  // Declare and initialise variables

  private Stack<Integer> nums = new Stack<>();

  private Stack<Integer> dStack = new Stack<>(); // Display Stack used to display
  // input and for performing stack functions

  private Stack<Integer> tempS = new Stack<>(); // Temporary stack used to display
  // last value of stack after calculated
  // value popped from stack
  int rCount = 0;

  public void processCommand(String s) {

    // Declare and initialise variables

    String stack = s;

    // Splits the input to handle multiple operators

    // in a line

    String[] stackArray = stack.split("((?<=[+-/*^d #=])|(?=[+-/*^d #=]))");

    int len = stackArray.length;

    // Loops through each of the elements of the stackArray

    for (int i = 0; i < len; i++) {

      // Handles comments entered in the input

      if (stackArray[i].equals("#") && stackArray[i + 1].equals(" ")) {

        i++;

        while (!(stackArray[i].equals("#") && stackArray[i - 1].equals(" "))) {

          i++;

        }

        i++;

        if (i >= len) {

          break;

        }

      }

      // Uses switch for similar operators

      // Checks input against the cases presented and executes the desired method

      try {

        switch (stackArray[i]) {

          case "+":
            addition(nums.peek()); // Peek used to save value

            continue; // Continue used to move to next element in split array

          case "*":
            multiply(nums.peek());

            continue;

          case "/":
            divide(nums.peek());

            continue;

          case "-":
            minus(nums.peek());

            continue;

          case "%":
            remainder(nums.peek());

            continue;

          case "^":
            power(nums.peek());

            continue;

          case "d":
            display();

            continue;

        }

      } catch (EmptyStackException e) {

        System.out.println("Stack underflow.");

        continue;

      }

      if (i >= len) {

        break;

      }

      if (stackArray[i].equals(" ")) {// if input is space moves to next element

        continue;

      }

      else if (stackArray[i].equals("=")) {

        // displays the value at the top of the stack to show the result of the last
        // calculation

        if (Arrays.asList(stackArray).indexOf("=") == 0) {

          try {

            if (s.contentEquals("=")) {

              System.out.println(nums.peek());

            }

          } catch (EmptyStackException e) {

            System.out.println("Stack empty");

          }

        }

      }

      // Calls random number from array of pseudo-random numbers

      else if (stackArray[i].equals("r")) {

        if (rCount == 22) {

          rCount = 0;

        }

        int currentRand = random(rCount);

        if (nums.size() < 23) {

          nums.push(currentRand);

          tempS.push(currentRand);

          rCount++; // Adds to randomCount so next time a new number is used

        }

        else {

          System.out.println("Stack overflow.");

          tempS.push(nums.peek());// checks and //return value from the temporary stack

        }

      }

      // moves to next line

      else if (stackArray[i].equals("")) {

        System.out.println("");

      }

      // passes value to the input method

      else {

        inVal(stackArray[i]);

      }

    }

    // Resets temporary stack ready for next input

    tempS.clear();

  }

  // Method used when no operator and incorrect operator, string is parsed to an
  // int and pushed //onto the numS stack.

  // If the stack isn't full (above 23 numbers), //add the number passed to the
  // stack,

  // otherwise, output stack overflow

  // error message displays unrecognised operator //or operand

  public void inVal(String integerStrg) {

    try {

      int val = Integer.parseInt(integerStrg);

      if (nums.size() < 23) {

        nums.push(val);

        tempS.push(val);

      }

      else {

        System.out.println("Stack overflow.");

      }

    } catch (NumberFormatException e) {

      System.out.println("Unrecognised operator or operand " + "\"" + integerStrg + "\".");

    }

  }

  // specifying the variable storage for the maximum or minimum possible values
  // the data type can hold//

  public void checkSaturation(int result, long total, int num2, int num1) {

    if (total > Integer.MAX_VALUE) {

      result = Integer.MAX_VALUE; // If max is exceeded, push max value to stack

      nums.push(result);

    }

    else if (total < Integer.MIN_VALUE) {

      result = Integer.MIN_VALUE;

      nums.push(result); // If min is exceeded, push min value to stack

    }

    else {

      nums.push(result);

    }

  }

  // addition function

  public void addition(int lastVal) { // Last value //parameter used to add value back to stack in the event of error

    try {

      int num1 = nums.pop();

      int num2 = nums.pop();

      long total = (long) num2 + (long) num1; // Convert to long to check for saturation

      int result = num2 + num1;

      checkSaturation(result, total, num2, num1);

    } catch (EmptyStackException e) {

      System.out.println("Stack underflow."); // If there are not enough operands to perform calc then display error

      nums.push(lastVal); // Last value is pushed back onto stack

    }

  }

  // Performs multiplication

  public void multiply(int lastVal) {

    try {

      int num1 = nums.pop();

      int num2 = nums.pop();

      long total = (long) num2 * (long) num1;

      int result = num2 * num1;

      checkSaturation(result, total, num2, num1);

    } catch (EmptyStackException e) {

      System.out.println("Stack underflow");

      nums.push(lastVal);

    }

  }

  public void minus(int lastVal) {

    try {

      int num1 = nums.pop();

      int num2 = nums.pop();

      int result = num2 - num1;

      nums.push(result);

    } catch (EmptyStackException e) {

      System.out.println("Stack underflow");

      nums.push(lastVal);

    }

  }
  // performs division

  public void divide(int lastVal) {

    try {

      if (!nums.peek().equals(0)) { // Checks if top value is NOT zero

        double num1 = nums.pop();

        double num2 = nums.pop();

        double result = num2 / num1;

        nums.push((int) result);

      }

      else { // If top value is 0 then remove the top two using pop, push a zero onto the
             // stack and display an error for dividing by zero

        nums.pop();

        nums.pop();

        nums.push(0);

        System.out.println("Divide by 0");

      }

    } catch (EmptyStackException e) { // If method //can not be performed display stack underflow //error due to
                                      // insufficient operands using try-catch

      System.out.println("Stack underflow");

    }

  }

  // performs remainder operation using modulo

  public void remainder(int lastVal) {

    try {

      int num1 = nums.pop();

      int num2 = nums.pop();

      int result = num2 % num1;

      nums.push(result);

    } catch (EmptyStackException e) {

      System.out.println("Stack underflow");

      nums.push(lastVal);

    }

  }

  // Performs power operation

  public void power(int lastVal) {

    try {

      int num1 = nums.pop();

      double dnum1 = num1; // Double for math pow method

      int num2 = nums.pop();

      double dnum2 = num2;// Double for math pow method

      double dresult = Math.pow(dnum2, dnum1);

      int result = (int) dresult;

      long total = (long) dresult;

      checkSaturation(result, total, num2, num1);

    } catch (EmptyStackException e) {

      System.out.println("Stack underflow");

      nums.push(lastVal);

    }

  }

  // Method used for input of display stack which displays stack in order or input

  public void display() {

    String values = Arrays.toString(nums.toArray());

    System.out.println(values);

    int stackSize = nums.size();

    if (stackSize == 0) {

      System.out.println(Integer.MIN_VALUE); // If stack is empty display the min value for type int

    }

    else {

      for (int j = 0; j < stackSize; j++) { // Push //top value from stack into temporary display
        // stack and pop from
        // main nums stack

        dStack.push(nums.peek());

        nums.pop();

      }

      int displaySize = dStack.size();

      for (int j = 0; j < displaySize; j++) { // Pushes values back into main stack whilst
        // displaying values to give
        // correct order

        nums.push(dStack.peek());
      }

    }

  }

  public boolean checkForUnderflow() {
    if (dStack.size() <= 1) {
      System.out.println("Stack underflow.");
      return true;
    }
    return false;
  }

  // the list of numbers the 'r' function loops through

  // Adds all the "Random" Numbers to the random Queue

  public int random(int rCount) {

    int[] rArray = { 1804289383, 46930886, 1681692777, 1714636915, 1957747793, 424238335, 719885386,

        1649760492, 596516649, 1189641421, 1025202362, 1350490027, 783368690, 1102520059, 2044897763,

        1967513926, 1365180540, 1540383426, 304089172, 1303455736, 35005211, 521595368 };

    int rNum = rArray[rCount];

    return rNum; // Returns random number

  }

}
