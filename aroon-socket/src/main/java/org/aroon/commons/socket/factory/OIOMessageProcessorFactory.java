package org.aroon.commons.socket.factory;

import java.io.IOException;
import java.net.InetAddress;

import org.aroon.commons.socket.AroonTransactionAcceptor;
import org.aroon.commons.socket.AroonTransactionStack;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.MessageProcessor;
import org.aroon.commons.socket.udp.UDPMessageProcessor;

public class OIOMessageProcessorFactory implements MessageProcessorFactory{

	@Override
	public MessageProcessor createMessageProcessor(
			AroonTransactionStack transactionStack, InetAddress ipAddress, int port,
			String transport) throws IOException {
		if(transport.equalsIgnoreCase(ListeningPoint.UDP)){
			UDPMessageProcessor udpMessageProcessor = new UDPMessageProcessor(
					transactionStack, ipAddress, port, transport);
			return udpMessageProcessor;
		}else if(transport.equalsIgnoreCase(ListeningPoint.TCP)){
			
			return null;
		}else if(transport.equalsIgnoreCase(ListeningPoint.TLS)){
			
			return null;
		}else if(transport.equalsIgnoreCase(ListeningPoint.SCTP)){
			
			return null;
		}else{
			throw new IllegalArgumentException("bad transport");
		}
	}

	@Override
	public MessageProcessor createMessageProcessor(
			AroonTransactionAcceptor acceptor,
			AroonTransactionStack transactionStack,
			ListeningPoint listeningPoint) throws IOException {
		if(listeningPoint.getTransport().equalsIgnoreCase(ListeningPoint.UDP)){
			UDPMessageProcessor udpMessageProcessor = new UDPMessageProcessor(
					acceptor, transactionStack, listeningPoint);
			return udpMessageProcessor;
		}else 
		if(listeningPoint.getTransport().equalsIgnoreCase(ListeningPoint.TCP)){
			
			return null;
		}else 
		if(listeningPoint.getTransport().equalsIgnoreCase(ListeningPoint.TLS)){
			
			return null;
		}else 
		if(listeningPoint.getTransport().equalsIgnoreCase(ListeningPoint.SCTP)){
			
			return null;
		}else{
			throw new IllegalArgumentException("bad transport");
		}
	}

}
