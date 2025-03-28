package repositories

import com.google.inject.ImplementedBy
import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@ImplementedBy(classOf[DataRepository])
trait DataRepositoryTrait {
  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]]

  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]]

  def read(id: String): Future[Either[APIError.BadAPIResponse, DataModel]]

  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]]

  def delete(id: String): Future[Either[APIError.BadAPIResponse, String]]

  def deleteAll(): Future[Either[APIError.BadAPIResponse, Unit]]

  def byName(name: String): Bson

  def readName(name: String): Future[Either[APIError.BadAPIResponse, DataModel]]

  def byDescription(description: String): Bson

  def readDescription(description: String): Future[Either[APIError.BadAPIResponse, DataModel]]

  def byAuthor(author: String): Bson

  def readAuthor(author: String): Future[Either[APIError.BadAPIResponse, DataModel]]

  def byPageCount(pageCount: Int): Bson

  def readPageCount(pageCount: String): Future[Either[APIError.BadAPIResponse, DataModel]]

  def getDatabaseBook(search: String, field: String): Future[Either[APIError.BadAPIResponse, DataModel]]

  def edit(id: String, field: String, replacement: String): Future[Either[APIError.BadAPIResponse, DataModel]]

}


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
) with DataRepositoryTrait {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]] =
    collection.find().toFuture().map {
      case books: Seq[DataModel] => Right(books)
      case _ => Left(APIError.BadAPIResponse(404, "Books cannot be found"))
    }

  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.insertOne(book).toFuture().map(_ => Right(book)).recover {
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

  def byName(name: String): Bson =
    Filters.and(
      Filters.regex("name", name, "i")
    )

  def readName(name: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.find(byName(name)).headOption.map {
      case Some(data) => Right(data)
      case None => Left(APIError.BadAPIResponse(404, s"Book with name $name not found"))
    }

  def byDescription(description: String): Bson =
    Filters.and(
      Filters.regex("description", description, "i")
    )

  def readDescription(description: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.find(byDescription(description)).headOption.map {
      case Some(data) => Right(data)
      case None => Left(APIError.BadAPIResponse(404, s"Book with description $description not found"))
    }

  def byAuthor(author: String): Bson =
    Filters.and(
      Filters.regex("author", author, "i")
    )

  def readAuthor(author: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.find(byAuthor(author)).headOption.map {
      case Some(data) => Right(data)
      case None => Left(APIError.BadAPIResponse(404, s"Book with author $author not found"))
    }

  def byPageCount(pageCount: Int): Bson =
    Filters.and(
      Filters.equal("pageCount", pageCount)
    )

  def readPageCount(pageCount: String): Future[Either[APIError.BadAPIResponse, DataModel]] = {
    try {
      collection.find(byPageCount(pageCount.toInt)).headOption.map {
        case Some(data) => Right(data)
        case None => Left(APIError.BadAPIResponse(404, s"Book with page count $pageCount not found"))
      }
    }
    catch {
      case _: NumberFormatException => Future(Left(APIError.BadAPIResponse(400, s"Invalid page count")))
    }
  }

  def getDatabaseBook(search: String, field: String): Future[Either[APIError.BadAPIResponse, DataModel]] = {
    field.toLowerCase match {
      case "id" => read(search)
      case "name" => readName(search)
      case "author" => readAuthor(search)
      case "description" => readDescription(search)
      case "pagecount" => readPageCount(search)
      case x => Future(Left(APIError.BadAPIResponse(400, s"Field $x not in data model")))
    }
  }


  def edit(id: String, field: String, replacement: String): Future[Either[APIError.BadAPIResponse, DataModel]] = read(id).flatMap {
    case Right(book) =>
      val editedBook: Either[APIError.BadAPIResponse, DataModel] = field.toLowerCase match {
        case "id" =>
          Right(book.copy(_id = replacement))
        case "name" =>
          Right(book.copy(name = replacement))
        case "author" =>
          Right(book.copy(author = replacement))
        case "description" =>
          Right(book.copy(description = replacement))
        case "pagecount" =>
          try {
            Right(book.copy(pageCount = replacement.toInt))
          }
          catch {
            case _: NumberFormatException => Left(APIError.BadAPIResponse(400, s"Invalid page count"))
          }
        case x => Left(APIError.BadAPIResponse(400, s"Field '$x' not in data model"))
      }

      editedBook match {
        case Right(book) => update(id, book)
        case Left(error) => Future(Left(error))
      }
    case Left(error) => Future(Left(error))
  }

}