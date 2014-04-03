package org.aroon.commons.socket.factory;

import java.io.IOException;
import java.net.InetAddress;

import org.aroon.commons.socket.AroonTransactionAcceptor;
import org.aroon.commons.socket.AroonTransactionStack;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.MessageProcessor;

public interface MessageProcessorFactory {
	
	MessageProcessor createMessageProcessor(AroonTransactionStack transactionStack,
			InetAddress ipAddress, int port, String transport) throws IOException;

	MessageProcessor createMessageProcessor(AroonTransactionAcceptor acceptor,
			AroonTransactionStack transactionStack,
			ListeningPoint listeningPoint) throws IOException;

}
