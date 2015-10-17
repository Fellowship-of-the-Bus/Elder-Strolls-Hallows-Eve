package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width,Height}

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
  def defense = base.defense
  def speed = base.speed

  def id: Int = base.id

  def move(xamt: Float, yamt: Float): Unit = {
    if (x < 0)  x = 0;
    if (x > Width - width) x = Width - width;
    x = x + xamt
    if (y < state.ui.HUD.height) y = state.ui.HUD.height
    if (y > (Height - height)) y = Height - height
    y = y + yamt
  }
  val animation = images(base.id).copy
  val height = animation.getHeight
  val width = animation.getWidth
}
