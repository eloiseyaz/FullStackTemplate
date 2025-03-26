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

  def viewDatabaseBook(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.read(id).map {
      case Right(book) => Ok(views.html.book(book))
      case Left(error) =>
        error match {
          case APIError.BadAPIResponse(_, message) =>
            NotFound(views.html.error(s"Book not found: $message"))
        }
    }
  }
}