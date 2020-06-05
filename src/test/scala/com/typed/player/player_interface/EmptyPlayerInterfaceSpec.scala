package com.typed.player.player_interface

import com.typed.player.models.Player

class EmptyPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  def playerInterface = testKit.spawn(PlayerInterface())

  "Empty player" should behave like anyPlayer(playerInterface, Player.empty)
  it should behave like emptyPlayer(playerInterface)
}
