package app
import app.Constants.customPadding
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.text.TextFlow
import tornadofx.*

class BottomView : View() {
    val textArea = TextFlow()
    val runButton = Button("Run")
    val progressBar = progressbar(0.0).apply {
        subscribe<ScriptComplete> { progress = 1.0 }
    }
    val exitCode = label("Exit Code: ").apply {
        subscribe<ScriptComplete> { result ->
            text = "Exit Code: ${result.exitCode}"
            runButton.isDisable = false
        }
    }

    override val root = vbox().apply {
        this += hbox {
            style{
                padding = customPadding()
                alignment = Pos.CENTER_LEFT
                spacing = 10.px
            }
            this += runButton.apply {
                action {
                    isDisable = true
                    progressBar.progress = -1.0
                    exitCode.text = "Exit Code: "
                    fire(RunScriptRequest(textArea))
                }
            }
            this += progressBar
            this += exitCode
        }
        this += scrollpane().apply {
            this += textArea.apply {
                style{
                    fontSize = 10.px
                    padding = customPadding()
                }
            }
            minHeight = 200.0
        }
    }
}

class RunScriptRequest(val textField: Node?) : FXEvent(EventBus.RunOn.BackgroundThread)
class ScriptComplete(val exitCode: Int) : FXEvent()
class MoveCursor(val s: String) : FXEvent()