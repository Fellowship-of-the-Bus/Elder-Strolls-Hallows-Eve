package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}

abstract class Character(xc: Float, yc: Float, val maxHp: Float) extends GameObject(xc, yc) {
  def hp: Float
}
