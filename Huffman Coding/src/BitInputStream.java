//import java.io.FileInputStream;
//import java.io.IOException;
//
//public class BitInputStream {
//	private FileInputStream input;
//	private int buffer;
//	private int bufferSize;
//
//	public BitInputStream(String fileName) throws IOException {
//		input = new FileInputStream(fileName);
//		buffer = 0;
//		bufferSize = 0;
//	}
//	public void close() throws IOException {
//		input.close();
//	}
//
//	public int readBit() throws IOException {
//		if (bufferSize == 0) {
//			buffer = input.read();
//			if (buffer == -1) {
//				return -1;
//			}
//			bufferSize = 8;
//		}
//		int bit = (buffer >>> (bufferSize - 1)) & 1;
//		bufferSize--;
//		return bit;
//	}
//}