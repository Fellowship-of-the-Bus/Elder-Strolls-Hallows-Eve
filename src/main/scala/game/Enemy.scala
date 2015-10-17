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
