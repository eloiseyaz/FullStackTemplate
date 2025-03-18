package services

import connectors.LibraryConnector
import play.api.libs.json.{Json, OFormat}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LibraryService @Inject()(connector: LibraryConnector) {

  def getGoogleBook(urlOverride: Option[String] = None, search: String, term: String)(implicit ec: ExecutionContext): Future[Book] =
    connector.get[Book](urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes?q=$search%$term"))

}

case class Book(_id: String, title: String, description: String, pageCount: Int)

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}