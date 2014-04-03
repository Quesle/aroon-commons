package org.aroon.commons.socket.context;

import org.aroon.commons.socket.AroonTransactionAcceptor;
import org.aroon.commons.socket.manager.ListeningPoint;

public interface ProtocolController {
	
	public void setTransactionAcceptor(AroonTransactionAcceptor acceptor);
	
	public void setListeningPoint(ListeningPoint listeningPoint);
	
	public void processRequest(Object object);
	
	public void processResponse(Object clas);
	
	public void sendBytesMessage(Object object);
	
}
