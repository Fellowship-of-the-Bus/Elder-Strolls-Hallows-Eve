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

object Game {
  def spawnX(width: Float, horse: Boolean = false) = {
    val side = rand(0,1)
    side match {
      case 0 if (! horse) => -width
      case _ => GameArea.width
    }
  }
  def spawnY(height: Float) = rand(GameArea.fenceHeight.toInt, GameArea.height.toInt) - height
}

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

    spawnWave()
    // enemies = new BossFull(state.ui.GameArea.width/2, 0)::enemies
    // enemies = new BossUncoat(state.ui.GameArea.width/2, 0)::enemies
    // enemies = new BossCellphone(state.ui.GameArea.width/2, 0)::enemies
    // enemies = new BossFinal(state.ui.GameArea.width/2, 0)::enemies
  }

  this += new TickTimer(240, cleanup _, RepeatForever)
  var waveTimer = new TickTimer(0, () => (), Finished)

  object spawnWave {
    var waveNum = 0
    var hordeSpawnInterval = 90
    var hordeSpawnNum = 1

    def numEnemy() = {
      waveNum + 2*players.filter(_.active).length
    }
    def numHorseMask() = {
      rand((waveNum/2).toInt, waveNum)*players.filter(_.active).length + 1
    }
    def interval() = {
      60
    }
    def apply(): Unit = {
      waveNum += 1
      waveNum match {
        case 1 => waveTimer = new TickTimer(60, () => enemies = new BossFull(state.ui.GameArea.width*4f/5, state.ui.GameArea.height/2, waveNum)::enemies)
        case 6 => {
          waveTimer = new TickTimer(hordeSpawnInterval, () => enemies = createEnemy(waveNum) :: enemies, FireN(hordeSpawnNum))
          Game.this += new TickTimer(hordeSpawnInterval, () => {
            waveNum = 5
            hordeSpawnInterval = math.max(hordeSpawnInterval-1,0)
            hordeSpawnNum += 1
            spawnWave()
          })
        }
        case _ => {
          waveTimer = new TickTimer(interval, () => enemies = createEnemy(waveNum) :: enemies, FireN(numEnemy))
          Game.this += new TickTimer(120, () => enemies = createHorseMask(waveNum) :: enemies, FireN(numHorseMask))
        }
      }
      Game.this += waveTimer
    }
  }

  var projectiles = List[BaseProjectile]()
  var enemies = List[Enemy]()

  def cleanup() = {
    enemies = enemies.filter(_.active)
    projectiles = projectiles.filter(_.active)
  }

  def createEnemy(waveNum: Int) : Enemy = {
    val t = rand(0, 3)
    val enemy =  t match {
      case 0 => new Ghost(0, 0, waveNum)
      case 1 => new Elsa(0, 0, waveNum)
      case 2 => 
        new PowerRanger(0, 0, waveNum,
          (rand(6)) match {
            case 0 => PowerRanger
            case 1 => PowerRangerBlue
            case 2 => PowerRangerGreen
            case 3 => PowerRangerYellow
            case 4 => PowerRangerPink
            case 5 => PowerRangerBlack
          })
      case 3 => new Hotdog(0, 0, waveNum)
    }
    enemy.x = Game.spawnX(enemy.width)
    enemy.y = Game.spawnY(enemy.height)
    if (enemy.x == 0) {
      enemy.direction = GameObject.Right
    }
    enemy
  }

  def createHorseMask(waveNum: Int) : Enemy = {
    val enemy = new HorseMask(0, 0, waveNum)
    enemy.x = Game.spawnX(enemy.width, true)
    enemy.y = Game.spawnY(enemy.height)
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
      if (canScroll == false) {
        for (player <- players.filter(_.active)) {
          player.heal(0.2f)
        }
      }
      canScroll = true
    }

    if (!players.exists(_.active)) {
      gameOver()
    }
  }

  def pause(isPaused: Boolean) = {
    for (e <- enemies; if (e.active)) {
      e.pause(isPaused)
    }
  }
}
