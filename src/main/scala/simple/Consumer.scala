package simple

import javax.jms._

import org.apache.activemq.ActiveMQConnectionFactory

object Consumer {
  val activeMqUrl: String = "failover://(tcp://192.168.203.176:61616,tcp://192.168.203.177:61616)"
  val queueName: String = "TEST.FOO"

  def main(args: Array[String]) {
    val connectionFactory = new ActiveMQConnectionFactory(activeMqUrl)
    val connection = connectionFactory.createConnection
    connection.start

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val destination = session.createQueue(queueName)
    val consumer: MessageConsumer = session.createConsumer(destination)

    val listener: MessageListener = new MessageListener {
      override def onMessage(message: Message): Unit = {
        try {
          if (message.isInstanceOf[TextMessage]) {
            val textMessage: TextMessage = message.asInstanceOf[TextMessage]
            println(textMessage.getText + " received @ " + System.currentTimeMillis())
            message.acknowledge
          }
        } catch {
          case je: JMSException => {
            println(je.getMessage)
          }
        }
      }
    }

    consumer.setMessageListener(listener)
  }

}
