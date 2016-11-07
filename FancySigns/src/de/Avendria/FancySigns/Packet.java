package de.Avendria.FancySigns;
import java.io.IOException;
import java.io.OutputStream;

public interface Packet {
	public void write(OutputStream out) throws IOException;
}
