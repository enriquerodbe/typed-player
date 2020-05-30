package com.typed.player.player_interface

import com.typed.player.models.{Player, Track}
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands.EnqueueTrack
import com.typed.player.player_interface.protocol.PlayerInterfaceReplies.Ok

class FirstTrackPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  val testTrack = Track("test")
  val state = Player.empty.enqueue(testTrack).enqueue(testTrack)
  def playerInterface = {
    val ref = testKit.spawn(PlayerInterface())
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    playerInterfaceProbe.expectMessageType[Ok]
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    playerInterfaceProbe.expectMessage(Ok(state))
    ref
  }

  "First track player" should behave like anyPlayer(playerInterface, state)
  it should behave like nonEmptyPlayer(playerInterface, state)
  it should behave like playerWithFutureTracks(playerInterface, state)
  it should behave like firstTrackPlayer(playerInterface)
}
