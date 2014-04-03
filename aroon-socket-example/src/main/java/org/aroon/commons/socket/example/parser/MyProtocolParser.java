package org.aroon.commons.socket.example.parser;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.aroon.commons.socket.context.ProtocolParser;
import org.aroon.commons.socket.manager.BlockingDatagramPackage;

public class MyProtocolParser implements ProtocolParser{
	private MyProtocol protocol = null;
	
	@Override
	public Object processProtocolResolver(
			BlockingDatagramPackage blockingPacket) {
		DatagramPacket packet = blockingPacket.getDatagramPacket();
		int packetLength = packet.getLength();
		
		byte[] bytes = packet.getData();
		byte[] messageBytes = new byte[packetLength];
		System.arraycopy(bytes, 0, messageBytes, 0, packetLength);
		System.out.println(Arrays.toString(messageBytes));
		
		protocol = parserBytesArrayProtocol(messageBytes);
		return protocol;
	}
	
	private MyProtocol parserBytesArrayProtocol(byte[] messageBytes) {
		MyProtocol protocol = new MyProtocol();
		int index = 0;
		protocol.setLength(ByteUtil.byte16ToInt(messageBytes, index));
		index += 2;
		try {
		if(protocol.getLength() < 0 ){
			throw new Exception("Protocol length is wrong!");
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<Integer, String> protocolSegments = new HashMap<Integer, String>();
		for (int i = 0; i < protocol.getLength(); i++) {
			int type = ByteUtil.byte16ToInt(messageBytes, index);
			index += 2;
			int length = ByteUtil.byte16ToInt(messageBytes, index);
			index += 2;
			if(protocolSegments.containsKey(type)){
				try {
					throw new Exception("The protocol not invalid. There is the same types.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			StringBuilder stringBuilder = new StringBuilder();
			for (int j = 0; j < length; j++) {
				stringBuilder.append((char)messageBytes[index++]);
			}
			protocolSegments.put(type, stringBuilder.toString());
		}
		protocol.setProtocolSegments(protocolSegments);
		return protocol;
	}
	
	@Override
	public BlockingDatagramPackage processProtocolCompiler(Object object) {
		BlockingDatagramPackage blockingDatagramPackage = null;
		try {
			MyProtocol protocol = null;
			if(object instanceof MyProtocol){
				protocol = (MyProtocol) object;
			}else{
				throw new Exception("Class type not know.");
			}
			/*InetSocketAddress inet = new InetSocketAddress("192.168.1.211", port);
			byte[] response = createBytesCompiler(protocol);
			DatagramPacket datagramPacket = new DatagramPacket(response, response.length);
			datagramPacket.setSocketAddress(inet);
			blockingDatagramPackage = new BlockingDatagramPackage();
			blockingDatagramPackage.setDatagramPacket(datagramPacket);*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blockingDatagramPackage;
	}

	private byte[] createBytesCompiler(MyProtocol protocol) {
		int lenght = protocol.getProtocolSegments().size();
		byte[] list = createProtocolSegmentsBytes(protocol);
		byte[] resposne = new byte[2 + list.length];
		ByteUtil.intToByte16(lenght, resposne, 0);
		System.arraycopy(list, 0, resposne, 2, list.length);
		return resposne;
	}

	private byte[] createProtocolSegmentsBytes(MyProtocol protocol) {
		List<byte[]> list = new ArrayList<byte[]>();
		Iterator<Integer> keys = protocol.getProtocolSegments().keySet().iterator();
		int lenght = 0;
		while (keys.hasNext()) {
			int key = keys.next();
			String str = protocol.getProtocolSegments().get(key);
			byte[] bytes = new byte[4 + str.length()];
			int index = 0;
			ByteUtil.intToByte16(key, bytes, index);
			index += 2;
			ByteUtil.intToByte16(str.length(), bytes, index);
			index += 2;
			char[] c = str.toCharArray();
			for (int i = 0; i < c.length; i++) {
				bytes[index++] = (byte) c[i];
			}
			lenght += bytes.length;
			list.add(bytes);
		}
		
		byte[] segments = new byte[lenght];
		int indexi = 0;
		for (int i = 0; i < list.size(); i++) {
			System.arraycopy(list.get(i), 0, segments, indexi, list.get(i).length);
			indexi += list.get(i).length;
		}
		return segments;
	}
	
}
