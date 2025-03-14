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
    component, repository, executionContext
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

  }

  "ApplicationController .update" should {
    beforeEach()

    afterEach()
  }

  "ApplicationController .delete" should {
    beforeEach()

    afterEach()
  }

  override def beforeEach(): Unit = await(repository.deleteAll())
  override def afterEach(): Unit = await(repository.deleteAll())

}
