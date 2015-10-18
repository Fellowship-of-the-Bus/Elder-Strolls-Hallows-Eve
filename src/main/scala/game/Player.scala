package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}
import org.newdawn.slick.{Graphics}
import lib.ui.{Drawable}

import eshe.state.ui.PlayerListener

trait PlayerType extends CharacterType {

}

abstract class Player(xc: Float, yc: Float, override val base: PlayerType) extends game.Character(xc, yc, base) {
  def tryAttack() = {

  }
  var score = 0

  def hit(e: Enemy) = {
    val damage = (attack - e.defense)
    e.hp = e.hp - damage
    score += damage

    if (e.hp <= 0) {
      e.inactivate
      notify(x => x.enemyDied(e))
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
}

object IVGuy extends PlayerType {
  val id = IVGuyW1ID
  val maxHp = 100
  val attack = 30
  val defense = 20
  val speed = 10
}

class IVGuy(xc: Float, yc: Float) extends Player(xc, yc, IVGuy) {
  val name = "Herbert"
  val armDefault = images(IVGuyArmID).copy
  val armPunch = images(IVGuyArmPunchID).copy
  var currArm = armDefault
  var time = 0
  val walk1 = images(IVGuyW1ID).copy
  val walk2 = images(IVGuyW2ID).copy
  val imgs = Array[Drawable](walk1, walk2)
  var currImage = walk1
  override def tryAttack() = {
    time = 10
    currArm = armPunch

  }

  override def draw(g: Graphics) = {
    time = Math.max(0, time-1)
    if ((time == 0)&&(currArm == armPunch)) currArm = armDefault
    drawScaledImage(img, x, y, g)
    drawScaledImage(currArm, x + (130 * state.ui.GameArea.scaleFactor), y + (240 * state.ui.GameArea.scaleFactor), g)
  }

}
