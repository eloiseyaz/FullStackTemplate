package controllers

import play.api.mvc._
import repositories.DataRepository

import javax.inject._
import scala.concurrent.Future

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository: DataRepository) extends BaseController {

  def index(): Action[AnyContent] = Action{Ok}

  def create() = TODO

  def read(id: String) = TODO

  def update(id: String) = TODO

  def delete(id: String) = TODO

}
