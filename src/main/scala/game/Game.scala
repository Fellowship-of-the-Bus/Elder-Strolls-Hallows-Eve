package com.github.fellowship_of_the_bus
package eshe
package game
import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException,Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import IDMap._
import lib.game.GameConfig.{Height,Width}
import lib.util.{TickTimer,TimerListener,RepeatForever,rand,FireN,Finished}
import lib.math.clamp

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
  var waveTimer = new TickTimer(0, () => (), Finished)

  object spawnWave {
    var waveNum = 0
    def apply() = {
      waveNum += 1
      waveTimer = new TickTimer(60, () => enemies = createEnemy :: enemies, FireN(waveNum))
      Game.this += waveTimer
    }
  }

  var projectiles = List[BaseProjectile]()
  var enemies = List[Enemy]()

  private var numSpawns = 0
  def cleanup() = {
    enemies = enemies.filter(_.active)
    projectiles = projectiles.filter(_.active)
  }

  def createEnemy() : Enemy = {
    val t = rand(0, 4)
    val x = GameArea.width

    val enemy =  t match {
      case 0 => new Ghost(x, 0)
      case 1 => new Elsa(x, 0)
      case 2 => new PowerRanger(x, 0)
      case 3 => new Hotdog(x, 0)
      case 4 => new HorseMask(x, 0)
    }
    enemy.y = rand(GameArea.fenceHeight.toInt, GameArea.height.toInt) - enemy.height
    enemy
  }

  var canScroll = false
  var scrollAmt = 0.0f
  lazy val waveWidth = 3*images(BackgroundID).width/4
  var remainingScrollAmt = waveWidth
  def scroll(amt: Float) = {
    if (canScroll) {
      for (player <- players) {
        player.x = clamp(player.x-amt, 0, Width-player.width)
      }
      for (projectile <- projectiles) {
        projectile.x -= amt
      }
      scrollAmt = (scrollAmt + amt) % images(BackgroundID).width
      remainingScrollAmt = clamp(remainingScrollAmt-amt, 0.0f, remainingScrollAmt)
    }
    if (remainingScrollAmt == 0) {
      canScroll = false
      spawnWave()
      remainingScrollAmt = waveWidth
    }
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

    if (! waveTimer.canFire() && ! enemies.exists(_.active)) {
      canScroll = true
    }

    if (!players.exists(_.active)) {
      gameOver()
    }
  }

  spawnWave()
  // enemies = new BossFull(state.ui.GameArea.width/2, 0)::enemies
  // enemies = new BossUncoat(state.ui.GameArea.width/2, 0)::enemies
}
