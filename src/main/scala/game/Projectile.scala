package com.github.fellowship_of_the_bus
package eshe
package game

object Projectile {

}

class Projectile(xc: Float, yc: Float, val id: Int, dir: Int) extends GameObject(xc, yc) {
  def move() = x = x + dir
}

