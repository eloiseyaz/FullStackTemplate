package services

import connectors.LibraryConnector
import models.{Book, GoogleBooksResponse}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LibraryService @Inject()(connector: LibraryConnector) {

  def getGoogleBook(urlOverride: Option[String] = None, search: String, term: String)(implicit ec: ExecutionContext): Future[Book] = {
    connector.get[GoogleBooksResponse](urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes?q=$search%$term")).map { response =>
      Book.fromVolume(response.items.head)
    }
  }
}