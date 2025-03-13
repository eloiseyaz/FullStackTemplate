package controllers

import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.test.Helpers._

class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component, repository
  )

  "ApplicationController .index" should {

    val result = TestApplicationController.index()(FakeRequest())

    "return 200 OK" in {
      status(result) shouldBe Status.OK
    }

  }

  "ApplicationController .create" should {

  }

  "ApplicationController .read" should {

  }

  "ApplicationController .update" should {

  }

  "ApplicationController .delete" should {

  }

}
