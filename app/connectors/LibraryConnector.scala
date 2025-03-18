package connectors

class LibraryConnector @Inject()(ws: WSClient) {
  def get[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): Future[Response] = {
    val request = ws.url(url)
    val response = request.get()
    response.map {
      result =>
        result.json.as[Response]
    }
  }
}
