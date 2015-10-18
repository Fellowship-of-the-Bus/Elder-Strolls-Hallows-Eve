package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}
import org.newdawn.slick.{Graphics}
import lib.ui.{Drawable}

object GameObject {
  val Left = -1
  val Right = 1
}

abstract class GameObject(xc: Float, yc: Float) {
  var x = xc
  var y = yc

  def id: Int

  private var isActive = true
  def active = isActive
  def inactivate = isActive = false

  var img = images(id).copy
  val height = img.getHeight
  val width = img.getWidth
  var direction = GameObject.Left

  def move(xamt: Float, yamt: Float): Unit = {
    x = x + xamt
    y = y + yamt
  }
  
  def topLeftCoord = (x-width/2, y-height/2)

  def drawScaledImage(im: Drawable, x: Float, y: Float, g: Graphics) = {
    val scale = state.ui.GameArea.scaleFactor
    //g.scale(scale,scale)
    if (direction == GameObject.Left) {
      //im.setCenterOfRotation(0,0)
      //im.setRotation(180.0f)
//      g.rotate(x,y,180.0f)
      //im.scaleFactor = -im.scaleFactor
      im.draw(x,y, true, false)
      //im.scaleFactor = -im.scaleFactor
//      g.rotate(x,y,180.0f)
      //im.setRotation(0.0f)
    } else {
      im.draw(x,y)
    }
    //im.draw(x,y)
    //g.scale(1/scale, 1/scale)
  }

  def update(delta: Long, game: Game) = {
    img.update(delta)
  }

  def draw(g: Graphics) = {
    drawScaledImage(img, x, y, g)
  }
}
