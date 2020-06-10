package model.entity

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.{Stipendio, Turno}
import caseclass.CaseClassHttpMessage.Id
import model.AbstractModel

import jsonmessages.JsonFormats._
import scala.concurrent.Future


/**
 * DriverModel extends [[model.Model]].
 * Interface for driver's operation on data
 */
trait DriverModel extends AbstractModel{
  /**
   * Return a set of one driver's turn
   * @param id
   * User id
   * @param startData
   * The start date of the workshift period to be shown
   * @param endData
   * The end date of the workshift period to be shown
   * @return
   * Future of list of Turno
   */
  def getWorkshift(id: Int, startData: String, endData: String): Future[List[Turno]]


  /**
   *  Method that obtains salary for a person
   * @param id id that represent case class that contains id of a persona
   * @return Option of List of Stipendio that represent all salary of a persona
   */
  def getSalary(id:Id):Future[Option[List[Stipendio]]]

}

object DriverModel {

  def apply(): DriverModel = new DriverResourceHttp()

  private class DriverResourceHttp extends DriverModel {

    override def getWorkshift(id: Int, startData: String, endData: String): Future[List[Turno]] = ???

    override def getSalary(id: Id): Future[Option[List[Stipendio]]] = {
      val request = Post(getURI("getstipendio"))
      callServerSalary(request)
    }
    private def callServerSalary(request: HttpRequest)=
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Option[List[Stipendio]]])
  }

}
