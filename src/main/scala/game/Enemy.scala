package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}

trait EnemyType extends CharacterType {

}

abstract class Enemy(xc: Float, yc: Float, override val base: EnemyType) extends game.Character(xc, yc, base) {
  def age: Int
  def fact: String

  def move() = {
    
  }
}
