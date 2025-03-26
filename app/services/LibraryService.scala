package services

import cats.data.EitherT
import connectors.LibraryConnector
import models.{APIError, Book, GoogleBooksResponse}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LibraryService @Inject()(connector: LibraryConnector) {

  def getGoogleBook(urlOverride: Option[String] = None, search: String, term: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, Book] = {
    connector.get[GoogleBooksResponse](urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes?q=$search%$term")).map { response =>
      Book.fromVolume(response.items.head)
    }
  }

  def getGoogleBookByISBN(isbn: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, Book] = {
    connector.get[GoogleBooksResponse](s"https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn").map { response =>
      Book.fromVolume(response.items.head)
    }
  }
}