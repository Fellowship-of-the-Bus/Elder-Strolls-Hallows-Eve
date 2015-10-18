package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}
import lib.ui.{Drawable}

import java.util.Scanner
import java.io.File

import lib.util.rand

trait EnemyType extends CharacterType {

}

object Enemy {
  private lazy val names = read("data/names.txt")
  private lazy val facts = read("data/true-facts.txt")

  def read(filename: String): List[String] = {
    var lst = List[String]()

    val stream = ElderStrolls.getClass.getClassLoader().getResourceAsStream(filename)
    val sc = new Scanner(stream)
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

abstract case class Enemy(xc: Float, yc: Float, override val base: EnemyType) extends game.Character(xc, yc, base) {
  val age: Int = rand(6, 12)

  val name = Enemy.name
  val fact = Enemy.fact
  var flying = false
  var knockTicks = 0
  var knockVelocity = 0f

  var target: Player = null
  override def update(delta: Long, game: Game) = {
    super.update(delta, game)

    // Knocked back behaviour
    if (flying) {
      knockTicks -= 1
      x = x + knockVelocity
      if (knockTicks <= 0) {
        flying = false
      }
    } else {
      if (target == null) {
        target = Enemy.randInSeq(game.players)
      } else {
        val xVec = target.x - x
        val yVec = target.y - y

        if (xVec > -50 && xVec < 50) {
          // if target is in range, attack
        } else {
          // otherwise move until enemy is in range
          move
        }

        def move() = {
    
          val norm = ((1 / Math.sqrt((xVec * xVec) + (yVec * yVec))) * speed).asInstanceOf[Float]

          x += xVec * norm
          y += yVec * norm
        }
      }
    }
  }

  

  def knockback(distance: Float) {
    knockVelocity = distance / 30
    knockTicks = 30
  }
}

object Ghost extends EnemyType {
  val id = GhostW1ID
  val maxHp = 10
  val attack = 2
  val defense = 1 
  val speed = 4
  val walk1 = images(GhostW1ID).copy
  val walk2 = images(GhostW2ID).copy
  val imgs = Array[Drawable](walk1, walk2)
}

class Ghost(xc: Float, yc: Float) extends Enemy(xc, yc, Ghost) {
}

object Elsa extends EnemyType {
  val id = ElsaID
  val maxHp = 15
  val attack = 4
  val defense = 3 
  val speed = 3
  val walk1 = images(ElsaID).copy
  val imgs = Array[Drawable](walk1)
}

class Elsa(xc: Float, yc: Float) extends Enemy(xc, yc, Elsa) {

}

object PowerRanger extends EnemyType {
  val id = PowerRangerW1ID
  val maxHp = 20
  val attack = 10
  val defense = 6 
  val speed = 5
  val walk1 = images(PowerRangerW1ID)
  val walk2 = images(PowerRangerW2ID)
  val imgs = Array[Drawable](walk1, walk2)
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

  override def draw(g: org.newdawn.slick.Graphics) = {
    super.draw(g)
    images(HorseMaskID).draw(0, 0)
  }


  override def update(delta: Long, game: Game) = {
    move(-speed, 0)
    // TODO: inactive when goes off left edge
  }
}
