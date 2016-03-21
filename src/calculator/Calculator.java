package calculator;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
/**
 * The Calculator class.  Takes from a .txt file a mathematical expression consisting of the operators "+", "-", "*", "%", or "/", the unary
 *                         operator "-", any integer, and left and right parenthesis. Evaluates the expression using two methods: 
 *                         recursion and stacks. 
 *                        
 *                             It evaluates using recursion by first checking if the input is but a number with no binary operators (base case),
 *                         and returns it if it is. it then iterates through the expression, keeping track
 *                         of the last and highest priority binary operator (using PEMDAS), unless it is contained in parenthesis. If the whole expression is 
 *                         within parenthesis, then it removes the parenthesis and recursively calls itself using the resulting expression.
 *                         If it was not in parenthesis, it splits the expression into two substrings: The expression before the last binary
 *                         operator and the expression after the binary operator. It then recursively calls itself on each of these two 
 *                         substrings and solves the two resulting numbers using the binary operator.
 *                         
 *                             It evaluates using stacks by first converting the expression to postfix notation, then solving the postfix expression.
 *                         it converts to postfix by iterating through the expression, adding any integers it finds to an output 
 *                         string and pushing any binary operators onto a stack. If the iterator comes across a binary operator whilst one 
 *                         more are already on the stack, it compares it to them. If the current operator is of greater priority than the first operator
 *                         on the stack, the current is pushed onto the stack and the method continues to iterate through the expression.
 *                         if the current is of lesser priority than the first operator on the stack, the stack is popped and added to the
 *                         output array until an operator of lesser priority than the current operator is found on the top of
 *                         the stack. The current operator is then pushed onto the stack and the iteration continues. If the 
 *                         iterator comes across a left parenthesis, it always pushes it onto the stack. If it comes across a 
 *                         right parenthesis, it pops the stack into the output array until it finds a left parenthesis, then 
 *                         continues iteration. Once the iterator reaches the end of the expression, all operators are popped
 *                         and added to the output string, which now consists of the original expression in postfix.
 *                         This output string is then converted to an array and fed into a different method, which solves it.
 *                         It does so by iterating through the array, pushing any integer it finds onto a stack. If the iterator
 *                         comes across an operator, it pops the first two integers off of the stack, solves them using the 
 *                         operator, then pushes the result back onto the stack. Once the iterator reaches the end of the array,
 *                         the stack consists of one integer, the answer to the original expression.
 * 
 * @author Laivi Malamut-Salvaggio
 * @version 1.0
 *
 */

public class Calculator {
	private static String fileToUse = "calc_expressions.txt";

	public static void main (String args[]){
		if(args.length !=0){
			fileToUse = args[0];
		}
		Scanner sc = null;
		FileReader fr = null;
		while(fr == null){
			try{
				fr = new FileReader(fileToUse);
			}
			catch(FileNotFoundException e){
				System.out.println("Error! Input file not found. Using default file");
				fileToUse = "calc_expressions.txt";
			}
		}
		sc = new Scanner(fr);
		String expression = "";

		while (sc.hasNextLine()){
			expression = sc.nextLine();
			expression = expression.replaceAll("\\s","");
			if (expression.length()>0 && expression.charAt(0) != '#'){
				System.out.println("Using recursion:");
				System.out.println(expression + " = " + iterator(expression));
				System.out.println("Using stacks and postfix:");
				System.out.println(expression + " = " + toPostfix(expression));
				System.out.println("");
			}
		}
		sc.close();
	}


	private static int iterator(String str){
		int multdiv = 0; // this will hold the place of the rightmost "*", "/", or "%" operator
		int plusminus = 0;// the rightmost "+" or "-" operator
		int counter = 0; //counts the number of parenthesis. "(" == +1 ; ")" == -1
		boolean isInt = true;
		int intChecker = 0;
		if (str.charAt(0) == '-'){
			intChecker++;
		}
		while (isInt && intChecker<str.length()){ //checking if str is just one integer
			if (isOperator(str, intChecker) != 0){
				isInt = false;
			}
			intChecker++;
		}
		if(isInt){ // if it is an integer, return it
			return Integer.parseInt(str);
		}
		if (str.charAt(0) == '-' && str.charAt(1) == '('){// if the string is preceded by a negative sign, then a parenthesis
			return (-iterator(str.substring(1,str.length())));
		}
		for (int i = 0; i < str.length(); i++){
			if (str.charAt(i) == '('){
				counter++;
			}
			else if (str.charAt(i) == ')'){
				counter--;
			}
			else if (counter == 0){ // there are no parenthesis, since the counter == 0 only outside of parenthesis
				if (isOperator(str, i) == 1){
					multdiv = i;
				}
				else if (isOperator(str, i) == 2){
					if (i != 0 && isOperator(str, i-1) == 0)// if the last char was a number(ie, this char is not unary)
						plusminus = i;
				}
			}
		}
		if (multdiv == 0 && plusminus == 0){ //didnt input any operators bc there are parenthesis around whole thing
			String result = str.substring(1, str.length() - 1);
			return iterator(result);
		}
		else if( plusminus > 0){ //first checks for plus or minus, which will be recursively called first, so used last
			String first = str.substring(0, plusminus);
			String last = str.substring(plusminus + 1, str.length());
			char operator = str.charAt(plusminus);
			if (operator == '+'){
				return iterator(first) + iterator(last);
			}
			else {
				return iterator(first) - iterator(last);
			}
		}
		else { //if there was no plus or minus
			String first = str.substring(0, multdiv);
			String last = str.substring(multdiv + 1, str.length());
			char operator = str.charAt(multdiv);
			if (operator == '*') {
				return iterator(first) * iterator(last);
			}
			else if(operator == '/') {
				try{
					return iterator(first) / iterator(last);
				}catch(java.lang.ArithmeticException e){
					System.out.println("Error! May not divide by zero!");
					return 0;
				}

			}
			else {
				return iterator(first) % iterator(last);
			}
		}
	}
	/**
	 * checks whether the char at the specified index in the specified string is an operator or an int. If it is an operand, return
	 * zero. if operator, returns one of three numbers: number 1 if is the multiplication, division, or modulo sign, 2 if it is
	 * the plus or minus sign, and 3 if it is a left parenthesis.
	 * 
	 * @param str The string in which the char is contained
	 * @param i The index of the char
	 * @return An int corresponding to operator, or zero if operand
	 */
	private static int isOperator(String str, int i){
		int result = 0;
		char compare = str.charAt(i);
		if ( compare == '*' || compare == '/' || compare == '%'){
			result = 1;
		}
		else if(compare == '+' || compare == '-') {
			result = 2;
		}
		else if( compare == '('){
			result = 3;
		}
		return result;
	}

	private static int isOperator(String[] str, int i){
		int result = 0;
		String compare = str[i];
		if ( compare.equals("*") || compare.equals("/") || compare.equals("%")){
			result = 1;
		}
		else if(compare.equals("+") || compare.equals("-")) {
			result = 2;
		}
		else if( compare.equals("(")){
			result = 3;
		}
		return result;
	}
	/**
	 * evaluates postfix expression contained within an array of strings, using "N" to denote a negative number
	 * 
	 * @param str The postfix expression 
	 * @return What the postfix expression equals.
	 */
	private static int evalPostfix(String[] str){
		Stack<Integer> stack = new Stack<>();
		for (int i = 0; i<str.length; i++){
			if (str[i].charAt(0)== 'N'){// it is a negative number, push it
				String toPush = str[i].substring(1, str[i].length());
				stack.push(-Integer.parseInt(toPush));
			}
			else if (isOperator(str, i) == 0){ // is an operand, push it
				stack.push(Integer.parseInt(str[i]));
			}
			else{// is an operator
				int b = stack.pop();
				int a = stack.pop();
				int toPush = 0;
				switch(str[i]) {
				case "*":
					toPush += (a*b);
					break;
				case "/":
					try{
						toPush += (a/b);
					}catch(java.lang.ArithmeticException e){
						System.out.println("Error! May not divide by zero!");
						return 0;
					}
					break;
				case "%":
					toPush += (a%b);
					break;
				case "+":
					toPush += (a+b);
					break;
				case "-":
					toPush += (a-b);
					break;
				}
				stack.push(toPush);
			}
		}
		return stack.pop();
	}
	/**
	 * replaces all unary negative signs with "N". Useful to keep track of whether a minus sign is unary or binary
	 * @param str A string containing a mathematical expression
	 * @return The same string will all unary minus signs replaced with "N"
	 */
	private static String replaceN(String str){
		for (int i = 0; i<str.length(); i++){
			if (str.charAt(i) == '-'){
				if (i == 0){ // is the first element
					str = "N" + str.substring(1, str.length());
				}
				else if (isOperator(str, i-1)>0){ // the "-" sign is preceded by an operator or a left parenthesis
					str = str.substring(0,i) + "N" + str.substring(i+1, str.length());
					//else it is binary
				}
			}
		}
		return str;
	}
	/**
	 * used in the toPostFix method when pushing operators onto the stack. Priority numbering follows PEMDAS, with the expception
	 * that an operator on the stack always has greater priority than the same operator not on the stack. Left parenthesis is
	 * always pushed onto stack, so it has greater priority than all others. It is also always popped from the stack, so it has no
	 * priority when on the stack.
	 * @param operator The operator whose priority is to be determined
	 * @param onStack Whether or not that operator was derived from a stack
	 * @return A number corresponding to the operator's priority
	 */
	private static int priority(String operator, boolean onStack){
		int result = 0;
		if (operator.equals("+") || operator.equals("-")){
			result = 10;
			if (onStack){
				result++;
			}
		}
		else if(operator.equals("*") || operator.equals("/") || operator.equals("%")){
			result = 20;
			if (onStack){
				result ++;
			}
		}
		else{ // is left parenthesis
			result = 30;
			if (onStack){
				result = 0;
			}
		}
		return result;
	}
	/**
	 * converts an infix expression contained within a string to a postfix expression contained in an array of strings, using stacks.
	 * @param str The infix expression to be converted
	 * @return The postfix expression
	 */
	private static int toPostfix(String str){
		Stack<String> stack = new Stack<>();
		ArrayList<String> arr = new ArrayList<String>();
		str = replaceN(str);
		for (int i = 0; i< str.length(); i++){
			if (str.charAt(i) == ')'){// need to pop all operators in stack, because any expression within parenthesis must be solved first
				String onto = "";
				while(!stack.isEmpty() && !onto.equals("(")){ // pop all until reach a right parenthesis
					onto = stack.pop();
					if(!onto.equals("(")){
						arr.add(onto);
					}
				}
			}
			else if (isOperator(str, i) == 0 || str.charAt(i) == 'N'){ // if it is an operand, or "N" denoting a negative operand
				int counter = i+1;
				while (counter < str.length() && isOperator(str, counter) == 0 && str.charAt(counter) != ')'){ // the next i is still an int
					counter++;
				}
				arr.add(str.substring(i,counter));
				i = counter -1;
			}
			else { // it is an operator
				if(stack.isEmpty()){ //nothing to compare it to, push it
					stack.push(str.substring(i, i+1));
				}
				else{ 
					String current = str.substring(i,i+1);
					String prev = "";
					int Pprev;
					int Pcurrent;
					boolean pushPrev = true;
					do {
						pushPrev = true;
						prev = stack.pop();
						Pprev = priority(prev, true);
						Pcurrent = priority(current, false);
						if (Pprev > Pcurrent){ //if the priority of the op on the stack is greater than the current, add it to output
							arr.add(prev);
							pushPrev = false;
						}
					} while((!(stack.isEmpty()) && Pprev > Pcurrent)); // keep on adding the prev op to output until an op of lesser priority is found
					if (pushPrev){ // if the previous was not just added to the output, need to push it back onto the stack
						stack.push(prev);
					}
					stack.push(current);
				}
			}
		}
		while (!stack.isEmpty()){ //reached end of iteration, pop everything onto output
			arr.add(stack.pop());
		}
		return evalPostfix(arr.toArray(new String[arr.size()]));
	}

}
