package app
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.text.Text
import tornadofx.plusAssign
import java.io.*
import java.util.concurrent.TimeUnit

object Compiler {
    fun startScript(script:String,textArea: Node?) : Pair<Int,String>{
        val home = System.getenv("KOTLIN_HOME")
        val processBuilder = ProcessBuilder().command("$home\\bin\\kotlinc.bat","-script", script)
            .redirectOutput(ProcessBuilder.Redirect.PIPE).redirectError(ProcessBuilder.Redirect.PIPE)
        val process = processBuilder.start()
        val reader = InputStreamReader(process.inputStream)
        while (printToTextArea(reader, textArea));
        process.waitFor(5, TimeUnit.MINUTES)
        process.destroy()
        val error= String(process.errorStream.readAllBytes())
        return process.exitValue() to error
    }
    private fun printToTextArea(reader: InputStreamReader, textArea: Node?) : Boolean{
        val buf = CharArray(128)
        if(reader.read(buf)<1)return false
        Platform.runLater {
            if (textArea != null)
                textArea+=Text(String(buf))
        }
        return true
    }
}



