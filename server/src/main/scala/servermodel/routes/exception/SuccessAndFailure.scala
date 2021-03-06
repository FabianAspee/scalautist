package servermodel.routes.exception

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import caseclass.CaseClassHttpMessage.Response
import messagecodes.{StatusCodes => statusCodes}
import jsonmessages.JsonFormats._

import scala.util.{Failure, Success, Try}

object SuccessAndFailure {
  val timeoutResponse: HttpResponse = HttpResponse(
    StatusCodes.EnhanceYourCalm,
    entity = "Unable to serve response within time limit, please enhance your calm.")
  def anotherSuccessAndFailure[A](result:Try[A]): StandardRoute =result match {
    case Success(None) => complete(StatusCodes.NotFound,Response[Int](statusCodes.NOT_FOUND))
    case Success((None,_)) => complete(StatusCodes.NotFound,Response[Int](statusCodes.NOT_FOUND))
    case Success(Some(List())| List())  =>    complete(StatusCodes.NotFound,Response[Int](statusCodes.NOT_FOUND))
    case Success(Some(statusCodes.NOT_FOUND)) =>    complete(StatusCodes.NotFound,Response[Int](statusCodes.NOT_FOUND))
    case Success(Some(statusCodes.ERROR_CODE1)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE1))
    case Success(Some(statusCodes.ERROR_CODE2)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE2))
    case Success(Some(statusCodes.ERROR_CODE3)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE3))
    case Success(Some(statusCodes.ERROR_CODE4)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE4))
    case Success(Some(statusCodes.ERROR_CODE5)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE5))
    case Success(Some(statusCodes.ERROR_CODE6)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE6))
    case Success(Some(statusCodes.ERROR_CODE7)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE7))
    case Success(Some(statusCodes.ERROR_CODE8)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE8))
    case Success(Some(statusCodes.ERROR_CODE9)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE9))
    case Success(Some(statusCodes.ERROR_CODE10)) => complete(StatusCodes.EnhanceYourCalm,Response[Int](statusCodes.ERROR_CODE10))
    case Success(Some(statusCodes.ERROR_CODE11)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE11))
    case Success(Some(statusCodes.ERROR_CODE12)) => complete(StatusCodes.BadRequest,Response[Int](statusCodes.ERROR_CODE12))
    case Success(Some(statusCodes.INFO_CODE1)) => complete(StatusCodes.Continue,Response[Int](statusCodes.INFO_CODE1))
    case Success(Some(statusCodes.INFO_CODE2)) => complete(StatusCodes.Continue,Response[Int](statusCodes.INFO_CODE2))
    case Success(Some(statusCodes.INFO_CODE3)) => complete(StatusCodes.Continue,Response[Int](statusCodes.INFO_CODE3))
    case Success(Some(statusCodes.INFO_CODE4)) => complete(StatusCodes.Continue,Response[Int](statusCodes.INFO_CODE4))
    case t => failure(t)
  }

  private def failure[A](result:Try[A]): StandardRoute =result match {
    case Success(_) => complete(StatusCodes.InternalServerError,Response[Int](StatusCodes.InternalServerError.intValue))
    case Failure(_) => complete(StatusCodes.InternalServerError,Response[Int](StatusCodes.InternalServerError.intValue))
  }
}
