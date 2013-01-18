package org.kevoree.library.javase.fakeDataProvider.kevgen.JavaSENode
import org.kevoree.framework._
import org.kevoree.library.javase.fakeDataProvider._
class ClientHWFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ClientHWFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ClientHWFactory.remove(instanceName)
def createInstanceActivator = ClientHWFactory.createInstanceActivator
}

object ClientHWFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ClientHWActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createClientHW()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.ClientHW].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.kevoree.library.javase.fakeDataProvider.ClientHW].stop()}
}}
def createClientHW() : org.kevoree.library.javase.fakeDataProvider.ClientHW ={
val newcomponent = new org.kevoree.library.javase.fakeDataProvider.ClientHW();
newcomponent.getNeededPorts().put("cpu",createClientHWPORTcpu(newcomponent))
newcomponent}
def createClientHWPORTcpu(component : ClientHW) : ClientHWPORTcpu ={ return new ClientHWPORTcpu(component);}
}
