package app

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App

class ScriptApplication(): App(MainMenu::class){
    override fun start(stage: Stage) {
        stage.icons.add(Image("/ic_main.png"))
        super.start(stage)
    }
}