package controllers

import models.DataModel
import play.api.libs.json.Json
import play.api.mvc._
import repositories.DataRepository

import javax.inject._
import scala.concurrent.{ExecutionContext}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository: DataRepository, implicit val ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.index().map{
      case Right(item: Seq[DataModel]) => Ok {Json.toJson(item)}
      case Left(error) => Status(error)(Json.toJson("Unable to find any books"))
    }
  }

  def create(): Action[AnyContent] = TODO

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.read(id).map(data => Ok {Json.toJson(data)})
  }

  def update(id: String): Action[AnyContent] = TODO

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.delete(id).map(_ => Accepted)

}
