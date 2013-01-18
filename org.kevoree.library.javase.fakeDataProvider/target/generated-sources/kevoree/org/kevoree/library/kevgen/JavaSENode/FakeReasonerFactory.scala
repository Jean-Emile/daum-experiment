package org.kevoree.library.kevgen.JavaSENode
import org.kevoree.framework._
import org.kevoree.library._
class FakeReasonerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=FakeReasonerFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=FakeReasonerFactory.remove(instanceName)
def createInstanceActivator = FakeReasonerFactory.createInstanceActivator
}

object FakeReasonerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new FakeReasonerActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createFakeReasoner()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.FakeReasoner].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.FakeReasoner].stop()}
}}
def createFakeReasoner() : org.kevoree.library.FakeReasoner ={
val newcomponent = new org.kevoree.library.FakeReasoner();
newcomponent}
}
