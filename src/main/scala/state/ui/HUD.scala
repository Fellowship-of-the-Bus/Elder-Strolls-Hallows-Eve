package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import lib.game.GameConfig.{Width,Height}
import lib.ui.{Button, Drawable, ImageButton, Pane}

import org.newdawn.slick.{GameContainer, Graphics, Color,Input}
import org.newdawn.slick.state.{StateBasedGame}

import game._
import game.IDMap._

object HUD extends Pane(0, 0, Width, 60)(Color.white) {
  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    super.draw(gc, sbg, g)
  }

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    val game = HUD.game.asInstanceOf[Game]
    val playerColors = Array(new Color(0f, 1f, 1f), new Color(0.6f, 0f, 0.6f), 
      new Color(1f, 1f, 0f), new Color(0f, 1f, 0.5f))

    val players = new Array[PlayerHUD](game.maxPlayers)
    for (i <- 0 until game.maxPlayers) {
      players(i) = new PlayerHUD(width*i/game.maxPlayers, 0, 
        width/game.maxPlayers, height, game.players(i), playerColors(i))
    }

    addChildren(players.toList)
    super.init(gc, sbg)
  }
}
