package com.reactive.player.models

object Queue {

  val empty: Queue = Queue()
}

case class Queue(
    pastTracks: List[Track] = List.empty,
    currentTrack: Option[Track] = None,
    futureTracks: List[Track] = List.empty,
    playing: Boolean = false,
    shuffleMode: Boolean = false) {

  def enqueue(track: Track): Queue = this.copy(futureTracks = futureTracks :+ track)

  def togglePlay(): Queue = this.copy(playing = !playing)

  def toggleShuffle(): Queue = this.copy(shuffleMode = !shuffleMode)

  lazy val hasNext: Boolean = futureTracks.nonEmpty

  lazy val isLastTrack: Boolean = !hasNext

  def isFirstTrack: Boolean = pastTracks.isEmpty

  def skip(): Queue = {
    this.copy(
      pastTracks = pastTracks ++ currentTrack.toList,
      currentTrack = futureTracks.headOption,
      futureTracks = futureTracks.drop(1)
    )
  }

  def skipBack(): Queue = {
    this.copy(
      pastTracks = pastTracks.dropRight(1),
      currentTrack = pastTracks.lastOption,
      futureTracks = currentTrack.toList ++ pastTracks
    )
  }
}
