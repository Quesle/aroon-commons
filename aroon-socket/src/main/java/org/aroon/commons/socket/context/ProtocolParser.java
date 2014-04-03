package org.aroon.commons.socket.context;

import org.aroon.commons.socket.manager.BlockingDatagramPackage;

public interface ProtocolParser{
	public Object processProtocolResolver(BlockingDatagramPackage blockingDatagramPackage);
	
	public BlockingDatagramPackage processProtocolCompiler(Object object);
}
