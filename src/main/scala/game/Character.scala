package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width,Height}
import lib.ui.{Drawable}
import state.ui.GameArea

trait CharacterType {
  def id: Int
  def maxHp: Int
  def attack: Int
  def defense: Int
  def speed: Int

  def imgs: Array[Drawable]
}

abstract class Character(xc: Float, yc: Float, val base: CharacterType) extends GameObject(xc, yc) {
  def name: String

  var hp: Float = base.maxHp
  def maxHp = base.maxHp
  def attack = base.attack
  def defense = base.defense
  def speed = base.speed

  def id: Int = base.id

  def copy(imgs: Array[Drawable]) = imgs.map(_.copy)

  val imgs: Array[Drawable] = copy(base.imgs)
  var currImage: Drawable = imgs(0)
  val numSteps = 20
  var steps = numSteps
  var index = 0
  
  override def move(xamt: Float, yamt: Float): Unit = {
    if (x < 0)  {
      x = 0
    }
    if (x > (Width - width)) {
      x = Width - width
    }
    x = x + xamt
    if (y < 0) {
       y = 0
    }
    if (y > (GameArea.height - height)) {
       y = GameArea.height - height
    }
    y = y + yamt
    if ((xamt != 0) || (yamt != 0)) {
      steps = Math.max(0, steps-1)
      if (steps == 0) {
        steps = numSteps
        index = (index + 1) % imgs.length
        img = imgs(index)
      }
    }
  }

}
