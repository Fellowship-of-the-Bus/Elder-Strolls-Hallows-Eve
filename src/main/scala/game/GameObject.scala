package com.github.fellowship_of_the_bus
package eshe
package game

import org.newdawn.slick.{GameContainer, Graphics, Color}

import lib.game.GameConfig.{Width}
import lib.slick2d.ui.{Drawable, SomeColor, NoColor}
import lib.util.{TickTimer, FireN}

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
  def inactivate() = isActive = false

  var img = images(id).copy
  def height = img.getHeight
  def width = img.getWidth
  var direction = GameObject.Left
  def isHurt = false
  def isHurt_=(b: Boolean) = ()

  var alive = true

  def move(xamt: Float, yamt: Float): Unit = {
    x = x + xamt
    y = y + yamt
  }

  def hit(c: Character, strength: Int) = {
    c.hurtTimer.cancelAll()
    c.isHurt = false
    c.hurtTimer += new TickTimer(15, () => c.isHurt = ! c.isHurt, FireN(6))

    val damage = strength // - c.defense // ignore defense for now
    c.hp = c.hp - damage

    if (c.hp <= 0) {
      //c.inactivate
      c match {
        case e: Enemy => {
          notify(x => x.enemyDied(e))
          e.alive = false
        }
        case p: Player => {
          notify(x => x.playerDied(p))
          p.inactivate
        }
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
    val filter = if (isHurt) (if (alive) SomeColor(Color.red) else SomeColor(Color.transparent)) else NoColor
    im.draw(x,y, direction == GameObject.Left, false, filter)
  }

  def update(delta: Long, game: Game) = {
  }

  def draw(g: Graphics, gc: GameContainer) = {
    drawScaledImage(img, x, y, g)
  }
}
