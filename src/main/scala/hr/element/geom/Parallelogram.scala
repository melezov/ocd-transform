package hr.element.geom

case class Parallelogram(p1: Point, p2: Point, p3: Point) extends Shape {
  val p4 = p3 + p1 - p2
  def points = IndexedSeq(p1, p2, p3, p4)
}
