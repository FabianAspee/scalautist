package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.sql.Date
import java.util.{Calendar, ResourceBundle}

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.Assumi
import javafx.beans.value
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.FXML
import javafx.scene.control._
import regularexpressionutilities.{NameChecker, NumberChecker}
import utils.UserType._
import view.fxview.component.HumanResources.subcomponent.parent.RecruitParent
import view.fxview.component.HumanResources.subcomponent.util.CreateDatePicker.{DatePickerC, MoveDatePeriod}
import view.fxview.component.HumanResources.subcomponent.util.{CreateDatePicker, TextFieldControl}
import view.fxview.component.{AbstractComponent, Component}

import scala.language.postfixOps
import view.fxview.util.ResourceBundleUtil._
/**
 * @author Francesco Cassano
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.RecruitParent]]
 */
trait RecruitBox extends Component[RecruitParent]{

  /**
   * Set a list of terminal in the view before the zone is chosen.
   *
   * @param l
   *          list of terminale to show
   */
  def setTerminals(l:List[Terminale]): Unit
}

/**
 * Companion object of [[view.fxview.component.HumanResources.subcomponent.RecruitBox]]
 *
 */
object RecruitBox {

  def apply(contractList: List[Contratto], shiftList: List[Turno], zoneList: List[Zona]): RecruitBox =
    new RecruitBoxImpl(contractList, shiftList, zoneList)

  /**
   * RecruitBox Fx implementation. It shows Human resource Recruit panel in home view
   *
   * @param contractList
   *                     list of type of contratto
   * @param shiftList
   *                  list of type of turno
   * @param zoneList
   *                 list of zona
   */
  private class RecruitBoxImpl(contractList: List[Contratto], shiftList: List[Turno], zoneList: List[Zona])
    extends AbstractComponent[RecruitParent]("humanresources/subcomponent/RecruitBox") with RecruitBox {

    case class ContractProperty(fix : Boolean = true, fulltime: Boolean = true, week5x2: Boolean = true)

    @FXML
    var name: TextField = _
    @FXML
    var surname: TextField = _
    @FXML
    var tel: TextField = _
    @FXML
    var recruitDate: DatePicker = _
    @FXML
    var role: ComboBox[String] = _
    @FXML
    var contractTypes: ComboBox[String] = _
    @FXML
    var shift1: ComboBox[String] = _
    @FXML
    var shift2: ComboBox[String] = _
    @FXML
    var day1: ComboBox[String] = _
    @FXML
    var day2: ComboBox[String] = _
    @FXML
    var zones: ComboBox[String] = _
    @FXML
    var terminals: ComboBox[String] = _
    @FXML
    var save: Button = _

    private var terminalList = List[Terminale]()
    private val fixedString = "-Fisso"
    private val rotateString = "-Rotazione"
    private val TEL_NUMBER: Int = 10
    private var contractTypeFI5FU = ContractProperty()
    private val DEFAULT_ZONA=resources.getResource("default-zona")
    private val DEFAULT_ID=None
    private val DEFAULT_ID_INT = 0
    private var baseDays  = List(resources.getResource("monday"),
      resources.getResource("tuesday"),
      resources.getResource("wednesday"),
      resources.getResource("thursday"),
      resources.getResource("friday"))

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initializeComboBox()
      setRecruitDate()
      setActions()
      setPromptText(resources)
    }

    override def setTerminals(terminal: List[Terminale]): Unit = {
      terminalList = terminal
      resetComboBox(terminals)
      terminals.getItems.clear()
      terminal.flatMap(t=>List(t.nomeTerminale)).foreach(t => terminals.getItems.add(t))
      terminals.setDisable(false)
    }

    private def initializeComboBox(): Unit = {
      //default values
      shiftList.foreach(shift => shift1.getItems.add(shift.fasciaOraria))
      shiftList.foreach(shift => shift2.getItems.add(shift.fasciaOraria))
      zoneList.foreach(zone => zones.getItems.add(zone.zones))
      getUserType.foreach(user => role.getItems.add(user))
      initialBlockComponent()
    }

    private def setRecruitDate(): Unit = {
      CreateDatePicker.createDataPicker(DatePickerC(recruitDate, MoveDatePeriod(months = 1), MoveDatePeriod(months = 1)))
      recruitDate.setOnAction(_ => ableSave())
    }

    private def setActions(): Unit = {

      def setNameStringControl(component: TextField): Unit = {
        component.textProperty().addListener((_, oldS, word) => {
          TextFieldControl.controlNewChar(component, NameChecker, word, oldS)
          ableSave()
        })
      }
      setNameStringControl(name)

      setNameStringControl(surname)

      tel.textProperty().addListener((_, oldS, word) => {
        TextFieldControl.controlNewChar(tel, NumberChecker, word, oldS, TEL_NUMBER)
        ableSave()
      })

      zones.setOnAction(_ => {
        parent.loadRecruitTerminals(getIdZone)
        ableSave()
      })

      role.setOnAction(_ => {
        if(!isDriver)
          notDrive(true)
        else
          notDrive(false)
        ableSave()
      })

      contractTypes.setOnAction(_ => {
        contractControl(getComboSelected(contractTypes))
        ableSave()
      })

      day1.valueProperty().addListener(new value.ChangeListener[String] {
        override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {
          Option(newValue).collect {
            case newValue if newValue.equals(getComboSelected(day2)) => day2.getSelectionModel.clearSelection()
          }
          ableSave()
        }
      })

      day2.valueProperty().addListener(new ChangeListener[String] {
        override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {
          Option(newValue) collect {
            case newValue if newValue.equals(getComboSelected(day1)) =>
              day1.getSelectionModel.clearSelection()
          }
          ableSave()
        }
      })

      shift1.setOnAction(_ => {
        val itemSelected: Int = getComboSelectedIndex(shift1)
        if(contractTypeFI5FU.fulltime)
          if( itemSelected == shift1.getItems.size - 1)
            shift2.getSelectionModel.selectFirst()
          else
            shift2.getSelectionModel.select(itemSelected + 1)
        ableSave()
      })

      terminals.setOnAction(_ => ableSave())

      save.setOnAction(_ => {
        if(noEmptyField()) {
          val hasShift1: Boolean = chosenSomething(shift1)
          val hasShift2: Boolean = chosenSomething(shift2)
          parent.recruitClicked(
            Assumi(
              Persona(name.getText, surname.getText, tel.getText, None, getIdRuolo, isNew = true, "", getIdTerminal, None),
              StoricoContratto(CreateDatePicker.createDataSql(recruitDate), None, None,
                  getContrattoId, getIdTurno(hasShift1, shift1), getIdTurno(hasShift2, shift2)),
              getDisponibilita
            )
          )
        }
      })
    }

    private def setPromptText(resources: ResourceBundle): Unit = {
      //set prompt test
      name.setPromptText(resources.getResource("name"))
      surname.setPromptText(resources.getResource("surname"))
      contractTypes.setPromptText(resources.getResource("contract"))
      shift1.setPromptText(resources.getResource("shift"))
      shift2.setPromptText(resources.getResource("shift"))
      zones.setPromptText(resources.getResource("zones"))
      role.setPromptText(resources.getResource("role"))
      day1.setPromptText(resources.getResource("day1"))
      day2.setPromptText(resources.getResource("day2"))
      terminals.setPromptText(resources.getResource("terminals"))
      tel.setPromptText(resources.getResource("tel"))
      save.setText(resources.getResource("save"))
    }

    /////////////////////////////////////////////////////////////////////////////             METODI DI CONTROLLO

    private def contractControl(contract: String): Unit = {
      if(isDriver){
        val workWeek: String = "5x2"    //tutti i 5x2 da lunedi a venerdi
        val typeWork: String = "Full"   //tutti i 6x1 da lunedi a sabato
        var fisso: Boolean = false
        var settimana5x2: Boolean = false
        var full: Boolean = false

        if(chosenSomething(contractTypes)) {
          if (contract.contains(fixedString))
            fisso = true
          if (contract.contains(workWeek))
            settimana5x2 = true
          if (contract.contains(typeWork))
            full = true
        }

        contractTypeFI5FU = ContractProperty(fix = fisso, fulltime = full, week5x2 = settimana5x2)

        fixedShift(contractTypeFI5FU.fix)
        if(contractTypeFI5FU.fix)
          refillDays()
      }
    }

    private def controlMainFields(): Boolean = {
      !(name.getText.equals("") || name.getText.equals(" ") || name.getText.equals("'")) &&
      !(surname.getText.equals("") || surname.getText.equals(" ") || surname.getText.equals("'")) &&
      !contractTypes.getSelectionModel.isEmpty && !tel.getText.equals("") && tel.getText.length == 10 &&
      !(recruitDate.getValue == null)
    }

    private def controlDriverFields(): Boolean = {
      var terminalChosen: Boolean = chosenSomething(terminals)
      if(contractTypeFI5FU.fix && terminalChosen){
        terminalChosen = chosenSomething(shift1) && chosenSomething(day2) && chosenSomething(day1)
        if(contractTypeFI5FU.fulltime && terminalChosen)
          terminalChosen = chosenSomething(shift2)
      }
      terminalChosen
    }

    private def noEmptyField(): Boolean = {
      if(role.getSelectionModel.isEmpty)
        false
      else if(!isDriver)
        controlMainFields()
      else
        controlMainFields() && controlDriverFields()
    }

    private def isDriver: Boolean = {
      getComboSelected(role).equals(driver)
    }

    /////////////////////////////////////////////////////////////////////////////             METODI DI GET

    private def getIdZone: Zona =
      zoneList.find(zone => zone.zones.equals(getComboSelected(zones))) match {
        case Some(zona) =>zona
        case None =>Zona(DEFAULT_ZONA,DEFAULT_ID)
      }

    private def getIdTerminal: Option[Int] ={
      if(isDriver)
        terminalList.find(terminal =>
          terminal.nomeTerminale.equals(getComboSelected(terminals))) match {
          case Some(terminal) => terminal.idTerminale
          case None => DEFAULT_ID
        }
      else
        None
    }

    private def getIdRuolo: Int =
      getComboSelectedIndex(role) + 1

    private def getIdContratto(contratto:Option[Contratto]):Int=
      contratto.toList.flatMap(_.idContratto.toList) match {
        case List(id)=> id
        case _=>DEFAULT_ID_INT
      }
    private def getContrattoId: Int = {
      if(isDriver) {
        val contract: String = getComboSelected(contractTypes)
        val fixedShift: Boolean = contract.contains(fixedString)
        contractList.find(contr => {
          contract.contains(contr.tipoContratto) && contr.turnoFisso == fixedShift
        }) match {
          case Some(contratto) => getIdContratto(Some(contratto))
          case None =>DEFAULT_ID_INT
        }
      }
      else
        contractList.find(contr => contr.ruolo == getIdRuolo) match {
          case Some(contratto) => getIdContratto(Some(contratto))
          case None =>DEFAULT_ID_INT
        }
    }

    private def getIdTurno(search: Boolean, component: ComboBox[String]): Option[Int] = {
      if(search)
        shiftList.find(shift => shift.fasciaOraria.equals(getComboSelected(component))) match {
          case Some(value) => value.id
          case None => DEFAULT_ID
        }
      else None
    }

    def getDisponibilita: Option[Disponibilita] = {
      if(isDriver && contractTypeFI5FU.fix) {
        val day = Calendar.getInstance()
        day.setTime(Date.valueOf(recruitDate.getValue))
        Some(Disponibilita(day.get(Calendar.WEEK_OF_YEAR), getComboSelected(day1), getComboSelected(day2)))
      }
      else
        None
    }

    /////////////////////////////////////////////////////////////////////////////             METODI DI BLOCCO COMPONENTI

    private def ableSave(): Unit = {
      save.setDisable(!noEmptyField)
    }

    private def initialBlockComponent(): Unit = {
      shift1.setDisable(true)
      shift2.setDisable(true)
      zones.setDisable(true)
      contractTypes.setDisable(true)
      day1.setDisable(true)
      day2.setDisable(true)
      terminals.setDisable(true)
      save.setDisable(true)
    }

    private def refillDays(): Unit = {
      if(!contractTypeFI5FU.week5x2)
        baseDays = baseDays.appended(resources.getResource("saturday"))
      refillComponent(day2, baseDays)
      refillComponent(day1, baseDays)
    }

    private def notDrive(value: Boolean): Unit = {
      contractTypes.setDisable(false)
      if(!value)
        refillComponent(contractTypes, contractList.filter(contr => contr.ruolo == getIdRuolo).map(contract =>
          contract.tipoContratto + (if(contract.turnoFisso) fixedString else rotateString)) )
      else {
        refillComponent(contractTypes, contractList.filter(contr => contr.ruolo == getIdRuolo)
          .map(contract => contract.tipoContratto))
        contractTypes.getSelectionModel.selectFirst()
        shift1.setDisable(value)
        day1.setDisable(value)
        day2.setDisable(value)
        terminals.setDisable(value)
        resetComboBox(day1)
        resetComboBox(day2)
      }

      zones.setDisable(value)
      resetComboBox(day1)
      resetComboBox(day2)
      resetComboBox(zones)
      resetComboBox(terminals)
    }

    private def fixedShift(value: Boolean): Unit = {
      shift1.setDisable(!value)
      day1.setDisable(!value)
      day2.setDisable(!value)
      resetComboBox(shift1)
      resetComboBox(shift2)
    }

    /////////////////////////////////////////////////////////////////////////////             METODI DI UTILS

    private def refillComponent(component: ComboBox[String], list: List[String]): Unit = {
      resetComboBox(component)
      emptyComboBox(component)
      list.foreach(day => component.getItems.add(day))
      component.setDisable(false)
    }

    private def chosenSomething(component: ComboBox[String]): Boolean =
      !component.getSelectionModel.isEmpty

    private def emptyComboBox(component: ComboBox[String]): Unit =
      component.getItems.clear()

    private def resetComboBox(component: ComboBox[String]): Unit =
      component.getSelectionModel.clearSelection()

    private def getComboSelected(component: ComboBox[String]): String =
      component.getSelectionModel.getSelectedItem

    private def getComboSelectedIndex(component: ComboBox[String]): Int =
      component.getSelectionModel.getSelectedIndex

  }
}
