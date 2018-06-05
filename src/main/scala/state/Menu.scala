package com.github.fellowship_of_the_bus
package eshe
package state

import lib.slick2d.ui.{Button, ToggleButton, InteractableUIElement}
import game.IDMap._
import lib.game.GameConfig
import lib.game.GameConfig.{Width,Height}

import org.newdawn.slick.{GameContainer, Graphics, Color, Input, KeyListener}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

object MenuState {
  val arrow = images(SelectArrow)
  val fotb = images(FotBLogoID)
  val logo = images(LogoID)
  val background = images(BackgroundFullID)
  // images are created with GameArea's scaleFactor by default. Need to set back to 1 to calculate correct scaleFactor
  arrow.scaleFactor = 1
  arrow.scaleFactor = Button.height/arrow.height // same height as a button
  fotb.scaleFactor = 1
  logo.scaleFactor = 1
  background.scaleFactor = state.ui.GameArea.ratio
}
import MenuState._

trait MenuState extends BasicGameState {
  implicit var input: Input = null
  implicit var SBGame: StateBasedGame = null
  var container: GameContainer = null

  val centerx = Width/2-Button.width/2
  val startY = 400
  val padding = 30 // space from start of one button to start of next
  val logoStartY = 200

  private var currentOption = 0

  def confirm(): Unit = choices(currentOption).doAction()
  def next(): Unit = currentOption = (currentOption+1)%choices.length
  def previous(): Unit = currentOption = (currentOption+choices.length-1)%choices.length
  def back(): Unit

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {}

  val bgColor = new Color(0, 20, 46)
  def render(gc: GameContainer, game: StateBasedGame, g: Graphics): Unit = {
    gc.getGraphics.setBackground(bgColor)

    background.draw(0,0)
    background.draw(background.width,0)
    background.draw(2*background.width,0)

    fotb.draw(Width/2-fotb.getWidth/2, 3*Height/4)
    logo.draw(Width/2-logo.getWidth/2, logoStartY)

    for ( item <- choices ) {
      item.render(gc, game, g)
    }

    // draw selection arrow next to highlighted choice
    arrow.draw(choices(currentOption).x-arrow.width, choices(currentOption).y)
  }

  def init(gc: GameContainer, game: StateBasedGame) = {
    input = gc.getInput
    SBGame = game
    container = gc
  }

  def choices: List[InteractableUIElement]
}

object Menu extends MenuState {
  implicit val id = getID

  lazy val choices = List(
    Button("New Game", centerx, startY, () => {
      Battle.newGame()
      SBGame.enterState(Mode.BattleID)
    }),
    Button("Options", centerx, startY+padding, () => SBGame.enterState(Mode.OptionsID)),
    Button("Quit", centerx, startY+2*padding, () => System.exit(0)))

  def back() = ()

  def getID() = Mode.MenuID
}

object Options extends MenuState {
  implicit val id = getID

  lazy val choices = List(
    ToggleButton("Display Lifebars", centerx, startY,
      () => GameConfig.showLifebars = !GameConfig.showLifebars, // update
      () => GameConfig.showLifebars), // query
    ToggleButton("Full Screen", centerx, startY+padding,
      () => container.setFullscreen(! container.isFullscreen), // update
      () => container.isFullscreen), // query
    ToggleButton("Show FPS", centerx, startY+2*padding,
      () => container.setShowFPS(! container.isShowingFPS()), // update
      () => container.isShowingFPS()), // query
    Button("Back", centerx, startY+3*padding, () => back()))

  def back() = SBGame.enterState(Mode.MenuID)

  def getID() = Mode.OptionsID
}
