package org.kevoree.library.javase.trustStuff.kevgen.JavaSENode
import org.kevoree.framework._
import org.kevoree.library.javase.trustStuff._
class TrustManagerMoverFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=TrustManagerMoverFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=TrustManagerMoverFactory.remove(instanceName)
def createInstanceActivator = TrustManagerMoverFactory.createInstanceActivator
}

object TrustManagerMoverFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new TrustManagerMoverActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createTrustManagerMover()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.TrustManagerMover].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.TrustManagerMover].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.TrustManagerMover].update()}
}}
def createTrustManagerMover() : org.kevoree.library.javase.trustStuff.TrustManagerMover ={
val newcomponent = new org.kevoree.library.javase.trustStuff.TrustManagerMover();
newcomponent}
}
