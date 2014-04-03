package org.aroon.commons.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.aroon.commons.socket.context.ProtocolController;
import org.aroon.commons.socket.context.ProtocolListener;
import org.aroon.commons.socket.context.ProtocolParser;
import org.aroon.commons.socket.factory.MessageProcessorFactory;
import org.aroon.commons.socket.factory.OIOMessageProcessorFactory;
import org.aroon.commons.socket.manager.ListeningPoint;
import org.aroon.commons.socket.manager.MessageProcessor;
import org.aroon.commons.socket.net.DefaultNetworkLayer;
import org.aroon.commons.socket.net.NetworkLayer;

public class AroonTransactionStack extends TransactionStack implements ProtocolListener{
	Logger logger = Logger.getLogger(AroonTransactionStack.class);
	private static final int DEFAULT_THREAD_POOL_SIZE= 3;
	public NetworkLayer networkLayer;
	
	private MessageProcessorFactory messageProcessorFactory;
	private Collection<MessageProcessor> messageProcessors;
	private Map<String, ListeningPoint> listeningPoints;
	
	
	/**
	 * A ConcurrentHashMap of acceptor.
	 * The key is the local ListeningPoint Name.
	 */
	private ConcurrentHashMap<String, AroonTransactionAcceptor> acceptorTable;
	
	/**
	 * A ConcurrentHashMap of constructor.
	 * The key is the remote ListeningPoint Name.
	 */
	private ConcurrentHashMap<String, AroonTransactionConstructor> constructorTable;
	
	
	/**对监听端口所分配的线程池大小*/
	public int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
	
	
	
	public AroonTransactionStack(){
		networkLayer = new DefaultNetworkLayer();
		messageProcessorFactory = new OIOMessageProcessorFactory();
		messageProcessors = new CopyOnWriteArrayList<MessageProcessor>();
		listeningPoints = new HashMap<String, ListeningPoint>();
		
		acceptorTable = new ConcurrentHashMap<String, AroonTransactionAcceptor>();
		constructorTable = new ConcurrentHashMap<String, AroonTransactionConstructor>();
	}
	
	public void start() throws IOException{
		Iterator<String> iterator = acceptorTable.keySet().iterator();
		while (iterator.hasNext()) {
			String pointName = iterator.next();
			AroonTransactionAcceptor acceptor = acceptorTable.get(pointName);
			acceptor.start();
			
			logger.info("Server started: " + pointName);
		}
	}
	
	public void stop(){
		for (MessageProcessor messageProcessor : messageProcessors) {
			messageProcessor.stop();
		}
	}
	
	public MessageProcessor createMessageProcessor(InetAddress ipAddress,
			int port, String transport) throws IOException{
		MessageProcessor newMessMessageProcessor = 
				messageProcessorFactory.createMessageProcessor(this, ipAddress, port, transport);
		this.addNewMessageProcessor(newMessMessageProcessor);
		return newMessMessageProcessor;
	}
	
	public MessageProcessor createMessageProcessor(ListeningPoint listeningPoint) throws IOException{
		MessageProcessor newMessMessageProcessor = 
				messageProcessorFactory.createMessageProcessor(this,
						listeningPoint.getLocalInetAddress(), listeningPoint.getLocalPort(), listeningPoint.getTransport());
		this.addNewMessageProcessor(newMessMessageProcessor);
		return newMessMessageProcessor;
	}
	
	public AroonTransactionConstructor getTransactionConstructor(ListeningPoint listeningPoint){
		String key = listeningPoint.getPeerListeningPointName();
		if(constructorTable.containsKey(key)){
			return constructorTable.get(key);
		}
		
		String localListeningPointName = listeningPoint.getLocalListeningPointName();
		if(acceptorTable.containsKey(localListeningPointName)){
			AroonTransactionAcceptor acceptor = acceptorTable.get(localListeningPointName);
			AroonTransactionConstructor constructor = acceptor.createConstructorTransaction(listeningPoint);
			return constructor;
		}
		
		AroonTransactionConstructor defaultConstrutor = createConstructorTransaction(listeningPoint);
		return defaultConstrutor;
	}
	
	/**
	 * Use bind(). Listening port will be binding with the remote accept port.
	 * @param localListeningPoint
	 * @param remotePort
	 */
	public void bind(ListeningPoint localListeningPoint, int remotePort){
		try {
			if(localListeningPoint == null)
				throw new Exception("ListeningPoint is null!");
			
			localListeningPoint.setPeerPort(remotePort);
			createAcceptTransaction(localListeningPoint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create AroonTransactionAcceptor object with ListeningPoint object.
	 * @param localListeningPoint
	 */
	private void createAcceptTransaction(ListeningPoint localListeningPoint) {
		String listeningPointKey = localListeningPoint.getLocalListeningPointName();
		listeningPoints.put(listeningPointKey, localListeningPoint);
		try {
			AroonTransactionAcceptor acceptor = null;
			if(!acceptorTable.containsKey(listeningPointKey)){
				acceptor = new AroonTransactionAcceptor(this, localListeningPoint);
				acceptorTable.put(listeningPointKey, acceptor);
			}else{
				acceptor = acceptorTable.get(listeningPointKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create TransactionConstructor with bind sending end port.
	 * @param localListeningPoint
	 * @param acceptor
	 */
	public void createConstructorTransaction(ListeningPoint localListeningPoint,
			AroonTransactionAcceptor acceptor) {
		try {
			String listeningPointKey = localListeningPoint.getLocalListeningPointName();
			String peerlisteningPointKey = localListeningPoint.getPeerListeningPointName();
			AroonTransactionConstructor constructor = null;
			if(!listeningPointKey.equals(peerlisteningPointKey)){
				if(!constructorTable.containsKey(peerlisteningPointKey)){
					constructor = acceptor.createConstructorTransaction(localListeningPoint);
					constructorTable.put(peerlisteningPointKey, constructor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create TransactionConstructor without bind sending end port.
	 * @param localListeningPoint
	 */
	public AroonTransactionConstructor createConstructorTransaction(ListeningPoint localListeningPoint){
		
		return null;
	}

	public void bind(ListeningPoint listeningPoint){
		try {
			if(listeningPoint == null)
				throw new Exception("ListeningPoint is null!");
			
			createAcceptTransaction(listeningPoint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addNewMessageProcessor(MessageProcessor newMessMessageProcessor){
		this.messageProcessors.add(newMessMessageProcessor);
	}
	
	public Collection<MessageProcessor> getMessageProcessors(){
		return messageProcessors;
	}
	
	public void setMessageProcessorFactory(
			MessageProcessorFactory messageProcessorFactory) {
		this.messageProcessorFactory = messageProcessorFactory;
	}
	
	public MessageProcessorFactory getMessageProcessorFactory(){
		return this.messageProcessorFactory;
	}
	public String getStackName(){
		return "DefaultJustestTransationStack";
	}
	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public int getReceiveUdpBufferSize() {
		return 8000;
	}

	public void addProtocolParser(ListeningPoint listeningPoint, ProtocolParser protocolParser){
		String acceptorName = listeningPoint.getLocalListeningPointName();
		AroonTransactionAcceptor acceptor = acceptorTable.get(acceptorName);
		acceptor.setProtocolParser(protocolParser);
	}

	public void addProtocolController(ListeningPoint listeningPoint,
			ProtocolController protocolController) {
		String acceptorName = listeningPoint.getLocalListeningPointName();
		AroonTransactionAcceptor acceptor = acceptorTable.get(acceptorName);
		acceptor.setProtocolController(protocolController);
	}
}
