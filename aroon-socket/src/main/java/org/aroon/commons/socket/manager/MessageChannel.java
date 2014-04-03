package org.aroon.commons.socket.manager;

import java.io.IOException;
import java.net.InetAddress;

import org.aroon.commons.socket.AroonTransactionStack;

public abstract class MessageChannel {
	
	public int useCount;
	
	public transient MessageProcessor messageProcessor;
	
	
	/**
	 * Close the message channel.
	 */
	public abstract void close();
	
	/**
	 * Get TransactionStack object from this message channel.
	 * @return TransactionStack object of this message channel
	 */
	public abstract AroonTransactionStack getTransactionStack();
	
	/**
	 * Get transport string of this message channel.
	 * @return Transport string of this message channel.
	 */
	public abstract String getTransport();
	
	/**
	 * Get whether this channel is reliable or not.
	 * @return True if reliable, false if not.
	 */
	public abstract boolean isReliable();
	
	/**
     * Return true if this is a secure channel.
     */
    public abstract boolean isSecure();
    
    /**
     * Send the message (after it has been formatted)
     *
     * @param sipMessage Message to send.
     */
    public abstract void sendMessage(Object object) throws IOException;
	
    public abstract String getPeerAddress();
    
    public abstract InetAddress getPeerInetAddress();
    
    public abstract String getPeerProtocol();
    
    public abstract int getPeerPort();
    
    
    /**
     * Get the message processor.
     */
    public MessageProcessor getMessageProcessor(){
    	return this.messageProcessor;
    }
}
