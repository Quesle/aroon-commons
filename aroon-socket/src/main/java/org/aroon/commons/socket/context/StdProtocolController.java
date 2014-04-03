package org.aroon.commons.socket.context;

import org.aroon.commons.socket.AroonTransactionAcceptor;
import org.aroon.commons.socket.AroonTransactionConstructor;
import org.aroon.commons.socket.manager.ListeningPoint;

public abstract class StdProtocolController implements ProtocolController{
	private AroonTransactionAcceptor acceptor;
	private ListeningPoint listeningPoint;
	
	@Override
	public void setTransactionAcceptor(AroonTransactionAcceptor acceptor){
		this.acceptor = acceptor;
	}
	
	@Override
	public void setListeningPoint(ListeningPoint listeningPoint){
		this.listeningPoint = listeningPoint;
	}
	
	@Override
	public void sendBytesMessage(Object object){
		AroonTransactionConstructor constructor = acceptor.createConstructorTransaction(listeningPoint);
		constructor.sendBytesMessage(object);
	}

	public abstract void processRequest(Object object);

	public abstract void processResponse(Object clas);


}
