package org.kevoree.library.javase.fakeDataProvider.kevgen.JavaSENode
import org.kevoree.framework._
import org.kevoree.library.javase.fakeDataProvider._
class FakeDataProviderFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=FakeDataProviderFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=FakeDataProviderFactory.remove(instanceName)
def createInstanceActivator = FakeDataProviderFactory.createInstanceActivator
}

object FakeDataProviderFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new FakeDataProviderActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createFakeDataProvider()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.FakeDataProvider].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.FakeDataProvider].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.FakeDataProvider].update()}
}}
def createFakeDataProvider() : org.kevoree.library.javase.fakeDataProvider.FakeDataProvider ={
val newcomponent = new org.kevoree.library.javase.fakeDataProvider.FakeDataProvider();
newcomponent.getNeededPorts().put("out",createFakeDataProviderPORTout(newcomponent))
newcomponent}
def createFakeDataProviderPORTout(component : FakeDataProvider) : FakeDataProviderPORTout ={ return new FakeDataProviderPORTout(component);}
}
