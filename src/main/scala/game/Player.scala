package com.github.fellowship_of_the_bus
package eshe
package game

import IDMap._

import org.newdawn.slick.{GameContainer, Graphics}

import lib.ui.{Drawable}
import lib.game.GameConfig.{Width}
import lib.util.{TickTimer,TimerListener}

import eshe.state.ui.PlayerListener

trait PlayerType extends CharacterType {

}

abstract case class Player(xc: Float, yc: Float, override val base: PlayerType) extends game.Character(xc, yc, base) {
  def tryAttack(game: Game) = {

  }
  def tryAttack2(game: Game) = {

  }
  var score = 0

  override def hit(c: Character) = {
    val damage = (attack - c.defense)
    c match {
      case e: Enemy => e.knockback(damage * 5)
      case _ => ()
    }
    score += damage
    super.hit(c)
  }
}

object IVGuys {
  val guys = Array(IVGuy, IVGuy2, IVGuy3, IVGuy4)
}

object IVGuy extends PlayerType {
  val id = IVGuyW1ID
  val maxHp = 100
  val attack = 30
  val defense = 20
  val speed = 5

  var walk1 = images(IVGuyW1ID)
  var walk2 = images(IVGuyW2ID)
  var imgs = Array[Drawable](walk1, walk2)

  val atkHeight = 370.0f
  val atkWidth = 60.0f

  val atkHeight2 = 500.0f
}

object IVGuy2 extends PlayerType {
  val id = IVGuy.id
  val maxHp = IVGuy.maxHp
  val attack = IVGuy.attack
  val defense = IVGuy.defense
  val speed = IVGuy.speed

  var walk1 = images(IVGuy2W1ID)
  var walk2 = images(IVGuy2W2ID)
  var imgs = Array[Drawable](walk1, walk2)

  val atkHeight = IVGuy.atkHeight
  val atkWidth = IVGuy.atkWidth
}

object IVGuy3 extends PlayerType {
  val id = IVGuy.id
  val maxHp = IVGuy.maxHp
  val attack = IVGuy.attack
  val defense = IVGuy.defense
  val speed = IVGuy.speed

  var walk1 = images(IVGuy3W1ID)
  var walk2 = images(IVGuy3W2ID)
  var imgs = Array[Drawable](walk1, walk2)

  val atkHeight = IVGuy.atkHeight
  val atkWidth = IVGuy.atkWidth
}

object IVGuy4 extends PlayerType {
  val id = IVGuy.id
  val maxHp = IVGuy.maxHp
  val attack = IVGuy.attack
  val defense = IVGuy.defense
  val speed = IVGuy.speed

  var walk1 = images(IVGuy4W1ID)
  var walk2 = images(IVGuy4W2ID)
  var imgs = Array[Drawable](walk1, walk2)

  val atkHeight = IVGuy.atkHeight
  val atkWidth = IVGuy.atkWidth
}

class IVGuy(xc: Float, yc: Float, playerNum: Int) extends Player(xc, yc, IVGuys.guys(playerNum)) {
  val name = "Herbert"

  def atkHeight2 = IVGuy.atkHeight2

  val armDefault = images(IVGuyArmID).copy
  val armPunch = images(IVGuyArmPunchID).copy
  val kick = images(IVGuyKickID).copy
  val jump = images(IVGuyJumpID).copy
  var currArm = armDefault
  var time = 0

  val action = new TimerListener {}
  val movement = new TimerListener {}

  override def tryAttack(game: Game) = {
    // only one action at a time
    action.cancelAll()

    def resetArm() = {
      currArm = armDefault
      img = imgs(index)      
    }

    action.addTimer(new TickTimer(10, resetArm _))

    currArm = armPunch
    var targs = getTargets(y+(atkHeight* state.ui.GameArea.scaleFactor), width, (atkWidth * state.ui.GameArea.scaleFactor), false, game)
    for (t <- targs){
      hit(t)
    }
  }

  override def tryAttack2(game: Game) = {
    // only one action at a time
    action.cancelAll()

    action.addTimer(new TickTimer(15, doKick _))
    action.addTimer(new TickTimer(30, () => img = jump))
    action.addTimer(new TickTimer(45, resetArm _))

    def doKick() = {
      img = kick
      val targs = getTargets(y+(atkHeight2* state.ui.GameArea.scaleFactor), kick.getWidth, 0, false, game)
      for (t <- targs) {
        hit(t)
      }
    }

    def resetArm() = {
      currArm = armDefault
      img = imgs(index)  
    }

    img = jump
  }

  override def draw(g: Graphics, gc: GameContainer) = {
    drawScaledImage(img, x, y, g)
    if (currArm != null && img != jump && img != kick) {
      if (direction == GameObject.Left) {
        drawScaledImage(currArm, x + ((width - 130) * state.ui.GameArea.scaleFactor), y + (240 * state.ui.GameArea.scaleFactor), g)
      } else {
        drawScaledImage(currArm, x + (130 * state.ui.GameArea.scaleFactor), y + (240 * state.ui.GameArea.scaleFactor), g)
      }
    }
  }

  override def update(delta: Long, game: Game) = {
    super.update(delta, game)

    // dependency between these two: some actions update the 
    // movement image, so they should be updated second
    movement.update(delta)
    action.update(delta)
  }
}
