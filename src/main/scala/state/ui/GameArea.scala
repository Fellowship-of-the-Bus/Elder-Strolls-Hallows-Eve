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

object GameArea extends Pane(0, HUD.height, Width, Height - HUD.height)(Color.blue) {

  val scaleFactor = 0.2f

  val widthRatio = width
  val heightRatio = height
  var controller: ControllerInput = null
  

  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    val thegame = game.asInstanceOf[eshe.game.Game]
    super.draw(gc, sbg, g)

    if (! gc.isPaused) {
      if (controller != null) {
        controller.update();
      }
    }

    for(i <- 0 until controller.controllerCount) {
      val p = thegame.players(i)
      p.draw(g)
    }
    for (e <- thegame.enemies) {
      e.draw(g)
    }
    for(p <- thegame.projectiles) {
      //p.draw(g)
    }
  }

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    val thegame = game.asInstanceOf[eshe.game.Game]
    // val lives = new TextBox(5, 15+buttonHeight, buttonWidth, buttonHeight,
    //   () => s"Lives: ${game.getLives}")(Color.white)

    // val money = new TextBox(width-100, 10, buttonWidth, buttonHeight,
    //   () => s"$$${game.getMoney}")(Color.white)

    // addChildren(lives, money, waveNum, sendWave, menu, speed, waveBar)
    super.init(gc, sbg)
    controller = new ControllerInput(thegame, gc, sbg)
    controller.setInput(gc.getInput)
  }
}
