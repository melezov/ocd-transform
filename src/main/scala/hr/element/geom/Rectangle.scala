package hr.element.geom

import scala.math._

object Rectangle extends Rectangle(Point, Dimension){
  def apply(pMin: Point): Rectangle =
    Rectangle(pMin, Dimension)

  def apply(p1: Point, p2: Point): Rectangle =
    Rectangle(
      Point(min(p1.x, p2.x), min(p1.y, p2.y))
    , Dimension(abs(p1.x - p2.x), abs(p1.y - p2.y))
    )
}

case class Rectangle private(pMin: Point, dim: Dimension) extends Shape {
  val pMax =
    pMin + dim

  val center = Point(
    (pMin.x + pMax.x) / 2
  , (pMin.y + pMax.y) / 2
  )

  def points =
    IndexedSeq(
      pMin
    , pMin.copy(x = pMax.x)
    , pMax
    , pMax.copy(y = pMin.y)
    )

  def isInside(pnt: Point) =
    (pMin.x <= pnt.x) && (pnt.x <= pMax.x) &&
    (pMin.y <= pnt.y) && (pnt.y <= pMax.y)

  def grow(pnt: Point) =
    if (isInside(pnt)) {
      this
    }
    else {
      Rectangle(
        Point(
          min(pMin.x, pnt.x)
        , min(pMin.y, pnt.y)
        )
      , Point(
          max(pMax.x, pnt.x)
        , max(pMax.y, pnt.y)
        )
      )
    }

  override def toString = "Rectangle(%s,%s)" format(pMin, pMax)
}