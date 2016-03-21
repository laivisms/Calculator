# Calculator
The Calculator class.  Takes from a .txt file a mathematical expression consisting of the operators "+", "-", "*", "%", or "/", the unary
                       operator "-", any integer, and left and right parenthesis. Evaluates the expression using two methods: 
                       recursion and stacks. 
                    
                        It evaluates using recursion by first checking if the input is but a number with no binary operators (base case),
                         and returns it if it is. it then iterates through the expression, keeping track
                         of the last and highest priority binary operator (using PEMDAS), unless it is contained in parenthesis. If the whole expression is 
                         within parenthesis, then it removes the parenthesis and recursively calls itself using the resulting expression.
                         If it was not in parenthesis, it splits the expression into two substrings: The expression before the last binary
                         operator and the expression after the binary operator. It then recursively calls itself on each of these two 
                         substrings and solves the two resulting numbers using the binary operator.
                        
                            It evaluates using stacks by first converting the expression to postfix notation, then solving the postfix expression.
                         it converts to postfix by iterating through the expression, adding any integers it finds to an output 
                         string and pushing any binary operators onto a stack. If the iterator comes across a binary operator whilst one 
                         more are already on the stack, it compares it to them. If the current operator is of greater priority than the first operator
                         on the stack, the current is pushed onto the stack and the method continues to iterate through the expression.
                         if the current is of lesser priority than the first operator on the stack, the stack is popped and added to the
                         output array until an operator of lesser priority than the current operator is found on the top of
                         the stack. The current operator is then pushed onto the stack and the iteration continues. If the 
                         iterator comes across a left parenthesis, it always pushes it onto the stack. If it comes across a 
                         right parenthesis, it pops the stack into the output array until it finds a left parenthesis, then 
                         continues iteration. Once the iterator reaches the end of the expression, all operators are popped
                         and added to the output string, which now consists of the original expression in postfix.
                         This output string is then converted to an array and fed into a different method, which solves it.
                         It does so by iterating through the array, pushing any integer it finds onto a stack. If the iterator
                         comes across an operator, it pops the first two integers off of the stack, solves them using the 
                         operator, then pushes the result back onto the stack. Once the iterator reaches the end of the array,
                         the stack consists of one integer, the answer to the original expression.
 
  @author Laivi Malamut-Salvaggio
  @version 1.0
