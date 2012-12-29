package hr.element.geom

object Point extends Point(0, 0)

case class Point(x: Double, y: Double) extends Shape {
  def +(pnt: Point) = Point(x + pnt.x, y + pnt.y)
  def -(pnt: Point) = Point(x - pnt.x, y - pnt.y)

  def +(dim: Dimension) = Point(x + dim.w, y + dim.h)
  def -(dim: Dimension) = Point(x - dim.w, y - dim.h)

  def points = IndexedSeq(this)
}
