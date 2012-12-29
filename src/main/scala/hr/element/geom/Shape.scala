package hr.element.geom

trait Shape {
  def points: IndexedSeq[Point]

  def lines =
    points sliding(2) map { g =>
      Line(g.head, g.last)
    } toIndexedSeq

  def box = {
    val p1 = points.head
    val pT = points.tail

    pT.foldLeft(Rectangle(p1))(_ grow _)
  }
}
