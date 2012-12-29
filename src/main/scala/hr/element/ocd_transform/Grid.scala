//package hr.element.ocd_transform
//
//import java.awt.Toolkit
//import scala.actors.Actor.actor
//import scala.swing.Component
//import scala.swing.Graphics2D
//import scala.swing.MainFrame
//import scala.swing.SimpleSwingApplication
//import javax.swing.JDialog
//import javax.swing.JFrame
//import java.awt.Color
//import scala.swing.event.KeyPressed
//import scala.math._
//import scala.util.Random
//import java.awt.Dimension
//import java.awt.RenderingHints
//import java.awt.geom.Line2D.{ Double => L2D }
//import java.awt.geom.Point2D.{ Double => P2D }
//import java.awt.geom.Ellipse2D.{ Double => E2D }
//import java.awt.geom.Rectangle2D.{ Double => R2D }
//import java.awt.Shape
//import java.awt.BasicStroke
//import java.awt.Font
//import scala.math._
//import scala.collection.mutable.{ LinkedHashSet => MSet }
//import scala.collection.mutable.{ LinkedHashMap => MMap }
//
//case class Vec(p1: P2D, p2: P2D) {
//  lazy val dX = p2.x - p1.x
//  lazy val dY = p2.y - p1.y
//
//  lazy val dist = pow(pow(dX, 2) + pow(dY, 2), 0.5)
//  lazy val fi = atan2(dX, dY)
//
//  override lazy val toString = "%.2f@%.1f" format(dist, fi)
//}
//
//object Grid {
//  val ScaleX = -2 to 2
//  val ScaleY = -2 to 2
//
//  val SX = ScaleX.head
//  val EX = ScaleX.last
//  val SY = ScaleY.head
//  val EY = ScaleY.last
//  val LX = EX - SX
//  val LY = EY - SY
//
//  import RenderingHints._
//  import scala.collection.JavaConverters._
//
//  val hints = Map(
//    KEY_ANTIALIASING        -> VALUE_ANTIALIAS_ON
//  , KEY_ALPHA_INTERPOLATION -> VALUE_ALPHA_INTERPOLATION_SPEED
//  , KEY_INTERPOLATION       -> VALUE_INTERPOLATION_BICUBIC
//  , KEY_RENDERING           -> VALUE_RENDER_QUALITY
//  , KEY_TEXT_ANTIALIASING   -> VALUE_TEXT_ANTIALIAS_ON
//  ).asJava
//
//  def draw(rnd: Random, g: Graphics2D, dim: Dimension, delta: P2D) {
//    g.setRenderingHints(hints);
//    new Grid(rnd, g, dim, delta).draw()
//  }
//}
//
//class Grid(rnd: Random, g: Graphics2D, dim: Dimension, delta: P2D) {
//  import Grid._
//  private def zX(v: Double) = v * dim.width / LX
//  private def zY(v: Double) = v * dim.height / LY
//  private def zZ(v: Double) = (zX(v) + zY(v)) / 2
//
//  private def tX(v: Double) = zX(v - SX)
//  private def tY(v: Double) = zY(EY - v)
//
//  private def d(width: Double, color: Color, shape: Shape) {
//    val oldStroke = g.getStroke
//    g.setStroke(new BasicStroke(zZ(width / 100).toFloat))
//    val oldColor = g.getColor
//    g.setColor(color)
//    g.draw(shape)
//    g.setColor(oldColor)
//    g.setStroke(oldStroke)
//  }
//
//  private def f(width: Double, color: Color, shape: Shape) {
//    val oldStroke = g.getStroke
//    g.setStroke(new BasicStroke(zZ(width / 100).toFloat))
//    val oldColor = g.getColor
//    g.setColor(color)
//    g.fill(shape)
//    g.setColor(oldColor)
//    g.setStroke(oldStroke)
//  }
//
//  private def d(width: Double, color: Color, pnt: P2D, text: String) {
//    val oldFont = g.getFont
//    g.setFont(oldFont.deriveFont(zZ(width / 40).toFloat))
//    val oldColor = g.getColor
//    g.setColor(color)
//    g.drawString(text, tX(pnt.x).toFloat, tY(pnt.y).toFloat)
//    g.setColor(oldColor)
//    g.setFont(oldFont)
//  }
//
//  private def l2d(width: Double, color: Color, x1: Double, y1: Double, x2: Double, y2: Double): Unit =
//    d(width, color, new L2D(tX(x1), tY(y1), tX(x2), tY(y2)))
//
//  private def l2d(width: Double, color: Color, p1: P2D, p2: P2D): Unit =
//    l2d(width, color, p1.x, p1.y, p2.x, p2.y)
//
//  private def c2d(width: Double, color: Color, pnt: P2D, diam: Double, name: String) {
//    d(width, color, new E2D(tX(pnt.x - diam / 2), tY(pnt.y + diam / 2), zX(diam), zY(diam)))
//
//    val textPos = new P2D(pnt.x + diam, pnt.y - diam)
//    d(width, color, textPos, name)
//  }
//
//  def draw() {
//    for (x <- ScaleX) {
//      l2d(if (x == 0) 1 else .25, Color.cyan, x, SY, x, EY)
//    }
//
//    for (y <- ScaleY) {
//      l2d(if (y == 0) 1 else .25, Color.cyan, SX, y, EX, y)
//    }
//
//    val p1 = new P2D(delta.x + rnd.nextDouble * 2 - 1, delta.y + rnd.nextDouble * 2 - 1)
//    val p2 = new P2D(delta.x + rnd.nextDouble * 2 - 1, delta.y + rnd.nextDouble * 2 - 1)
//    val p3 = new P2D(delta.x + rnd.nextDouble * 2 - 1, delta.y + rnd.nextDouble * 2 - 1)
//
//    val v23 = Vec.apply(p2, p3)
//    val p4 = new P2D(p1.x + v23.dX, p1.y + v23.dY)
//
//    val l12 = new L2D(p1, p2)
//    val l23 = new L2D(p2, p3)
//    val l34 = new L2D(p3, p4)
//    val l41 = new L2D(p4, p1)
//
//    val secX = new MSet[(Int, Double)]
//    val secY = new MSet[(Int, Double)]
//
//    case class Poly(
//      line: L2D
//    , cutX: Map[Int, Set[Double]]
//    , cutY: Map[Int, Set[Double]]
//    , middles: Set[(Int, Int)]
//    )
//
//    val lines = Seq(l12, l23, l34, l41) map { l =>
//      val (x1, y1) = l.x1 -> l.y1
//      val (x2, y2) = l.x2 -> l.y2
//
//      val ix1 = floor(x1).toInt
//      val iy1 = floor(y1).toInt
//      val ix2 = floor(x2).toInt
//      val iy2 = floor(y2).toInt
//
//      val fi = (y2 - y1) / (x2 - x1)
//
//      if (ix1 == ix2 && iy1 == iy2) {
//        val cnt = new P2D(l.getBounds2D.getCenterX, l.getBounds2D.getCenterY)
//        d(2, Color.white, cnt, "Straight")
//      }
//      else {
//        if (ix1 != ix2) {
//          val sx = ceil(min(x1, x2)).toInt
//          val ex = floor(max(x1, x2)).toInt
//
//          for (x <- sx to ex) {
//            val y = y1 + fi * (x - x1)
//            secX += x -> y
//          }
//        }
//
//        if (iy1 != iy2) {
//          val sy = ceil(min(y1, y2)).toInt
//          val ey = floor(max(y1, y2)).toInt
//
//          for (y <- sy to ey) {
//            val x = x1 + (y - y1) / fi
//            secY += y -> x
//          }
//        }
//      }
//
//      val cutX = secX.groupBy(_._1).mapValues(_.map(_._2).toSet)
//      val cutY = secY.groupBy(_._1).mapValues(_.map(_._2).toSet)
//
//      val middles = Set.empty[(Int, Int)] ++ (for {
//          (x, cY) <- cutX if cY.size > 1
//          sy = ceil(cY.min).toInt
//          ey = floor(cY.max).toInt
//          y <- sy to ey
//        } yield x -> y) ++ (for {
//          (y, cX) <- cutY if cX.size > 1
//          sx = ceil(cX.min).toInt
//          ex = floor(cX.max).toInt
//          x <- sx to ex
//        } yield x -> y)
//
//      Poly(l, cutX, cutY, middles)
//    }
//
//    val dots = lines.flatMap { p =>
//      (p.middles.map{ case (x, y) => x.toDouble -> y.toDouble }) ++
//      (for { (x, cY) <- p.cutX; y <- cY } yield x.toDouble -> y) ++
//      (for { (y, cX) <- p.cutY; x <- cX } yield x -> y.toDouble) +
//      (p.line.x1 -> p.line.y1)
//    }
//
//    val sx = dots.map(d => floor(d._1).toInt).min
//    val ex = dots.map(d => ceil(d._1).toInt).max
//    val sy = dots.map(d => floor(d._2).toInt).min
//    val ey = dots.map(d => ceil(d._2).toInt).max
//
//    val used =
//      (for {
//        y <- sy until ey
//        x <- sx until ex
//      } yield (x, y) -> (dots.filter{ case (dX, dY) =>
//          (dX >= x && dX <= x + 1) &&
//          (dY >= y && dY <= y + 1)
//        } toSet)
//      ) filter { case (_, ds) => ds.nonEmpty }
//
//    used foreach { case((x, y), _) =>
//      f(0.5, Color.blue.darker.darker, new R2D(tX(x + 0.01), tY(y + 0.99), zX(0.98), zY(0.98)))
//    }
//
//    l2d(2, Color.red, p1, p2)
//    l2d(2, Color.red, p2, p3)
//    l2d(2, Color.red, p3, p4)
//    l2d(2, Color.red, p4, p1)
//
//    c2d(2, Color.yellow, p1, 0.02, "P1(%.2f:%.2f)" format(p1.x, p1.y))
//    c2d(2, Color.yellow, p2, 0.02, "P2(%.2f:%.2f)" format(p2.x, p2.y))
//    c2d(2, Color.yellow, p3, 0.02, "P3(%.2f:%.2f)" format(p3.x, p3.y))
//    c2d(2, Color.yellow, p4, 0.02, "P4(%.2f:%.2f)" format(p4.x, p4.y))
//
//    lines foreach { p =>
//      for { (x, cY) <- p.cutX; y <- cY } {
//        c2d(2, Color.white, new P2D(x, y), 0.02, "X(%.2f)" format y)
//      }
//
//      for { (y, cX) <- p.cutY; x <- cX } {
//        c2d(2, Color.white, new P2D(x, y), 0.02, "Y(%.2f)" format x)
//      }
//
//      for { (x, y) <- p.middles } {
//        c2d(2, Color.green, new P2D(x, y), 0.03, "o")
//      }
//    }
//
//    used foreach { case ((x, y), poly) =>
//      val col = new Color(0xff000000 | rnd.nextInt)
//
//      val cXY = new P2D(
//        poly.toList.map(_._1).sum / poly.size
//      , poly.toList.map(_._2).sum / poly.size
//      )
//
//      val centerLines = (poly map { case (pX, pY) =>
//        Vec(cXY, new P2D(pX, pY))
//      }).toList
//
//      val centerPoints = centerLines.sortBy(_.fi).map(_.p2);
//      val hXY :: vectors = centerPoints
//
//      val dX = rnd.nextDouble * 2 - 1
//      val dY = rnd.nextDouble * 2 - 1
//
//      val areas = vectors sliding(2) map{ p23 =>
//        val p1 = hXY
//        val p2 = p23.head
//        val p3 = p23.last
//
//        val area = - (
//          p1.x * (p2.y - p3.y)
//        + p2.x * (p3.y - p1.y)
//        + p3.x * (p1.y - p2.y)
//        ) / 2;
//
//        val cT = new P2D(
//          (p1.x + p2.x + p3.x) / 3
//        , (p1.y + p2.y + p3.y) / 3
//        );
//
//        c2d(2, Color.orange, cT, 0.05, "%.2f" format area)
//
//        val r = new Random
//
//        val ap1 = new P2D(p1.x + dX + r.nextDouble / 20, p1.y + dY + r.nextDouble / 20)
//        val ap2 = new P2D(p2.x + dX + r.nextDouble / 20, p2.y + dY + r.nextDouble / 20)
//        val ap3 = new P2D(p3.x + dX + r.nextDouble / 20, p3.y + dY + r.nextDouble / 20)
//
//        l2d(5, col.brighter, ap1, ap2)
//        l2d(3, col, ap2, ap3)
//        l2d(1, col.darker, ap3, ap1)
//
////        c2d(15, col, ap1, 0.02, ap2)
//
//        area
//      }
//
//      val area = areas.sum
//      c2d(2, Color.magenta, cXY, 0.05, "%.5f" format area)
//    }
//  }
//}
