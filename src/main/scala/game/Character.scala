package com.github.fellowship_of_the_bus
package eshe
package game

import IDMap._
import eshe.state.ui.{GameArea}

import org.newdawn.slick.{GameContainer, Graphics}

import lib.game.GameConfig.{Width,Height}
import lib.slick2d.game.Lifebar
import lib.slick2d.ui.{Drawable}
import lib.math.{Rect, clamp}
import lib.util.{TimerListener}

trait CharacterType {
  def id: Int
  def maxHp: Int
  def attack: Int
  def defense: Int
  def speed: Int

  def imgs: Array[Drawable]

  def atkHeight: Float
  def atkWidth: Float
}

abstract class Character(xc: Float, yc: Float, val base: CharacterType) extends GameObject(xc, yc) with Lifebar {
  def name: String

  var hp: Float = base.maxHp
  def maxHp = base.maxHp
  def attack = base.attack
  def defense = base.defense
  def speed = base.speed

  def atkHeight = base.atkHeight
  def atkWidth = base.atkWidth

  def hitbox = Rect(x, y, x + width, y + height)

  override var isHurt = false
  val hurtTimer = new TimerListener {}

  def id: Int = base.id

  def copy(imgs: Array[Drawable]) = imgs.map(_.copy)

  val imgs: Array[Drawable] = copy(base.imgs)
  img = imgs(0)
  val numSteps = 20
  var steps = numSteps
  var index = 0

  def getTargets(x1: Float, y1: Float, x2: Float, y2: Float, enemy: Boolean, game: Game) = {
    var inrange: List[Character] = List()
    val targets =
      if (enemy) game.players.toList
      else game.enemies
    val hitbox = Rect(x1,y1,x2,y2)
    for (t <- targets; if (t.active && t.alive)) {
      if (hitbox.intersect(t.hitbox)) {
        inrange = t :: inrange
      }
    }
    inrange
  }

  override def move(xamt: Float, yamt: Float): Unit = {
    if (xamt < 0) direction = GameObject.Left
    else if (xamt > 0) direction = GameObject.Right

    x = x + xamt
    y = y + yamt
    if ((xamt != 0) || (yamt != 0)) {
      steps = Math.max(0, steps-1)
      if ((steps == 0) && (imgs.indexOf(img) != -1)) {
        steps = numSteps
        index = (index + 1) % imgs.length
        img = imgs(index)
      }
    }
  }

  override def update(delta: Long, game: Game) = {
    super.update(delta, game)
    hurtTimer.tick(delta)
  }

  def heal(amt: Float) = {
    hp = clamp( (hp + amt*base.maxHp).toInt, 0, base.maxHp)
  }

  override def draw(g: Graphics, gc: GameContainer): Unit = {
    super.draw(g) // Lifebars
    super.draw(g, gc)
  }
}
