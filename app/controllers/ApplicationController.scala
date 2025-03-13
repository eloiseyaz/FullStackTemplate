package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.Future

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index(): Action[AnyContent] = Action{Ok}

  def create() = TODO

  def read(id: String) = TODO

  def update(id: String) = TODO

  def delete(id: String) = TODO

}
