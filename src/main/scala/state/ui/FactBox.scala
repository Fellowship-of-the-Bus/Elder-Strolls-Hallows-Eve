package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException, Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import lib.ui.{Pane,TextBox}

import eshe.game.{GameObject,Player,Enemy}

class FactBox(x: Float, y: Float, width: Float, height: Float, player: Player) extends Pane(x, y, width, height)(Color.white) {
  var enemy: Option[Enemy] = None
  var enemyQueue = Vector[Enemy]()

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    implicit val color = Color.white

    val name = new TextBox(0, 0, width, height, () => {
      enemy.map(_.name) getOrElse "Name"
    })

    val age = new TextBox(0, 20, width, height, () => {
      enemy.map(_.age.toString) getOrElse "Age" 
    })

    val fact = new TextBox(0, 40, width, height, () => {
      enemy.map(_.fact) getOrElse "Fact"  
    })

    addChildren(name, age, fact)
  }

  override def update(gc: GameContainer, sbg: StateBasedGame, delta: Int): Unit = {
    super.update(gc, sbg, delta)
    // modify queue
  }
}
