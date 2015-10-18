package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import org.newdawn.slick.{GameContainer, Graphics, Color, Input}
import org.newdawn.slick.state.{StateBasedGame}
import org.newdawn.slick.util.InputAdapter

import game.IDMap._

class ControllerInput(g: game.Game, gc: GameContainer, sbg: StateBasedGame) extends InputAdapter() {
  var input : Input = null
  var controllerCount = 0
  val game = g

  import lib.game.GameConfig.{OS,MacOS,Windows}
  lazy val BUTTON_A = OS match {
    case MacOS => 15
    case _ => 1
  } 
  lazy val BUTTON_B = OS match {
    case MacOS => 14
    case _ => 2
  } 
  lazy val BUTTON_X = OS match {
    case MacOS => 16
    case _ => 3
  } 
  lazy val BUTTON_Y = OS match {
    case MacOS => 13
    case _ => 4
  }
    lazy val BUTTON_LB = OS match {
    case MacOS => 11
    case _ => 5
  } 
  lazy val BUTTON_RB = OS match {
    case MacOS => 12
    case _ => 6
  } 
  lazy val BUTTON_BACK = OS match {
    case MacOS => 1
    case _ => 7
  } 
  lazy val BUTTON_START = OS match {
    case MacOS => 4
    case _ => 8
  } 
  lazy val BUTTON_LS = OS match {
    case MacOS => 2
    case _ => 9
  }
  lazy val BUTTON_RS = OS match {
    case MacOS => 3
    case _ => 10
  }
  lazy val BUTTON_LT = OS match {
    case MacOS => 9
    case _ => 0
  }
  lazy val BUTTON_RT = OS match {
    case MacOS => 10
    case _ => 0
  }

  override def setInput(in: Input) = {
    in.addControllerListener(this)
    input = in
    controllerCount = in.getControllerCount()
  }

  override def controllerButtonPressed(controller: Int, button: Int) = {
    println(button)

    if (button == BUTTON_START) {
      gc.setPaused(!gc.isPaused)
    }
    if (sbg.getCurrentStateID == Mode.MenuID) {
      if (button == BUTTON_A) {
        sbg.enterState(Mode.BattleID)
      }
      else if (button == BUTTON_B) {
        System.exit(0)
      }
    } else {
      if (button == BUTTON_A) {

      }
    }
  }

  lazy val AXIS_X = OS match {
    case MacOS => 0
    case _ => 1
  }
  lazy val AXIS_Y = OS match {
    case MacOS => 1
    case _ => 0
  }

  def update() = {
    if (!gc.isPaused) {
      var i = 0
      for (i <- 0 until controllerCount) {
        var j = 0
        var p = game.players(i)
        p.move(3*input.getAxisValue(i,AXIS_X),3*input.getAxisValue(i,AXIS_Y))
      }
    }
  }

}