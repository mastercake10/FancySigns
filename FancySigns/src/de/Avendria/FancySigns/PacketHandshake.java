package de.Avendria.FancySigns;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PacketHandshake implements Packet{
	int protocolVersion;
	String serverAddress;
	short port;
	int state; //1 STATUS 2 LOGIN
	
	public PacketHandshake(String serverAddress, short port, int state, int protocolVersion){
		this.protocolVersion = protocolVersion;
		this.serverAddress = serverAddress;
		this.port = port;
		this.state = state;
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		List<Byte> list = new ArrayList<Byte>();
		list.add((byte) 15); //length?
		list.add((byte) 0);  //ID
		list.add((byte) protocolVersion); //Protocl Version
		list.add((byte) serverAddress.length()); //Length of address
		list.addAll(writeString(serverAddress));
		list.add((byte)((port >> 8) & 0xff));
		list.add((byte)(port & 0xff));
		if(state == 1){
			list.add((byte) 1);
			list.add((byte) 1);
			list.add((byte) 0);
		}else{
			list.add((byte) 2);
			list.add((byte) 10);
			list.add((byte) 0);
		}
		/*for(byte b : writeUnsignedVarInt(state)){
			list.add(b);
		}*/
		list.set(0, (byte) (list.size() - 3));
		byte[] ar = new byte[list.size()];
		int i = 0;
		for(byte b : list){
			ar[i] = b;
			i++;
		}
		out.write(ar);
	}
    public static byte[] writeSignedVarInt(int value) {
        return writeUnsignedVarInt((value << 1) ^ (value >> 31));
    }
    public static byte[] writeUnsignedVarInt(int value) {
        byte[] byteArrayList = new byte[10];
        int i = 0;
        while ((value & 0xFFFFFF80) != 0L) {
            byteArrayList[i++] = ((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        byteArrayList[i] = ((byte) (value & 0x7F));
        byte[] out = new byte[i + 1];
        for (; i >= 0; i--) {
            out[i] = byteArrayList[i];
        }
        return out;
    }
	public List<Byte> writeString(String s){
		List<Byte> list = new ArrayList<Byte>();
		for(char c : s.toCharArray()){
			list.add((byte) c);
		}
		return list;
	}
}
