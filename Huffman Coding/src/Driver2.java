import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;

import javax.swing.JOptionPane;

public class Driver2 {
	static int huffmanCodeArraySize;
	static HuffmanCode[] huffmanCodearr;
	static int originalSize;
	static int compressedSize;
	static String name = "";

	public static void main(String[] args) throws IOException {
		Decompress22();
		System.exit(1);

		try {
			File file = new File("30sec.mp4");
			byte[] frequency = new byte[1024];
			int[] temp = new int[256];
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			String fileName = new String("30sec.mp4");
			originalSize = is.available();
			int size = is.read(frequency, 0, frequency.length);
			int index = -1;
			do {
				for (int i = 0; i < size; i++) { // put he value to the tmp array
					index = frequency[i];
					if (index < 0)
						index += 256;
					if (temp[index] == 0)
						huffmanCodeArraySize++;// Counting the number of different
												// characters for huffman code
					temp[index]++;
				}
				size = is.read(frequency, 0, 1024);
			} while (size > 0);
			for (int i = 0; i < frequency.length; i++)
				frequency[i] = 0;
			int tracker = 0;
			int nbChar = 0;
			huffmanCodearr = new HuffmanCode[huffmanCodeArraySize];
			for (int i = 0; i < 256; i++) {
				if (temp[i] > 0) {
					huffmanCodearr[tracker++] = new HuffmanCode((char) i, temp[i]);// Shrinking code array to only chars
																					// // // that were represented
					nbChar += temp[i];// Saves count of total character in orginal file
					temp[i] = 0;
				}
			}
			if (huffmanCodeArraySize != 1) {
				HuffmanNode[] hufNodes = new HuffmanNode[huffmanCodeArraySize];
				PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>(huffmanCodeArraySize + 10);
				for (int i = 0; i < huffmanCodeArraySize; i++) {
					hufNodes[i] = new HuffmanNode(huffmanCodearr[i].getCounter(), huffmanCodearr[i].getCh());
					queue.add(hufNodes[i]);// adding elements of huffmanNodes-Trees to queue to build the huffman tree
				}
				HuffmanNode root = null;
				// Building the Huffman tree using a priority queue
				while (queue.size() > 1) {
					HuffmanNode x = queue.peek();
					queue.poll();
					HuffmanNode y = queue.peek();
					queue.poll();
					HuffmanNode f = new HuffmanNode();
					f.setFrequency(x.getFrequency() + y.getFrequency());
					f.setData('-');
					f.setLeft(x);
					f.setRight(y);
					root = f;
					queue.add(f);
				}
				getCodes(root, "");
			} else {// in case of only one unique char
				huffmanCodearr[0].sethuffCode("1");
				huffmanCodearr[0].setCodeLength(1);
			}
			String[] outToFileData = new String[256];
			for (int i = 0; i < huffmanCodeArraySize; i++) {
				// saves ascii frequency in string
				outToFileData[(int) huffmanCodearr[i].getCh()] = new String(huffmanCodearr[i].getHuffCode());
			}
			String outFile = new String("output2.huff");
			FileOutputStream output = new FileOutputStream(outFile);
			byte[] outBuffer = new byte[1024];
			tracker = 0;
			// name of original File header write to file
			for (int i = 0; i < fileName.length(); i++)
				outBuffer[tracker++] = (byte) fileName.charAt(i);
			outBuffer[tracker++] = '\n';
			// Number of charactes in the original file
			String nbchar = String.valueOf(nbChar);
			for (int i = 0; i < nbchar.length(); i++) {
				outBuffer[tracker++] = (byte) nbchar.charAt(i);
			}
			outBuffer[tracker++] = '\n';
			// number of unique characters
			for (int i = 0; i < String.valueOf(huffmanCodeArraySize).length(); i++) {// note might cause a problem
				outBuffer[tracker++] = (byte) (String.valueOf(huffmanCodeArraySize).charAt(i));
			}

			outBuffer[tracker++] = '\n';
			output.write(outBuffer, 0, tracker);// writes the header data into the file without compressing here
			tracker = 0;
			for (int i = 0; i < outBuffer.length; i++)
				outBuffer[i] = 0;
			// The HuffCode for Each Character
			for (int i = 0; i < huffmanCodeArraySize; i++) {
				if (tracker == 1024) {
					output.write(outBuffer);
					tracker = 0;
				}
				outBuffer[tracker++] = (byte) huffmanCodearr[i].getCh(); // will start if the buffer is no full yet to
																			// addd
				// charcters
				if (tracker == 1024) {
					output.write(outBuffer);
					tracker = 0;
				}
				// Add the Counter
				outBuffer[tracker++] = (byte) huffmanCodearr[i].getCodeLength();
				String res = "";
				Long x; // if the huffcode up than 15 will div to two and work on it and re buion it
				if (huffmanCodearr[i].getHuffCode().length() > 15) {
					for (int z = 0; z < huffmanCodearr[i].getHuffCode().length() / 2; z++) {
						res += huffmanCodearr[i].getHuffCode().charAt(z) + "";
					}
					x = Long.parseLong(res);
					res = "";
					for (int z = (huffmanCodearr[i].getHuffCode().length() + 1) / 2; z < huffmanCodearr[i].getHuffCode()
							.length(); z++) {
						res += huffmanCodearr[i].getHuffCode().charAt(z) + "";
					}
					x += Long.parseLong(res);

				} else {
					x = Long.parseLong(huffmanCodearr[i].getHuffCode());
				}
				byte[] code = new byte[50];
				int l = 0;
				if (x == 0) {
					outBuffer[tracker++] = 0;
					if (tracker == 1024) {
						output.write(outBuffer);
						tracker = 0;
					}
					outBuffer[tracker++] = 0;
					if (tracker == 1024) {
						output.write(outBuffer);
						tracker = 0;
					}
				} else {
					while (x != 0) {
						if (tracker == 1024) {
							output.write(outBuffer);
							tracker = 0;
						}
						code[l++] = (byte) (x % 256);
						x /= 256;
					}
					outBuffer[tracker++] = (byte) l;
					if (tracker == 1024) {
						output.write(outBuffer);
						tracker = 0;
					}
					for (int j = 0; j < l; j++) {
						outBuffer[tracker++] = code[j];
						if (tracker == 1024) {
							output.write(outBuffer);
							tracker = 0;
						}
					}
				}

				if (tracker == 1024) {
					output.write(outBuffer);
					tracker = 0;
				}
				outBuffer[tracker++] = '\n';
			} // end for
				// Print Out The Header
			output.write(outBuffer, 0, tracker);
			for (int i = 0; i < outBuffer.length; i++)
				outBuffer[i] = 0;
			output.close();
			is.close();
			is = new FileInputStream(file);
			BitOutputStream outputStream = new BitOutputStream(outFile, true);
			for (int n = is.read(); n != -1; n = is.read()) {
				String code = outToFileData[n];
				if (code == null)
					continue;
				for (char c : code.toCharArray()) {
					outputStream.writeBit(c - '0');
				}
			}
			// heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
//			is.close();
//			is = new FileInputStream(fileName);
//			tracker = 0;
//			size = is.read(frequency, 0, 1024);
//			do {
//				for (int i = 0; i < size; i++) {
//					index = frequency[i];
//					if (index < 0)// If the Value was negative
//						index += 256;
//					for (int j = 0; j < outToFileData[index].length(); j++) {
//						char ch = outToFileData[index].charAt(j);
//						if (ch == '1')
//							outBuffer[tracker / 8] = (byte) (outBuffer[tracker / 8] | 1 << 7 - tracker % 8);
//						tracker++;
//						if (tracker / 8 == 1024) {
//							output.write(outBuffer);
//							for (int k = 0; k < outBuffer.length; k++)
//								outBuffer[k] = 0;
//							tracker = 0;
//						}
//					}
//				}
//				size = is.read(frequency, 0, 1024);
//			} while (size > 0);
//			output.write(outBuffer, 0, (tracker / 8) + 1);
//			output.close();
//			is.close();
//			is = new FileInputStream(file);
//			compressedSize = is.available();
//			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getCodes(HuffmanNode hfN, String code) {// Gets codes for each unique char and saves in in the
																// huffman code array
		// Using recursive methods to iterate through Huffmna Node tree
		// Note: post order traversal of huffman node
		if (hfN.getLeft() == null & hfN.getRight() == null) {
			for (int i = 0; i < huffmanCodeArraySize; i++) {// iterates through all different chars represented in
															// current file
				if (huffmanCodearr[i].getCh() == hfN.getData()) {
					huffmanCodearr[i].sethuffCode(code);
					huffmanCodearr[i].setCodeLength(code.length());
				}
			}
		} else {
			getCodes(hfN.getLeft(), code + '0');
			getCodes(hfN.getRight(), code + '1');
		}
	}

	private static void Decompress22() {
		try {

			int size = 0;
			String fileName = "output2.huff";
//			fileName = Path.getText();
			int tracker = 0;
			int bufferTracker = 0;
			boolean flag = true;
			String originalFileName = "";
			File file = new File(fileName);

			// Status.setText("Reading the Header");
			@SuppressWarnings("resource")
			FileInputStream scan = new FileInputStream(file);
			byte[] buffer = new byte[1024];

			// Get The File Name
			size = scan.read(buffer, 0, 1024);
			char[] tmp = new char[256];
			while (flag) {
				if (buffer[tracker] != '\n')
					tmp[tracker++] = (char) buffer[bufferTracker++];
				else
					flag = false;
			}
			bufferTracker++;
			originalFileName = String.valueOf(tmp, 0, tracker); // the assci

			// Get the Number of Characters in the file
			long nbChar = 0;
			tracker = 0;
			flag = true;
			while (flag) {
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				if (buffer[bufferTracker] != '\n')
					tmp[tracker++] = (char) buffer[bufferTracker++];
				else
					flag = false;
			}
			nbChar = Integer.parseInt(String.valueOf(tmp, 0, tracker));
			bufferTracker++;

			// Get the Number of Distinct characters
			int loopSize = 0;
			tracker = 0;
			flag = true;
			while (flag) {
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				if (buffer[bufferTracker] != '\n')
					tmp[tracker++] = (char) buffer[bufferTracker++];
				else
					flag = false;
			}
			loopSize = Integer.parseInt(String.valueOf(tmp, 0, tracker));
			bufferTracker++;

			// Reading the huff Code
			huffmanCodearr = new HuffmanCode[loopSize];
			huffmanCodeArraySize = loopSize;
			for (int i = 0; i < loopSize; i++) {
				huffmanCodearr[i] = new HuffmanCode((char) Byte.toUnsignedInt(buffer[bufferTracker++]));
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				huffmanCodearr[i].setCodeLength(buffer[bufferTracker++]);
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				int l = buffer[bufferTracker++];
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				if (l == 0)
					bufferTracker++;
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				long x = 0;
				for (int j = 0; j < l; j++) {
					x += Byte.toUnsignedLong(buffer[bufferTracker++]) * (1 << 8 * j);
					if (bufferTracker == 1024) {
						size = scan.read(buffer, 0, 1024);
						bufferTracker = 0;
					}
				}
				huffmanCodearr[i].sethuffCode(String.valueOf(x));
				if (huffmanCodearr[i].getHuffCode().length() != huffmanCodearr[i].getCodeLength()) {
					String s = "";
					for (int j = 0; j < huffmanCodearr[i].getCodeLength()
							- huffmanCodearr[i].getHuffCode().length(); j++)
						s += "0";
					s += huffmanCodearr[i].getHuffCode();
					huffmanCodearr[i].sethuffCode(s);
				}
				bufferTracker++;
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
			}

			// Status.setText("Extract the File");

			DecompressTree tree = new DecompressTree();
			for (int i = 0; i < huffmanCodeArraySize; i++) {
				tree = DecompressTree.addElt(tree, huffmanCodearr[i].getHuffCode(), 0, huffmanCodearr[i].getCh());
			}
			for (int i = 0; i < tmp.length; i++)
				tmp[i] = '\0';

			if (bufferTracker == 1024) {
				size = scan.read(buffer, 0, 1024);
				bufferTracker = 0;
			}

			int index = bufferTracker;
			bufferTracker = 0;
			byte[] outputBuffer = new byte[1024];
			tracker = 0;
			File n = new File(name);

			if (n.exists())
				n.delete();

			FileOutputStream output = new FileOutputStream(originalFileName);
			DecompressTree root = tree;
			long count = 0;
			flag = false;
			do {
				while (tree.left != null || tree.right != null) {
					if ((buffer[index] & (1 << 7 - bufferTracker)) == 0)
						tree = tree.left;
					else
						tree = tree.right;
					bufferTracker++;
					if (bufferTracker == 8) {
						bufferTracker = 0;
						index++;
						if (index == 1024) {
							size = scan.read(buffer, 0, 1024);
							index = 0;
							if (size == -1)
								flag = true;
						}
					}
				}
				if (flag)
					break;
				outputBuffer[tracker++] = (byte) tree.ch;
				if (tracker == 1024) {
					output.write(outputBuffer);
					tracker = 0;
				}
				count++;
				tree = root;
				if (count == nbChar)
					break;
			} while (size != -1);
			output.write(outputBuffer, 0, tracker);
			output.close();
		} catch (Exception e) {
		}
	}// Decompress Finish

}
