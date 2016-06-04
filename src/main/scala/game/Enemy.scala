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
    images(HotdogW1ID) -> ((4,-18)),
    images(HotdogW2ID) -> ((-4,-18)),
    images(HotdogKnockbackID) -> ((3,-18))
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

  private val enemyKinds = Vector(Ghost, Elsa, PowerRanger, Hotdog)
  def random() = randInSeq(enemyKinds)
}

abstract case class Enemy(xc: Float, yc: Float, override val base: EnemyType) extends game.Character(xc, yc, base) with TimerListener {
  val age: Int = rand(6, 12)

  val knockback = base.knockback.copy

  val name = Enemy.name
  val fact = Enemy.fact
  var flying = false
  var attacking = false

  def attackPoint: Int = 0
  def backswing: Int = 20

  this += new ConditionalTickTimer(60, () => hit(target, attack), () => ! flying && targetInRange && ! attacking, RepeatForever)
  this += new ConditionalTickTimer(1, move _, () => ! flying && ! targetInRange && ! attacking && alive, RepeatForever)

  def distanceToTarget(): (Float, Float) = {
    val box = target.hitbox
    val xVec = (box.x1 + (box.x2 - box.x1) / 2) - (x + width / 2)
    val yVec = (box.y1 + (box.y2 - box.y1) / 2) - (y + height / 2)
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
      clampY()
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
      if (alive) img = imgs(0)
      else {
        hurtTimer.cancelAll()
        isHurt = false
        hurtTimer += new TickTimer(15, () => isHurt = ! isHurt, FireN(6))
        cancelAll
        //e.img.setCenterOfRotation(e.x + e.width/2, e.y + e.height/2)
        if (knockVelocity < 0) {
          img.setCenterOfRotation(0, height)
          img.setRotation(270)
        } else {
          img.setCenterOfRotation(width, height)
          img.setRotation(90)
        }

        this += new TickTimer(90, () => {inactivate})
      }
    }
  }
  override def hit(c: Character, strength: Int) {
    attacking = true
    img = base.attackImg
    this += new TickTimer(attackPoint, () => {
      if (targetInRange) {
        super.hit(c, strength)
      }
      Enemy.this += new TickTimer(backswing, () => {
        img = base.walk1
        attacking = false
      })
    })
  }
}

abstract class RangedEnemy(xc: Float, yc: Float, b: EnemyType) extends Enemy(xc, yc, b) with TimerListener {
  def range: Int
  def projType: ProjectileID
  def shootImg = base.attackImg
  def defaultImg = base.walk1
  def shootSpeed: Int

  def offsetx: Float
  def offsety: Float

  cancelAll()
  this += new ConditionalTickTimer(shootSpeed, () => hit(target, attack), () => ! flying && targetInRange, RepeatForever)
  this += new ConditionalTickTimer(1, move _, () => ! flying, RepeatForever)
  override def hit(c: Character, strength: Int) = {
    // determine which direction to fire
    var dir = 0
    if (target.x < x) dir = GameObject.Left
    else dir = GameObject.Right
    direction = dir
    val (xc, yc) = centerCoord
    val proj = Projectile(projType, xc+offsetx, yc+offsety, base.attack, dir, (state.ui.GameArea.width/2).toInt)
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
      clampY()
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
  val knockback = images(ElsaKnockbackID)  // TODO: fix this...
  val imgs = Array[Drawable](walk1)
  val attackImg = images(ElsaShootID)
  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class Elsa(xc: Float, yc: Float) extends RangedEnemy(xc, yc, Elsa) {
  def range = 400
  def projType = ElsaProj
  def shootSpeed = 120
  def offsetx = 0
  def offsety = 0
}

object Hotdog extends EnemyType {
  val id = HotdogW1ID
  val maxHp = 60
  val attack = 5
  val defense = 0
  val speed = 4
  val walk1 = images(HotdogW1ID)
  val walk2 = images(HotdogW2ID)
  val knockback = images(HotdogKnockbackID)  // TODO: fix this...
  val imgs = Array[Drawable](walk1, walk2)
  val attackImg = images(HotdogW1ID)
  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class Hotdog(xc: Float, yc: Float) extends RangedEnemy(xc, yc, Hotdog) {
  def range = 600
  def projType = Ketchup
  def shootSpeed = 180
  def offsetx = 0
  def offsety = 0
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
    if (alive) move(-speed, 0)
    if (x < -width) {
      inactivate
    }
  }

}

trait Boss extends Enemy {
  override def knockback(distance: Float) = {
    if (hp <= 0) inactivate
  }
}

object BossFull extends EnemyType {
  val id = BossFullID
  val maxHp = 500
  val attack = 0
  val defense = 3
  val speed = 3
  val walk1 = images(BossFullID)
  val knockback = images(ElsaKnockbackID)
  val imgs = Array[Drawable](walk1)
  val attackImg = images(BossFullID)
  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class BossFull(xc: Float, yc: Float)  extends RangedEnemy(xc, yc, BossFull) with Boss {
  def range: Int = GameArea.width.toInt
  def projType: ProjectileID = BossFullProjectile
  def shootSpeed = 1

  lazy val soaker = images(BossFullSuperSoakerID)
  def offsetx = if (direction == GameObject.Left) -soaker.width/2 else soaker.width/2-25
  def offsety = -soaker.height/2+images(projType.id).height+10

  override def draw(g: org.newdawn.slick.Graphics, gc: org.newdawn.slick.GameContainer) = {
    super.draw(g, gc)
    drawScaledImage(soaker, x + width/2 - soaker.width/2, y + height/2 - soaker.height/2, g)
  }
}

object BossUncoat extends EnemyType {
  val id = BossUncoatID
  val maxHp = 500
  val attack = 0
  val defense = 3
  val speed = 3
  val walk1 = images(BossUncoatID)
  val knockback = images(ElsaKnockbackID)
  val imgs = Array[Drawable](walk1, images(BossUncoatWalkID))
  val attackImg = images(BossUncoatAttackID)
  val atkHeight = 5.0f
  val atkWidth = 50.0f
}

class BossUncoat(xc: Float, yc: Float)  extends Enemy(xc, yc, BossUncoat) with Boss {
  val attackDuration = 120
  var attackProgress = 0
  val windup = 1f/2
  val swing = 1f/4
  val recovery = 1f/4
  override def attackPoint = ((windup+swing)*attackDuration).toInt
  override def backswing = (attackDuration*recovery).toInt
  def rotation() = {
    attackProgress = (attackProgress+1)%attackDuration
    if (0 <= attackProgress && attackProgress <= attackDuration*windup) {
      30f*attackProgress/(attackDuration*windup)
    } else if (attackDuration*windup <= attackProgress && attackProgress <= attackDuration*(windup+swing)) {
      30-120f*(attackProgress-attackDuration*windup)/(attackDuration*swing)
    } else {
      -90+90f*(attackProgress-attackDuration*(windup+swing))/(attackDuration*recovery)
    }
  }
  override def draw(g: org.newdawn.slick.Graphics, gc: org.newdawn.slick.GameContainer) = {
    if (attacking) {
      import BossUncoat.attackImg
      val xrot = (if (direction == GameObject.Left) 3f else 4f) *attackImg.width/8
      val yrot = 15*attackImg.height/18
      attackImg.setCenterOfRotation(xrot,yrot)
      attackImg.setRotation(-rotation*direction)
      img = attackImg
      images(BossUncoatAttackLegID).draw(x+xrot,y+yrot, direction != GameObject.Left)
    } else {
      img = base.walk1
      attackProgress = 0
    }
    super.draw(g, gc)
  }
}

