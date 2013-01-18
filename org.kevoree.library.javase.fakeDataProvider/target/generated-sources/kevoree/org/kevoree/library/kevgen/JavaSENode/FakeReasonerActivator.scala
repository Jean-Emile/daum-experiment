package org.kevoree.library.kevgen.JavaSENode;
import java.util.Hashtable
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService
import org.kevoree.framework.KevoreeActor
import org.kevoree.framework.KevoreeComponent
import org.kevoree.framework._
import org.kevoree.library._
class FakeReasonerActivator extends org.kevoree.framework.osgi.KevoreeComponentActivator {
def callFactory() : KevoreeComponent = { FakeReasonerFactory.createComponentActor() } }
