package hr.element.geom

object Dimension extends Dimension(0, 0)

case class Dimension(w: Double, h: Double) {
  def this() = this(0, 0)
}