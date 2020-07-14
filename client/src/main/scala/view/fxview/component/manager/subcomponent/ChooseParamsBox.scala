package view.fxview.component.manager.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Zona
import javafx.scene.control.{Button, CheckBox, ComboBox, DatePicker, RadioButton}
import view.fxview.component.HumanResources.subcomponent.util.CreateDatePicker
import view.fxview.component.manager.subcomponent.parent.ChooseParamsParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait ChooseParamsBox extends Component[ChooseParamsParent] {

}

object ChooseParamsBox {

  def apply(zones: List[Zona]): ChooseParamsBox = new ChooseParamsBoxFX(zones)

  private class ChooseParamsBoxFX(zoneList: List[Zona])
    extends AbstractComponent[ChooseParamsParent]("manager/subcomponent/ChooseParamsBox")
      with ChooseParamsBox {

    var resourceBundle: ResourceBundle = _
    var initDate: DatePicker = _
    var endDate: DatePicker = _
    var zones: ComboBox[String] = _
    var terminals: ComboBox[String] = _
    var sabato: CheckBox = _
    var normal: RadioButton = _
    var special: RadioButton = _
    var old: Button = _
    var reset: Button = _
    var run: Button = _

    val MIN_MONTH_BEFORE_START_ALGORITHM: Int = 1
    val MAX_MONTH_TO_SHOW: Int = 3
    val LAST_DAY_OF_MONTH_TO_START: Int = 15

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      resourceBundle = resources

      initText()
      resetComponent()
      initAction()
    }

    private def initText(): Unit = {
      sabato.setText(resourceBundle.getResource("txtcheck"))
      normal.setText(resourceBundle.getResource("txtnormal"))
      special.setText(resourceBundle.getResource("txtspecial"))
      old.setText(resourceBundle.getResource("txtold"))
      reset.setText(resourceBundle.getResource("txtreset"))
      run.setText(resourceBundle.getResource("txtrun"))
    }

    private def initAction(): Unit = {
      zones.setOnAction(_ => parent.getTerminals(getZone))

      initDate = CreateDatePicker.firstMonthDay(LocalDate.now(), MIN_MONTH_BEFORE_START_ALGORITHM,
        MAX_MONTH_TO_SHOW, LAST_DAY_OF_MONTH_TO_START)
    }

    private def resetComponent(): Unit = {
      zones.setPromptText(resourceBundle.getResource("txtzones"))
      terminals.setPromptText(resourceBundle.getResource("txtterminals"))
      initDate.setPromptText(resourceBundle.getResource("initdate"))
      endDate.setPromptText(resourceBundle.getResource("enddate"))

      zones.getItems.clear()
      zoneList.foreach(zona => zones.getItems.add(zona.zones))

      terminals.setDisable(true)
      endDate.setDisable(true)
      run.setDisable(true)
      normal.selectedProperty().setValue(true)
    }

    private def getComboSelected(component: ComboBox[String]): String =
      component.getSelectionModel.getSelectedItem

    private def getZone: Zona =
      zoneList.find(zona => zona.zones.equals(getComboSelected(zones))).getOrElse(Zona("???"))
  }

}
