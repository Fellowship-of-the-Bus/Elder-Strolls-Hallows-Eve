package com.github.fellowship_of_the_bus
package eshe
package state
import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException, Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import game._
import game.IDMap._
import lib.ui.{Image,Pane}
import lib.game.GameConfig.{Width,Height}
import ui._

object Battle extends BasicGameState {
  var game = new Game
  var players: List[TempPlayer] = List()

  val ui = new Pane(0, 0, Width, Height)(Color.white)
  var controller: ControllerInput = null
  var pauseTimer = 0
  def update(gc: GameContainer, sbg: StateBasedGame, delta: Int) = {
    implicit val input = gc.getInput

    if (pauseTimer == 0 && input.isKeyDown(Input.KEY_P)) {
      gc.setPaused(!gc.isPaused)
      pauseTimer = 15
    }

    if (! gc.isPaused) {
      game.update(gc, sbg, delta)
      if (controller != null) {
        controller.update();
      }
    }

    pauseTimer = Math.max(0, pauseTimer-1)
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
    ui.addChildren(HUD, GameArea)
    ui.setState(getID)
    ui.resetGame(game)
    ui.init(gc, sbg)
    controller = new ControllerInput(game, gc, sbg)
    controller.setInput(gc.getInput)
  }

  def getID() = Mode.BattleID
}
