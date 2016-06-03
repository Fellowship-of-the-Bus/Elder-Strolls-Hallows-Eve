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

    val lightBlue = new Color(150,150,255,0)
    g.setBackground(lightBlue)


    if (game.isGameOver) {
      g.setColor(new Color(255, 0, 0, (0.5 * 255).asInstanceOf[Int]))
      g.fillRect(0, 0, Width, Height)
      images(GameOverID).draw(0,0)
    }
  }

  def init(gc: GameContainer, sbg: StateBasedGame) = {
    ui.addChildren(GameArea, HUD)
    ui.setState(getID)
    ui.resetGame(game)
    ui.init(gc, sbg)
  }

  def getID() = Mode.BattleID
}
