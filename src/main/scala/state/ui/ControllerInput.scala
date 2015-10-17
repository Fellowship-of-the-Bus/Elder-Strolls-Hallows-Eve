package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import org.newdawn.slick.{GameContainer, Graphics, Color, Input}
import org.newdawn.slick.state.{StateBasedGame}
import org.newdawn.slick.util.InputAdapter

import game.IDMap._

class ControllerInput(gc: GameContainer, sbg: StateBasedGame) extends InputAdapter() {
    var input : Input = null
    var players : Array[TempPlayer] = Array()
	var controllerCount = 0

	val BUTTON_A = 1
	val BUTTON_B = 2
	val BUTTON_X = 3
	val BUTTON_Y = 4
	val BUTTON_LB = 5
	val BUTTON_RB = 6
	val BUTTON_BACK = 7
	val BUTTON_START = 8
	
	override def setInput(in: Input) = {
		in.addControllerListener(this)
		input = in
		controllerCount = in.getControllerCount()
		var i = 0
	    var colors = List(Color.red, Color.green, Color.yellow, Color.gray)
	    for(i <- 0 until controllerCount) {
	      println(s"Creating player with color ${colors.head}")
	      players = players :+ new TempPlayer(400,300,colors.head)
	      colors = colors.tail
	    }
	}

	override def controllerButtonPressed(controller: Int, button: Int) = {
		println(s"$button")
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
		}
	}

	def getPlayers() = players

	def update() = {
		if (!gc.isPaused) {
			var i = 0
			for (i <- 0 until controllerCount) {
				var j = 0
				players(i).y += input.getAxisValue(i,0)
				players(i).x += input.getAxisValue(i,1)
			}
		}
	}

}