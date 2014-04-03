package org.aroon.commons.socket;

import java.io.IOException;

import org.aroon.commons.socket.context.ProtocolParser;
import org.aroon.commons.socket.manager.MessageChannel;

/**
 * Transaction constructor.
 * send message from client to server.
 * @author Z·R
 */
public class AroonTransactionConstructor {
	private AroonTransactionAcceptor acceptor;
	private AroonTransactionStack transactionStack;
	private MessageChannel messageChannel;
	
	public AroonTransactionConstructor(AroonTransactionAcceptor acceptor, 
			AroonTransactionStack transactionStack, MessageChannel messageChannel){
		this.acceptor = acceptor;
		this.transactionStack = transactionStack;
		this.messageChannel = messageChannel;
	}
	
	
	
	public void sendTextMessage(Object object){
		
	}
	
	public void sendBytesMessage(Object object){
		ProtocolParser parser = acceptor.getProtocolParser();
		parser.processProtocolCompiler(object);
		try {
			messageChannel.sendMessage(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
