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
  val IVGuyDodgeID = 152
  val IVGuy2DodgeID = 153
  val IVGuy3DodgeID = 154
  val IVGuy4DodgeID = 155

  val GhostW1ID = 200
  val GhostW2ID = 201
  val GhostKnockbackID = 202
  val GhostWindupID = 203
  val GhostKickID = 204

  val ElsaID = 210
  val ElsaShootID = 211
  val ElsaKnockbackID = 212

  val HotdogW1ID = 220
  val HotdogW2ID = 221
  val HotdogKnockbackID = 222

  val PowerRangerW1ID = 230
  val PowerRangerW2ID = 231
  val PowerRangerKnockbackID = 232
  val PowerRangerPunchID = 233

  val PowerRangerW1BlueID = 240
  val PowerRangerW2BlueID = 241
  val PowerRangerKnockbackBlueID = 242
  val PowerRangerPunchBlueID = 243

  val PowerRangerW1GreenID = 250
  val PowerRangerW2GreenID = 251
  val PowerRangerKnockbackGreenID = 252
  val PowerRangerPunchGreenID = 253

  val PowerRangerW1YellowID = 260
  val PowerRangerW2YellowID = 261
  val PowerRangerKnockbackYellowID = 262
  val PowerRangerPunchYellowID = 263

  val PowerRangerW1PinkID = 270
  val PowerRangerW2PinkID = 271
  val PowerRangerKnockbackPinkID = 272
  val PowerRangerPunchPinkID = 273

  val PowerRangerW1BlackID = 280
  val PowerRangerW2BlackID = 281
  val PowerRangerKnockbackBlackID = 282
  val PowerRangerPunchBlackID = 283

  val HorseMaskID = 40

  val FotBLogoID = 1000
  val GameOverID = 1001
  val BackgroundID = 1002
  val LogoID = 1003
  val ScrollArrowID = 1004
  val BackgroundFullID = 1005
  val SelectArrow = 1006

  val ElsaShotID = 10000
  val KetchupID = 10001

  val BossFullID = 20000
  val BossUncoatID = 20001
  val BossUncoatAttackID = 20002
  val BossUncoatAttackLegID = 20003
  val BossUncoatWalkID = 20004
  val BossFullAttackID = 20005
  val BossFullSuperSoakerID = 20006
  val BossCellphoneID = 20007
  val BossCellphoneAttackID = 20008
  val BossCellphoneWalkID = 20008
  val BossFinalID = 20009
  val BossFinalAttackID = 20010
  val BossFinalWalkID = 20011
  val TrenchcoatID = 20012
  val BottomGuyID = 20013
  val MiddleGuyID = 20014
  val TopGuyID = 20015

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
    IVGuyDodgeID -> "img/Dodge.png",
    IVGuy2DodgeID -> "img/P2Dodge.png",
    IVGuy3DodgeID -> "img/P3Dodge.png",
    IVGuy4DodgeID -> "img/P4Dodge.png",
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
    PowerRangerW1BlueID -> "img/PowerRangerRun1Blue.png",
    PowerRangerW2BlueID -> "img/PowerRangerRun2Blue.png",
    PowerRangerKnockbackBlueID -> "img/PowerRangerKnockbackBlue.png",
    PowerRangerPunchBlueID -> "img/PowerRangerPunchBlue.png",
    PowerRangerW1GreenID -> "img/PowerRangerRun1Green.png",
    PowerRangerW2GreenID -> "img/PowerRangerRun2Green.png",
    PowerRangerKnockbackGreenID -> "img/PowerRangerKnockbackGreen.png",
    PowerRangerPunchGreenID -> "img/PowerRangerPunchGreen.png",
    PowerRangerW1YellowID -> "img/PowerRangerRun1Yellow.png",
    PowerRangerW2YellowID -> "img/PowerRangerRun2Yellow.png",
    PowerRangerKnockbackYellowID -> "img/PowerRangerKnockbackYellow.png",
    PowerRangerPunchYellowID -> "img/PowerRangerPunchYellow.png",
    PowerRangerW1PinkID -> "img/PowerRangerRun1Pink.png",
    PowerRangerW2PinkID -> "img/PowerRangerRun2Pink.png",
    PowerRangerKnockbackPinkID -> "img/PowerRangerKnockbackPink.png",
    PowerRangerPunchPinkID -> "img/PowerRangerPunchPink.png",
    PowerRangerW1BlackID -> "img/PowerRangerRun1Black.png",
    PowerRangerW2BlackID -> "img/PowerRangerRun2Black.png",
    PowerRangerKnockbackBlackID -> "img/PowerRangerKnockbackBlack.png",
    PowerRangerPunchBlackID -> "img/PowerRangerPunchBlack.png",
    HorseMaskID -> "img/HorseMask.png",
    HotdogW1ID -> "img/HotdogWalk1.png",
    HotdogW2ID -> "img/HotdogWalk2.png",
    HotdogKnockbackID -> "img/HotdogKnockback.png",

    BossFullID -> "img/BossFull.png",
    BossUncoatID -> "img/BossUncoat.png",
    BossUncoatAttackID -> "img/BossUncoatAttack.png",
    BossUncoatAttackLegID -> "img/BossUncoatAttackLeg.png",
    BossUncoatWalkID -> "img/BossUncoatWalk.png",
    BossFullAttackID -> "img/Water.png",
    BossFullSuperSoakerID -> "img/SuperSoaker.png",
    BossCellphoneID -> "img/BossUncoat2.png",
    BossCellphoneAttackID -> "img/BossUncoat2.png",
    BossCellphoneWalkID -> "img/BossUncoat2Walk.png",
    BossFinalID -> "img/BossFinal.png",
    BossFinalAttackID -> "img/BossFinalAttack.png",
    BossFinalWalkID -> "img/BossFinalWalk.png",
    TrenchcoatID -> "img/Trenchcoat.png",
    BottomGuyID -> "img/BottomGuy.png",
    MiddleGuyID -> "img/MiddleGuy.png",
    TopGuyID -> "img/TopGuy.png",

    FotBLogoID -> "img/FotB-Logo.png",
    GameOverID -> "img/GameOver.png",
    BackgroundID -> "img/BackGround.png",
    BackgroundFullID -> "img/BackGroundFull.png",
    LogoID -> "img/GameLogo.png",
    ScrollArrowID -> "img/Arrow.png",
    SelectArrow -> "img/SelectionArrow.png",

    ElsaShotID -> "img/Elsa_Projectile.png",
    KetchupID -> "img/Ketchup.png",
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
