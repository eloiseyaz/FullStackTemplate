package controllers

import models.{Book, DataModel}
import models.DataModel.formats
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.DataRepository
import services.{LibraryService, RepositoryService}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val repositoryService: RepositoryService, implicit val ec: ExecutionContext, val libraryService: LibraryService) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.index().map {
      case Right(item: Seq[DataModel]) => Ok {
        Json.toJson(item)
      }
      case Left(error) => Status(error.httpResponseStatus)(error.reason)
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repositoryService.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def read(id: String): Action[AnyContent] = Action.async {
    repositoryService.read(id).map {
      case Right(book) => Ok(Json.toJson(book))
      case Left(error) => NotFound(Json.toJson(error))
    }
  }


  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repositoryService.update(id, dataModel).map(_ => Accepted {
          request.body
        }) //can also consider .read()
      case JsError(_) => Future(BadRequest)
    }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.delete(id).map {
      case Right(book) => Accepted
      case Left(error) => NotFound
    }
  }

  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
    libraryService.getGoogleBook(search = search, term = term).value.map {
      case Right(book) => Ok(Json.toJson(book))
      case Left(error) => Status(error.httpResponseStatus)(error.reason)
    }
  }

  def getDatabaseBook(search: String, field: String): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.getDatabaseBook(search = search, field = field).map {
      case Right(book) => Ok(Json.toJson(book))
      case Left(error) => NotFound(Json.toJson(error))
    }
  }

  def edit(id: String, field: String, replacement: String): Action[AnyContent] = Action.async {
    implicit request =>
      repositoryService.edit(id = id, field = field, replacement = replacement).map {
        case Right(book) => Ok(Json.toJson(book))
        case Left(error) => NotFound(Json.toJson(error))
      }
  }

  def getAndStoreGoogleBookByISBN(isbn: String): Action[AnyContent] = Action.async {
    implicit request =>
      libraryService.getGoogleBookByISBN(isbn).value.map({
        case Right(book) =>
          repositoryService.create(book.toDataModel)
          Ok(Json.toJson(book))
        case Left(error) => Status(error.httpResponseStatus)(error.reason)
      })
  }
}
