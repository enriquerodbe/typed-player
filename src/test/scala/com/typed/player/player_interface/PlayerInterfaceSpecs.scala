package com.typed.player.player_interface

import akka.actor.typed.ActorRef
import com.typed.player.BaseSpec
import com.typed.player.models.{Player, Track}
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands._
import com.typed.player.player_interface.protocol.PlayerInterfaceReplies.{Error, Ok}

trait PlayerInterfaceSpecs extends BaseSpec {

  def anyPlayer(playerInterface: => ActorRef[Command], state: Player): Unit = {
    it should "accept EnqueueTrack command" in {
      val testTrack = Track("testing")
      playerInterface ! EnqueueTrack(testTrack, playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessage(Ok(state.enqueue(testTrack)))
    }

    it should "accept ToggleShuffle command" in {
      playerInterface ! ToggleShuffle(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessage(Ok(state.toggleShuffle()))
    }
  }

  def emptyPlayer(playerInterface: => ActorRef[Command]): Unit = {
    it should "reject TogglePlay command" in {
      playerInterface ! TogglePlay(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessageType[Error]
    }
    it should "reject Skip command" in {
      playerInterface ! Skip(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessageType[Error]
    }
    it should "reject SkipBack command" in {
      playerInterface ! SkipBack(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessageType[Error]
    }
    it should "reject Stop command" in {
      playerInterface ! Stop(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessageType[Error]
    }
  }

  def nonEmptyPlayer(playerInterface: => ActorRef[Command], state: Player): Unit = {
    it should "accept TogglePlay command" in {
      playerInterface ! TogglePlay(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessage(Ok(state.togglePlay()))
    }

    it should "accept Stop command" in {
      playerInterface ! Stop(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessage(Ok(state))
    }
  }

  def playerWithFutureTracks(playerInterface: => ActorRef[Command], state: Player): Unit = {
    it should "accept Skip command" in {
      playerInterface ! Skip(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessage(Ok(state.skip()))
    }
  }

  def playerWithPastTracks(playerInterface: => ActorRef[Command], state: Player): Unit = {
    it should "accept SkipBack command" in {
      playerInterface ! SkipBack(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessage(Ok(state.skipBack()))
    }
  }

  def lastTrackPlayer(playerInterface: ActorRef[Command]): Unit = {
    it should "reject Skip command" in {
      playerInterface ! Skip(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessageType[Error]
    }
  }

  def firstTrackPlayer(playerInterface: ActorRef[Command]): Unit = {
    it should "reject SkipBack command" in {
      playerInterface ! SkipBack(playerInterfaceProbe.ref)
      playerInterfaceProbe.expectMessageType[Error]
    }
  }
}
