package com.github.fellowship_of_the_bus
package eshe
package game
import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException,Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import IDMap._
import lib.game.GameConfig.{Height,Width}
import lib.util.rand

class Game extends lib.game.Game {
  private var counter = 0

  val maxPlayers = 4
  var players = new Array[Player](maxPlayers)
  for (i <- 0 until maxPlayers) {
    players(i) = new IVGuy(0, 0, i)
  }

  def setPlayers(nplayers: Int) = {
    val ps = players
    players = new Array[Player](nplayers)
    for (i <- 0 until maxPlayers) {
      if (i < nplayers) {
        players(i) = ps(i)
      } else {
        ps(i).inactivate
      } 
    }
  }


  var projectiles = List[Projectile]()
  var enemies = List[Enemy]()

  var cleanUpPeriod = 120
  private val spawnTimer = 300
  private var numSpawns = 0
  var timer = 0
  def cleanup() = {
    enemies = enemies.filter(_.active)
    projectiles = projectiles.filter(_.active)
  }

  def createEnemy() : Enemy = {
    val t = rand(0, 3)
    val y = rand(300, 1000)

    t match {
      case 0 => return new Ghost(1400, y)
      case 1 => return new Elsa(1400, y)
      case 2 => return new PowerRanger(1400, y)
      case _ => return new HorseMask(1400, y)
    }
  }

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    counter += 1

    implicit val input = gc.getInput
    if (timer == 0) {
      cleanup
      timer = cleanUpPeriod
    } else {
       timer -= 1
    }

    for (e <- enemies; if (e.active)) {
      e.update(delta, this)
    }

    for (p <- projectiles; if (p.active)) {
      p.update(delta, this)
    }

    for (p <- players; if (p.active)) {
      p.update(delta, this)
      if (p.hp < 0) {
        p.hp = 0
        p.inactivate
      }
    }

    if (counter >= spawnTimer) {
      enemies = createEnemy :: enemies
      counter = 0
    }
  }
}
