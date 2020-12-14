package app

import app.Constants.customPadding
import javafx.geometry.Pos
import javafx.stage.FileChooser
import tornadofx.*

class TopView: View(){
    private val controller : TopViewController by inject()
    val editor = textarea()
    override val root = vbox().apply {
        style{
            padding = customPadding()
        }
        this+=hbox {
            style{
                alignment = Pos.CENTER_LEFT
                spacing = 10.px
                padding = customPadding()
            }
            this+=label("Current File: ")
            this+=label(controller.currFile).apply {
                minWidth = 50.0
                maxWidth = 500.0
            }
            this+=text("")
            this+=button("Open").apply {
                action {
                    val file = chooseFile(
                        "Pick your script",
                        arrayOf(FileChooser.ExtensionFilter("kotlin script (*.kts)","*.kts"))
                    ).lastOrNull()
                    file?.let { controller.chooseScript(file)}
                }
            }
            this+=button("Save").apply {
                action {
                    controller.saveCurrentFile(editor.text)
                }
            }
        }
        this+= editor.apply{
            subscribe<MoveCursor> { result->
            val l = result.s.substringAfterLast(".kts:")
            val line = l.substringBefore(":").toInt()
            val char = (l.substringBefore(": ").substringAfter(":")).toInt()
                println(line.toString()+""+char.toString())
                requestFocus()
                positionCaret(cursorOffset(line,char,text))
        }
        subscribe<FileLoaded> { text = controller.editorText }
        }
    }
    fun cursorOffset(line:Int,char :Int, text:String) : Int{
        var x=1; var y=0; var z=0
        for(i in text){
            ++z
            ++y
            if(i=='\n') {
                ++x
                y=0
            }
            if(x==line&&y==char){
                return z
            }
        }
        return 0
    }
}
object FileLoaded : FXEvent()

