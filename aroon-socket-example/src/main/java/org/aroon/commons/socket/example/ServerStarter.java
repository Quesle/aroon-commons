package org.aroon.commons.socket.example;

import java.io.IOException;

import org.aroon.commons.socket.AroonTransactionStack;
import org.aroon.commons.socket.context.ProtocolController;
import org.aroon.commons.socket.context.ProtocolParser;
import org.aroon.commons.socket.example.parser.ServerProtocolController;
import org.aroon.commons.socket.example.parser.MyProtocolParser;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.ListeningPointImpl;

public class ServerStarter {
	public static void main(String[] args) {
		AroonTransactionStack transactionStack = new AroonTransactionStack();
		
		ListeningPoint listeningPoint = new ListeningPointImpl(
				"192.168.1.211", 3304, ListeningPoint.UDP);
		
		transactionStack.bind(listeningPoint, 3306);
		ProtocolParser parser = new MyProtocolParser();
		transactionStack.addProtocolParser(listeningPoint, parser);
		
		ProtocolController protocolController = new ServerProtocolController();
		transactionStack.addProtocolController(listeningPoint, protocolController);
		try {
			transactionStack.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
