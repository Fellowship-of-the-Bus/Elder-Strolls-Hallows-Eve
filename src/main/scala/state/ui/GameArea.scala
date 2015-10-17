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

  def drawScaledImage(im: Drawable, x: Float, y: Float, g: Graphics) = {
    g.scale(scaleFactor,scaleFactor)

    im.draw(x,y)
  
    g.scale(1/scaleFactor, 1/scaleFactor)
  }

  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    super.draw(gc, sbg, g)
    for(i <- 0 until Battle.controller.controllerCount) {
      val p = Battle.game.players(i)
      drawScaledImage(images(p.id), p.x, p.y, g)
    }
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
