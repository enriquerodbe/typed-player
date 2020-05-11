package com.typed.player.player_interface

import akka.actor.typed.ActorRef
import com.typed.player.BaseSpec
import com.typed.player.models.{Queue, Track}
import com.typed.player.player_interface.PlayerInterfaceCommands._
import com.typed.player.player_interface.PlayerInterfaceReplies.{Error, Ok}

trait PlayerInterfaceSpecs extends BaseSpec {

  def anyQueue(playerInterface: => ActorRef[Command], state: Queue): Unit = {
    it should "accept EnqueueTrack command" in {
      val testTrack = Track("testing")
      playerInterface ! EnqueueTrack(testTrack, testProbe.ref)
      testProbe.expectMessage(Ok(state.enqueue(testTrack)))
    }

    it should "accept ToggleShuffle command" in {
      playerInterface ! ToggleShuffle(testProbe.ref)
      testProbe.expectMessage(Ok(state.toggleShuffle()))
    }
  }

  def emptyQueue(playerInterface: => ActorRef[Command]): Unit = {
    it should "reject TogglePlay command" in {
      playerInterface ! TogglePlay(testProbe.ref)
      testProbe.expectMessageType[Error]
    }
    it should "reject Skip command" in {
      playerInterface ! Skip(testProbe.ref)
      testProbe.expectMessageType[Error]
    }
    it should "reject SkipBack command" in {
      playerInterface ! SkipBack(testProbe.ref)
      testProbe.expectMessageType[Error]
    }
    it should "reject Stop command" in {
      playerInterface ! Stop(testProbe.ref)
      testProbe.expectMessageType[Error]
    }
  }

  def nonEmptyQueue(playerInterface: => ActorRef[Command], state: Queue): Unit = {
    it should "accept TogglePlay command" in {
      playerInterface ! TogglePlay(testProbe.ref)
      testProbe.expectMessage(Ok(state.togglePlay()))
    }

    it should "accept Stop command" in {
      playerInterface ! Stop(testProbe.ref)
      testProbe.expectMessage(Ok(state))
    }
  }

  def queueWithFutureTracks(playerInterface: => ActorRef[Command], state: Queue): Unit = {
    it should "accept Skip command" in {
      playerInterface ! Skip(testProbe.ref)
      testProbe.expectMessage(Ok(state.skip()))
    }
  }

  def queueWithPastTracks(playerInterface: => ActorRef[Command], state: Queue): Unit = {
    it should "accept SkipBack command" in {
      playerInterface ! SkipBack(testProbe.ref)
      testProbe.expectMessage(Ok(state.skipBack()))
    }
  }

  def lastTrackQueue(playerInterface: ActorRef[Command]): Unit = {
    it should "reject Skip command" in {
      playerInterface ! Skip(testProbe.ref)
      testProbe.expectMessageType[Error]
    }
  }

  def firstTrackQueue(playerInterface: ActorRef[Command]): Unit = {
    it should "reject SkipBack command" in {
      playerInterface ! SkipBack(testProbe.ref)
      testProbe.expectMessageType[Error]
    }
  }
}
