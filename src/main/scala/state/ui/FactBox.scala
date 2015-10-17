package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import scala.collection.immutable.Queue

import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException, Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import lib.ui.{Pane,TextBox}

import eshe.game.{GameObject,Player,Enemy}

class FactBox(x: Float, y: Float, width: Float, height: Float, player: Player, parentCol: Color) 
extends Pane(x, y, width, height)(parentCol) with PlayerListener {
  var enemy: Option[Enemy] = None
  var enemyQueue = Queue[Enemy]()

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    implicit val color = parentCol

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

    player.addListener(this)
  }

  val timeToNext = 120
  var timer = 0
  override def update(gc: GameContainer, sbg: StateBasedGame, delta: Int): Unit = {
    super.update(gc, sbg, delta)
    // modify queue

    if (timer <= 0) {
      enemy = None

      if (! enemyQueue.isEmpty) {
        val (e, eq) = enemyQueue.dequeue
        enemy = Some(e)
        enemyQueue = eq
        timer = timeToNext
      }
    } else {
      timer = timer - delta
    }
  }

  override def enemyDied(e: Enemy): Unit = {
    enemyQueue = enemyQueue :+ e
  }
}
