package org.kevoree.library.javase.fakeDataProvider.kevgen.JavaSENode
import org.kevoree.framework._
import org.kevoree.library.javase.fakeDataProvider._
class ComputerDataProviderFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ComputerDataProviderFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ComputerDataProviderFactory.remove(instanceName)
def createInstanceActivator = ComputerDataProviderFactory.createInstanceActivator
}

object ComputerDataProviderFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ComputerDataProviderActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createComputerDataProvider()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.ComputerDataProvider].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.ComputerDataProvider].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.ComputerDataProvider].update()}
}}
def createComputerDataProvider() : org.kevoree.library.javase.fakeDataProvider.ComputerDataProvider ={
val newcomponent = new org.kevoree.library.javase.fakeDataProvider.ComputerDataProvider();
newcomponent.getHostedPorts().put("cpu",createComputerDataProviderPORTcpu(newcomponent))
newcomponent.getNeededPorts().put("ram",createComputerDataProviderPORTram(newcomponent))
newcomponent}
def createComputerDataProviderPORTcpu(component : ComputerDataProvider) : ComputerDataProviderPORTcpu ={ new ComputerDataProviderPORTcpu(component)}
def createComputerDataProviderPORTram(component : ComputerDataProvider) : ComputerDataProviderPORTram ={ return new ComputerDataProviderPORTram(component);}
}
