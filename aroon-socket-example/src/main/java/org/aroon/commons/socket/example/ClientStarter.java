package org.aroon.commons.socket.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aroon.commons.socket.AroonTransactionConstructor;
import org.aroon.commons.socket.AroonTransactionStack;
import org.aroon.commons.socket.context.ProtocolController;
import org.aroon.commons.socket.context.ProtocolParser;
import org.aroon.commons.socket.example.parser.ClientProtocolController;
import org.aroon.commons.socket.example.parser.MyProtocol;
import org.aroon.commons.socket.example.parser.MyProtocolParser;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.ListeningPointImpl;

public class ClientStarter {
	public static void main(String[] args) {
		AroonTransactionStack transactionStack = new AroonTransactionStack();
		
		ListeningPoint listeningPoint = new ListeningPointImpl(
				"192.168.1.211", 3306, ListeningPoint.UDP);
		
		
		transactionStack.bind(listeningPoint);
		ProtocolParser parser = new MyProtocolParser();
		transactionStack.addProtocolParser(listeningPoint, parser);
		
		ProtocolController controller = new ClientProtocolController();
		transactionStack.addProtocolController(listeningPoint, controller);
		try {
			transactionStack.start();
			Thread.sleep(1000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		MyProtocol myProtocol = createMyProtocol();
		
		try {
			listeningPoint.setPeerAddress("127.0.0.1");
			listeningPoint.setPeerPort(3304);
			AroonTransactionConstructor constructor = 
					transactionStack.getTransactionConstructor(listeningPoint);
			for (int i = 0; i < 1; i++) {
				constructor.sendBytesMessage(myProtocol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static MyProtocol createMyProtocol() {
		MyProtocol myProtocol = new MyProtocol();
		Map<Integer, String> segments = new HashMap<Integer, String>();
		segments.put(333, "It's a Socket test. Please response OK.");
		myProtocol.setProtocolSegments(segments);
		return myProtocol;
	}
}
