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

object PlayerHUD {
  implicit val color: Color = new Color(0.8f, 0.8f, 0.9f, 1f)  
}
import PlayerHUD.color

class PlayerHUD(x: Float, y: Float, width: Float, height: Float, player: Player, playerColor: Color) extends Pane(x, y, width, height) {
  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    super.draw(gc, sbg, g)
  }

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    val visible = () => player.active

    val nameText = new TextBox(0, 0, width / 2, height, () => player.name)
    val colorBox = new Pane(width / 2, 0, width / 2, height)(playerColor)
    val scoreText = new TextBox(0, 20, width, height, () => ("Score: " + player.score))
    val hp = new Lifebar(0, 40, player)
    val facts = new FactBox(0, 60, width, height, player, color)

    val components = List(nameText, colorBox, scoreText, hp, facts)

    for (c <- components) {
      c.setIsVisible(visible)
    }

    addChildren(components)
    super.init(gc, sbg)
  }
}
