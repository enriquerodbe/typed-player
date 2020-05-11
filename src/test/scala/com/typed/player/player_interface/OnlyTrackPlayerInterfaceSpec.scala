package com.typed.player.player_interface

import com.typed.player.models.{Queue, Track}
import com.typed.player.player_interface.PlayerInterfaceCommands._
import com.typed.player.player_interface.PlayerInterfaceReplies.Ok

class OnlyTrackPlayerInterfaceSpec extends PlayerInterfaceSpecs {

  private val testTrack = Track("test")
  val state: Queue = Queue.empty.enqueue(testTrack)
  def playerInterface = {
    val ref = testKit.spawn(PlayerInterface())
    ref ! EnqueueTrack(testTrack, testProbe.ref)
    testProbe.expectMessage(Ok(state))
    ref
  }

  "Player playing the only track" should behave like anyQueue(playerInterface, state)
  it should behave like nonEmptyQueue(playerInterface, state)
  it should behave like firstTrackQueue(playerInterface)
  it should behave like lastTrackQueue(playerInterface)
}
