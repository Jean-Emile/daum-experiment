package org.kevoree.library.javase.fakeDataProvider.kevgen.JavaSENode
import org.kevoree.framework.port._
import scala.{Unit=>void}
import org.kevoree.library.javase.fakeDataProvider._
class FakeDataProviderPORTout(component : FakeDataProvider) extends org.kevoree.framework.MessagePort with KevoreeRequiredPort {
def getName : String = "out"
def getComponentName : String = component.getName 
def process(o : Object) = {
{this ! o}
}
def getInOut = false
}
