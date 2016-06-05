package com.github.fellowship_of_the_bus
package eshe

import org.newdawn.slick.Sound

import lib.util.rand

import game._

object BossSFX {
  val powerRanger1 = new Sound("sfx/power-ranger1.wav")
  val powerRanger2 = new Sound("sfx/power-ranger2.wav")
  val powerRanger3 = new Sound("sfx/power-ranger3.wav")
  val horseMask1 = new Sound("sfx/horse-mask1.wav")
  val horseMask2 = new Sound("sfx/horse-mask2.wav")
  val horseMask3 = new Sound("sfx/horse-mask3.wav")
  val elsa1 = new Sound("sfx/elsa1.wav")
  val elsa2 = new Sound("sfx/elsa2.wav")
  val elsa3 = new Sound("sfx/elsa3.wav")
  val hotdog1 = new Sound("sfx/hotdog1.wav")
  val hotdog2 = new Sound("sfx/hotdog2.wav")
  val hotdog3 = new Sound("sfx/hotdog3.wav")
  val ghost1 = new Sound("sfx/ghost1.wav")
  val ghost2 = new Sound("sfx/ghost2.wav")
  val ghost3 = new Sound("sfx/ghost3.wav")

  trait Spawner[EnemyKind <: Enemy] {
    def soundList: Seq[Sound]
    def sound: Sound = rand(soundList)
    def apply(): List[EnemyKind]
    def apply(x: Float, y: Float): EnemyKind
    def spawnRandomly(n: Int): List[EnemyKind] = List.fill(n)({
      val enemy = apply(Game.spawnX, 0)
      enemy.y = Game.spawnY(enemy.height)
      enemy
    })
  }

  object GhostSpawner extends Spawner[Ghost] {
    val soundList = Vector(ghost1, ghost2, ghost3)
    def apply(x: Float, y: Float) = new Ghost(x, y)
    def apply() = spawnRandomly(3)
  }

  object ElsaSpawner extends Spawner[Elsa] {
    val soundList = Vector(elsa1, elsa2, elsa3)
    def apply(x: Float, y: Float) = new Elsa(x, y)
    def apply() = spawnRandomly(4)
  }

  object PowerRangerSpawner extends Spawner[PowerRanger] {
    val soundList = Vector(powerRanger1, powerRanger2, powerRanger3)
    def apply(x: Float, y: Float) = new PowerRanger(x, y)
    def apply() = spawnRandomly(5)
  }

  object HotdogSpawner extends Spawner[Hotdog] {
    val soundList = Vector(hotdog1, hotdog2, hotdog3)
    def apply(x: Float, y: Float) = new Hotdog(x, y)
    def apply() = spawnRandomly(2)
  }

  object HorseMaskSpawner extends Spawner[HorseMask] {
    val soundList = Vector(horseMask1, horseMask2, horseMask3)
    def apply(x: Float, y: Float) = new HorseMask(x, y)
    def apply() = spawnRandomly(20)
  }

  def random() = rand(spawners)
  val spawners = Vector(GhostSpawner, ElsaSpawner, PowerRangerSpawner, HotdogSpawner, HorseMaskSpawner)
}
