package org.aroon.commons.socket.udp;

import java.net.InetAddress;

import org.aroon.commons.socket.AroonTransactionStack;
import org.aroon.commons.socket.context.ProtocolParser;
import org.aroon.commons.socket.manager.BlockingDatagramPackage;
import org.aroon.commons.socket.manager.MessageChannel;

public class UDPMessageChannel extends MessageChannel implements Runnable{
	private UDPMessageProcessor messageProcessor;
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
			if(messageProcessor.threadPoolSize != -1){
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
		try {
			ProtocolParser parser = messageProcessor.acceptor.getProtocolParser();
			if(parser == null){
				throw new ClassNotFoundException("Not found ProtocolParser class");
			}
			
			parser.processProtocolResolver(blockingPacket);
		
			/*ProtocolFilter protocolFilter = transactionStack.getProtocolFilter();
			if(protocolFilter == null){
				throw new ClassNotFoundException("Not found ProtocolFilter class");
			}
		
			ProtocolController protocolController = transactionStack.getProtocolController();
			if(protocolController == null){
				throw new ClassNotFoundException("Not found ProtocolController class");
			}*/
			//protocolController.setMessageChannel(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(Object object) {
/*		if(compiler == null){
			try {
				throw new Exception("Not found ProtocolCompiler class.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BlockingDatagramPackage blockingDatagramPackage = compiler.processProtocolCompiler(object);
		

		DatagramPacket datagramPacket = blockingDatagramPackage.getDatagramPacket();
		try {
			if(datagramPacket == null){
				throw new ClassNotFoundException("Not found DatagramPacket class.");
			}
			
			if(myPort == datagramPacket.getPort()){
				System.out.println("............");
				return;
			}
			
			if(messageProcessor.sock == null){
				throw new ClassNotFoundException("Not found DatagramScoket class.");
			}
			//messageProcessor.sock = new DatagramSocket();
			messageProcessor.sock.send(datagramPacket);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
*/	
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
