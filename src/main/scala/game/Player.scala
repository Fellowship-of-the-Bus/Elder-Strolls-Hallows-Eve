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

abstract case class Player(xc: Float, yc: Float, override val base: PlayerType) extends game.Character(xc, yc, base) {
  def tryAttack(game: Game) = {

  }
  var score = 0

  override def hit(c: Character) = {
    val damage = (attack - c.defense)
    c match {
      case e: Enemy => e.knockback(damage * 3)
      case _ => ()
    }
    score += damage
    super.hit(c)
  }
}

object IVGuy extends PlayerType {
  val id = IVGuyW1ID
  val maxHp = 100
  val attack = 30
  val defense = 20
  val speed = 10

  val walk1 = images(IVGuyW1ID)
  val walk2 = images(IVGuyW2ID)
  val imgs = Array[Drawable](walk1, walk2)
}

class IVGuy(xc: Float, yc: Float) extends Player(xc, yc, IVGuy) {
  val name = "Herbert"

  val armDefault = images(IVGuyArmID).copy
  val armPunch = images(IVGuyArmPunchID).copy
  var currArm = armDefault
  var time = 0
  override def tryAttack(game: Game) = {
    time = 10
    currArm = armPunch
    getTargets(y+(370* state.ui.GameArea.scaleFactor), (60 * state.ui.GameArea.scaleFactor), false,game);
  }

  override def draw(g: Graphics) = {
    time = Math.max(0, time-1)
    if ((time == 0)&&(currArm == armPunch)) currArm = armDefault
    drawScaledImage(img, x, y, g)
    drawScaledImage(currArm, x + (130 * state.ui.GameArea.scaleFactor), y + (240 * state.ui.GameArea.scaleFactor), g)
  }

}
