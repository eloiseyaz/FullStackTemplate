package repositories

import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DataRepository @Inject()(
                                mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "dataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id")
  )),
  replaceIndexes = false
) {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]] =
    collection.find().toFuture().map {
      case books: Seq[DataModel] => Right(books)
      case _ => Left(APIError.BadAPIResponse(404, "Books cannot be found"))
    }

  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection
      .insertOne(book)
      .toFuture()
      .map(_ => Right(book))
      .recover {
        case e: Exception => Left(APIError.BadAPIResponse(500, s"Failed to create book: ${e.getMessage}"))
      }

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  def read(id: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.find(byID(id)).headOption.map {
      case Some(data) => Right(data)
      case None => Left(APIError.BadAPIResponse(404, s"Book with ID $id not found"))
    }

  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.replaceOne(
        filter = byID(id),
        replacement = book,
        options = new ReplaceOptions().upsert(true)
      ).toFuture()
      .map(_ => Right(book))
      .recover {
        case e: Exception => Left(APIError.BadAPIResponse(500, s"Failed to update book: ${e.getMessage}"))
      }

  def delete(id: String): Future[Either[APIError.BadAPIResponse, String]] =
    collection.deleteOne(
        filter = byID(id)
      ).toFuture()
      .map(result =>
        if (result.getDeletedCount > 0) Right(id)
        else Left(APIError.BadAPIResponse(404, s"Book with ID $id not found or could not be deleted"))
      )
      .recover {
        case e: Exception => Left(APIError.BadAPIResponse(500, s"Failed to delete book: ${e.getMessage}"))
      }

  def deleteAll(): Future[Either[APIError.BadAPIResponse, Unit]] =
    collection.deleteMany(empty()).toFuture()
      .map(_ => Right(()))
      .recover {
        case e: Exception => Left(APIError.BadAPIResponse(500, s"Failed to delete all books: ${e.getMessage}"))
      }
}