
import scala.quoted._

object Test {
  def loop[T](x: Expr[T])(implicit t: Type[T]): Expr[T] = '{ val y = $x; ${loop('y)} }
}
