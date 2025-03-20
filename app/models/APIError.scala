package models

import play.api.http.Status
import play.api.libs.json.{JsValue, Json, Writes}

sealed abstract class APIError(val httpResponseStatus: Int, val reason: String)

object APIError {

  final case class BadAPIResponse(upstreamStatus: Int, upstreamMessage: String)
    extends APIError(
      Status.INTERNAL_SERVER_ERROR,
      s"Bad response from upstream; Status: $upstreamStatus, Reason: $upstreamMessage"
    )
  implicit val badAPIResponseWrites: Writes[BadAPIResponse] = Json.writes[BadAPIResponse]

  implicit def eitherWrites[A: Writes, B: Writes]: Writes[Either[A, B]] = new Writes[Either[A, B]] {
    def writes(either: Either[A, B]): JsValue = either match {
      case Left(a) => Json.obj("error" -> Json.toJson(a))
      case Right(b) => Json.toJson(b)
    }
  }
}
