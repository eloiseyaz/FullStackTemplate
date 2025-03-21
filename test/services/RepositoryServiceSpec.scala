package services

import baseSpec.BaseSpecWithApplication
import models.DataModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import repositories.DataRepository
import uk.gov.hmrc.mongo.MongoComponent

import scala.concurrent.{ExecutionContext, Future}


class RepositoryServiceSpec extends BaseSpecWithApplication with MockFactory {
  val mockRepositoryService: RepositoryService = mock[RepositoryService]

  "RepositoryService .index" should {

    "return books" in {
      (mockRepositoryService.index _)
        .expects()
        .returning(Future.successful(Right(Seq(DataModel("1", "name", "author", "desc", 100)))))

      whenReady(mockRepositoryService.index()) { result =>
        result shouldBe Right(Seq(DataModel("1", "name", "author", "desc", 100)))
      }
    }

    "return 404 error" in {

    }
  }
  //
  //  "RepositoryService .create" should {
  //
  //    "create a book in the database" in {
  //      beforeEach()
  //
  //
  //      afterEach()
  //    }
  //
  //    "return 500 error" in {
  //      beforeEach()
  //
  //      afterEach()
  //    }
  //
  //  }
  //
  //  "RepositoryService .read" should {
  //
  //    "find a book in the database by id" in {
  //      beforeEach()
  //
  //      afterEach()
  //    }
  //
  //    "return 404 NotFound for invalid ID" in {
  //      beforeEach()
  //
  //
  //      afterEach()
  //    }
  //
  //  }
  //
  //  "RepositoryService .update" should {
  //
  //    "update a book in the database by id" in {
  //      beforeEach()
  //
  //      afterEach()
  //    }
  //
  //    "return 400 BadRequest if JSON is invalid" in {
  //      beforeEach()
  //
  //      afterEach()
  //    }
  //
  //  }
  //
  //  "RepositoryService .delete" should {
  //
  //    "delete a book in the database by id" in {
  //      beforeEach()
  //
  //      afterEach()
  //    }
  //
  //    "return 500 error for invalid ID" in {
  //      beforeEach()
  //
  //      afterEach()
  //    }
  //
  //  }

}
