package com.github.fellowship_of_the_bus
package eshe
package game

import lib.math.Rect.intersect

object Projectile {
  val attack = 3
}

class Projectile(xc: Float, yc: Float, val id: Int, dir: Int) extends GameObject(xc, yc) {
  def move() = x = x + dir

  val attack = Projectile.attack

  override def update(delta: Long, game: Game) = {
    super.update(delta, game)

    move
    for (p <- game.players) {
      if (intersect(this, p)) {
        hit(p)
        inactivate
      }
    }
    img.update(delta)
  }
}

