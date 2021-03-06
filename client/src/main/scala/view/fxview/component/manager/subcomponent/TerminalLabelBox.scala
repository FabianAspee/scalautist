package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.ShowParamAlgorithmBoxParent

trait TerminalLabelBox extends Component[ShowParamAlgorithmBoxParent]

object TerminalLabelBox {

  def apply(terminals: List[String]): TerminalLabelBox = new TerminalLabelBoxFX(terminals)

  private class TerminalLabelBoxFX(terminals: List[String]) extends AbstractComponent[ShowParamAlgorithmBoxParent]("manager/subcomponent/OneLabel")
    with TerminalLabelBox {

    @FXML
    var labelOne: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      labelOne.setText(setTerminalName())
    }
    def setTerminalName():String={
      val DEFAULT_VALUE:String =String.valueOf("")
      val VALUE:String = String.valueOf(" ")
      terminals.foldLeft(DEFAULT_VALUE)((default,actual)=>default+actual+VALUE)
    }
  }

}
