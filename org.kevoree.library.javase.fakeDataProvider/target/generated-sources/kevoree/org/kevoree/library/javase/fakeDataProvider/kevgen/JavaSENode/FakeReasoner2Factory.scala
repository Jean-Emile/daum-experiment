package org.kevoree.library.javase.fakeDataProvider.kevgen.JavaSENode
import org.kevoree.framework._
import org.kevoree.library.javase.fakeDataProvider._
class FakeReasoner2Factory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=FakeReasoner2Factory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=FakeReasoner2Factory.remove(instanceName)
def createInstanceActivator = FakeReasoner2Factory.createInstanceActivator
}

object FakeReasoner2Factory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new FakeReasoner2Activator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createFakeReasoner2()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.FakeReasoner2].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.FakeReasoner2].stop()}
}}
def createFakeReasoner2() : org.kevoree.library.javase.fakeDataProvider.FakeReasoner2 ={
val newcomponent = new org.kevoree.library.javase.fakeDataProvider.FakeReasoner2();
newcomponent}
}
