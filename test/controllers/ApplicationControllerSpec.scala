package controllers

import baseSpec.BaseSpecWithApplication
import models.DataModel
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.test.Helpers._

import scala.concurrent.Future

class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component, repository, repoService, executionContext, service
  )

  private val dataModel: DataModel = DataModel(
    "testId",
    "test name",
    "test description",
    100
  )

  "ApplicationController .index" should {

    "return 200 OK" in {
      beforeEach()

      val result = TestApplicationController.index()(FakeRequest())
      status(result) shouldBe Status.OK

      afterEach()
    }

  }

  "ApplicationController .create" should {

    "create a book in the database" in {
      beforeEach()

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED

      afterEach()
    }

  /*  "return BadRequest if id already exists" in {
      beforeEach()

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED

      val badRequest: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val badResult: Future[Result] = TestApplicationController.create()(badRequest)

      println(status(badResult))
      //val answer = status(badResult) shouldBe Status.BAD_REQUEST
      //println(answer)
      status(badResult) shouldBe Status.BAD_REQUEST

      afterEach()
    }*/ //can add later if we want to improve create :)

    "return BadRequest if JSON is invalid" in {
      beforeEach()

      val invalidRequest = buildPost("/api").withBody[JsValue](Json.obj("invalidField" -> "value"))
      val result = TestApplicationController.create()(invalidRequest)

      status(result) shouldBe Status.BAD_REQUEST

      afterEach()
    }

  }

  "ApplicationController .read" should {

    "find a book in the database by id" in {
      beforeEach()

      val request: FakeRequest[JsValue] = buildGet("/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED

      val readResult: Future[Result] = TestApplicationController.read("testId")(FakeRequest())

      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[DataModel] shouldBe dataModel

      afterEach()
    }

/*    "return 404 NotFound for invalid ID" in {
      beforeEach()

      val readResult: Future[Result] = TestApplicationController.read("invalidId")(FakeRequest())

      status(readResult) shouldBe Status.NOT_FOUND

      afterEach()
    }*/ //can use when we refactor .read with Eithers

  }

  "ApplicationController .update" should {

    "update a book in the database by id" in {
      beforeEach()

      val request: FakeRequest[JsValue] = buildGet("/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED

      val updateDataModel: DataModel = DataModel(
        "testId",
        "Reverend Insanity",
        "Waris's favourite book!",
        100000
      )
      val updateRequest: FakeRequest[JsValue] = buildGet("/api/${dataModel._id}").withBody[JsValue](Json.toJson(updateDataModel))
      val updateResult: Future[Result] = TestApplicationController.update("testId")(updateRequest)

      status(updateResult) shouldBe Status.ACCEPTED
      contentAsJson(updateResult).as[DataModel] shouldBe updateDataModel

      afterEach()
    }


    //Double check it actually updates and returns bad request correctly
    "return 400 BadRequest if JSON is invalid" in {
      beforeEach()

      // First, create a book in the database
      val createRequest: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createResult: Future[Result] = TestApplicationController.create()(createRequest)
      status(createResult) shouldBe Status.CREATED

      // Try to update the book with invalid JSON
      val invalidRequest = buildPost("/api/testId").withBody[JsValue](Json.obj("invalidField" -> "value"))
      val updateResult: Future[Result] = TestApplicationController.update("testId")(invalidRequest)

      // response status should be 400 BadRequest
      status(updateResult) shouldBe Status.BAD_REQUEST

      afterEach()
    }

  }

  "ApplicationController .delete" should {

    "delete a book in the database by id" in {
      beforeEach()

      val request: FakeRequest[JsValue] = buildGet("/api/${dataModel._id}").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED

      val deleteResult: Future[Result] = TestApplicationController.delete("testId")(FakeRequest())

      status(deleteResult) shouldBe Status.ACCEPTED

      afterEach()
    }

    /*"return 404 NotFound for invalid ID" in {
      beforeEach()

      val deleteResult: Future[Result] = TestApplicationController.delete("invalidId")(FakeRequest())

      status(deleteResult) shouldBe Status.NOT_FOUND

      afterEach()

    }*/

  }

  override def beforeEach(): Unit = await(repository.deleteAll())
  override def afterEach(): Unit = await(repository.deleteAll())

}
