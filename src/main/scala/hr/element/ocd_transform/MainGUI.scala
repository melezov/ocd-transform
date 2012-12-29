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
//import scala.util.Random
//import scala.swing.event.Key
//import java.awt.geom.Point2D.{ Double => P2D }
//
//object MainGUI extends SimpleSwingApplication {
//  private def setLookAndFeel(dlf: Boolean) {
//    Toolkit.getDefaultToolkit.setDynamicLayout(dlf)
//    System.setProperty("sun.awt.noerasebackground", dlf.toString)
//
//    JFrame.setDefaultLookAndFeelDecorated(dlf)
//    JDialog.setDefaultLookAndFeelDecorated(dlf)
//  }
//
//  val TranslateStep = 0.02
//
//  var running = true
//  var init: Int = 0 & 0xffff
//  var seed: Int = 0 & 0xff
//  val dXY: P2D = new P2D
//
//  override def startup(args: Array[String]) {
//    setLookAndFeel(true)
//    top.visible = true
//
//    actor {
//      while (running) {
//        try {
////          vista.repaint()
//          Thread.sleep(30)
//        }
//        catch {
//          case e: InterruptedException =>
//        }
//      }
//    }
//  }
//
//  val vista: Component = new Component {
//    listenTo(this.keys)
//
//    reactions += {
//      case KeyPressed(source, key, modifiers, location) =>
//        key match {
//          case Key.Enter =>
//            init = System.currentTimeMillis.toInt & 0xffff
//
//          case Key.Right =>
//            dXY.x += TranslateStep
//
//          case Key.Left =>
//            dXY.x -= TranslateStep
//
//          case Key.Up =>
//            dXY.y += TranslateStep
//
//          case Key.Down =>
//            dXY.y -= TranslateStep
//
//          case _ =>
//            seed = key.hashCode & 0xff
//        }
//
//        vista.repaint()
//    }
//
//    focusable = true
//    requestFocus
//
//    override def paint(g: Graphics2D) {
//      super.paint(g)
//
//      g.setColor(Color.BLACK)
//      g.fillRect(0, 0, size.width, size.height)
//
//      val rnd = new Random(init + seed)
//      Grid.draw(rnd, g, size, dXY)
//
//      g.setColor(Color.yellow)
//      g.drawString("Random: %04X + %2X" format(init, seed), 20, 50)
//    }
//  }
//
//  def top = new MainFrame {
//    title = "OCD Transform"
//    contents = vista
//  }
//
//  override def shutdown() {
//    running = false
//  }
//}