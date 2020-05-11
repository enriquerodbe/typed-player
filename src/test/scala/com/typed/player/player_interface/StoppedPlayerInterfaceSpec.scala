package com.typed.player.player_interface

import com.typed.player.models.Queue

class StoppedPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  def playerInterface = testKit.spawn(PlayerInterface())

  "Player stopped" should behave like anyQueue(playerInterface, Queue.empty)
  it should behave like emptyQueue(playerInterface)
}
