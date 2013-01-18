package org.kevoree.library.javase.fakeDataProvider.kevgen.JavaSENode
import org.kevoree.framework.port._
import scala.{Unit=>void}
import org.kevoree.library.javase.fakeDataProvider._
class ClientHWPORTcpu(component : ClientHW) extends org.kevoree.library.javase.fakeDataProvider.Ihardware with KevoreeRequiredPort {
def getName : String = "cpu"
def getComponentName : String = component.getName 
def getInOut = true
def getCpu() : java.lang.Object ={
val msgcall = new org.kevoree.framework.MethodCallMessage
msgcall.setMethodName("getCpu")
(this !? msgcall).asInstanceOf[java.lang.Object]}
}
