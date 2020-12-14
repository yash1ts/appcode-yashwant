package app
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import javafx.scene.control.Hyperlink
import javafx.scene.paint.Color
import javafx.scene.text.Text
import tornadofx.Controller
import tornadofx.plusAssign
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class TopViewController : Controller() {
    var currFile = SimpleStringProperty("")
    var editorText = ""

    init {
        subscribe<RunScriptRequest> { result ->
            executeScript(editorText, result.textField)
        }
    }

    fun chooseScript(file: File) {
        currFile.value = file.canonicalPath
        runAsync { editorText = loadFile(file) } ui {
            fire(FileLoaded)
        }
    }

    fun saveCurrentFile(editorText: String) {
        this.editorText = editorText
        runAsync { saveFile(File(currFile.value), editorText) }
    }

    private fun executeScript(script: String, textArea: Node?) {
        if(Files.exists(Path.of(currFile.value)))
        runAsync { Compiler.startScript(currFile.value, textArea) } ui {
            findLine(it.second, currFile.value, textArea)
            fire(ScriptComplete(it.first))
        }
    }

    private fun findLine(reader: String, path: String, txt: Node?) {
        for (l in reader.split("\n")) {
            if (l.contains(path)) {
                val error = l.substringAfterLast(".kts")
                val hl = Hyperlink("script $error")
                hl.setOnAction { fire(MoveCursor(l)) }
                txt?.let { txt += hl }
                break
            } else {
                txt?.let { it += Text(l).apply { fill = Color.RED } }
            }
        }
    }
}