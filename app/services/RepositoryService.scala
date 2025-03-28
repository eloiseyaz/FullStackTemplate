package services

import models.{APIError, DataModel}
import repositories.{DataRepository, DataRepositoryTrait}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RepositoryService @Inject()(dataRepository: DataRepositoryTrait)(implicit ec: ExecutionContext) {

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

  def getDatabaseBook(search: String, field: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    dataRepository.getDatabaseBook(search = search, field = field)

  def edit(id: String, field: String, replacement: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    dataRepository.edit(id = id, field = field, replacement = replacement)

}