import java.io.FileOutputStream;
import java.io.IOException;

public class BitOutputStream implements java.lang.AutoCloseable {
	private FileOutputStream output;
	private int buffer;
	private int bufferSize;

	public BitOutputStream(String fileName, boolean appendingStatus) throws IOException {
		output = new FileOutputStream(fileName, appendingStatus);
		buffer = 0;
		bufferSize = 0;
	}

	public void close() throws IOException {
		if (bufferSize > 0) {
			output.write(buffer);
		}
		output.close();
	}

	public void writeBit(int b) throws IOException {
//		if (b != 0 && b != 1) {
//			throw new IllegalArgumentException("Argument must be 0 or 1");
//		}
		buffer = (buffer << 1) | b;
		bufferSize++;
		if (bufferSize == 8) {
			output.write(buffer);
			buffer = 0;
			bufferSize = 0;
		}
	}
}