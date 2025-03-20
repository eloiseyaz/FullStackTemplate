package services

import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import repositories.DataRepository


class RepositoryServiceSpec extends BaseSpecWithApplication {

  val TestDataRepository = new DataRepository(???)(executionContext)

  "RepositoryService .index" should {

    "return books" in {
      beforeEach()

      val result = TestDataRepository.index()

      result shouldBe ???

      afterEach()
    }

    "retun 404 error" in {

    }
  }

  "RepositoryService .create" should {

    "create a book in the database" in {
      beforeEach()


      afterEach()
    }

    "return 500 error" in {
      beforeEach()

      afterEach()
    }

  }

  "RepositoryService .read" should {

    "find a book in the database by id" in {
      beforeEach()

      afterEach()
    }

    "return 404 NotFound for invalid ID" in {
      beforeEach()


      afterEach()
    }

  }

  "RepositoryService .update" should {

    "update a book in the database by id" in {
      beforeEach()

      afterEach()
    }

    "return 400 BadRequest if JSON is invalid" in {
      beforeEach()

      afterEach()
    }

  }

  "RepositoryService .delete" should {

    "delete a book in the database by id" in {
      beforeEach()

      afterEach()
    }

    "return 404 NotFound for invalid ID" in {
      beforeEach()

      afterEach()
    }

  }

}
