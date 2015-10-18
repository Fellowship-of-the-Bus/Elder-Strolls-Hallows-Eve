package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width,Height}
import lib.ui.{Drawable}
import state.ui.GameArea

import eshe.state.ui.PlayerListener

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

abstract class Character(xc: Float, yc: Float, val base: CharacterType) extends GameObject(xc, yc) {
  def name: String

  var hp: Float = base.maxHp
  def maxHp = base.maxHp
  def attack = base.attack
  def defense = base.defense
  def speed = base.speed

  def atkHeight = base.atkHeight
  def atkWidth = base.atkWidth

  def id: Int = base.id

  def copy(imgs: Array[Drawable]) = imgs.map(_.copy)

  val imgs: Array[Drawable] = copy(base.imgs)
  var currImage: Drawable = imgs(0)
  val numSteps = 20
  var steps = numSteps
  var index = 0

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
  
  def getTargets(y:Float, w: Float, range: Float, enemy: Boolean, game: Game) = {
    val tolerance: Float = 20.0f
    var targets: List[Character] = null
    var inrange: List[Character] = List()
    if (enemy) targets = game.players.toList
    else targets = game.enemies
    for (t <- targets; if (t.active)) {
      if ((y+tolerance <= t.y+t.height) && (y-tolerance >= t.y) && (x + w + range >= t.x) && (x + w + range <= t.x + t.width)) {
        inrange = t :: inrange
      }
    }
    inrange
  }

  override def move(xamt: Float, yamt: Float): Unit = {
    if (xamt < 0) direction = GameObject.Left
    else direction = GameObject.Right
    if (x < 0)  {
      x = 0
    }
    if (x > (Width - width)) {
      x = Width - width
    }
    x = x + xamt
    if (y < 0) {
       y = 0
    }
    if (y > (GameArea.height - height)) {
       y = GameArea.height - height
    }
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

}
