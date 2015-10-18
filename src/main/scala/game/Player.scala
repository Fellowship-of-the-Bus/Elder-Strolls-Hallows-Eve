package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}

import eshe.state.ui.PlayerListener

trait PlayerType extends CharacterType {

}

abstract class Player(xc: Float, yc: Float, override val base: PlayerType) extends game.Character(xc, yc, base) {
  var score = 0

  def hit(e: Enemy) = {
    val damage = (attack - e.defense)
    e.hp = e.hp - damage
    score += damage

    if (e.hp <= 0) {
      e.inactivate
      notify(x => x.enemyDied(e))
    }
  }

  var listeners = List[PlayerListener]()
  def addListener(l: PlayerListener) = {
    listeners = l::listeners
  }

  def notify(event: (PlayerListener) => Unit) = {
    for (l <- listeners) {
      event(l)
    }
  }
}

object IVGuy extends PlayerType {
  val id = IVGuyID
  val maxHp = 100
  val attack = 30
  val defense = 20
  val speed = 10
}

class IVGuy(xc: Float, yc: Float) extends Player(xc, yc, IVGuy) {
  val name = "Herbert"

}
