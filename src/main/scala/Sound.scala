package com.github.fellowship_of_the_bus
package eshe

import org.newdawn.slick.Sound

import lib.util.{rand,openFileAsStream}

import game._

object BossSFX {
  def makeSound(name: String) = {
    val path = s"sfx/${name}.wav"
    if (openFileAsStream(path) == null) {
      new Sound("sfx/default.wav")
    } else {
      new Sound(path)
    }
  }
  val powerRanger1 = makeSound("power-ranger1")
  val powerRanger2 = makeSound("power-ranger2")
  val powerRanger3 = makeSound("power-ranger3")
  val horseMask1 = makeSound("horse-mask1")
  val horseMask2 = makeSound("horse-mask2")
  val horseMask3 = makeSound("horse-mask3")
  val elsa1 = makeSound("elsa1")
  val elsa2 = makeSound("elsa2")
  val elsa3 = makeSound("elsa3")
  val hotdog1 = makeSound("hotdog1")
  val hotdog2 = makeSound("hotdog2")
  val hotdog3 = makeSound("hotdog3")
  val ghost1 = makeSound("ghost1")
  val ghost2 = makeSound("ghost2")
  val ghost3 = makeSound("ghost3")

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
