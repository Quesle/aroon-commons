package org.aroon.commons.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.aroon.commons.socket.AroonTransactionAcceptor;
import org.aroon.commons.socket.AroonTransactionStack;
import org.aroon.commons.socket.manager.BlockingDatagramPackage;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.MessageChannel;
import org.aroon.commons.socket.manager.MessageProcessor;

public class UDPMessageProcessor extends MessageProcessor implements Runnable{

	protected BlockingQueue<BlockingDatagramPackage> messageQueue;
	
	protected DatagramSocket sock;
	protected int threadPoolSize;
	
	public boolean isRunning = false;
	
	private LinkedList<UDPMessageChannel> messageChannels;
	
	public UDPMessageProcessor(AroonTransactionStack transactionStack, InetAddress ipAddress, int port, String transport) {
		super(transactionStack, ipAddress, port, transport);
		if(transactionStack.getThreadPoolSize() >0){
			threadPoolSize = transactionStack.getThreadPoolSize();
		}else{
			threadPoolSize = 1;
		}
		
		messageQueue = new LinkedBlockingQueue<BlockingDatagramPackage>();
		try {
			sock = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor. Use AroonTransactionStack object and ListeningPoint object
	 * create UDPMessageProcessor object.
	 * @param transactionStack
	 * @param listeningPoint
	 */
	public UDPMessageProcessor(AroonTransactionAcceptor acceptor,
			AroonTransactionStack transactionStack,
			ListeningPoint listeningPoint) {
		super(acceptor, transactionStack, listeningPoint);
		if(transactionStack.getThreadPoolSize() >0){
			threadPoolSize = transactionStack.getThreadPoolSize();
		}else{
			threadPoolSize = 1;
		}
		messageQueue = new LinkedBlockingQueue<BlockingDatagramPackage>();
		try {
			sock = new DatagramSocket(listeningPoint.getLocalPort());
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		this.messageChannels = new LinkedList<UDPMessageChannel>();
		if(threadPoolSize != -1){
			for (int i = 0; i < threadPoolSize; i++) {
				UDPMessageChannel channel = new UDPMessageChannel(transactionStack, this,
						transactionStack.getStackName() + "-UDPMessageChannelThread-" + i);
				this.messageChannels.add(channel);
			}
		}
		
		DatagramPacket packet = new DatagramPacket(new byte[8192], 8192);
		try {
			BlockingDatagramPackage blockingPackage = null;
			while(isRunning){
				sock.receive(packet);
				blockingPackage = new BlockingDatagramPackage();
				blockingPackage.setDatagramPacket(packet);
				messageQueue.add(blockingPackage);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void start() {
		this.isRunning = true;
		Thread thread = new Thread(this);
		thread.setName("UDPMessageProcessorThread");
		thread.start();
	}


	@Override
	public void stop() {
		this.isRunning = false;
	}


	@Override
	public AroonTransactionStack getTransactionStack() {
		return this.transactionStack;
	}


	@Override
	public MessageChannel createMessageChannel(InetAddress targetAddress, int port) {
		return new UDPMessageChannel(targetAddress, port, transactionStack, this);
	}


	@Override
	public boolean isSecure() {
		return false;
	}


	@Override
	public int getMaximumMessageSize() {
		return transactionStack.getReceiveUdpBufferSize();
	}

	/**
	 * Return ture if there are any messages in use.
	 */
	@Override
	public boolean isUse() {
		return !messageQueue.isEmpty();
	}

}
