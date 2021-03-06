package com.typed.player.player_interface

import com.typed.player.models.{Player, Track}
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands.{EnqueueTrack, Skip}
import com.typed.player.player_interface.protocol.PlayerInterfaceReplies.Ok

class LastTrackPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  private val testTrack = Track("test")
  val state: Player = Player.empty.enqueue(testTrack).enqueue(testTrack).skip()
  def playerInterface = {
    val ref = testKit.spawn(PlayerInterface())
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    ref ! Skip(playerInterfaceProbe.ref)
    playerInterfaceProbe.expectMessageType[Ok]
    playerInterfaceProbe.expectMessageType[Ok]
    playerInterfaceProbe.expectMessage(Ok(state))
    ref
  }

  "Last track player" should behave like anyPlayer(playerInterface, state)
  it should behave like nonEmptyPlayer(playerInterface, state)
  it should behave like playerWithPastTracks(playerInterface, state)
  it should behave like lastTrackPlayer(playerInterface)
}
