package com.github.fellowship_of_the_bus
package eshe
import java.util.logging.{Level, Logger}
import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException,Color, Input, Image}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import state._
import game._
import lib.util.Native
import lib.game.GameConfig

class ElderStrolls(gamename: String) extends StateBasedGame(gamename) {
  def initStatesList(gc: GameContainer) = {
    gc.setShowFPS(true)
    addState(Menu)
    addState(Battle)
    addState(Options)
  }
}

object ElderStrolls extends App {
  def makeImg(loc: String) = new Image(loc)

  GameConfig.FrameRate = 60

  def calculateScreenSize(width: Int, height: Int): (Int, Int) = {
    // calculate maximum screen size using desired aspect ratio and known width and height.
    val aspect = 16.0/9
    val newWidth = (height * aspect).toInt
    if (width < newWidth) {
      (width, (width/aspect).toInt)
    } else {
      (newWidth, height)
    }
  }

  try {
    import GameConfig._
    import scala.math.min
    Native.loadLibraryFromJar()
    val appgc = new AppGameContainer(new ElderStrolls("Elder Strolls: Hallow's Eve"))
    val (w, h) = calculateScreenSize(appgc.getScreenWidth, appgc.getScreenHeight)
    Width = w
    Height = h
    appgc.setDisplayMode(Width, Height, false)
    appgc.setTargetFrameRate(FrameRate)
    appgc.setVSync(if (GameConfig.OS == GameConfig.MacOS) false else true)
    appgc.start()
  } catch {
    case ex: SlickException => Logger.getLogger(ElderStrolls.getClass.getName()).log(Level.SEVERE, null, ex)
    case t: Throwable =>
      println("Library path is: " + System.getProperty("java.library.path"))
      t.printStackTrace
  }
}
