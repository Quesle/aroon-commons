package org.aroon.commons.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.aroon.commons.socket.context.ProtocolParser;
import org.aroon.commons.socket.factory.MessageProcessorFactory;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.MessageChannel;
import org.aroon.commons.socket.manager.MessageProcessor;

public class AroonTransactionAcceptor {
	/**
	 * The socket Transaction Stack.
	 */
	private AroonTransactionStack transactionStack;
	
	/**
	 * The server MessageProcessor. Used to create and manager MessageChannel. 
	 */
	private MessageProcessor messageProcessor;
	
	/**
	 * The server listeningPoint.
	 */
	private ListeningPoint listeningPoint;
	
	
	private ProtocolParser protocolParser;
	
	public AroonTransactionAcceptor(AroonTransactionStack transactionStack,
			ListeningPoint listeningPoint){
		this.transactionStack = transactionStack;
		this.listeningPoint = listeningPoint;
		createMessageProcessor();
	}
	
	public void createMessageProcessor(){
		try {
		MessageProcessorFactory factory = transactionStack.getMessageProcessorFactory();
			if(factory != null){
				messageProcessor = factory.createMessageProcessor(this, transactionStack, listeningPoint);
			}else{
				throw new ClassNotFoundException("MessageProcessorFactory is not inilization.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public AroonTransactionConstructor createConstructorTransaction(
			ListeningPoint localListeningPoint) {
		AroonTransactionConstructor constructor = null;
		try {
			InetAddress inetAddress = InetAddress.getByName(localListeningPoint.getPeerAddress());
			MessageChannel messageChannel = messageProcessor.createMessageChannel(
					inetAddress, localListeningPoint.getPeerPort());
			constructor = new AroonTransactionConstructor(
					this, transactionStack, messageChannel);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return constructor;
	}

	public void start() {
		messageProcessor.start();
	}

	public void setProtocolParser(ProtocolParser protocolParser) {
		this.protocolParser = protocolParser;
	}
	
	public ProtocolParser getProtocolParser(){
		return protocolParser;
	}
}
