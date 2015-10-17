package com.github.fellowship_of_the_bus
package eshe
package game
import IDMap._
import lib.game.GameConfig.{Width}

import java.util.Scanner
import java.io.File

import lib.util.rand

trait EnemyType extends CharacterType {

}

object Enemy {
  private lazy val names = read("names.txt")
  private lazy val facts = read("true-facts.txt")

  def read(filename: String): List[String] = {
    var lst = List[String]()
    val sc = new Scanner(new File(filename))
    while (sc.hasNextLine) {
      lst = sc.nextLine::lst 
    }
    lst
  }

  def name() = names(rand(names.length-1))
  def fact() = facts(rand(facts.length-1))  
}

abstract class Enemy(xc: Float, yc: Float, override val base: EnemyType) extends game.Character(xc, yc, base) {
  val age: Int = rand(10, 15)

  val name = Enemy.name
  val fact = Enemy.fact

  def move() = {
    
  }
}

object Ghost extends EnemyType {
  val id = GhostID
  val maxHp = 10
  val attack = 2
  val defense = 1 
  val speed = 1
}

class Ghost(xc: Float, yc: Float) extends Enemy(xc, yc, Ghost) {
  
}

object Elsa extends EnemyType {
  val id = ElsaID
  val maxHp = 15
  val attack = 4
  val defense = 3 
  val speed = 2
}

class Elsa(xc: Float, yc: Float) extends Enemy(xc, yc, Elsa) {

}

object PowerRanger extends EnemyType {
  val id = PowerRangerID
  val maxHp = 20
  val attack = 10
  val defense = 6 
  val speed = 4
}

class PowerRanger(xc: Float, yc: Float) extends Enemy(xc, yc, PowerRanger) {

}
