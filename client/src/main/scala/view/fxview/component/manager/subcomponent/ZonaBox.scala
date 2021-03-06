package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Zona
import javafx.fxml.FXML
import javafx.scene.control.{Button, TableView, TextField}
import regularexpressionutilities.ZonaChecker
import view.fxview.component.HumanResources.subcomponent.util.{CreateTable, TextFieldControl, ZonaTable}
import view.fxview.component.manager.subcomponent.parent.ZonaParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

/**
 * @author Francesco Cassano
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[ZonaParent]]
 */
trait ZonaBox extends Component[ZonaParent] {

}

/**
 * Companion object of ZonaBox
 *
 */
object ZonaBox {

  def apply(zones: List[Zona]): ZonaBox = new ZonaBoxFX(zones)

  /**
   * javaFX private implementation of [[ZonaBox]]
   *
   * @param zones
   *              List of [[caseclass.CaseClassDB.Zona]] to manage
   */
  private class ZonaBoxFX(zones: List[Zona])
    extends AbstractComponent[ZonaParent]("manager/subcomponent/ZonaBox") with ZonaBox {

    @FXML
    var zonaTable: TableView[ZonaTable] = _
    @FXML
    var zonaButton: Button = _
    @FXML
    var newName: TextField = _
    @FXML
    var searchBox: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

      initializeButton(resources)
      initializeSearch(resources)
      initializeTextField(resources)
      initializeTable()
    }

    private def initializeTable(): Unit = {
      val columnFields = List("id", "name")
      CreateTable.createColumns[ZonaTable](zonaTable, columnFields,250)
      CreateTable.fillTable[ZonaTable](zonaTable, zones)
      CreateTable.clickListener[ZonaTable](
        zonaTable,
        item => parent.openZonaModal(Zona(item.name.get, Some(item.id.get().toInt)))
      )
    }

    private def initializeTextField(resources: ResourceBundle): Unit = {
      newName.setPromptText(resources.getResource("nametxt"))
      newName.textProperty().addListener((_, old, word) => {
        TextFieldControl.controlNewChar(newName, ZonaChecker, word, old)
        ableToSave()
      })
    }

    private def initializeButton(resources: ResourceBundle): Unit = {
      zonaButton.setText(resources.getResource("add"))
      zonaButton.setDisable(true)
      zonaButton.setOnAction(_ => parent.newZona(Zona(newName.getText)))
    }

    private def initializeSearch(resourceBundle: ResourceBundle): Unit = {
      searchBox.setPromptText(resourceBundle.getResource("search"))

      searchBox.textProperty().addListener((_, _, word) => {
        CreateTable.fillTable[ZonaTable](
          zonaTable,
          zones.filter(zone => zone.zones.toLowerCase.contains(word) || zone.idZone.head.toString.contains(word)))
      })
    }

    ////////////////////////////////////////////////////////////////////////////////  Controll

    private def ableToSave(): Unit =
      zonaButton.setDisable(newName.getText().equals(""))

  }
}

