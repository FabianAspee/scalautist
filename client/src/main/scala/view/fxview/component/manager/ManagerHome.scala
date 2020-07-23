package view.fxview.component.manager

import java.net.URL
import java.sql.Date
import java.util.ResourceBundle

import caseclass.{CaseClassDB, CaseClassHttpMessage}
import caseclass.CaseClassDB.{Terminale, Turno}
import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement, ResultAlgorithm}
import view.fxview.util.ResourceBundleUtil._
import javafx.fxml.FXML
import javafx.scene.control.{Accordion, Button, Label, TitledPane}
import javafx.scene.layout.{BorderPane, VBox}
import org.controlsfx.control.PopOver
import view.fxview.{FXHelperFactory, NotificationHelper}
import view.fxview.NotificationHelper.NotificationParameters
import view.fxview.component.manager.subcomponent.{FillHolesBox, ManagerRichiestaBox, SelectResultBox}
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent
import view.fxview.component.{AbstractComponent, Component}
/** @author Gianni Mormone, Fabian Aspee Encina
 *  Trait which allows to perform operations on richiesta view.
 */
trait ManagerHome extends Component[ManagerHomeParent]{
  def drawNotifica(str: String,tag:Long): Unit

  def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit

  def drawResultTerminal(terminal: List[Terminale]): Unit

  /**
   * method that re paint all element that belong to Richiesta
   */
  def reDrawRichiesta(): Unit

  /**
   * method that send all shift that existing in system and allow draw this
   * @param listShift list with all shift in the system
   */
  def drawShiftRichiesta(listShift: List[Turno]): Unit

  /**
   *  method that send all terminal that existing in system and allow draw this
   * @param terminal list with all terminal existing in system
   */
  def drawRichiesta(terminal: List[Terminale]): Unit

  /**
   * Method used to draw the list of turns that needs a replacement
   *
   * @param absences
   *                 The list of turns that needs a replacement
   */
  def drawManageAbsence(absences: List[InfoAbsenceOnDay]): Unit

  /**
   * Method used to draw the list of people avalaible for the turn that needs a replacement
   * @param replacement
   *                    The people avalaible for the turn that needs a replacement
   */
  def drawManageReplacement(replacement: List[InfoReplacement]): Unit

  def loadingReplacements(): Unit

  def stopLoadingReplacements(): Unit
}

object ManagerHome{

  def apply(userName: String, userId:String): ManagerHome = new ManagerHomeFX(userName,userId)

  private class ManagerHomeFX(userName: String, userId:String) extends AbstractComponent[ManagerHomeParent]("manager/BaseManager")
    with ManagerHome{
    @FXML
    var nameLabel: Label = _
    @FXML
    var baseManager: BorderPane = _
    @FXML
    var notificationButton: Button = _
    @FXML
    var generateTurnsButton: Button = _
    @FXML
    var manageAbsenceButton: Button = _
    @FXML
    var redoTurnsButton: Button = _
    @FXML
    var printResultButton: Button = _
    @FXML
    var manageZoneButton: Button = _
    @FXML
    var manageTerminalButton: Button = _
    @FXML
    var richiestaButton: Button = _
    @FXML
    var idLabel: Label = _
    @FXML
    var popover: PopOver = _
    @FXML
    var accordion:Accordion= _

    var fillHolesView: FillHolesBox = _
    var managerRichiestaBoxView:ManagerRichiestaBox = _
    var selectResultBox:SelectResultBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      nameLabel.setText(resources.getResource("username-label"))
      idLabel.setText(resources.getResource("id-label"))
      notificationButton.setText(resources.getResource("notification-button"))
      generateTurnsButton.setText(resources.getResource("generate-turns-button"))
      manageAbsenceButton.setText(resources.getResource("manage-absence-button"))
      redoTurnsButton.setText(resources.getResource("redo-turns-button"))
      printResultButton.setText(resources.getResource("print-result-button"))
      manageZoneButton.setText(resources.getResource("manage-zone-button"))
      manageTerminalButton.setText(resources.getResource("manage-terminal-button"))
      richiestaButton.setText(resources.getResource("richiesta-button"))
      manageAbsenceButton.setOnAction(_ => parent.drawAbsencePanel())
      richiestaButton.setOnAction(_ => parent.drawRichiestaPanel())
      printResultButton.setOnAction(_=> parent.drawResultPanel())
      notificationButton.setOnAction(_=>openAccordion())
      nameLabel.setText(resources.println("username-label",userName))
      idLabel.setText(resources.println("id-label",userId))
    }
    //rabbit manda la notificacion, pero donde la manda? llega primero que el inizializate?
    private def openAccordion(): Unit ={
      popover.show(notificationButton)
    }
    override def drawManageAbsence(absences: List[InfoAbsenceOnDay]): Unit = {
      fillHolesView = FillHolesBox()
      baseManager.setCenter(fillHolesView.setParent(parent).pane)
      fillHolesView.drawAbsenceList(absences)
    }

    override def drawManageReplacement(replacement: List[InfoReplacement]): Unit = {
      fillHolesView.endLoading()
      fillHolesView.drawSubstituteList(replacement)
    }

    override def startLoading(): Unit =
      baseManager.setCenter(FXHelperFactory.loadingBox)

    override def loadingReplacements(): Unit =
      fillHolesView.startLoading()

    override def stopLoadingReplacements(): Unit =
      fillHolesView.endLoading()

    override def drawRichiesta(terminal: List[Terminale]): Unit = {
      managerRichiestaBoxView = ManagerRichiestaBox(terminal)
      baseManager.setCenter(managerRichiestaBoxView.setParent(parent).pane)
    }

    override def reDrawRichiesta(): Unit = {
      managerRichiestaBoxView.reDrawRichiesta()
      baseManager.setCenter(managerRichiestaBoxView.setParent(parent).pane)
    }

    override def drawShiftRichiesta(listShift: List[Turno]): Unit = {
      managerRichiestaBoxView.drawShiftRequest(listShift)
    }

    override def drawResultTerminal(terminal: List[Terminale]): Unit = {
      selectResultBox = SelectResultBox(terminal)
      baseManager.setCenter(selectResultBox.setParent(parent).pane)
    }

    override def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit = selectResultBox.createResult(resultList,dateList)

    private def consumeNotification(tag:Long): Unit ={
    }

    override def drawNotifica(str: String,tag:Long): Unit = {
      NotificationHelper.drawNotifica(str,tag, NotificationParameters(accordion,popover,consumeNotification))
    }
  }
}
