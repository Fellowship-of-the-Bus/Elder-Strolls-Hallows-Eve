package com.github.fellowship_of_the_bus
package eshe
package state

import lib.slick2d.ui.{Button, ToggleButton}
import game.IDMap._
import lib.game.GameConfig
import lib.game.GameConfig.{Width,Height}

import org.newdawn.slick.{GameContainer, Graphics, Color, Input, KeyListener}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}


object Menu extends BasicGameState {
  val centerx = Width/2-Button.width/2
  implicit val id = getID

  implicit var input: Input = null
  implicit var SBGame: StateBasedGame = null

  lazy val choices = List(
    Button("New Game (A/X)", centerx, 400, () => SBGame.enterState(Mode.BattleID)),
    Button("Options", centerx, 400+30, () => SBGame.enterState(Mode.OptionsID)),
    Button("Quit (B/O)", centerx, 400+60, () => System.exit(0)))

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
  }

  def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    val fotb = images(FotBLogoID)
    val logo = images(LogoID)
    fotb.scaleFactor = 1
    logo.scaleFactor = 1

    fotb.draw(Width/2-fotb.getWidth/2, 3*Height/4)
    for ( item <- choices ) {
      item.render(g)
    }
    logo.draw(Width/2-logo.getWidth/2, 200)
  }

  def init(gc: GameContainer, game: StateBasedGame) = {
    input = gc.getInput
    SBGame = game
    gc.getGraphics.setBackground(Color.cyan)
  }

  def getID() = Mode.MenuID
}

object Options extends BasicGameState {
  import Menu.{centerx, SBGame, input}

  implicit val id = getID

  lazy val choices = List(
    ToggleButton("Display Lifebars", centerx, 200,
      () => GameConfig.showLifebars = !GameConfig.showLifebars, // update
      () => GameConfig.showLifebars), // query
    Button("Back", centerx, 200+30, () => SBGame.enterState(Mode.MenuID)))

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
  }

  def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    for ( item <- choices ) {
      item.render(g)
    }
  }

  def init(gc: GameContainer, game: StateBasedGame) = {

  }

  def getID() = Mode.OptionsID
}
