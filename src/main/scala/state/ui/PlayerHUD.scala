package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import lib.game.GameConfig.{Width,Height}
import lib.slick2d.ui.{Button, Drawable, ImageButton, TextBox}

import org.newdawn.slick.{GameContainer, Graphics, Color,Input}
import org.newdawn.slick.state.{StateBasedGame}

import game._
import game.IDMap._

object PlayerHUD {
  implicit val color: Color = new Color(0.8f, 0.8f, 0.9f, 1f)
}
import PlayerHUD.color

class PlayerHUD(x: Float, y: Float, width: Float, height: Float, var player: Player, playerColor: Color) extends Pane(x, y, width, height) {
  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    super.draw(gc, sbg, g)
  }
  var hp : Lifebar = null
  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    val nameText = new TextBox(0, 0, width/2, height/3, () => player.name)
    val colorBox = new Pane(width/2, 0, width/2, height/3)(playerColor)
    val scoreText = new TextBox(0, height/3, width, 2*height/3, () => ("Score: " + player.score))
    hp = new Lifebar(0, 2*height/3, player)
    val facts = new FactBox(0, height, width, 4*height/3, player, color)

    val wasActive = player.active
    setIsVisible(() => wasActive)
    addChildren(nameText, colorBox, scoreText, hp, facts)
    super.init(gc, sbg)
  }
}
