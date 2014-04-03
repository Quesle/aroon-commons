package org.aroon.commons.socket.example.parser;

import java.util.Map;

public class MyProtocol {
	private int length ;
	private Map<Integer, String> protocolSegments;
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Map<Integer, String> getProtocolSegments() {
		return protocolSegments;
	}
	public void setProtocolSegments(Map<Integer, String> protocolSegments) {
		this.protocolSegments = protocolSegments;
	}
}
