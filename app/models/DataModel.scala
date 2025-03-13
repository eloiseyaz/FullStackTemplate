package models

import play.api.libs.json.{Json, OFormat}

case class DataModel(_id: String,
                     name: String,
                     description: String,
                     pageCount: Int)

object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}