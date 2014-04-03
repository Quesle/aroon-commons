package org.aroon.commons.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import org.aroon.commons.socket.AroonTransactionAcceptor;
import org.aroon.commons.socket.AroonTransactionStack;
import org.aroon.commons.socket.manager.BlockingDatagramPackage;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.MessageChannel;

public class UDPMessageChannel extends MessageChannel implements Runnable{
	private UDPMessageProcessor udpMessageProcessor;
	AroonTransactionStack transactionStack;
	Thread mythread;
	String myAddress;
	int myPort;
	
	private BlockingDatagramPackage incomingPacket;

	private InetAddress peerAddress;
	private int peerPort;
	
	/**
     * Protocol to use when talking to receiver (i.e. when sending replies).
     */
	private String peerProtocol;
	
	public UDPMessageChannel(AroonTransactionStack transactionStack,
			UDPMessageProcessor messageProcessor, String threadName) {
		
		this.transactionStack = transactionStack;
		this.messageProcessor = messageProcessor;
		
		mythread = new Thread(this);
		
		this.myAddress = messageProcessor.getInetAddress().getHostAddress();
		this.myPort = messageProcessor.getPort();
		
		mythread.setName(threadName);
		mythread.start();
	}
	
	/**
	 * Constructor. We create one of these when we send out a message.
	 * 
	 * @param targetAddress
	 * 				INET address of the place where we want to send messages.
	 * @param port
	 * 				target port (where we want to send the message).
	 * @param justestTransactionStack
	 * 				our transaction stack.
	 * @param messageProcessor
	 */
	public UDPMessageChannel(InetAddress targetAddress, int port, 
			AroonTransactionStack transactionStack, UDPMessageProcessor messageProcessor){
		peerAddress = targetAddress;
		peerPort = port;
		peerProtocol = "UDP";
		
		super.messageProcessor = messageProcessor;
		this.myAddress = messageProcessor.getInetAddress().getHostAddress();
		this.myPort = messageProcessor.getPort();
		this.transactionStack = transactionStack;
	}

	@Override
	public void run() {
		//线程管理器
		
		final UDPMessageProcessor udpMessageProcessor = (UDPMessageProcessor)messageProcessor;
		
		while(true){
			BlockingDatagramPackage blockingPacket = null;
			if(udpMessageProcessor.threadPoolSize != -1){
				try {
					blockingPacket = udpMessageProcessor.messageQueue.take();
				} catch (InterruptedException e) {
					if(!udpMessageProcessor.isRunning){
						return ;
					}
				}
				this.incomingPacket = blockingPacket;
			}else{
				blockingPacket = this.incomingPacket;
			}
			
			processIncomingDataPacket(blockingPacket);
		}
	}

	private void processIncomingDataPacket(BlockingDatagramPackage blockingPacket) {
		DatagramPacket datagramPacket = blockingPacket.getDatagramPacket();
		int length = datagramPacket.getLength();
		byte[] message = new byte[length];
		System.arraycopy(datagramPacket.getData(), 0, message, 0, message.length);
		
		try {
			AroonTransactionAcceptor acceptor = messageProcessor.getTransactionAcceptor();
			
			ListeningPoint listeningPoint = acceptor.getListeningPoint();
			listeningPoint.setPeerAddress(datagramPacket.getAddress().getHostAddress());
			listeningPoint.setPeerPort(datagramPacket.getPort());
			acceptor.processAcceptedBytesMessage(listeningPoint, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(BlockingDatagramPackage	blockingDatagramPackage) {
		udpMessageProcessor = (UDPMessageProcessor) this.messageProcessor;
		DatagramPacket datagramPacket = blockingDatagramPackage.getDatagramPacket();
		try {
			if(datagramPacket == null){
				throw new ClassNotFoundException("Not found DatagramPacket class.");
			}
			
			if(myPort == datagramPacket.getPort()){
				throw new IOException("Warn: The sending port is same of the listening port.");
			}
			if(udpMessageProcessor.sock == null){
				throw new ClassNotFoundException("Not found DatagramScoket class.");
			}
			udpMessageProcessor.sock.send(datagramPacket);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * UDP is not a secure protocol.
	 * @return
	 */
	public boolean isSecure(){
		return false;
	}

	@Override
	public void close() {
		if(mythread != null){
			mythread.interrupt();
			mythread = null;
		}
	}

	@Override
	public AroonTransactionStack getTransactionStack() {
		return this.transactionStack;
	}

	@Override
	public String getTransport() {
		return null;
	}

	@Override
	public boolean isReliable() {
		return false;
	}

	@Override
	public String getPeerAddress() {
		return this.peerAddress.getHostAddress();
	}

	@Override
	public InetAddress getPeerInetAddress() {
		return this.peerAddress;
	}

	@Override
	public String getPeerProtocol() {
		return this.peerProtocol;
	}

	@Override
	public int getPeerPort() {
		return this.peerPort;
	}
}
