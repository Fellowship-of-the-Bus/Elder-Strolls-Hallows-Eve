package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException, Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import lib.slick2d.ui.{UIElement,AbstractUIElement}

import eshe.game.{GameObject,Character}

class Lifebar(x: Float, y: Float, width: Float, height: Float, var obj: Character) extends AbstractUIElement(x, y, width, height) {
  def this(x: Float, y: Float, obj: Character) = this(x+2, y, HUD.width/4-2, HUD.height/3, obj)

  def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    val color = g.getColor

    val hp = obj.hp
    val maxHp = obj.maxHp

    g.setColor(Color.black)
    g.drawRect(x, y, width, height)

    val lower = maxHp/3
    val upper = 2*maxHp/3

    if (hp > upper) {
      g.setColor(Color.green)
    } else if (hp < lower) {
      g.setColor(Color.red)
    } else {
      g.setColor(Color.yellow)
    }
    g.fillRect(x, y, width * hp/maxHp, height)

    g.setColor(color)
  }

  def update(gc: GameContainer, sbg: StateBasedGame, delta: Int): Unit = ()
}
