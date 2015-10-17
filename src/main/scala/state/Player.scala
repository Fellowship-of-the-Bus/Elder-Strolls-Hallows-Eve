package com.github.fellowship_of_the_bus
package eshe
package state

import lib.ui.{Button, ToggleButton}
import game.IDMap._
import lib.game.GameConfig
import lib.game.GameConfig.{Width,Height}

import org.newdawn.slick.{GameContainer, Graphics, Color, Input, KeyListener}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}



class Player(px: Float, py: Float, pc: Color) {
  var x: Float = px
  var y: Float = py
  var color: Color = pc
}