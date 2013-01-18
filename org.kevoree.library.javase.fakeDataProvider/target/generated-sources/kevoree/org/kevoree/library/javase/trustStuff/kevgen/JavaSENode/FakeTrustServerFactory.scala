package org.kevoree.library.javase.trustStuff.kevgen.JavaSENode
import org.kevoree.framework._
import org.kevoree.library.javase.trustStuff._
class FakeTrustServerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=FakeTrustServerFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=FakeTrustServerFactory.remove(instanceName)
def createInstanceActivator = FakeTrustServerFactory.createInstanceActivator
}

object FakeTrustServerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new FakeTrustServerActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createFakeTrustServer()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.FakeTrustServer].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.FakeTrustServer].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.FakeTrustServer].update()}
}}
def createFakeTrustServer() : org.kevoree.library.javase.trustStuff.FakeTrustServer ={
val newcomponent = new org.kevoree.library.javase.trustStuff.FakeTrustServer();
newcomponent}
}
