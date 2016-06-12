package com.github.fellowship_of_the_bus
package eshe
package state
import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException, Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import game._
import game.IDMap._
import lib.slick2d.ui.{Image,Pane}
import lib.game.GameConfig.{Width,Height}
import ui._

object Battle extends BasicGameState {
  var game = new Game

  val ui = new Pane(0, 0, Width, Height)(Color.white)

  def update(gc: GameContainer, sbg: StateBasedGame, delta: Int) = {
    if (! gc.isPaused) {
      game.update(gc, sbg, delta)
      ui.update(gc, sbg, delta)
    }
  }

  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics) = {
    ui.render(gc, sbg, g)

    if (game.isGameOver) {
      g.setColor(new Color(255, 0, 0, (0.5 * 255).asInstanceOf[Int]))
      g.fillRect(0, 0, Width, Height)
      val img = images(GameOverID)
      img.draw((Width - img.width) / 2, (Height - img.height) / 2)
    }
  }

  var thegc : GameContainer = null
  var thesbg : StateBasedGame = null
  def init(gc: GameContainer, sbg: StateBasedGame) = {
    ui.addChildren(GameArea, HUD)
    ui.setState(getID)
    ui.resetGame(game)
    ui.init(gc, sbg)
    thegc = gc
    thesbg = sbg
  }

  def newGame() = {
    game.pause(true)
    game = new Game
    GameArea.controller.game = game
    game.setPlayers(GameArea.controller.controllers.length)
    ui.resetGame(game)
    HUD.restart(game)
  }

  def getID() = Mode.BattleID
}
