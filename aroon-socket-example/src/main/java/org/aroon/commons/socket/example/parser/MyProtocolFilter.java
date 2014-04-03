package org.aroon.commons.socket.example.parser;

import org.aroon.commons.socket.context.ProtocolFilter;

public class MyProtocolFilter implements ProtocolFilter{

	@Override
	public Object doFilter(Object clas) {
		try {
			if(clas instanceof MyProtocol){
				MyProtocol protocol = (MyProtocol) clas;
				if(protocol.getLength() > 15){
					throw new Exception("The protocol data seguments is too lang.");
				}
				return clas;
			}
			throw new Exception("Class type is wrong");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
