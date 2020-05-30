package com.typed.player.player_interface

import com.typed.player.models.{Queue, Track}
import com.typed.player.player_interface.PlayerInterfaceCommands.{EnqueueTrack, Skip}
import com.typed.player.player_interface.PlayerInterfaceReplies.Ok

class LastTrackPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  private val testTrack = Track("test")
  val state: Queue = Queue.empty.enqueue(testTrack).enqueue(testTrack).skip().skip()
  def playerInterface = {
    val ref = testKit.spawn(PlayerInterface())
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    ref ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
    ref ! Skip(playerInterfaceProbe.ref)
    ref ! Skip(playerInterfaceProbe.ref)
    playerInterfaceProbe.expectMessageType[Ok]
    playerInterfaceProbe.expectMessageType[Ok]
    playerInterfaceProbe.expectMessageType[Ok]
    playerInterfaceProbe.expectMessage(Ok(state))
    ref
  }

  "Player playing the last track" should behave like anyQueue(playerInterface, state)
  it should behave like nonEmptyQueue(playerInterface, state)
  it should behave like queueWithPastTracks(playerInterface, state)
  it should behave like lastTrackQueue(playerInterface)
}
