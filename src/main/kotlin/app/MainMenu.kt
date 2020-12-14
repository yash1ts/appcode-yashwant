package app
import app.Constants.customPadding
import javafx.application.Platform
import javafx.scene.control.Alert
import tornadofx.*
import java.nio.file.Files
import java.nio.file.Paths

class MainMenu: View("Scripting tool") {
    val topView: TopView by inject()
    val bottomView: BottomView by inject()

    override val root =vbox{
        style{
            padding = customPadding()
        }
        this+=topView.root
        this+=bottomView.root
    }

    override fun onDock() {
        checkSDKPath()
        super.onDock()
    }

    fun checkSDKPath(){
        if(!Files.isDirectory(Paths.get(System.getenv("KOTLIN_HOME")))){
            Alert(Alert.AlertType.ERROR,"Kotlin SDK not found. Please set KOTLIN_HOME path.")
                .showAndWait()
            Platform.exit()
        }
    }
}
