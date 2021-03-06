package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import org.newdawn.slick.{GameContainer, Graphics, Color, Input}
import org.newdawn.slick.state.{StateBasedGame}
import org.newdawn.slick.util.InputAdapter

import game.GameObject

import game.IDMap._

class ControllerInput(g: game.Game, gc: GameContainer, sbg: StateBasedGame) extends InputAdapter() {
  var input : Input = null
  var game = g

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
  lazy val BUTTON_UP = OS match {
    case MacOS => 5
    case _ => -1
  }
  lazy val BUTTON_LEFT = OS match {
    case MacOS => 8
    case _ => -1
  }
  lazy val BUTTON_DOWN = OS match {
    case MacOS => 7
    case _ => -1
  }
  lazy val BUTTON_RIGHT = OS match {
    case MacOS => 6
    case _ => -1
  }
  var controllers: Vector[(Int, Int)] = Vector()
  override def setInput(in: Input) = {
    if (input != null) {
      input.removeControllerListener(this)
      input.removeKeyListener(this)
    }
    input = in
    input.addControllerListener(this)
    val controllerCount = input.getControllerCount()
    controllers = Vector()
    for (i <- 0 until controllerCount) {
      if (input.getAxisCount(i) >= 2) {
        controllers = controllers :+ ((i, controllers.length))
      }
    }
    if (controllers.length == 0) {
      input.addKeyListener(this)
      game.setPlayers(1)
    } else {
      game.setPlayers(controllers.length)
    }
  }

  override def controllerButtonPressed(controller: Int, button: Int) = {
    if (button == BUTTON_START) {
      gc.setPaused(!gc.isPaused)
      game.pause(gc.isPaused)
    }
    else if (button == BUTTON_BACK) {
      sbg.enterState(Mode.MenuID)
      Battle.newGame()
    }
    if (!gc.isPaused) {
      if (sbg.getCurrentStateID == Mode.MenuID || sbg.getCurrentStateID == Mode.OptionsID) {
        // menu controls
        val state = sbg.getCurrentState().asInstanceOf[MenuState]
        if (button == BUTTON_A) {
          state.confirm()
        } else if (button == BUTTON_B) {
          state.back()
        } else if (button == BUTTON_UP) {
          state.previous()
        } else if (button == BUTTON_DOWN) {
          state.next()
        }
      } else if (game.players(controller).active && !game.players(controller).dodging && game.players(controller).imgs.indexOf(game.players(controller).img) != -1) {
        // game controls
        if (button == BUTTON_A) {
          game.players(controller).tryAttack(game)
        } else if (button == BUTTON_B) {
          //if (game.players(controller).imgs.indexOf(game.players(controller).img) != -1) {
            game.players(controller).tryAttack2(game)
          //}
        } else if (button == BUTTON_X) {
          val p = game.players(controller)
          p.dodgeDirX = input.getAxisValue(controller,AXIS_X)
          p.dodgeDirY = input.getAxisValue(controller,AXIS_Y)
          if (p.dodgeDirX == 0f && p.dodgeDirY == 0f) {
            p.dodgeDirX = if (p.direction == GameObject.Left) -1f else 1f
          }
          p.dodge(game)
        }
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
      for ((cnum,pnum) <- controllers) {
        val p = game.players(pnum)
        if (p.active) {
          if (p.dodging) p.move(p.speed * p.dodgeDirX * p.dodgeSpeed, p.speed * p.dodgeDirY * p.dodgeSpeed)
          else p.move(p.speed*input.getAxisValue(cnum,AXIS_X),p.speed*input.getAxisValue(cnum,AXIS_Y))
        }
      }

      if (controllers.length == 0) {
        // support single player if there are no controllers attached
        val p = game.players(0)
        if (p.dodging) p.move(p.speed * p.dodgeDirX * p.dodgeSpeed, p.speed * p.dodgeDirY * p.dodgeSpeed)
        else p.move(p.speed*horizontal, p.speed*vertical)
      }
    }
  }

  var horizontal = 0
  var vertical = 0
  override def keyPressed(key: Int, c: Char) = {
    import Input._
    key match {
      // movement
      case KEY_LEFT => horizontal += -1
      case KEY_RIGHT => horizontal += 1
      case KEY_UP => vertical += -1
      case KEY_DOWN => vertical += 1
      case KEY_ESCAPE => sbg.enterState(Mode.MenuID)
      // pause/unpause
      case KEY_P => gc.setPaused(!gc.isPaused)

      case _ => ()
    }

    if (!gc.isPaused) {
      if (sbg.getCurrentStateID == Mode.MenuID || sbg.getCurrentStateID == Mode.OptionsID) {
        // menu controls
        val state = sbg.getCurrentState().asInstanceOf[MenuState]
        if (key == KEY_A) {
          state.confirm()
        } else if (key == KEY_ESCAPE) {
          state.back()
        } else if (key == KEY_UP) {
          state.previous()
        } else if (key == KEY_DOWN) {
          state.next()
        }
      } else {
        val player = game.players(0)
        // game controls
        if (player.active && !player.dodging && player.imgs.indexOf(player.img) != -1) {

          key match {
            // punch/confirm button
            case Input.KEY_A =>
              player.tryAttack(game)

            // kick/cancel button
            case Input.KEY_S =>
              player.tryAttack2(game)

            case Input.KEY_D =>
              player.dodgeDirX = horizontal
              player.dodgeDirY = vertical
              if (player.dodgeDirX == 0f && player.dodgeDirY == 0f) {
                player.dodgeDirX = if (player.direction == GameObject.Left) -1f else 1f
              }
              player.dodge(game)

            case _ => ()
          }
        }
      }
    }
  }

  override def keyReleased(key: Int, c: Char) = {
    key match {
      case Input.KEY_LEFT => horizontal -= -1
      case Input.KEY_RIGHT => horizontal -= 1
      case Input.KEY_UP => vertical -= -1
      case Input.KEY_DOWN => vertical -= 1
      case _ => ()
    }
  }
}
