package org.aroon.commons.socket.context;

import org.aroon.commons.socket.manager.MessageChannel;

public interface ProtocolController {
	
	public void setMessageChannel(MessageChannel messageChannel);
	
	public void processRequest(Object clas);
	
	public void processResponse(Object clas, int port);
	
}
