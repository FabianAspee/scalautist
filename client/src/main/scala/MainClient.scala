import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import view.fxview.mainview.{DriverView, HumanResourceView, LoginView, ManagerView}

object MainClient{
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main])
  }
}

private class Main extends Application{
  override def start(primaryStage: Stage): Unit = {
    val image = new Image(getClass.getResource("images/program_icon.png").toString)
    primaryStage.getIcons.add(image)
    //LoginView(primaryStage).show()
        ManagerView(primaryStage,"test","0").show()
  }
}
