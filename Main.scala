import shapeless.labelled.FieldType
import shapeless.{:+:, CNil, Coproduct, the, Witness => W}
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.MediaTypes
import scala.concurrent.Future
import de.heikoseeberger.akkahttpcirce.CirceSupport._

object Main {
  type A = FieldType[W.`'int`.T, Future[Int]] :+: CNil
  type B = FieldType[W.`'int`.T, Int] :+: CNil

  val instance = TypeClass[A, B]

  def main(args: Array[String]){
      println(instance.route(null))
      }
}
