package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._

import java.util.Scanner
import java.io.File

import lib.game.GameConfig.{Width}
import lib.ui.{Drawable}
import lib.util.rand
import lib.util.{TickTimer,TimerListener,FireN,RepeatForever,ConditionalTickTimer}
import lib.math.sqrt

trait EnemyType extends CharacterType {
  def knockback: Drawable
}

object Enemy {
  private lazy val names = read("data/names.txt")
  private lazy val facts = wordwrap(read("data/true-facts.txt"), 35)

  /** given a sequence of strings, bound the length of each line by maxlen;
    * currently only works for two line strings */
  def wordwrap(words: Seq[String],maxlen: Int): Seq[String] = {
    def wrap(word: String): String = {
      if (word.length <= maxlen) word
      else {
        var i = maxlen
        while (word(i) != ' ') i = i-1
        val chars = word.toCharArray
        chars(i) = '\n'
        new String(chars)
      }
    }
    for (w <- words) yield wrap(w) 
  }

  def read(filename: String): List[String] = {
    var lst = List[String]()
    val sc = lib.util.scanFile(filename)
    while (sc.hasNextLine) {
      lst = sc.nextLine::lst 
    }
    lst
  }

  def randInSeq[T](s: Seq[T]): T = s(rand(s.length))
  def name() = randInSeq(names)
  def fact() = randInSeq(facts)

  private val enemyKinds = Vector(Ghost, Elsa, PowerRanger)
  def random() = randInSeq(enemyKinds)
}

abstract case class Enemy(xc: Float, yc: Float, override val base: EnemyType) extends game.Character(xc, yc, base) with TimerListener {
  val age: Int = rand(6, 12)

  val knockback = base.knockback.copy

  val name = Enemy.name
  val fact = Enemy.fact
  var flying = false

  addTimer(new ConditionalTickTimer(60, () => hit(target, attack), () => ! flying && targetInRange, RepeatForever))
  addTimer(new ConditionalTickTimer(1, move _, () => ! flying && ! targetInRange, RepeatForever))

  def distanceToTarget(): (Float, Float) = {
    val xVec = (target.x + target.width / 2) - (x + width / 2)
    val yVec = (target.y + target.height / 2) - (y + height / 2)
    (xVec, yVec)
  }

  def targetInRange(): Boolean = {
    if (target == null || ! target.active) false
    else {
      val (xVec, yVec) = distanceToTarget
      xVec > -100 && xVec < 100 && yVec > -100 && yVec < 100
    }
  }

  def move() = {
    if (target != null && target.active) {
      val (xVec, yVec) = distanceToTarget    
      val norm = ((1 / sqrt((xVec * xVec) + (yVec * yVec))) * speed)
      super.move(xVec * norm, yVec * norm)      
    }
  }

  var target: Player = null
  override def update(delta: Long, game: Game) = {
    super.update(delta, game)
    super.update(delta)  // for timers
    
    if (target == null || ! target.active) {
      target = Enemy.randInSeq(game.players)
    }
  }

  def knockback(distance: Float) {
    flying = true
    val kbTicks = 15
    val knockVelocity = distance / kbTicks
    img = knockback

    addTimer(new TickTimer(1, doKnockback _, FireN(kbTicks)))
    addTimer(new TickTimer(kbTicks, endKnockback _))

    def doKnockback() = {
      x = x + knockVelocity
    }

    def endKnockback() = {
      flying = false
      img = imgs(0)
    }
  }
}

object Ghost extends EnemyType {
  val id = GhostW1ID
  val maxHp = 100
  val attack = 2
  val defense = 1 
  val speed = 4
  val walk1 = images(GhostW1ID)
  val walk2 = images(GhostW2ID)
  val knockback = images(GhostKnockbackID)
  val imgs = Array[Drawable](walk1, walk2)

  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class Ghost(xc: Float, yc: Float) extends Enemy(xc, yc, Ghost) {
}

object Elsa extends EnemyType {
  val id = ElsaID
  val maxHp = 15
  val attack = 4
  val defense = 3 
  val speed = 3
  val walk1 = images(ElsaID)
  val knockback = images(GhostKnockbackID)  // TODO: fix this...
  val imgs = Array[Drawable](walk1)

  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class Elsa(xc: Float, yc: Float) extends Enemy(xc, yc, Elsa) {

}

object PowerRanger extends EnemyType {
  val id = PowerRangerW1ID
  val maxHp = 150
  val attack = 10
  val defense = 6 
  val speed = 3
  val walk1 = images(PowerRangerW1ID)
  val walk2 = images(PowerRangerW2ID)
  val knockback = images(PowerRangerKnockbackID).copy
  val imgs = Array[Drawable](walk1, walk2)

  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class PowerRanger(xc: Float, yc: Float) extends Enemy(xc, yc, PowerRanger) {

}

// this doesn't need to be an enemy type
object HorseMask {
  val id = HorseMaskID

  val mask = images(HorseMaskID)
  val imgs = Array[Drawable](mask)
}

class HorseMask(xc: Float, yc: Float) extends Enemy(xc, yc, Enemy.random) {
  val kind = Enemy.random
  cancelAll()
  override def draw(g: org.newdawn.slick.Graphics, gc: org.newdawn.slick.GameContainer) = {
    super.draw(g, gc)
    drawScaledImage(images(HorseMaskID), x, y, g)
  }

  override def update(delta: Long, game: Game) = {
    super.update(delta, game)
    move(-speed, 0)
    if (x < -width) {
      inactivate
    }
  }

}
