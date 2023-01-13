//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.util.PriorityQueue;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class Driver {
//	public static void main(String[] args) throws IOException {
////		FileInputStream inputStream = new FileInputStream(new File("video to compress.mp4"));
////		BufferedReader bfr = new BufferedReader(new FileReader("ss.txt"));
//		System.out.println("REading file");
//		byte[] frequency = new byte[65536];// last element indicates end of file
//		try (InputStream is = new BufferedInputStream(new FileInputStream(new File("ss.txt")))) {
//			int b;
//			while ((b = is.read()) != -1) {
//				frequency[b]++;
//			}
//		}
//		System.out.println("Finished 1");
//		System.out.println("Calculating frequency");
////		frequency = frequencyCount(s, frequency);
//		int numberOfChars = 0;
//		for (int i = 0; i < frequency.length - 1; i++) {
//			if (frequency[i] > 1) {
//				numberOfChars++;
//				System.out.println((char) i + " Frequency " + frequency[i]);
//			}
//		}
//		// adding presented chars into priority Queue based on their frequencies
//		// Note: Adding them as leafs only so that no char has a child therefore no char
//		// code
//		// is subsequence of the other
//		PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(numberOfChars);
//		for (int i = 0; i < frequency.length - 1; i++) {
//			if (frequency[i] > 0) {
//				HuffmanNode hn = new HuffmanNode();
//				hn.setData((char) i);
//				hn.setFrequency(frequency[i]);
//				hn.setLeft(null);
//				hn.setRight(null);
//				q.add(hn);
//			}
//		}
//		HuffmanNode root = null;
//		// Building the Huffman tree using a priority queue
//		while (q.size() > 1) {
//			HuffmanNode x = q.peek();
//			q.poll();
//			HuffmanNode y = q.peek();
//			q.poll();
//			HuffmanNode f = new HuffmanNode();
//			f.setFrequency(x.getFrequency() + y.getFrequency());
//			f.setData('-');
//			f.setLeft(x);
//			f.setRight(y);
//			root = f;
//			q.add(f);
//		}
//		System.out.println("Char    ||| Huffman CODE");
//		System.out.println("______________________________________");
//		printHuffmanCode(root, "");
////		printIntoFile(root, s);
//		String[] table = buildCodeTable(root);
//		encodeHeader(root, new BitOutputStream("compress2.txt", false), "s");
//		encodeToFile(table, "compress2.txt");
//	}
//
//	private static void encodeHeader(HuffmanNode root, BitOutputStream bos, String s) throws IOException {
//		if (root == null) {
//			bos.writeBit(0);
//			return;
//		}
//		if (root.getLeft() == null && root.getRight() == null && Character.isAlphabetic(root.getData())) {
//			bos.writeBit(1);
//			int towrite = root.getData();
//			String s2 = Integer.toBinaryString(towrite);
//			char[] c2 = s2.toCharArray();
//			for (int i = 0; i < c2.length; i++) {
//				bos.writeBit(c2[i] - '0');
//			}
//		}
////		 else {
////			bos.writeBit(0);
////		}
//		encodeHeader(root.getLeft(), bos, s);
//		encodeHeader(root.getRight(), bos, s);
//	}
//
////	private static void printHuffmanCode(HuffmanNode root, String s) {
////		if (root == null)
////			return;
////		if (root.getLeft() == null && root.getRight() == null && Character.isAlphabetic(root.getData())) {
////			System.out.println(root.getData() + "  |   " + s);
////			return;
////		}
////		printHuffmanCode(root.getLeft(), s + "0");
////		printHuffmanCode(root.getRight(), s + "1");
////
////	}
////	public static void encodeToFile(String data, String[] table, String fileName) {
////		try (FileOutputStream fos = new FileOutputStream(fileName)) {
////			for (char ch : data.toCharArray()) {
////				String code = table[ch];
////				for (char bit : code.toCharArray()) {
////					fos.write(bit - '0');
////				}
////			}
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
//	public static void encodeToFile(String[] table, String fileName) {
//		try (InputStream in = new BufferedInputStream(new FileInputStream("ss.txt"))) {
//			try (BitOutputStream output = new BitOutputStream(fileName, true)) {
//				for (int ch = in.read(); ch != -1; ch = in.read()) {
//					String code = table[ch];
//					if (code == null)
//						continue;
//					for (char bit : code.toCharArray()) {
//						output.writeBit(bit - '0');
//					}
//				}
//			}
//		} catch (IOException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
//
//	// Generate the encoding table
//	public static String[] buildCodeTable(HuffmanNode root) {
//		String[] table = new String[65536];
//		buildCodeTable(root, "", table);
//		return table;
//	}
//
//	private static void buildCodeTable(HuffmanNode node, String s, String[] table) {
//		if (node.getLeft() == null && node.getRight() == null && node.getData() <= 65535) {
//			table[node.getData()] = s;
//			return;
//		}
//		buildCodeTable(node.getLeft(), s + '0', table);
//		buildCodeTable(node.getRight(), s + '1', table);
//	}
//
//	private static void printHuffmanCode(HuffmanNode root, String s) {
//		if (root == null)
//			return;
//		if (root.getLeft() == null && root.getRight() == null && Character.isAlphabetic(root.getData())) {
//			System.out.println(root.getData() + "  |   " + s);
//			return;
//		}
//		printHuffmanCode(root.getLeft(), s + "0");
//		printHuffmanCode(root.getRight(), s + "1");
//
//	}
//
//	private static byte[] frequencyCount(String s, byte[] freq) {
//		int i = 0;
//		while (i != s.length()) {
//			freq[s.charAt(i)]++;
//			i++;
//		}
//		freq[freq.length - 1] = (byte) i;
//		return freq;
//	}
//
//}
