package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import lib.slick2d.ui.AbstractPane
import org.newdawn.slick.{Color}

class Pane(x: Float, y: Float, width: Float, height: Float)
          (implicit bg: Color) extends AbstractPane(x,y,width,height) {

  type Game = eshe.game.Game
}
