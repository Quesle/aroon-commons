package org.aroon.commons.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.aroon.commons.socket.context.ProtocolParser;
import org.aroon.commons.socket.factory.MessageProcessorFactory;
import org.aroon.commons.socket.manager.BlockingDatagramPackage;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.MessageChannel;
import org.aroon.commons.socket.manager.MessageProcessor;

/**
 * Transaction constructor.
 * send message from client to server.
 * @author Z·R
 */
public class AroonTransactionConstructor {
	private AroonTransactionAcceptor acceptor;
	private AroonTransactionStack transactionStack;
	private MessageChannel messageChannel;
	private ListeningPoint listeningPoint;
	
	
	public AroonTransactionConstructor(AroonTransactionAcceptor acceptor, 
			AroonTransactionStack transactionStack, MessageChannel messageChannel, ListeningPoint listeningPoint){
		this.acceptor = acceptor;
		this.transactionStack = transactionStack;
		this.messageChannel = messageChannel;
		this.listeningPoint = listeningPoint;
	}
	
	public AroonTransactionConstructor(AroonTransactionStack transactionStack, ListeningPoint listeningPoint){
		this.transactionStack = transactionStack;
		this.listeningPoint = listeningPoint;
		createDefaultTransactionConstructor();
	}
	
	private void createDefaultTransactionConstructor() {
		try {
			MessageProcessorFactory factory = transactionStack.getMessageProcessorFactory();
			MessageProcessor messageProcessor = factory.createMessageProcessor(null, transactionStack, listeningPoint);
			InetAddress inetAddress = InetAddress.getByName(listeningPoint.getPeerAddress());
			messageChannel = messageProcessor.createMessageChannel(inetAddress, listeningPoint.getPeerPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendTextMessage(Object object){
		
	}
	
	public void sendBytesMessage(Object object){
		ProtocolParser parser = acceptor.getProtocolParser();
	 	byte[] message = parser.processProtocolBytesCompiler(object);
		try {
			BlockingDatagramPackage blockingDatagramPackage = new BlockingDatagramPackage();
			DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
			blockingDatagramPackage.setDatagramPacket(datagramPacket);
			InetSocketAddress inetSocketAddress = new InetSocketAddress(
					listeningPoint.getPeerAddress(), listeningPoint.getPeerPort());
			datagramPacket.setSocketAddress(inetSocketAddress);
			messageChannel.sendMessage(blockingDatagramPackage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
