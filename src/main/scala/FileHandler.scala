import java.io.{BufferedReader, BufferedWriter, File, FileNotFoundException, FileReader, FileWriter, IOException}
import org.eclipse.jface.text.Document
import scala.util.Properties

object FileHandler {

  //creates document and writes appropriate string
  def writingToFile(toWrite:String, filePath:String) = {
    // Writing AST to file
    val doc = new Document(toWrite)
    val file = new File(filePath)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(doc.get())
    bw.close()
  }

  //Creates file path and returns string
  @throws[FileNotFoundException]
  @throws[IOException]
  def readFileToString(filePath: String): String = {
    val br = new BufferedReader(new FileReader(filePath))
    val sb = new StringBuilder
    var line = br.readLine()
    while (line != null) {
      sb.append(line)
      sb.append(Properties.lineSeparator)
      line = br.readLine
    }
    sb.toString
  }

}
