package hr.element.ocd_transform

import hr.element.geom._
import scala.math._

object A extends App {
  Intrusion.calc(Parallelogram(Point(-0.05, -0.05), Point(1.05, -0.05), Point(1.05,1.05)))
}

object Intrusion {
  private object LineData {
    def order(crosses: IndexedSeq[(Int, Double)]) =
      crosses.groupBy(_._1).mapValues(
        _.map(_._2)
         .distinct
         .sortBy(identity)
      )
  }

  class LineData(line: Line) {
    val box = line.box

    val Point(x1, y1) = box.pMin
    val Point(x2, y2) = box.pMax

    val sX = ceil(x1).toInt
    val sY = ceil(y1).toInt
    val eX = floor(x2).toInt
    val eY = floor(y2).toInt

    val fi =
      if (box.dim.h == 0) {
        None
      }
      else {
        Some(box.dim.w / box.dim.h)
      }

    val cutX = LineData.order(for {
      x <- sX to eY
      nzFi <- fi
    } yield {
      x -> (y1 + nzFi * (x - x1))
    })

    val cutY = LineData.order(for {
      y <- sY to eY
    } yield {
      y -> (fi match {
        case Some(nzFi) =>
          x1 + (y - y1) / nzFi

        case None =>
          x1
      })
    })

    val middles = (
      (for {
        (x, cY) <- cutX if cY.size > 1
        sy = ceil(cY.head).toInt
        ey = floor(cY.last).toInt
        y <- sy to ey
      } yield x -> y) ++
      (for {
        (y, cX) <- cutY if cX.size > 1
        sx = ceil(cX.head).toInt
        ex = floor(cX.last).toInt
        x <- sx to ex
      } yield x -> y)
    ).toIndexedSeq[(Int, Int)].distinct.sortBy(identity)
  }

  def calc(par: Parallelogram) = {
    val lineData = par.lines map(new LineData(_))

    val dots = lineData.flatMap { lD =>
      (for ( (x, y) <- lD.middles) yield x.toDouble -> y.toDouble) ++
      (for ( (x, cY) <- lD.cutX; y <- cY ) yield x.toDouble -> y)
      (for ( (y, cX) <- lD.cutY; x <- cX ) yield x -> y.toDouble)

      //(p.line.x1 -> p.line.y1)
    }

//      (p.line.x1 -> p.line.y1)
//    }
  }
}
