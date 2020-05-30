package com.typed.player.player_interface

import com.typed.player.models.{Queue, Track}
import com.typed.player.player_interface.PlayerInterfaceCommands.EnqueueTrack
import com.typed.player.player_interface.PlayerInterfaceReplies.Ok

class FirstTrackPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  val testTrack = Track("test")
  val state = Queue.empty.enqueue(testTrack).enqueue(testTrack)
  def playerInterface = {
    val ref = testKit.spawn(PlayerInterface())
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    playerInterfaceProbe.expectMessageType[Ok]
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    playerInterfaceProbe.expectMessage(Ok(state))
    ref
  }

  "Player playing the first track" should behave like anyQueue(playerInterface, state)
  it should behave like nonEmptyQueue(playerInterface, state)
  it should behave like queueWithFutureTracks(playerInterface, state)
  it should behave like firstTrackQueue(playerInterface)
}
