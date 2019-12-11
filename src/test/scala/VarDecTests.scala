
import com.typesafe.config.{Config, ConfigFactory}
import org.json4s.DefaultFormats
import org.scalatest.FunSuite
import org.eclipse.jface.text.Document

import scala.reflect.io.File


class VarDecTests extends FunSuite {

  protected implicit val formats = DefaultFormats

  private val config: Config = ConfigFactory.load("config.conf")
  //private val testUserLogin = config.getString(" ")

  val filepath = "./src/test/java/files/original/VarDec.java"
  val ast =  ASTHandler.parseFile(filepath)
  ast.accept(new Visitor(ast))

  //FileHandler.writingToFile(ast.toString, "./src/test/java/files/instrumented/VarDecInstrum")
  //println(ast.toString.trim() == FileHandler.readFileToString("./src/test/java/files/instrumented/VarDecInstrum").trim())


  /**
   * In our test file, we are adding two instrum methods: for method and for variable
   * The test asserts whether we have infact added only two line as we should have
   */

  test("Number of instrumented lines") {

    val result:String = ast.toString
    val splitResult = result.split("\n")

    val originalLines = FileHandler.readFileToString(filepath).split("\n").length
    val newLines = splitResult.length

    // 2 new lines inserted for method and variable declaration
    assert(newLines-originalLines == 2)
  }

  /**
   * We check whether the line number we have logged for variable declaration is correct i.e. it is declared at line 4
   */
  test("Variable Line check") {
    val result:String = ast.toString
    val splitResult = result.split("\n")

    val variableInstrum = splitResult(5).trim
    val args = variableInstrum.substring(21,42)
    val splt = args.split(",")
    assert(splt(0) == "4")
  }

  /**
   * We check whether we have inserted the correct type for variable declaration
   */
  test("Variable Type check") {
    val result:String = ast.toString
    val splitResult = result.split("\n")

    val variableInstrum = splitResult(5).trim
    val args = variableInstrum.substring(21,42)
    val splt = args.split(",")
    assert(splt(1) == "\"Declaration\"")
  }

  /**
   * We check whether we have inserted the correct type for method declaration
   */
  test("Method type check") {

    val result:String = ast.toString
    val splitResult = result.split("\n")

    val methodInstrum:String = splitResult(3).trim
    val args = methodInstrum.substring(21,74)
    val splt = args.split(",")
    assert(splt(1) == "\"MethodDeclaration\"")
  }

  /**
   *We check whether the line number we have logged for method declaration is correct i.e. it is declared at line 3
   */
  test("Method line check") {

    val result:String = ast.toString
    val splitResult = result.split("\n")

    val methodInstrum:String = splitResult(3).trim
    val args = methodInstrum.substring(21,74)
    val splt = args.split(",")
    assert(splt(0) == "3")
  }

}