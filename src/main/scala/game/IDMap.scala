package com.github.fellowship_of_the_bus
package eshe.game

import lib.ui.{Image, Animation}

object IDMap{

  val IVGuyID = 10

  val GhostID = 20
  val ElsaID = 21
  val PowerRangerID = 22

  val FotBLogoID = 1000
  val GameOverID = 1001
  val BackgroundID = 1002


  val imageMap = Map(
    IVGuyID -> Array("img/IVWalk1.png", "img/IVWalk2.png"),
    GhostID -> Array("img/GhostRun1.png", "img/GhostRun2.png"),
    ElsaID -> "img/Elsa.png",
    FotBLogoID -> "img/FotB-Logo.png",
    GameOverID -> "img/GameOver.png",
    BackgroundID -> "img/BackGround.png",
    12345 -> Array("img/GameOver.png", "img/FotB-Logo.png")
  )

  lazy val images: Map[Int, lib.ui.Drawable] = imageMap.map { x =>
    val (id, loc) = x
    val img = loc match {
      case xs: Array[String] => Animation(xs, eshe.state.ui.GameArea.scaleFactor)
      case str: String => Image(str, eshe.state.ui.GameArea.scaleFactor)
    }
    id -> img
  }
}
