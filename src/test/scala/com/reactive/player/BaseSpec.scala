package com.reactive.player

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import com.reactive.player.player_interface.PlayerInterfaceReplies.Reply
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait BaseSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  val testKit: ActorTestKit = ActorTestKit()
  val testProbe = testKit.createTestProbe[Reply]()
  override protected def afterAll(): Unit = testKit.shutdownTestKit()
}
