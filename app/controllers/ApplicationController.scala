package controllers

import play.api.mvc._
import javax.inject._

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def index() = TODO
}
