package com.github.fellowship_of_the_bus
package eshe.game

import lib.ui.{Image, Animation}

object IDMap{

  val IVGuyW1ID = 10
  val IVGuyW2ID = 11
  val IVGuyJumpID = 12
  val IVGuyKickID = 13
  val IVGuyArmID = 14
  val IVGuyArmPunchID = 15

  val GhostW1ID = 20
  val GhostW2ID = 21
  val ElsaID = 22
  val PowerRangerW1ID = 23
  val PowerRangerW2ID = 24

  val FotBLogoID = 1000
  val GameOverID = 1001
  val BackgroundID = 1002


  val imageMap = Map(
    IVGuyW1ID -> "img/IVWalk1.png",
    IVGuyW2ID -> "img/IVWalk2.png",
    IVGuyJumpID -> "img/Jump.png",
    IVGuyKickID -> "img/KickFull.png",
    IVGuyArmID -> "img/ArmDefault.png",
    IVGuyArmPunchID -> "img/ArmPunch.png",
    GhostW1ID -> "img/GhostRun1.png",
    GhostW2ID -> "img/GhostRun2.png",
    ElsaID -> "img/Elsa.png",
    PowerRangerW1ID -> "img/PowerRangerRun1.png",
    PowerRangerW2ID -> "img/PowerRangerRun2.png",
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
