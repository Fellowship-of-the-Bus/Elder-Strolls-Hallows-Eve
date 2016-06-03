package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import scala.collection.immutable.Queue

import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException, Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import lib.slick2d.ui.{Pane,TextBox}
import lib.util.{TickTimer,TimerListener,RepeatForever}

import eshe.game.{GameObject,Player,Enemy}

class FactBox(x: Float, y: Float, width: Float, height: Float, player: Player, parentCol: Color)
extends Pane(x, y, width, height)(parentCol) with PlayerListener with TimerListener {
  var enemy: Option[Enemy] = None
  var enemyQueue = Queue[Enemy]()

  override def init(gc: GameContainer, sbg: StateBasedGame) = {
    implicit val color = parentCol

    val name = new TextBox(0, 0, width/2, 20, () => {
      enemy.map(_.name) getOrElse "Name"
    })

    val age = new TextBox(width/2, 0, width/2, 20, () => {
      enemy.map(_.age.toString) getOrElse "Age"
    })

    val fact = new TextBox(0, 20, width, 40, () => {
      enemy.map(_.fact) getOrElse "Fact"
    })

    // setup timer to trigger facts
    def nextEnemy() = {
      enemy = None
      if (! enemyQueue.isEmpty) {
        val (e, eq) = enemyQueue.dequeue
        enemy = Some(e)
        enemyQueue = eq
      }
    }
    addTimer(new TickTimer(5*60, nextEnemy _, RepeatForever))

    setIsVisible(() => ! enemy.isEmpty)

    addChildren(name, age, fact)

    player.addListener(this)
  }

  override def update(gc: GameContainer, sbg: StateBasedGame, delta: Int): Unit = {
    super.update(gc, sbg, delta)
    super.update(delta)
  }

  override def enemyDied(e: Enemy): Unit = {
    enemyQueue = enemyQueue :+ e
  }
}
