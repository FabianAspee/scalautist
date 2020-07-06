package algoritmo

import java.sql.Date

import algoritmo.Algoritmo.InfoForAlgorithm
import scala.concurrent.ExecutionContext.Implicits.global
import caseclass.CaseClassHttpMessage.AlgorithmExecute
import _root_.emitter.ConfigEmitter

import scala.concurrent.Future
trait AssignmentOperation{
  def initOperationAssignment(algorithmExecute: AlgorithmExecute,infoForAlgorithm: Future[InfoForAlgorithm]):Unit
}
object AssignmentOperation extends AssignmentOperation {
  //TODO DETTO DA GIANNI! assegnare prima i liberi ai 5x2 sabato e domenica libero
  //TODO assegnare domeniche 6x1 indipendente sia fisso o rotatorio
  //TODO se ci sono assegniamo i gruppi per ogni gruppo assegna tutti un libero controllare in che data serve di piu
  //TODO che lavorare la persona prima di assegnare il libero del gruppo
  //TODO assegnare tutti turni prima fissi dopo rotatorio, verificare InfoReq quando si sta assegnando
  //TODO verificare se regola tre sabato e attiva, se e cosi e il conducente ha 3 sabati di seguito lavorando,
  //TODO il 3 e libero, se il 3 e insieme a una domenica libera allora lo avrà il 2 e cosi via via
  //TODO assegnare i liberi ai 6x1 fissi, controllare quantità di giorni senza libero. in piu guardare se ce una settimana normale o speciale
  //TODO due liberi non possono essere insieme, due liberi devono avere una distanza minima di 2 giorni
  //TODO assegnare liberi ai 6x1 rotatorio, controllare InfoReq per vedere quanti possono essere liberi in quel turno in quella data

  final case class InfoDay(data:Date,shift:Int,shift2:Option[Int],straordinario:Option[Int])
  final case class Info(idDriver:Int,idTerminal:Int,isFisso:Boolean,tipoContratto:Int,infoDay: List[InfoDay])
  private val emitter=ConfigEmitter()
  def apply():AssignmentOperation ={
    emitter.start()
    this
  }
  override def initOperationAssignment(algorithmExecute: AlgorithmExecute,infoForAlgorithm: Future[InfoForAlgorithm]): Unit = {
    emitter.sendMessage("Iniziando processo di assegnazione")

  }
  private def assignSaturdayAndSunday5x2(infoForAlgorithm: Future[InfoForAlgorithm])={
    //infoForAlgorithm.flatMap(driver=>driver.persons.map(idPerson=>idPerson._1))
  }
  private def assignSunday6x1()={

  }
  private def assignGroup()={

  }
  private def assignShiftFixed()={

  }
  private def assignShiftRotary()={

  }
  private def rulerThirfSaturday()={

  }
  private def assignFreeDayFixed6x1()={

  }
  private def assignFreeDayRotary6x1()={

  }

}