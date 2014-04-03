package org.aroon.commons.socket.context;

import org.aroon.commons.socket.manager.MessageChannel;

public abstract class StdProtocolController implements ProtocolController{
	
	protected MessageChannel messageChannel;
	
	@Override
	public void setMessageChannel(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}



}
