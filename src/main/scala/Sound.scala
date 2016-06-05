package com.github.fellowship_of_the_bus
package eshe

import org.newdawn.slick.Music

import lib.util.{rand,openFileAsStream}

import game._
import state.ui.GameArea

object BossSFX {
  def makeMusic(name: String) = {
    val path = s"sfx/${name}.wav"
    if (openFileAsStream(path) == null) {
      default
    } else {
      new Music(path)
    }
  }
  val default = new Music("sfx/default.wav")
  val powerRanger1 = makeMusic("power-ranger1")
  val powerRanger2 = makeMusic("power-ranger2")
  val powerRanger3 = makeMusic("power-ranger3")
  val horseMask1 = makeMusic("horse-mask1")
  val horseMask2 = makeMusic("horse-mask2")
  val horseMask3 = makeMusic("horse-mask3")
  val elsa1 = makeMusic("elsa1")
  val elsa2 = makeMusic("elsa2")
  val elsa3 = makeMusic("elsa3")
  val hotdog1 = makeMusic("hotdog1")
  val hotdog2 = makeMusic("hotdog2")
  val hotdog3 = makeMusic("hotdog3")
  val ghost1 = makeMusic("ghost1")
  val ghost2 = makeMusic("ghost2")
  val ghost3 = makeMusic("ghost3")

  trait Spawner[EnemyKind <: Enemy] {
    def isHorse = false
    def musicList: Seq[Music]
    var curMusic = 0
    def music: Music = {
      curMusic = (curMusic+1)%musicList.length
      musicList(curMusic)
    }
    def apply(): List[EnemyKind]
    def apply(x: Float, y: Float): EnemyKind
    def spawnRandomly(n: Int): List[EnemyKind] = List.fill(n)({
      val enemy = apply(0, 0)
      enemy.x = Game.spawnX(enemy.width, isHorse)
      enemy.y = Game.spawnY(enemy.height)
      enemy
    })
  }

  object GhostSpawner extends Spawner[Ghost] {
    val musicList = Vector(ghost1, ghost2, ghost3)
    def apply(x: Float, y: Float) = new Ghost(x, y, 0)
    def apply() = spawnRandomly(3)
  }

  object ElsaSpawner extends Spawner[Elsa] {
    val musicList = Vector(elsa1, elsa2, elsa3)
    def apply(x: Float, y: Float) = new Elsa(x, y, 0)
    def apply() = spawnRandomly(4)
  }

  object PowerRangerSpawner extends Spawner[PowerRanger] {
    val musicList = Vector(powerRanger1, powerRanger2, powerRanger3)
    def apply(x: Float, y: Float) = new PowerRanger(x, y, 0)
    def apply() = spawnRandomly(5)
  }

  object HotdogSpawner extends Spawner[Hotdog] {
    val musicList = Vector(hotdog1, hotdog2, hotdog3)
    def apply(x: Float, y: Float) = new Hotdog(x, y, 0)
    def apply() = spawnRandomly(2)
  }

  object HorseMaskSpawner extends Spawner[HorseMask] {
    override def isHorse = true
    val musicList = Vector(horseMask1, horseMask2, horseMask3)
    def apply(x: Float, y: Float) = new HorseMask(x, y, 0)
    def apply() = spawnRandomly(20)
  }

  def random() = rand(spawners)
  val spawners = Vector(GhostSpawner, ElsaSpawner, PowerRangerSpawner, HotdogSpawner, HorseMaskSpawner)
}
