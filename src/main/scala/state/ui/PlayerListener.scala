package com.github.fellowship_of_the_bus
package eshe
package state
package ui

import eshe.game.{Player,Enemy}

trait PlayerListener {
  def enemyDied(e: Enemy): Unit = ()
  def playerDied(p: Player): Unit = ()
}
