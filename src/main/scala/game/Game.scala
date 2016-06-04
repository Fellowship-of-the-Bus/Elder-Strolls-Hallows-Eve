package com.github.fellowship_of_the_bus
package eshe
package game
import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException,Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import IDMap._
import lib.game.GameConfig.{Height,Width}
import lib.util.rand
import lib.util.{TickTimer,TimerListener,RepeatForever}

import state.ui.GameArea

class Game extends lib.slick2d.game.Game with TimerListener {
  val maxPlayers = 4
  val players = new Array[Player](maxPlayers)
  for (i <- 0 until maxPlayers) {
    players(i) = new IVGuy(0, 0, i)
  }

  def setPlayers(nplayers: Int) = {
    for (i <- nplayers until maxPlayers) {
      players(i).inactivate
    }
  }

  this += new TickTimer(240, cleanup _, RepeatForever)
  this += new TickTimer(240, () => enemies = createEnemy :: enemies, RepeatForever)

  var projectiles = List[Projectile]()
  var enemies = List[Enemy]()

  private var numSpawns = 0
  def cleanup() = {
    enemies = enemies.filter(_.active)
    projectiles = projectiles.filter(_.active)
  }

  def createEnemy() : Enemy = {
    val t = rand(0, 3)
    val x = 1400

    val enemy = t match {
      case 0 => new Ghost(x, 0)
      case 1 => new Elsa(x, 0)
      case 2 => new PowerRanger(x, 0)
      case 3 => new HorseMask(x, 0)
    }
    enemy.y = rand(GameArea.y.toInt, (GameArea.y + GameArea.height - enemy.height).toInt)
    enemy
  }

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    tick(delta)
    for (e <- enemies; if (e.active)) {
      e.update(delta, this)
    }
    for (p <- projectiles; if (p.active)) {
      p.update(delta, this)
    }
    for (p <- players; if (p.active)) {
      p.update(delta, this)
    }
  }
}
