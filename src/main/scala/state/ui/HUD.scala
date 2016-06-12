package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import lib.game.GameConfig.{Width,Height}
import lib.slick2d.ui.{Button, Drawable, ImageButton}

import org.newdawn.slick.{GameContainer, Graphics, Color,Input}
import org.newdawn.slick.state.{StateBasedGame}

import game._
import game.IDMap._
// 1280x1024
object HUD extends Pane(0, 0, Width, Height*60.0f/1024)(Color.white) {
  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    super.draw(gc, sbg, g)
  }

  var players: Array[PlayerHUD] = null

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    val game = HUD.game.asInstanceOf[Game]
    val playerColors = Array(new Color(0f, 1f, 1f), new Color(0.6f, 0f, 0.6f),
      new Color(1f, 1f, 0f), new Color(0f, 1f, 0.5f))

    players = new Array[PlayerHUD](game.asInstanceOf[Game].maxPlayers)
    for (i <- 0 until game.maxPlayers) {
      players(i) = new PlayerHUD(width*i/game.maxPlayers, 0,
        width/game.maxPlayers, height, game.players(i), playerColors(i))
    }

    addChildren(players.toList)
    super.init(gc, sbg)
  }

  def restart(g: Game) = {
    HUD.game = g
    var i = 0
    for (p <- players) {
      p.player = g.players(i)
      p.hp.obj = g.players(i)
      i += 1
    }
  }
}
