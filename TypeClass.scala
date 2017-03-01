import shapeless.labelled.FieldType
import shapeless.{:+:, CNil, Coproduct}
import scala.concurrent.Future
import akka.http.scaladsl.marshalling.ToResponseMarshaller

trait TypeClass[In, Out] {
  def route(in: In): Unit
}

object TypeClass {
   def apply[In, Out](implicit instance: TypeClass[In, Out]) = instance

  implicit val cnil: TypeClass[CNil, CNil] = new TypeClass[CNil, CNil] {
    def route(res: CNil) = res.impossible
  }

  implicit def ccons[K, I, O, IT <: Coproduct, OT <: Coproduct]
  (implicit head: TypeClass[I, O], tail: TypeClass[IT, OT]): TypeClass[FieldType[K, I] :+: IT, FieldType[K, O] :+: OT] =
    new TypeClass[FieldType[K, I] :+: IT, FieldType[K, O] :+: OT] {
      def route(res: FieldType[K, I] :+: IT) = ()
    }

  implicit def single[A](implicit marshaller: ToResponseMarshaller[A]): TypeClass[A, A] =
    new TypeClass[A, A] {
      def route(res: A) = ()
    }

  implicit def future[A](implicit marshaller: ToResponseMarshaller[A]): TypeClass[Future[A], A] =
    new TypeClass[Future[A], A] {
      def route(res: Future[A]) = ()
    }
}
