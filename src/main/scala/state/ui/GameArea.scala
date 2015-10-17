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
  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    super.draw(gc, sbg, g)
  }

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    // val lives = new TextBox(5, 15+buttonHeight, buttonWidth, buttonHeight,
    //   () => s"Lives: ${game.getLives}")(Color.white)

    // val money = new TextBox(width-100, 10, buttonWidth, buttonHeight,
    //   () => s"$$${game.getMoney}")(Color.white)

    // addChildren(lives, money, waveNum, sendWave, menu, speed, waveBar)
    super.init(gc, sbg)
  }
}
