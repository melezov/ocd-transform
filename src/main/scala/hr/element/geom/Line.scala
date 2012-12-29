package hr.element.geom

case class Line(p1: Point, p2: Point) extends Shape {
  def points = IndexedSeq(p1, p2)
}
