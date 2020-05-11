package com.reactive.player.player_interface

import com.reactive.player.models.{Queue, Track}
import com.reactive.player.player_interface.PlayerInterfaceCommands.EnqueueTrack
import com.reactive.player.player_interface.PlayerInterfaceReplies.Ok

class FirstTrackPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  val testTrack = Track("test")
  val state: Queue = Queue.empty.enqueue(testTrack).enqueue(testTrack)
  def playerInterface = {
    val ref = testKit.spawn(PlayerInterface())
    ref ! EnqueueTrack(testTrack, testProbe.ref)
    testProbe.expectMessageType[Ok]
    ref ! EnqueueTrack(testTrack, testProbe.ref)
    testProbe.expectMessage(Ok(state))
    ref
  }

  "Player playing the first track" should behave like anyQueue(playerInterface, state)
  it should behave like nonEmptyQueue(playerInterface, state)
  it should behave like queueWithFutureTracks(playerInterface, state)
  it should behave like firstTrackQueue(playerInterface)
}
