package servermodel.routes.subroute

import java.sql.Date

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Risultato
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, Id, Request, Response}
import dbfactory.operation.RisultatoOperation
import jsonmessages.ImplicitDate._
import jsonmessages.JsonFormats._
import messagecodes.{StatusCodes => statusCodes}
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.concurrent.Future
import scala.util.Success

/**
 * @author Francesco Cassano
 * RisultatoRoute is an object that manage methods that act on the risultato entity
 */
object RisultatoRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)

  def getRisultato: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(RisultatoOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllRisultato: Route =
    post {
      onComplete(RisultatoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createRisultato(): Route =
    post {
      entity(as[Risultato]) { risultato =>
        onComplete(RisultatoOperation.insert(risultato)) {
          case Success(t) =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def updateShift(): Route =
    post {
      entity(as[Request[(Int, Int)]]) {
        case Request(Some(shift)) =>
          onComplete(RisultatoOperation.updateAbsence(shift._1, shift._2)) {
            case Success(Some(statusCodes.SUCCES_CODE)) => complete(Response[Int](statusCodes.SUCCES_CODE))
            case other => anotherSuccessAndFailure(other)
        }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }

  def runAlgorithm(): Route = // TODO
  post {
      entity(as[Request[AlgorithmExecute]]) {
        case Request(Some(infoAlgorithm)) =>
          onComplete(Future.successful()) {
            case Success(_) => complete(Response[Int](statusCodes.SUCCES_CODE))
            case other => anotherSuccessAndFailure(other)
          }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }

  def getResultAlgorithm: Route = // TODO
  post {
      entity(as[Request[(Int,Date,Date)]]) {
        case Request(Some(resultAlgoritm)) =>onComplete(RisultatoOperation.getResultAlgorithm(resultAlgoritm._1,resultAlgoritm._2,resultAlgoritm._3)) {
            case Success(_) => complete(Response[Int](statusCodes.SUCCES_CODE))
            case other => anotherSuccessAndFailure(other)
          }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }


}
