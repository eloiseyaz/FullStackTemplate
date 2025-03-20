package services

import models.{APIError, DataModel}
import repositories.DataRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RepositoryService @Inject()(dataRepository: DataRepository)(implicit ec: ExecutionContext) {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]] =
    dataRepository.index()

  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    dataRepository.create(book)

  def read(id: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    dataRepository.read(id)

  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    dataRepository.update(id, book)

  def delete(id: String): Future[Either[APIError.BadAPIResponse, String]] =
    dataRepository.delete(id)

}