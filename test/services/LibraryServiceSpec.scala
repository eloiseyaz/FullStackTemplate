package services

import cats.data.EitherT
import connectors.LibraryConnector
import models.{APIError, Book, GoogleBooksResponse}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json, OFormat}

import scala.concurrent.{ExecutionContext, Future}

class LibraryServiceSpec extends AnyWordSpec with Matchers with MockFactory with ScalaFutures with GuiceOneAppPerSuite {

  val mockConnector: LibraryConnector = mock[LibraryConnector]
  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testService = new LibraryService(mockConnector)

  // Test code will go here

  val gameOfThronesResponse: JsValue = Json.obj(
    "items" -> Json.arr(
      Json.obj(
        "id" -> "someId",
        "volumeInfo" -> Json.obj(
          "title" -> "A Game of Thrones",
          "authors" -> Json.arr("George R.R. Martin"),
          "description" -> "The best book!!!",
          "pageCount" -> 100
        )
      )
    )
  )

  "getGoogleBook" should {
    val url: String = "testUrl"

    "return a book" in {
      (mockConnector.get[GoogleBooksResponse](_: String)(_: OFormat[GoogleBooksResponse], _: ExecutionContext))
        .expects(url, *, *)
        .returning(EitherT.rightT[Future, APIError](gameOfThronesResponse.as[GoogleBooksResponse]))
        .once()

      whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) { result =>
        val expectedBook = Book(
          id = "someId",
          title = "A Game of Thrones",
          authors = List("George R.R. Martin"),
          description = "The best book!!!",
          pageCount = 100
        )

        result shouldBe Right(expectedBook)
      }
    }


//    "return an error" in {
//      val url: String = "testUrl"
//
//      (mockConnector.get[???](_: ???)(_: OFormat[???], _: ???))
//        .expects(url, *, *)
//        .returning(???)// How do we return an error?
//        .once()
//
//      whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) { result =>
//        result shouldBe ???
//      }
//    }
  }



}














