package com.typed.player.player_interface

import com.typed.player.models.Player

class StoppedPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  def playerInterface = testKit.spawn(PlayerInterface())

  "Stopped player" should behave like anyPlayer(playerInterface, Player.empty)
  it should behave like emptyPlayer(playerInterface)
}
