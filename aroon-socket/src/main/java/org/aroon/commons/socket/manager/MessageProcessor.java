package org.aroon.commons.socket.manager;

import java.net.InetAddress;

import org.aroon.commons.socket.AroonTransactionAcceptor;
import org.aroon.commons.socket.AroonTransactionStack;

public abstract class MessageProcessor {
	public static final String IN4_ADDR_ANY = "0.0.0.0";
	public static final String IN6_ADDR_ANY = "::0";
	
	public MessageProcessor(){}
	
	private String messageProcessorName;
	
	public String getMessageProcessorName(){
		return messageProcessorName;
	}
	
	public void setMessageProcessorName(String messageProcessorName){
		this.messageProcessorName = messageProcessorName;
	}
	
	public AroonTransactionStack transactionStack;
	private AroonTransactionAcceptor acceptor;
	private String transport;
	private InetAddress inetAddress;
	private int port;
	public MessageProcessor(String transport){
		this.transport = transport;
	}
	
	public MessageProcessor(AroonTransactionAcceptor acceptor,
			AroonTransactionStack transactionStack,
			ListeningPoint listeningPoint){
		this.acceptor = acceptor;
		this.transactionStack = transactionStack;
		this.inetAddress = listeningPoint.getLocalInetAddress();
		this.port = listeningPoint.getLocalPort();
		this.transport = listeningPoint.getTransport();
	}
	
	public MessageProcessor(AroonTransactionStack transactionStack, InetAddress inetAddress, int port, String transport){
		this.transactionStack = transactionStack;
		this.inetAddress = inetAddress;
		this.port = port;
		this.transport = transport;
	}
	
	public String getTransport(){
		return this.transport;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public InetAddress getInetAddress(){
		return inetAddress;
	}
	
	public AroonTransactionAcceptor getTransactionAcceptor(){
		return acceptor;
	}
	
	/**
	 * Get the Transaction Stack
	 * @return the stack
	 */
	public abstract AroonTransactionStack getTransactionStack();
	
	/**
	 * Create a message channel for the specified host/port.
	 * @return
	 */
	public abstract MessageChannel createMessageChannel(InetAddress targetHost,
            int port);
	
	/**
	 * Start our thread.
	 */
	public abstract void start();
	
	/**
	 * Stop method
	 */
	public abstract void stop();
	
	/**
	 * Flags whether this processor is secuer or not.
	 * @return
	 */
	public abstract boolean isSecure();
	
	/**
	 * Maxinum number of bytes that this processor can handle.
	 */
	public abstract int getMaximumMessageSize();
	
	/**
	 * Return true if there are pending message to be processed (which prevents
	 * the message channel from being closed.
	 */
	public abstract boolean isUse();
	
	/**
     * Run method.
     */
    public abstract void run();
}
