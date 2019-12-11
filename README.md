# Automatic Java Program Logger (Written in Scala)

### Team Members: Abdullah Aleem and Muhammad Maaz Khan



# Overview

The project is an instrumentation program that takes syntactically correct java files and uses the Eclipse Java Abstract Syntax Tree parser to parse, rewrite and log them. The Eclipse AST is used to parse the file and obtain relevant information using pattern Visitor to compute scopes and variables declared, insert instrumenting statements pertaining to a template class. The template class is used as a logging class which while returning logging statements, also returns relevant information back to our launcher program via socket programming. The launcher is responsible for parsing, rewriting, and using the returned information to create a HashMap of declared variables and their reassignment. The launcher program also automatically executes multiple iterations of said java program with different inputs take from config file and writes the logging statements into the file. 

## Example

Below is an examples how to Logger works: Original code is syntactically correct code provided by the user. Instrumented code is the code after logging statements are inserted using AST parsing. And the log is collected in a different processes are running multiple (2 in this instances of this code) with input 10,50 and 20,70.

Original Code:
```java
public class TestCode {  
  
  public static String getString(){  
    String str = "Abdullah";  
 return str;  
  }  
  public static int getInt(){  
    int num = 1;  
 return num;  
  }  
 public static void main(String[] args){  
    int var1 = Integer.parseInt(args[0]) ;  
 int var2 = Integer.parseInt(args[1]);  
 int var3 = var1 + var2;  
  
 while(var2 < 10){  
      var2 = var2+2;  
  }  
  
    var3 = getInt();  
  
 if (var3 == 1) {  
      String name = "Maaz";  
  name = getString();  
  }  
  }  
}
```

Instrumented Code:

```java
public class TestCode {  
  public static String getString(){
  LogAssistant.instrum(3,"MethodDeclaration","getString","String","[]");  
  String str="Abdullah";  
  LogAssistant.instrum(4,"Declaration","str",str);
  return str;  
 }  
  public static int getInt(){  
    LogAssistant.instrum(7,"MethodDeclaration","getInt","int","[]");
    int num=1;
    LogAssistant.instrum(8,"Declaration","num",num);
    return num;  
  }  
  public static void main(  String[] args){  
    LogAssistant.instrum(11,"MethodDeclaration","main","void","[String[] args]");
    int var1=Integer.parseInt(args[0]);
    LogAssistant.instrum(12,"Declaration","var1",var1);
    int var2=Integer.parseInt(args[1]);
    LogAssistant.instrum(13,"Declaration","var2",var2);
    int var3=var1 + var2;
    LogAssistant.instrum(14,"Declaration","var3",var3);  
    while (var2 < 10) {  
      LogAssistant.instrum(16,"WhileStatement","var2 < 10");  
	  var2=var2 + 2;  
	  LogAssistant.instrum(17,"Assignment","var2",var2,"var2 + 2");
	  }  
    var3=getInt();
    LogAssistant.instrum(20,"Assignment","var3",var3,"getInt()");
    if (var3 == 1) {  
      LogAssistant.instrum(22,"IfStatement","var3 == 1");  
      String name="Maaz";  
	  LogAssistant.instrum(23,"Declaration","name",name);  
	  name=getString();  
	  LogAssistant.instrum(24,"Assignment","name",name,"getString()");
	  }  
  }  
}
```
Log Collected:

```
22:21:54.799 [Thread-2] INFO  Launcher - Running instrumented file: ./src/main/java/TestCode.java with arguments 10 50  
22:21:55.543 [Thread-3] INFO  Launcher - MethodDeclaration statement of method main with parameters [String[] args] and a return type of void at line number 11  
22:21:55.558 [Thread-3] INFO  Launcher - Declaration statement of variable var1 with value of 10 at line number 12  
22:21:55.582 [Thread-3] INFO  Launcher - Declaration statement of variable var2 with value of 50 at line number 13  
22:21:55.583 [Thread-3] INFO  Launcher - Declaration statement of variable var3 with value of 60 at line number 14  
22:21:55.583 [Thread-3] INFO  Launcher - MethodDeclaration statement of method getInt with parameters [] and a return type of int at line number 7  
22:21:55.584 [Thread-3] INFO  Launcher - Declaration statement of variable num with value of 1 at line number 8  
22:21:55.585 [Thread-3] INFO  Launcher - Assignment statement of variable var3 to getInt() resulting in value of 1 at line number 20  
22:21:55.589 [Thread-3] INFO  Launcher - IfStatement loop with statement var3 == 1 at line number 22  
22:21:55.597 [Thread-3] INFO  Launcher - Declaration statement of variable name with value of Maaz at line number 23  
22:21:55.599 [Thread-3] INFO  Launcher - MethodDeclaration statement of method getString with parameters [] and a return type of String at line number 3  
22:21:55.600 [Thread-3] INFO  Launcher - Declaration statement of variable str with value of Abdullah at line number 4  
22:21:55.602 [Thread-3] INFO  Launcher - Assignment statement of variable name to 24 resulting in value of getString() at line number Abdullah  
22:21:55.642 [Thread-2] INFO  Launcher - Running instrumented file: ./src/main/java/TestCode.java with arguments 20 70  
22:21:56.374 [Thread-3] INFO  Launcher - MethodDeclaration statement of method main with parameters [String[] args] and a return type of void at line number 11  
22:21:56.385 [Thread-3] INFO  Launcher - Declaration statement of variable var1 with value of 20 at line number 12  
22:21:56.387 [Thread-3] INFO  Launcher - Declaration statement of variable var2 with value of 70 at line number 13  
22:21:56.388 [Thread-3] INFO  Launcher - Declaration statement of variable var3 with value of 90 at line number 14  
22:21:56.390 [Thread-3] INFO  Launcher - MethodDeclaration statement of method getInt with parameters [] and a return type of int at line number 7  
22:21:56.391 [Thread-3] INFO  Launcher - Declaration statement of variable num with value of 1 at line number 8  
22:21:56.399 [Thread-3] INFO  Launcher - Assignment statement of variable var3 to getInt() resulting in value of 1 at line number 20  
22:21:56.408 [Thread-3] INFO  Launcher - IfStatement loop with statement var3 == 1 at line number 22  
22:21:56.416 [Thread-3] INFO  Launcher - Declaration statement of variable name with value of Maaz at line number 23  
22:21:56.419 [Thread-3] INFO  Launcher - MethodDeclaration statement of method getString with parameters [] and a return type of String at line number 3  
22:21:56.420 [Thread-3] INFO  Launcher - Declaration statement of variable str with value of Abdullah at line number 4  
22:21:56.422 [Thread-3] INFO  Launcher - Assignment statement of variable name to 24 resulting in value of getString() at line number Abdullah
```

## Project Structure and Design
All the functionality is implemented is Scala except the LogAssistant. We figured that as this code would be instrumented into Java code it would be better to write this is java.

Scala componenets can be broken down into 5 parts:

* ASTHandler
*  Visitor
* Launcher
* FileExecutor
* FileHandler

Below is the explanation what these objects/class are supposed to do and what functionality they provide.

### ASTHandler
ASTHandler has 2 main functionalities:

1. ParseFile
2. UpdateFile

ParseFile takes the path of a file which we want to instrument and eventually run and collect logs. It sets up the environment for parsing and creates an AST of the file as an instance of Compilation Unit.

UpdateFile takes the updates AST and writes it back to file. It moves the original file to a folder called OldJavaFiles and overwrites our file so it  contains all the new instrumented statements.

### Visitor
Once the AST is generated it is traversed using Visitor class which extends ASTVisitor and is implemented using visitor pattern. This class add logging states (Method Invocation nodes) into the AST for 5 types of Nodes. We limited logging statements to these 5 types of nodes for the scope of the project. These can be easily extended by overriding the relevant method in the Visitor class.

The five nodes that it adds logging for are:

1. Variable Declaration
2. Assignment Operation
3. Method Declaration
4. While Statements
5. IF Statements

For variable declaration we extract the name (String Literal)  and value (SimpleName) to be extracted at run time. For Method declaration we extract the name, arguments and the return type. For the while and if statement we return the conditions. All of these extraction also include the line where the code was extracted in original code. All these nodes are inserted directly below the original nodes in the given scope. All the methods instrumented using this Visitor are overloading in the LogAssistant which infers what method to call at runtime. We overloaded a few functions for basic types however all types are not converted but can easily be added in LogAssisant by overloading


### Launcher
This is our main launcher class. It first uses the file executor to build the LogAssistant class, followed by creating an AST of the target Java file. We then visit the AST and visit variable declaration, assignments, method declarations, while and if loops. The launcher then updates the target Java program with instrumentation statements pertaining to methods implemented in the LogAssistant. 
Once the file is instrumented, we create two seperate threads:

 - File executor for multiple iterations of target Java program: This thread logs takes inputs from the config file and runs different iterations of the java program.
 - Server: The second thread creates a server socket and listens to receive logging information from the LogAssistant. Once it receives said information, it determines which of the above type of logging information it has received. It then logs all the information into a file. If the information is pertaining to variable assignment or declaration, it pushes it into a Hash Map where the key is the variable and the value is a tuple of line number and variable value so as to keep track of our updating variable values when the java program executes.


### FileExecutor
File executor methods build the LogAssistant and launches the instrumented program in a different process using scala.Process package (command line execution). While running the Instrumented Code it is necessary that the LogAssistant has been already built.

### FileHandler
FileHandler has basic operation where it reads file as string and output a string to a given file and path. This is utility function using by other objects.

### LogAssistant
This class is the TemplateClass which is used to instrument the target java files. The class creates an instance of a client socket and utilizes it to communicate with our main launcher program. The function contains multiple overloaded 'instrum' functions which use the printstream of the client to send relevant information to the logger. The instrum functions instrument the following: 

 - Integer and String variable declaration statements: Takes the name and value of the variables along with the line numbers where they are declared.
 - Variable reassignment statements: Takes the name and value along with the right hand side of the assignment statement. The line number is also displayed where the statement takes place.
 - Method Declarations: Takes the name, parameters, return type and line numbers.
 - While loop conditions: Takes the name, line number, and while condition statement
 - If conditions: Takes the name, line number, and if condition statement


## Instructions and Usage

**Prerequisites**

-   JVM and SBT must be installed on your system.
-   This project was done is Intellij Idea IDE. And can be easily loaded for proper project access.

**Important Notes**

- The java file to be tested must be put placed the src/main/java folder. LogAssistant must also be placed in the same folder. 
- The built LogAssistant file must be places in the LogBuild folder in the same directory.
- The file to be run must be named TestCode (this can be changed ASTHandler and changing the path in Launcher.
- If you run the code multiple times it would add multiple instrumentation as the original file is overwritten. You can comment out UpdateFile in the launcher program if the file has already been instrumented.

**Running project using SBT**

-   Clone or download this repository onto your system
-   Open the Command Prompt (if using Windows) or the Terminal (if using Linux/Mac) and go to the project directory
-   Build the project and generate the jar file using SBT

**sbt clean compile run**

-   To run the scalatest using SBT use

**sbt clean compile test**

**Running project using Intellij Idea**

-   Clone or download this repository onto your system
-   Go to the project and open it using Intellij, or open Intellij and go to file and load the project.
-   The entry point of the program is src->main->scala->Main. You can run this by right clicking the screen and clicking run Main.
-   You can run the tests by navigating to src->test->scala. You can right click this folder and run all tests from different classes.



## Testing

We use a simple java program with just a main method and a variable declaration statement. The scala tests are written using FunSuite and we follow a similar pattern of creating an AST and rewritting an instrumented file. We then write tests to check our instrumentation by confirming line numbers, type of statements, and if we have inserted the correct number of instrumenting statements. 
The tests are contained in the test folder where VarDecTests.scala are the scala tests run on the VarDec.java file.