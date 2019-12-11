import java.io.{BufferedWriter, File, FileWriter}
import java.net.ServerSocket
import java.nio.file.{Files, Paths, StandardCopyOption}

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.mutable.{HashMap, Set}
import scala.io.BufferedSource

object Main {

  private val LOG_TAG = Logger(LoggerFactory.getLogger("Launcher"))
  private val config: Config = ConfigFactory.load("config.conf")

  def main(args: Array[String]): Unit = {

    // Log tag. Collects log and dumps it in log.log in project dir
    val args = config.getStringList("TestCodeArgs.args").asScala

    // Filepath to be collect logs from
    val filepath = "./src/main/java/TestCode.java"

    // Maintain a hash table of all variables.
    val variableData = new HashMap[String, Set[VarInfo]]

    // Builds the logger file. Requried before executing the instrumented file
    FileExecutor.buildLogAssistant()

    // Genreates the from filepath and inputs the visitor to traverse nodes.
    val ast = ASTHandler.parseFile(filepath)
    ast.accept(new Visitor(ast))

    // Updates the file with instrumneted code. Dumps old file in OldJavaFiles folder.
    ASTHandler.updateFile(ast, filepath)

    // Executes each file using args provided in config file. After the server has started receiving
    val CodeExecutor = new Thread {
      override def run: Unit = {
        args.foreach(arg => {
          LOG_TAG.info("Running instrumented file: " + filepath + " with arguments " + arg)
          FileExecutor.runFile(filepath, arg)
        })
      }
    }

    // Runs server in a seperate thread. Blocking Call.
    val Server = new Thread{
      override def run: Unit = {

        val server = new ServerSocket((9999))
        //s.setSoTimeout(30*1000)

        while (true) {
          val s = server.accept()
          val in = new BufferedSource(s.getInputStream()).getLines()
          in.foreach(x => {
            if (x!="COMPLETE"){
              val info:Array[String] = x.split(",")
              val name:String = info(1)
              if (info(0) == "Declaration" || info(0) == "Assignment"){
                if(info(0)=="Declaration") LOG_TAG.info(info(0)+ " statement of variable "+name+" with value of "+info(2)+" at line number "+info(3))
                if(info(0) == "Assignment") LOG_TAG.info(info(0)+ " statement of variable "+name+" to "+info(4)+" resulting in value of "+info(2)+" at line number "+info(3))
                if (variableData.contains(name)) variableData.get(name).get.add(VarInfo(info(3),info(2)))
                else variableData.addOne(name, Set(VarInfo(info(3),info(2))))
              }
              else if (info(0) == "MethodDeclaration") LOG_TAG.info(info(0) + " statement of method "+name+" with parameters "+info(2)+" and a return type of "+info(3)+" at line number "+info(4))
              else if (info(0) == "WhileStatement" || info(0) == "IfStatement") LOG_TAG.info(info(0)+ " loop with statement "+info(1)+" at line number "+info(2))
            }
          })
          println("Variable HastTable = " + variableData)
          s.close()
        }
      }
    }

    Server.start()
    Thread.sleep(500)
    CodeExecutor.start()
  }
}
