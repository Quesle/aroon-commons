package org.aroon.commons.socket.example.parser;

public class ByteUtil {
	public static int byte16ToInt(byte[] bytes, int index){
        return 	(((bytes[  index] & 0XFF) << 8) +
                (bytes[++index] & 0XFF));
	}
	
	public static int intToByte16(int value, byte[] bytes, int index){
		bytes[  index] = (byte)((value >> 8) & 0XFF);
		bytes[++index] = (byte)(value & 0XFF);
        return 2;
    }
}
