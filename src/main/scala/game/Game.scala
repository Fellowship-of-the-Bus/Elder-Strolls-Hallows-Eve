package com.github.fellowship_of_the_bus
package eshe
package game
import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException,Color, Input}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import IDMap._
import lib.game.GameConfig.{Height,Width}

class Game extends lib.game.Game {
  var counter = 0

  val maxPlayers = 4
  val players = new Array[Player](maxPlayers)
  for (i <- 0 until maxPlayers) {
    players(i) = new IVGuy(0, 0)
  }

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    implicit val input = gc.getInput

  }
}
