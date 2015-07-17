package akka

import akka.actor.{Props, Actor, ActorSystem}
import akka.camel._
import org.apache.activemq.camel.component.ActiveMQComponent
import org.apache.activemq.ScheduledMessage._

case class Message(body: String)

class SimpleProducer() extends Actor with Producer with Oneway {
  override def endpointUri: String = "TEST.FOO"
}

class SimpleConsumer extends Actor with Consumer {
  override def endpointUri: String = "TEST.FOO"
  override def receive: Receive = {
    case msg: CamelMessage => println(msg)
  }
}

object AkkaCamelAndActiveMQ extends App {
  val actorSystem = ActorSystem("CamelTesting")
  val system = CamelExtension(actorSystem)

  val activeMqUrl: String = "failover://(tcp://192.168.203.***:61616,tcp://192.168.203.***:61616)"
  system.context.addComponent("activemq", ActiveMQComponent.activeMQComponent(activeMqUrl))

  val simpleProducer = actorSystem.actorOf(Props[SimpleProducer])
  val simpleConsumer = actorSystem.actorOf(Props[SimpleConsumer])

  Thread.sleep(100)

  simpleProducer ! Message("first")
  simpleProducer ! Message("second")
  simpleProducer ! Message("third")

  val delayedMessage = CamelMessage(Message("delayed fourth"), Map(AMQ_SCHEDULED_DELAY -> 3000))
  simpleProducer ! delayedMessage

  Thread.sleep(5000)
  actorSystem.shutdown()
}


