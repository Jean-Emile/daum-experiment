package org.kevoree.library.javase.trustStuff.kevgen.JavaSENode
import org.kevoree.framework._
import org.kevoree.library.javase.trustStuff._
class TrustManagerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=TrustManagerFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=TrustManagerFactory.remove(instanceName)
def createInstanceActivator = TrustManagerFactory.createInstanceActivator
}

object TrustManagerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new TrustManagerActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createTrustManager()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.TrustManager].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.TrustManager].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.trustStuff.TrustManager].update()}
}}
def createTrustManager() : org.kevoree.library.javase.trustStuff.TrustManager ={
val newcomponent = new org.kevoree.library.javase.trustStuff.TrustManager();
newcomponent}
}
