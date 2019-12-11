import scala.sys.process._

object FileExecutor {

  //Calls log assistant builder to be used in newly instrumented java files
  def buildLogAssistant() = {
    "javac ./src/main/java/LogAssistant.java -d ./src/main/java/LogBuild".!
  }

  //Overloaded methods to run files
  def runFile(fileToRun:String) = {
    val codeToExecute = "java " + "-cp ./src./main/java/LogBuild " + fileToRun
    codeToExecute.!
  }

  def runFile(fileToRun:String, args:String) = {
    val codeToExecute = "java " + "-cp ./src./main/java/LogBuild " + fileToRun + " " + args
    codeToExecute.!
  }
}
