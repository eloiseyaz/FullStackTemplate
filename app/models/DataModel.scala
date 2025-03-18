package models

import play.api.libs.json.{Json, OFormat}

case class DataModel(_id: String,
                     name: String,
                     description: String,
                     pageCount: Int)

object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}

// New Google Books API models
case class VolumeInfo(
                       title: String,
                       authors: Option[List[String]],
                       description: Option[String],
                       pageCount: Option[Int],
                       publisher: Option[String],
                       publishedDate: Option[String]
                     )

object VolumeInfo {
  implicit val format: OFormat[VolumeInfo] = Json.format[VolumeInfo]
}

case class Volume(
                   id: String,
                   volumeInfo: VolumeInfo
                 )

object Volume {
  implicit val format: OFormat[Volume] = Json.format[Volume]
}

case class GoogleBooksResponse(
                                items: List[Volume]
                              )

object GoogleBooksResponse {
  implicit val format: OFormat[GoogleBooksResponse] = Json.format[GoogleBooksResponse]
}

// Our simplified Book model that we'll convert to
case class Book(
                 id: String,
                 title: String,
                 authors: List[String],
                 description: String,
                 pageCount: Int
               )

object Book {
  implicit val format: OFormat[Book] = Json.format[Book]

  def fromVolume(volume: Volume): Book = {
    Book(
      id = volume.id,
      title = volume.volumeInfo.title,
      authors = volume.volumeInfo.authors.getOrElse(List("Unknown")),
      description = volume.volumeInfo.description.getOrElse("No description available"),
      pageCount = volume.volumeInfo.pageCount.getOrElse(0)
    )
  }
}
