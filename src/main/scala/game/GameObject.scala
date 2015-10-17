package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}
import org.newdawn.slick.{Graphics}
import lib.ui.{Drawable}

abstract class GameObject(xc: Float, yc: Float) {
  var x = xc
  var y = yc

  def id: Int
  def width: Float
  def height: Float

  private var isActive = true
  def active = isActive
  def inactivate = isActive = false

  def move(xamt: Float, yamt: Float): Unit = {
    x = x + xamt
    y = y + yamt
  }
  
  def topLeftCoord = (x-width/2, y-height/2)

  def drawScaledImage(im: Drawable, x: Float, y: Float, g: Graphics) = {
    val scale = state.ui.GameArea.scaleFactor
    g.scale(scale,scale)

    im.draw(x,y)
  
    g.scale(1/scale, 1/scale)
  }

  def draw(g: Graphics) = {
    drawScaledImage(images(id), x, y, g)
  }
}
