package org.aroon.commons.socket.context;

public interface ProtocolParser{
	public Object processProtocolBytesResolver(byte[] message);
	
	public byte[] processProtocolBytesCompiler(Object object);
	
	public Object processProtocolTextResolver(String text);
	
	public String processProtocolTextComplier(Object object);
}
