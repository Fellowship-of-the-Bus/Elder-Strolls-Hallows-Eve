package com.github.fellowship_of_the_bus
package eshe
package game

import org.newdawn.slick.{Graphics}

import lib.game.GameConfig.{Width}
import lib.ui.{Drawable}

import state.ui.PlayerListener
import IDMap._

object GameObject {
  val Left = -1
  val Right = 1
}

abstract class GameObject(xc: Float, yc: Float) extends lib.game.TopLeftCoordinates {
  var x = xc
  var y = yc

  def id: Int
  def attack: Int

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

  def hit(c: Character) = {
    val damage = attack // - c.defense // ignore defense for now
    c.hp = c.hp - damage

    if (c.hp <= 0) {
      c.inactivate
      c match {
        case e: Enemy => notify(x => x.enemyDied(e))
        case p: Player => notify(x => x.playerDied(p))
      }
    }
  }

  var listeners = List[PlayerListener]()
  def addListener(l: PlayerListener) = {
    listeners = l::listeners
  }

  def notify(event: (PlayerListener) => Unit) = {
    for (l <- listeners) {
      event(l)
    }
  }


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
