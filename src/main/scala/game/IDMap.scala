package com.github.fellowship_of_the_bus
package eshe.game

import lib.ui.{Image, Animation}

object IDMap{

  val FotBLogoID = 1000
  val GameOverID = 1001

  val imageMap = Map(
    FotBLogoID -> "img/FotB-Logo.png",
    GameOverID -> "img/GameOver.png",
    12345 -> Array("img/GameOver.png", "img/FotB-Logo.png")
  )

  lazy val images: Map[Int, lib.ui.Drawable] = imageMap.map { x =>
    val (id, loc) = x
    val img = loc match {
      case xs: Array[String] => Animation(xs)
      case str: String => Image(str)
    }
    id -> img
  }
}
