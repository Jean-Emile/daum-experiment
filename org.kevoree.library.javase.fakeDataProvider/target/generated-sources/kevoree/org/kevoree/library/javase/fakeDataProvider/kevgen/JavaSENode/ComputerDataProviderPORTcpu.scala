package org.kevoree.library.javase.fakeDataProvider.kevgen.JavaSENode
import org.kevoree.framework.port._
import org.kevoree.library.javase.fakeDataProvider._
import scala.{Unit=>void}
class ComputerDataProviderPORTcpu(component : ComputerDataProvider) extends org.kevoree.library.javase.fakeDataProvider.Ihardware with KevoreeProvidedPort {
def getName : String = "cpu"
def getComponentName : String = component.getName 
def getCpu() : java.lang.Object ={
val msgcall = new org.kevoree.framework.MethodCallMessage
msgcall.setMethodName("getCpu")
(this !? msgcall).asInstanceOf[java.lang.Object]}
override def internal_process(msg : Any)= msg match {
case opcall : org.kevoree.framework.MethodCallMessage => reply(opcall.getMethodName match {
case "getCpu"=> try { component.getCpu()} catch {case _ @ e => e.printStackTrace();println("Uncatched exception while processing Kevoree message");null }
case _ @ o => println("uncatch message , method not found in service declaration : "+o);null 
})}
}
