package com.reactive.player.player_interface

import com.reactive.player.models.Queue

class StoppedPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  def playerInterface = testKit.spawn(PlayerInterface())

  "Player stopped" should behave like anyQueue(playerInterface, Queue.empty)
  it should behave like emptyQueue(playerInterface)
}
