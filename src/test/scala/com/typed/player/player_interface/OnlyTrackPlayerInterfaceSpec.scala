package com.typed.player.player_interface

import com.typed.player.models.{Player, Track}
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands._
import com.typed.player.player_interface.protocol.PlayerInterfaceReplies.Ok

class OnlyTrackPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  private val testTrack = Track("test")
  val state: Player = Player.empty.enqueue(testTrack)
  def playerInterface = {
    val ref = testKit.spawn(PlayerInterface())
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    playerInterfaceProbe.expectMessage(Ok(state))
    ref
  }

  "Only track player" should behave like anyPlayer(playerInterface, state)
  it should behave like nonEmptyPlayer(playerInterface, state)
  it should behave like firstTrackPlayer(playerInterface)
  it should behave like lastTrackPlayer(playerInterface)
}
