package view.fxview

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import view.{BaseView, GoBackView}
import view.fxview.loader.FXLoader

/**
 * Template class of type [[view.BaseView]] with basic funtionality to show
 * and hide a view loaded from fxml file.
 * @param myStage
 *                The [[javafx.stage.Stage]] where the view is Shown.
 */
abstract class AbstractFXView(val myStage:Stage) extends Initializable with BaseView{
  /**
   * The base pane of the fxView where the components are added.
   */
  @FXML
  protected var pane: BorderPane = _
  /**
   * Stage of this view.
   */
  FXLoader.loadScene(myStage,this,"BorderBase")

  override def initialize(location: URL, resources: ResourceBundle): Unit ={
    myStage.setTitle(resources.getString("nome"))
  }

  override def show(): Unit =
    myStage show

  override def hide(): Unit =
    myStage hide

}

/**
 * Template class of type [[view.GoBackView]] with basic funtionality to show
 * and hide a view loaded from fxml file and to go back to a previous scene, if present.
 * @param myStage
 *              The [[javafx.stage.Stage]] where the view is Shown.
 * @param oldScene
 *                 The Scene to show if go back is called.
 */
abstract class AbstractFXViewWithBack(override val myStage:Stage, oldScene: Option[Scene]) extends AbstractFXView(myStage) with GoBackView{
  override def back(): Unit =
    myStage.setScene(oldScene.getOrElse(myStage.getScene))
}