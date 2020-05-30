package com.typed.player.models

import scala.util.Random

object Queue {

  val empty: Queue = new Queue()
}

case class Queue(
    pastTracks: Seq[Track],
    currentTrack: Option[Track],
    futureTracks: Seq[Track],
    playing: Boolean,
    shuffleMode: Boolean) {

  def this() = this(Seq.empty, None, Seq.empty, false, false)

  def enqueue(track: Track): Queue = this.copy(futureTracks = futureTracks :+ track)

  def togglePlay(): Queue = this.copy(playing = !playing)

  def toggleShuffle(): Queue = this.copy(shuffleMode = !shuffleMode)

  def isLastTrack: Boolean = futureTracks.isEmpty

  def isFirstTrack: Boolean = pastTracks.isEmpty

  def skip(): Queue = {
    if (shuffleMode) shuffleSkip()
    else regularSkip()
  }

  private def regularSkip(): Queue = {
    this.copy(
      pastTracks = pastTracks ++ currentTrack.toList,
      currentTrack = futureTracks.headOption,
      futureTracks = futureTracks.drop(1)
    )
  }

  private def shuffleSkip(): Queue = {
    val randomTrack = Random.nextInt(futureTracks.length)
    val (beforeChosenTrack, chosenTrackAndAfter) = futureTracks.splitAt(randomTrack)
    this.copy(
      pastTracks = pastTracks ++ currentTrack.toList,
      currentTrack = chosenTrackAndAfter.headOption,
      futureTracks = beforeChosenTrack ++ chosenTrackAndAfter.drop(1)
    )
  }

  def skipBack(): Queue = {
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
