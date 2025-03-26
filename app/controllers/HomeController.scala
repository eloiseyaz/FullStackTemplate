package controllers

import models.APIError

import javax.inject._
import play.api.mvc._
import services.{LibraryService, RepositoryService}

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               repositoryService: RepositoryService)
                              (implicit ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.index())
  }

}