import java.io.{BufferedReader, BufferedWriter, File, FileNotFoundException, FileReader, FileWriter, IOException}
import java.nio.file.{Files, Paths, StandardCopyOption}

import org.eclipse.jdt.core.{ICompilationUnit, JavaCore}
import org.eclipse.jdt.core.dom.{AST, ASTNode, ASTParser, ASTVisitor, Block, CompilationUnit, MethodDeclaration, SimpleName, TypeDeclaration, VariableDeclarationFragment, WhileStatement}

import scala.util.Properties
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jface.text.Document


/**
 * AST Hander repossible for genereating AST and writing back to files
 */
object ASTHandler {

  /**
   * Converts a string into compilation unit
   * @param filepath path of the file to parse
   * @return AST Compilation Unit
   */
  def parseFile(filepath: String): CompilationUnit = {
    val parser = ASTParser.newParser(AST.JLS12)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    parser.setResolveBindings(true)
    parser.setBindingsRecovery(true)
    parser.setCompilerOptions(JavaCore.getOptions)
    val unitName = "TestCode.java"
    parser.setUnitName(unitName)
    val sources = Array("./src")
    val classpath = Array("C:\\Users\\User\\.sbt\\0.13\\java9-rt-ext-oracle_corporation_12_0_2\\rt.jar")
    parser.setEnvironment(classpath, sources, Array[String]("UTF-8"), true)
    parser.setSource(FileHandler.readFileToString(filepath).toCharArray)
    parser.createAST(null).asInstanceOf[CompilationUnit]
  }


  // Parser for source. Not Used
  def parseSource(unit: ICompilationUnit): CompilationUnit = {
    val parser = ASTParser.newParser(AST.JLS12)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    parser.setSource(unit)
    parser.setResolveBindings(true)
    parser.createAST(null).asInstanceOf[CompilationUnit]
  }


  def updateFile(cu:CompilationUnit, filePath: String): Unit = {
    println("Writing to file")

    //Rename old file
    val originalFile = new File(filePath)
    val oldFileNewName = new File((filePath.substring(0, filePath.length-5) + "_old"))

    //Rename old file
    originalFile.renameTo(oldFileNewName)

    // Moving old files to different location
    Files.move(
      Paths.get((filePath.substring(0, filePath.length-5) + "_old")),
      Paths.get((filePath.substring(0, filePath.length-5) + "_old").patch(16, "OldJavaFiles/", 0)),
      StandardCopyOption.REPLACE_EXISTING
    )

    FileHandler.writingToFile(cu.toString, filePath)
  }
}