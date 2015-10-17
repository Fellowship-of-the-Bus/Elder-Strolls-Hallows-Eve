package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}

trait CharacterType {
  def id: Int
  def maxHp: Int
  def attack: Int
  def defense: Int
  def speed: Int
}

abstract class Character(xc: Float, yc: Float, val base: CharacterType) extends GameObject(xc, yc) {
  def name: String

  var hp: Float = base.maxHp
  def maxHp = base.maxHp
  def attack = base.attack
  def defense = base.maxHp
  def speed = base.maxHp

  def id: Int = base.id

  def move(xamt: Float, yamt: Float): Unit = {
    x = x + xamt
    y = y + yamt
  }

  def width: Int = 10
  def height: Int = 10
}
