package models

import play.api.http.Status

sealed abstract class APIError(val httpResponseStatus: Int, val reason: String)

object APIError {

  final case class BadAPIResponse(upstreamStatus: Int, upstreamMessage: String)
    extends APIError(
      Status.INTERNAL_SERVER_ERROR,
      s"Bad response from upstream; Status: $upstreamStatus, Reason: $upstreamMessage"
    )

  final case class NotFoundError(details: String)
    extends APIError(
      Status.NOT_FOUND,
      s"Resource not found: $details"
    )

  final case class DatabaseError(details: String)
    extends APIError(
      Status.INTERNAL_SERVER_ERROR,
      s"Database operation failed: $details"
    )
}
