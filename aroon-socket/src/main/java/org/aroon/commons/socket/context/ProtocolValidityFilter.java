package org.aroon.commons.socket.context;

public interface ProtocolValidityFilter {
	public void protocolValidityDetection(Class<?> protocolObject);
}
