package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._

import java.util.Scanner
import java.io.File

import lib.game.GameConfig.{Width}
import lib.slick2d.ui.{Drawable}
import lib.util.{TickTimer,TimerListener,FireN,RepeatForever,ConditionalTickTimer,rand}
import lib.math.{sqrt, clamp}

import state.ui.GameArea

trait EnemyType extends CharacterType {
  def knockback: Drawable
  def attackImg: Drawable
  def walk1: Drawable
}

object HorseMaskOffset {
  lazy val offset = Map(
    images(GhostW1ID) -> ((-13-15,-4-10)),
    images(GhostW2ID) -> ((-1-15,-4-10)),
    images(GhostKnockbackID) -> ((30,-20)),
    images(ElsaID) -> ((-15,-6)),
    images(PowerRangerW1ID) -> ((5,-5)),
    images(PowerRangerW2ID) -> ((5,-4)),
    images(PowerRangerKnockbackID) -> ((40,-10)),
    images(HotdogW1ID) -> ((0,0)),
    images(HotdogW2ID) -> ((0,0))
  )
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
  var attacking = false

  this += new ConditionalTickTimer(60, () => hit(target, attack), () => ! flying && targetInRange, RepeatForever)
  this += new ConditionalTickTimer(1, move _, () => ! flying && ! targetInRange && ! attacking, RepeatForever)

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

      val lower = GameArea.fenceHeight-height
      val upper = GameArea.height
      y = clamp(y, lower, upper)
    }
  }

  var target: Player = null
  override def update(delta: Long, game: Game) = {
    super.update(delta, game)
    super.tick(delta)  // for timers

    if (target == null || ! target.active) {
      target = Enemy.randInSeq(game.players)
    }
  }

  def knockback(distance: Float) {
    flying = true
    val kbTicks = 15
    val knockVelocity = distance / kbTicks
    img = knockback

    this += new TickTimer(1, doKnockback _, FireN(kbTicks))
    this += new TickTimer(kbTicks, endKnockback _)

    def doKnockback() = {
      x = x + knockVelocity
    }

    def endKnockback() = {
      flying = false
      img = imgs(0)
    }
  }
  override def hit(c: Character, strength: Int) {
    attacking = true
    super.hit(c, strength)
    img = base.attackImg
    this += new TickTimer(20, () => {
      img = base.walk1
      attacking = false
      })
  }
}

abstract class RangedEnemy(xc: Float, yc: Float, b: EnemyType) extends Enemy(xc, yc, b) with TimerListener {
  def range: Int
  def projType: ProjectileID
  def shootImg = base.attackImg
  def defaultImg = base.walk1

  cancelAll()
  this += new ConditionalTickTimer(120, () => hit(target, attack), () => ! flying && targetInRange, RepeatForever)
  this += new ConditionalTickTimer(1, move _, () => ! flying, RepeatForever)
  override def hit(c: Character, strength: Int) = {
    // determine which direction to fire
    var dir = 0
    if (target.x < x) dir = GameObject.Left
    else dir = GameObject.Right
    val (xc, yc) = centerCoord
    val proj = Projectile(projType, xc, yc, base.attack, dir, (state.ui.GameArea.width/2).toInt)
    state.Battle.game.projectiles = proj :: state.Battle.game.projectiles
    img = shootImg
    this += new TickTimer(20, () => img = defaultImg)
  }
  override def targetInRange(): Boolean = {
    if (target == null || ! target.active) false
    else {
      val (xVec, yVec) = distanceToTarget
      xVec > -range && xVec < range && yVec > -range && yVec < range
    }
  }
  override def move() = {
    if (target != null && target.active) {
      val (xVec, yVec) = distanceToTarget
      val norm = ((1 / sqrt((xVec * xVec) + (yVec * yVec))) * speed)
      if (targetInRange)
        super.move(0, yVec * norm)
      else
        super.move(xVec * norm, yVec * norm)
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
  val attackImg2 = images(GhostWindupID)
  val attackImg = images(GhostKickID)
  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class Ghost(xc: Float, yc: Float) extends Enemy(xc, yc, Ghost) {
  override def hit(c: Character, strength: Int) {
    img = Ghost.attackImg2
    attacking = true
    this += new TickTimer(30, () => {
      if (targetInRange()) {
        super.hit(c, strength)
      } else {
        img = base.attackImg
        Ghost.this += new TickTimer(20, () => {
          img = base.walk1
          attacking = false
          })
      }})
  }
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
  val attackImg = images(ElsaShootID)
  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class Elsa(xc: Float, yc: Float) extends RangedEnemy(xc, yc, Elsa) {
  def range = 400
  def projType = ElsaProj
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
  val attackImg = images(PowerRangerPunchID)
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
  cancelAll()
  val mask = HorseMask.mask.copy()
  if (base == Ghost) {
    mask.scaleFactor *= 1.5f
  }
  override def draw(g: org.newdawn.slick.Graphics, gc: org.newdawn.slick.GameContainer) = {
    super.draw(g, gc)
    val (offsetx, offsety): (Int, Int) = HorseMaskOffset.offset.get(img) getOrElse ((0,0))
    drawScaledImage(mask, x + offsetx, y + offsety, g)
  }

  override def update(delta: Long, game: Game) = {
    super.update(delta, game)
    move(-speed, 0)
    if (x < -width) {
      inactivate
    }
  }

}
