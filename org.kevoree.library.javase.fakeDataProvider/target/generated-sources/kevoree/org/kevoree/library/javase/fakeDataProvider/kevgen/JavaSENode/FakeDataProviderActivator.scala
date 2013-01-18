package org.kevoree.library.javase.fakeDataProvider.kevgen.JavaSENode;
import java.util.Hashtable
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService
import org.kevoree.framework.KevoreeActor
import org.kevoree.framework.KevoreeComponent
import org.kevoree.framework._
import org.kevoree.library.javase.fakeDataProvider._
class FakeDataProviderActivator extends org.kevoree.framework.osgi.KevoreeComponentActivator {
def callFactory() : KevoreeComponent = { FakeDataProviderFactory.createComponentActor() } }
