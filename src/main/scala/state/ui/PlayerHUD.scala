package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import lib.game.GameConfig.{Width,Height}
import lib.ui.{Button, Drawable, ImageButton, Pane, TextBox}

import org.newdawn.slick.{GameContainer, Graphics, Color,Input}
import org.newdawn.slick.state.{StateBasedGame}

import game._
import game.IDMap._

class PlayerHUD(x: Float, y: Float, width: Float, height: Float, player: Player) extends Pane(x, y, width, height)(Color.white) {
  implicit val color: Color = Color.white
  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    super.draw(gc, sbg, g)
  }

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    val nameText = new TextBox(0, 0, width, height, () => player.name)
    val hp = new Lifebar(0, 20, player)
    val facts = new FactBox(0, 40, width, height, player)

    // val lives = new TextBox(5, 15+buttonHeight, buttonWidth, buttonHeight,
    //   () => s"Lives: ${game.getLives}")(Color.white)

    // val money = new TextBox(width-100, 10, buttonWidth, buttonHeight,
    //   () => s"$$${game.getMoney}")(Color.white)

    addChildren(nameText, hp, facts)
    super.init(gc, sbg)
  }
}
