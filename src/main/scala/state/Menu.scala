package com.github.fellowship_of_the_bus
package eshe
package state

import lib.slick2d.ui.{Button, ToggleButton}
import game.IDMap._
import lib.game.GameConfig
import lib.game.GameConfig.{Width,Height}

import org.newdawn.slick.{GameContainer, Graphics, Color, Input, KeyListener}
import org.newdawn.slick.state.{BasicGameState => SlickBasicGameState, StateBasedGame}

trait BasicGameState extends SlickBasicGameState {
  implicit var input: Input = null
  implicit var SBGame: StateBasedGame = null
  var container: GameContainer = null

  val centerx = Width/2-Button.width/2
  val startY = 400
  val padding = 30 // space from start of one button to start of next
  val logoStartY = 200

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {}

  val bgColor = new Color(0, 20, 46)
  def render(gc: GameContainer, game: StateBasedGame, g: Graphics): Unit = {
    gc.getGraphics.setBackground(bgColor)
    val fotb = images(FotBLogoID)
    val logo = images(LogoID)
    val background = images(BackgroundFullID)
    fotb.scaleFactor = 1
    logo.scaleFactor = 1
    background.scaleFactor = state.ui.GameArea.ratio

    background.draw(0,0)
    background.draw(background.width,0)
    background.draw(2*background.width,0)

    fotb.draw(Width/2-fotb.getWidth/2, 3*Height/4)
    logo.draw(Width/2-logo.getWidth/2, logoStartY)
  }

  def init(gc: GameContainer, game: StateBasedGame) = {
    input = gc.getInput
    SBGame = game
    container = gc
  }
}

object Menu extends BasicGameState {
  implicit val id = getID

  lazy val choices = List(
    Button("New Game (A/X)", centerx, startY, () => SBGame.enterState(Mode.BattleID)),
    Button("Options", centerx, startY+padding, () => SBGame.enterState(Mode.OptionsID)),
    Button("Quit (B/O)", centerx, startY+2*padding, () => System.exit(0)))

  override def render(gc: GameContainer, game: StateBasedGame, g: Graphics): Unit = {
    super.render(gc, game, g)

    for ( item <- choices ) {
      item.render(g)
    }
  }

  def getID() = Mode.MenuID
}

object Options extends BasicGameState {
  implicit val id = getID

  lazy val choices = List(
    ToggleButton("Display Lifebars", centerx, startY,
      () => GameConfig.showLifebars = !GameConfig.showLifebars, // update
      () => GameConfig.showLifebars), // query
    ToggleButton("Full Screen", centerx, startY+padding,
      () => container.setFullscreen(! container.isFullscreen), // update
      () => container.isFullscreen), // query
    Button("Back", centerx, startY+2*padding, () => SBGame.enterState(Mode.MenuID)))

  override def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    super.render(gc, game, g)
    for ( item <- choices ) {
      item.render(g)
    }
  }

  def getID() = Mode.OptionsID
}
