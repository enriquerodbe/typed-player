package com.typed.player.models

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

  def enqueue(track: Track): Player = {
    if (currentTrack.isEmpty) this.copy(currentTrack = Some(track))
    else this.copy(futureTracks = futureTracks :+ track)
  }

  def togglePlay(): Player = this.copy(playing = !playing)

  def toggleShuffle(): Player = this.copy(shuffleMode = !shuffleMode)

  def isLastTrack: Boolean = futureTracks.isEmpty

  def isFirstTrack: Boolean = pastTracks.isEmpty

  def skip(): Player = {
    this.copy(
      pastTracks = pastTracks ++ currentTrack.toList,
      currentTrack = futureTracks.headOption,
      futureTracks = futureTracks.drop(1)
    )
  }

  def skipBack(): Player = {
    this.copy(
      pastTracks = pastTracks.dropRight(1),
      currentTrack = pastTracks.lastOption,
      futureTracks = currentTrack.toList ++ futureTracks
    )
  }

  def stop(): Player = Player.empty.copy(shuffleMode = shuffleMode)
}
