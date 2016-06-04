package com.github.fellowship_of_the_bus
package eshe.game

import lib.slick2d.ui.{Image, Animation, Drawable}

object IDMap{

  val IVGuyW1ID = 100
  val IVGuyW2ID = 101
  val IVGuyJumpID = 102
  val IVGuyKickID = 103
  val IVGuy2W1ID = 105
  val IVGuy2W2ID = 106
  val IVGuy2JumpID = 107
  val IVGuy2KickID = 108
  val IVGuy3W1ID = 109
  val IVGuy3W2ID = 110
  val IVGuy3JumpID = 111
  val IVGuy3KickID = 112
  val IVGuy4W1ID = 113
  val IVGuy4W2ID = 114
  val IVGuy4JumpID = 115
  val IVGuy4KickID = 116
  val IVGuyArmID = 150
  val IVGuyArmPunchID = 151

  val GhostW1ID = 200
  val GhostW2ID = 201
  val GhostKnockbackID = 202
  val GhostWindupID = 203
  val GhostKickID = 204

  val ElsaID = 210
  val ElsaShootID = 211
  val ElsaKnockbackID = 212

  val PowerRangerW1ID = 220
  val PowerRangerW2ID = 221
  val PowerRangerKnockbackID = 222
  val PowerRangerPunchID = 223

  val HotdogW1ID = 230
  val HotdogW2ID = 231

  val HorseMaskID = 40

  val FotBLogoID = 1000
  val GameOverID = 1001
  val BackgroundID = 1002
  val LogoID = 1003
  val ScrollArrowID = 1004

  val ElsaShotID = 10000

  val BossFullID = 20000
  val BossUncoatID = 20001
  val BossUncoatAttackID = 20002
  val BossUncoatAttackLegID = 20003
  val BossUncoatWalkID = 20004
  val BossFullAttackID = 20005
  val BossFullSuperSoakerID = 20006

  val imageMap = Map(
    IVGuyW1ID -> "img/IVWalk1.png",
    IVGuyW2ID -> "img/IVWalk2.png",
    IVGuyJumpID -> "img/Jump.png",
    IVGuyKickID -> "img/KickFull.png",
    IVGuy2W1ID -> "img/P2IVWalk1.png",
    IVGuy2W2ID -> "img/P2IVWalk2.png",
    IVGuy2JumpID -> "img/P2Jump.png",
    IVGuy2KickID -> "img/P2KickFull.png",
    IVGuy3W1ID -> "img/P3IVWalk1.png",
    IVGuy3W2ID -> "img/P3IVWalk2.png",
    IVGuy3JumpID -> "img/P3Jump.png",
    IVGuy3KickID -> "img/P3KickFull.png",
    IVGuy4W1ID -> "img/P4IVWalk1.png",
    IVGuy4W2ID -> "img/P4IVWalk2.png",
    IVGuy4JumpID -> "img/P4Jump.png",
    IVGuy4KickID -> "img/P4KickFull.png",
    IVGuyArmID -> "img/ArmDefault.png",
    IVGuyArmPunchID -> "img/ArmPunch.png",
    GhostW1ID -> "img/GhostRun1.png",
    GhostW2ID -> "img/GhostRun2.png",
    GhostWindupID -> "img/GhostWindup.png",
    GhostKnockbackID -> "img/GhostKnockback.png",
    GhostKickID -> "img/GhostKick.png",
    ElsaID -> "img/Elsa.png",
    ElsaShootID -> "img/ElsaShoot.png",
    ElsaKnockbackID -> "img/ElsaKnockback.png",
    PowerRangerW1ID -> "img/PowerRangerRun1.png",
    PowerRangerW2ID -> "img/PowerRangerRun2.png",
    PowerRangerKnockbackID -> "img/PowerRangerKnockback.png",
    PowerRangerPunchID -> "img/PowerRangerPunch.png",
    HorseMaskID -> "img/HorseMask.png",
    HotdogW1ID -> "img/HotdogWalk1.png",
    HotdogW2ID -> "img/HotdogWalk2.png",
    BossFullID -> "img/BossFull.png",
    BossUncoatID -> "img/BossUncoat.png",
    BossUncoatAttackID -> "img/BossUncoatAttack.png",
    BossUncoatAttackLegID -> "img/BossUncoatAttackLeg.png",
    BossUncoatWalkID -> "img/BossUncoatWalk.png",
    BossFullAttackID -> "img/Water.png",
    BossFullSuperSoakerID -> "img/SuperSoaker.png",
    FotBLogoID -> "img/FotB-Logo.png",
    GameOverID -> "img/GameOver.png",
    BackgroundID -> "img/BackGround.png",
    LogoID -> "img/Strolls.png",
    ScrollArrowID -> "img/Arrow.png",
    ElsaShotID -> "img/Elsa_Projectile.png",
    12345 -> Array("img/GameOver.png", "img/FotB-Logo.png")
  )

  lazy val images: Map[Int, Drawable] = imageMap.map { x =>
    val (id, loc) = x
    val img = loc match {
      case xs: Array[String] => Animation(xs, eshe.state.ui.GameArea.scaleFactor)
      case str: String => Image(str, eshe.state.ui.GameArea.scaleFactor)
    }
    id -> img
  }
  images(BackgroundID).scaleFactor = 1.0f
}
