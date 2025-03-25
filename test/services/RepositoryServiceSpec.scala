package services

import baseSpec.BaseSpecWithApplication
import models.{APIError, Book, DataModel}
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Future


class RepositoryServiceSpec extends BaseSpecWithApplication with MockFactory {
  val mockRepositoryService: RepositoryService = mock[RepositoryService]

  "RepositoryService .index" should {

    "return books" in {
      (mockRepositoryService.index _)
        .expects()
        .returning(Future.successful(Right(Seq(DataModel("1", "name", "author", "description", 100)))))

      whenReady(mockRepositoryService.index()) { result =>
        result shouldBe Right(Seq(DataModel("1", "name", "author", "description", 100)))
      }
    }

    "return 404 error" in {
      (mockRepositoryService.index _)
        .expects()
        .returning(Future.successful(Left(APIError.BadAPIResponse(404, "Books cannot be found"))))

      whenReady(mockRepositoryService.index()) { result =>
        result shouldBe Left(APIError.BadAPIResponse(404, "Books cannot be found"))
      }
    }
  }

  "RepositoryService .create" should {

    "create a book in the database" in {
      beforeEach()
      val createdBook = DataModel("1", "name", "author", "description", 100)
      (mockRepositoryService.create _)
        .expects(createdBook)
        .returning(Future.successful(Right(DataModel("1", "name", "author", "description", 100))))

      whenReady(mockRepositoryService.create(createdBook)) { result =>
        result shouldBe Right(DataModel("1", "name", "author", "description", 100))
      }
      afterEach()
    }

    "return 500 error" in {
      beforeEach()

      val createdBook = DataModel("1", "name", "author", "description", 100)
      (mockRepositoryService.create _)
        .expects(*)
        .returning(Future.successful(Left(APIError.BadAPIResponse(500, "Book cannot be created"))))

      whenReady(mockRepositoryService.create(createdBook)) { result =>
        result shouldBe Left(APIError.BadAPIResponse(500, "Book cannot be created"))
      }

      afterEach()
    }

  }

  "RepositoryService .read" should {

    "find a book in the database by id" in {
      beforeEach()
      val createdBook = DataModel("1", "name", "author", "description", 100)
      (mockRepositoryService.read _)
        .expects(*)
        .returning(Future.successful(Right(DataModel("1", "name", "author", "description", 100))))

      whenReady(mockRepositoryService.read("111")) { result =>
        result shouldBe Right(DataModel("1", "name", "author", "description", 100))
      }


      afterEach()
    }

    "return 404 NotFound for invalid ID" in {
      beforeEach()

      (mockRepositoryService.read _)
        .expects(*)
        .returning(Future.successful(Left(APIError.BadAPIResponse(404, "Book ID not found"))))

      whenReady(mockRepositoryService.read("111")) { result =>
        result shouldBe Left(APIError.BadAPIResponse(404, "Book ID not found"))
      }

      afterEach()
    }

  }

  "RepositoryService .update" should {

    "update a book in the database by id" in {
      beforeEach()

      val createdBook = DataModel("1", "name", "author", "description", 100)
      val updatedBook = DataModel("1", "ShadowSlave", "author", "description", 100)
      (mockRepositoryService.update _)
        .expects(*, *)
        .returning(Future.successful(Right(DataModel("1", "name", "author", "description", 100))))

      whenReady(mockRepositoryService.update("111", createdBook)) { result =>
        result shouldBe Right(DataModel("1", "name", "author", "description", 100))
      }

      afterEach()
    }

    "return 400 BadRequest if JSON is invalid" in {
      beforeEach()
      val createdBook = DataModel("1", "name", "author", "description", 100)
      (mockRepositoryService.update _)
        .expects(*, *)
        .returning(Future.successful(Left(APIError.BadAPIResponse(400, "JSON is invalid"))))

      whenReady(mockRepositoryService.update("111", createdBook)) { result =>
        result shouldBe Left(APIError.BadAPIResponse(400, "JSON is invalid"))
      }

      afterEach()
    }

  }

  "RepositoryService .delete" should {

    "delete a book in the database by id" in {
      beforeEach()
      (mockRepositoryService.delete _)
        .expects(*)
        .returning(Future.successful(Right("1234")))

      whenReady(mockRepositoryService.delete("1234")) { result =>
        result shouldBe Right("1234")
      }
      afterEach()
    }

    "return 404 error for invalid ID" in {
      beforeEach()
      (mockRepositoryService.delete _)
        .expects(*)
        .returning(Future.successful(Left(APIError.BadAPIResponse(404, "Book ID not found"))))

      whenReady(mockRepositoryService.delete("111")) { result =>
        result shouldBe Left(APIError.BadAPIResponse(404, "Book ID not found"))
      }
      afterEach()
    }

    "return 500 error failed to delete" in {
      beforeEach()
      (mockRepositoryService.delete _)
        .expects(*)
        .returning(Future.successful(Left(APIError.BadAPIResponse(500, "Failed to delete book"))))

      whenReady(mockRepositoryService.delete("121")) { result =>
        result shouldBe Left(APIError.BadAPIResponse(500, "Failed to delete book"))
      }
      afterEach()

    }
  }

  "RepositoryService .getDatabaseBook" should {

    "return book with correct id" in {
      beforeEach()

      (mockRepositoryService.getDatabaseBook _)
        .expects(*, "id")
        .returning(Future.successful(Right(DataModel("1", "name", "author", "description", 100))))

      whenReady(mockRepositoryService.getDatabaseBook("111", "id")) { result =>
        result shouldBe Right(DataModel("1", "name", "author", "description", 100))
      }


      afterEach()
    }

    "return 404 NotFound for invalid ID" in {
      beforeEach()

      (mockRepositoryService.getDatabaseBook _)
        .expects(*, "id")
        .returning(Future.successful(Left(APIError.BadAPIResponse(404, "Book ID not found"))))

      whenReady(mockRepositoryService.getDatabaseBook("111", "id")) { result =>
        result shouldBe Left(APIError.BadAPIResponse(404, "Book ID not found"))
      }

      afterEach()
    }
    "return book with correct name" in {

      (mockRepositoryService.getDatabaseBook _)
        .expects(*, "name")
        .returning(Future.successful(Right(DataModel("1", "name", "author", "description", 100))))

      whenReady(mockRepositoryService.getDatabaseBook("111", "name")) { result =>
        result shouldBe Right(DataModel("1", "name", "author", "description", 100))
      }
    }

    "return 404 NotFound for invalid name" in {
      (mockRepositoryService.getDatabaseBook _)
        .expects(*, "name")
        .returning(Future.successful(Left(APIError.BadAPIResponse(404, "Book name not found"))))

      whenReady(mockRepositoryService.getDatabaseBook("111", "name")) { result =>
        result shouldBe Left(APIError.BadAPIResponse(404, "Book name not found"))
      }
    }
    "return book with correct author" in {

      (mockRepositoryService.getDatabaseBook _)
        .expects(*, "author")
        .returning(Future.successful(Right(DataModel("1", "name", "author", "description", 100))))

      whenReady(mockRepositoryService.getDatabaseBook("111", "author")) { result =>
        result shouldBe Right(DataModel("1", "name", "author", "description", 100))
      }
    }

    "return 404 NotFound for invalid author" in {
      (mockRepositoryService.getDatabaseBook _)
        .expects(*, "name")
        .returning(Future.successful(Left(APIError.BadAPIResponse(404, "Author name not found"))))

      whenReady(mockRepositoryService.getDatabaseBook("111", "name")) { result =>
        result shouldBe Left(APIError.BadAPIResponse(404, "Author name not found"))
      }
    }
  }


  "RepositoryService .edit" should {

    "return book with name field edited" in {

      (mockRepositoryService.edit _)
        .expects(*, *, *)
        .returning(Future.successful(Right(DataModel("0605", "ShadowSlave", "author", "description", 1000000))))

      whenReady(mockRepositoryService.edit("0605", "name", "ShadowSlave")) { result =>
        result shouldBe Right(DataModel("0605", "ShadowSlave", "author", "description", 1000000))
      }
    }
    "return book with description field edited" in {

      (mockRepositoryService.edit _)
        .expects(*, *, *)
        .returning(Future.successful(Right(DataModel("0605", "ShadowSlave", "author", "Growing up in poverty, Sunny never expected anything good from life. " +
          "However, even he did not anticipate being chosen by the Nightmare Spell and becoming one of the Awakened - an elite group of people gifted with supernatural powers. " +
          "Transported into a ruined magical world, he found himself facing against terrible monsters - and other Awakened - in a deadly battle of survival." +
          "\nWhat's worse, the divine power he received happened to possess a small, but potentially fatal side effect...", 1000000))))

      whenReady(mockRepositoryService.edit("0605", "description", "")) { result =>
        result shouldBe Right(DataModel("0605", "ShadowSlave", "author", "Growing up in poverty, Sunny never expected anything good from life. " +
          "However, even he did not anticipate being chosen by the Nightmare Spell and becoming one of the Awakened - an elite group of people gifted with supernatural powers. " +
          "Transported into a ruined magical world, he found himself facing against terrible monsters - and other Awakened - in a deadly battle of survival." +
          "\nWhat's worse, the divine power he received happened to possess a small, but potentially fatal side effect...", 1000000))
      }
    }

    "return 400 BadRequest when field is invalid" in {
      (mockRepositoryService.edit _)
        .expects(*, *, *)
        .returning(Future.successful(Left(APIError.BadAPIResponse(404, "Field is invalid"))))

      whenReady(mockRepositoryService.edit("0605", "nameeee", "ShadowSlave")) { result =>
        result shouldBe Left(APIError.BadAPIResponse(404, "Field is invalid"))
      }

    }
  }
}
