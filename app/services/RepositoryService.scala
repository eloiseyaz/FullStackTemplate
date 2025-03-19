package services

import models.{APIError, DataModel}
import repositories.DataRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RepositoryService @Inject()(dataRepository: DataRepository)(implicit ec: ExecutionContext) {




}