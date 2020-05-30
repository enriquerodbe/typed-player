package com.typed.player.models

import scala.util.Random

object Player {

  val empty: Player = new Player()
}

case class Player(
    pastTracks: Seq[Track],
    currentTrack: Option[Track],
    futureTracks: Seq[Track],
    playing: Boolean,
    shuffleMode: Boolean) {

  def this() = this(Seq.empty, None, Seq.empty, false, false)

  def enqueue(track: Track): Player = this.copy(futureTracks = futureTracks :+ track)

  def togglePlay(): Player = this.copy(playing = !playing)

  def toggleShuffle(): Player = this.copy(shuffleMode = !shuffleMode)

  def isLastTrack: Boolean = futureTracks.isEmpty

  def isFirstTrack: Boolean = pastTracks.isEmpty

  def skip(): Player = {
    if (shuffleMode) shuffleSkip()
    else regularSkip()
  }

  private def regularSkip(): Player = {
    this.copy(
      pastTracks = pastTracks ++ currentTrack.toList,
      currentTrack = futureTracks.headOption,
      futureTracks = futureTracks.drop(1)
    )
  }

  private def shuffleSkip(): Player = {
    val randomTrack = Random.nextInt(futureTracks.length)
    val (beforeChosenTrack, chosenTrackAndAfter) = futureTracks.splitAt(randomTrack)
    this.copy(
      pastTracks = pastTracks ++ currentTrack.toList,
      currentTrack = chosenTrackAndAfter.headOption,
      futureTracks = beforeChosenTrack ++ chosenTrackAndAfter.drop(1)
    )
  }

  def skipBack(): Player = {
    this.copy(
      pastTracks = pastTracks.dropRight(1),
      currentTrack = pastTracks.lastOption,
      futureTracks = currentTrack.toList ++ futureTracks
    )
  }

  override def toString: String = {
    s"$playingStatusString " +
      s"$shuffleStatusString" +
      s"$pastTracksString " +
      s"$currentTrackString " +
      s"$futureTracksString"
  }

  private def playingStatusString: String = if (playing) "Playing" else "Paused"

  private def shuffleStatusString: String = if (shuffleMode) "(Shuffle) " else ""

  private def pastTracksString: String = mkString(pastTracks)

  private def currentTrackString: String = mkString(currentTrack.toList)

  private def futureTracksString: String = mkString(futureTracks)

  private def mkString(tracks: Seq[Track]): String = tracks.mkString("[", ",", "]")
}
