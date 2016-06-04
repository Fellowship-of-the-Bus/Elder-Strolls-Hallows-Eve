package com.github.fellowship_of_the_bus
package eshe
package game

import lib.math.Rect.intersect
import lib.util.{TimerListener, TickTimer}

sealed trait ProjectileID {
  val id: Int       //image map index
  val speed: Float // projectile movement speed
}
case object ElsaProj extends ProjectileID {
  val id = IDMap.ElsaShotID
  val speed = 3.5f
}

case object Ketchup extends ProjectileID {
  val id = IDMap.KetchupID
  val speed = 5f
}

case object BossFullProjectile extends ProjectileID {
  val id = IDMap.BossFullAttackID
  val speed = 10f
}

class BaseProjectile(val projID: ProjectileID, xc: Float, yc: Float, val attack: Int, dir: Int) extends GameObject(xc, yc){
  def id = projID.id
  def speed() = projID.speed

  direction = dir
  override def update(delta: Long, game: Game) = {
    super.update(delta, game)

    x = x + direction * speed
    for (p <- game.players; if (p.active)) {
      if (p.hitbox.intersect(this)) {
        hit(p, attack)
        inactivate
      }
    }
    if (x < -width) {
      inactivate
    }
    img.update(delta)
  }
}

//distance = number of pixels this projectile lives for
class TimedProjectile(pid: ProjectileID, xc: Float, yc: Float, damage: Int, dir: Int, distance: Int) extends BaseProjectile(pid, xc, yc, damage, dir) with TimerListener {
  def duration(): Int = {
    (distance / speed).toInt
  }
  this += new TickTimer(duration, () => inactivate)

  def tickOnce() = {
    if (ticking()) {
      tick(1)
    } else {
      cancelAll()
    }
  }

  override def update(delta: Long, game: Game) = {

    tickOnce()
    super.update(delta, game)
  }
}

object Projectile {
  def apply(projID: ProjectileID, xc: Float, yc: Float, damage: Int, dir: Int, distance: Int = 0): BaseProjectile = {
    projID match {
      case ElsaProj => new TimedProjectile(ElsaProj, xc, yc, damage, dir, distance)
      case Ketchup => new TimedProjectile(Ketchup, xc, yc, damage, dir, distance)
      case BossFullProjectile => new BaseProjectile(BossFullProjectile, xc, yc, damage, dir)
      case _ => throw new Exception("Unrecognized projectile type")
    }
  }
}
