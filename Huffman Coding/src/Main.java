
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {
	static int huffmanCodeArraySize;
	static HuffmanCode[] huffmanCodearr;
	static int originalSize;
	static int compressedSize;
	static String name = "";
	static File file;
	final static Font font4 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 18);
	static String headerData = new String("");

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Huffman Encoding-Decoding");
		BorderPane rootPane = new BorderPane();
		rootPane.setPadding(new Insets(40));
		// CompressButton
		Button compressButoon = new Button("Chose a file to compress");
		compressButoon.setFont(font4);
		compressButoon.setPrefSize(300, 100);
		// Decompress button
		Button decompressButton = new Button("Chose a file to decompress");
		decompressButton.setFont(font4);
		decompressButton.setPrefSize(300, 100);
		HBox choicesBox = new HBox(100);
//		choicesBox.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		choicesBox.getChildren().addAll(compressButoon, decompressButton);
		Label treeLabel = new Label("Huffman Tree Encoding");
//		treeLabel.setStyle("-fx-border-color:black;");
		treeLabel.setTextFill(Color.WHITE);
		treeLabel.setFont(font4);
		TextArea treeArea = new TextArea();

		Label HeaderLable = new Label("Header Infomation");
		HeaderLable.setTextFill(Color.WHITE);
		HeaderLable.setFont(font4);
		TextArea headerArea = new TextArea();
		headerArea.setFont(font4);
		headerArea.setEditable(false);
		headerArea.setPromptText("Header Information");
		headerArea.setPrefHeight(200);
		headerArea.setPrefWidth(300);
		treeArea.setFont(font4);

		treeArea.setEditable(false);
		treeArea.setPromptText("Enter your address here");
		treeArea.setPrefHeight(200);
		treeArea.setMaxWidth(370);
		// Header Info
		VBox headerBox = new VBox();
		headerBox.setSpacing(20);
		headerBox.setPadding(new Insets(0, 0, 10, 0));
		headerBox.getChildren().addAll(HeaderLable, headerArea);
		// treeInfo
		VBox treeBox = new VBox();
		treeBox.setSpacing(20);
		treeBox.setPadding(new Insets(0, 0, 10, 0));
		treeBox.getChildren().addAll(treeLabel, treeArea);
		// all data boxes
		HBox dataBox = new HBox();
		dataBox.setSpacing(30);
		dataBox.setPadding(new Insets(0, 0, 10, 0));
		dataBox.getChildren().addAll(treeBox, headerBox);
		// Adding them to pane
		rootPane.setTop(choicesBox);
		rootPane.setCenter(dataBox);
		BorderPane.setMargin(choicesBox, new Insets(0, 0, 30, 0));
		BorderPane.setAlignment(choicesBox, Pos.CENTER);
		BorderPane.setAlignment(dataBox, Pos.CENTER);
		rootPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(47, 31, 31), CornerRadii.EMPTY, Insets.EMPTY)));
		Scene scene1 = new Scene(rootPane, 750, 450);
		primaryStage.setScene(scene1);
		primaryStage.show();
		compressButoon.setOnAction(e -> {
			CompressAFile();
			if (huffmanCodearr != null) {
				// Find percetange of compression
				double percentage = (100.0 - (((double) compressedSize) / originalSize) * 100.0);
				confirmation_Message("Compressing Done\n Percentage compressed =" + percentage + "");
				String s = headerData + "Original Size" + originalSize + "\n" + "Compressed Size" + compressedSize
						+ "\n";
				headerArea.setText(s);
				treeArea.clear();
				treeArea.setText("char" + "-->" + "count" + "-->" + "Code" + "-->" + "codeLength\n");
				for (int i = 0; i < huffmanCodearr.length; i++) {
					if (huffmanCodearr[i] != null)
						treeArea.appendText(huffmanCodearr[i].toString());
				}
			}
		});
		decompressButton.setOnAction(e -> {
			boolean statusOfDecompress = Decompress22();
			if (statusOfDecompress)
				confirmation_Message("Decompressed File successfully");
		});
	}

	// Self note: try to edit the header
	// current total size of header
	/*
	 * the size of the file path string, plus 1 for the newline character at the end
	 * the size of the string representation of the original size of the file, plus
	 * 1 for the newline character at the end the size of the string representation
	 * of the number of unique characters in the file, plus 1 for the newline
	 * character at the end the size of each character and its encoded value, plus 1
	 * for the newline character at the end
	 */
	private static void CompressAFile() {
		try {

			FileChooser fileChooser = new FileChooser();
			file = fileChooser.showOpenDialog(null);
//			file = new File("code.txt");
			String filesMainPath = file.getAbsolutePath().toString();
			headerData += filesMainPath + "\n";
			System.out.println(file.getAbsolutePath().toString());
			String outFile = (filesMainPath.substring(0, filesMainPath.lastIndexOf(".")) + ".huff");
			// Saves the frequency of each character
			byte[] frequency = new byte[1024];
			int[] temp = new int[256];
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			// filesMainPath.substring(filesMainPath.lastIndexOf("\\") + 1)
			String fileName = filesMainPath;
			originalSize = is.available();// Gets the number of chars in original file
			int size = is.read(frequency, 0, frequency.length);// reads 1024 bytes into frequency array
			int index = -1;
			do {
				for (int i = 0; i < size; i++) { // put he value to the tmp array
					index = frequency[i];
					if (index < 0)// Deals with negative bytes so it adds 256 to it to become positive
						index += 256;
					if (temp[index] == 0)
						huffmanCodeArraySize++;// Counting the number of different
												// characters for huffman code
					temp[index]++;// adds count to charactes
				}
				size = is.read(frequency, 0, 1024);// re-reads a new 1024 bytes of data
			} while (size > 0);
			for (int i = 0; i < frequency.length; i++)// re initialize frequency with 0's
				frequency[i] = 0;
			int tracker = 0;// tracks the number of chars represented in-order to shrink the huffman code
							// array
			int nbChar = 0;// counts total number of characters
			huffmanCodearr = new HuffmanCode[huffmanCodeArraySize];
			for (int i = 0; i < 256; i++) {
				if (temp[i] > 0) {// if character is represented added it to huffman-code-array
					huffmanCodearr[tracker++] = new HuffmanCode((char) i, temp[i]);// Shrinking code array to only chars
																					// // // that were represented
					nbChar += temp[i];// Saves count of total character in orginal file
					temp[i] = 0;
				}
			}
			if (huffmanCodeArraySize != 1) {// if file has more than one unique character
				HuffmanNode[] hufNodes = new HuffmanNode[huffmanCodeArraySize];
				PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>(huffmanCodeArraySize + 10);
				for (int i = 0; i < huffmanCodeArraySize; i++) {// creating huffman codes
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
				// saves ascii frequency in string-->
				// saves codes of each char according to it's ascii val in the array
				outToFileData[(int) huffmanCodearr[i].getCh()] = new String(huffmanCodearr[i].getHuffCode());
			}
//			String outFile = new String("output2.huff");
			FileOutputStream output = new FileOutputStream(outFile);
			byte[] outBuffer = new byte[1024];
			tracker = 0;
			// name of original File header write to file Note each header data is seperated
			// with a new line
			for (int i = 0; i < fileName.length(); i++)
				outBuffer[tracker++] = (byte) fileName.charAt(i);
			outBuffer[tracker++] = '\n';//
			// Number of charactes in the original file
			String nbchar = String.valueOf(nbChar);
			for (int i = 0; i < nbchar.length(); i++) {
				outBuffer[tracker++] = (byte) nbchar.charAt(i);
			}
			outBuffer[tracker++] = '\n';
			// number of unique characters
			// prints number of unique characters as bytes saved
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
				if (tracker == 1024) {// if we have reached 1024 bits write it
					output.write(outBuffer);
					tracker = 0;
				}
				// will start if the buffer is no full yet to
				// add charcters
				outBuffer[tracker++] = (byte) huffmanCodearr[i].getCh();
				if (tracker == 1024) {
					output.write(outBuffer);
					tracker = 0;
				}
				// Add the Counter
				outBuffer[tracker++] = (byte) huffmanCodearr[i].getCodeLength();
				String res = "";
				Long x; // if the huff-code up than 15 will div to two and work on it and re build it
				if (huffmanCodearr[i].getHuffCode().length() > 15) {
					for (int z = 0; z < huffmanCodearr[i].getHuffCode().length() / 2; z++) {// sets huffman code to half
						res += huffmanCodearr[i].getHuffCode().charAt(z) + "";
					}
					x = Long.parseLong(res);
					res = "";
					for (int z = (huffmanCodearr[i].getHuffCode().length() + 1) / 2; z < huffmanCodearr[i].getHuffCode()
							.length(); z++) {// builds the other half and add then adds it
						res += huffmanCodearr[i].getHuffCode().charAt(z) + "";
					}
					x += Long.parseLong(res);

				} else {
					x = Long.parseLong(huffmanCodearr[i].getHuffCode());
				}
				byte[] code = new byte[50];
				int l = 0;
				if (x == 0) {// if x==0 then write the code normally
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
				} else {// while it doesn't equal 0, we have to get ones and zeros and write the
						// representation of the code
					while (x != 0) {
						if (tracker == 1024) {// write current bytes
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
					for (int j = 0; j < l; j++) {// l holds the data len
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
			output.write(outBuffer, 0, tracker);// print the rest of header
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
			is.close();
			is = new FileInputStream(outFile);
			compressedSize = is.available();
			is.close();
			outputStream.close();
		} catch (IOException e) {
			warning_Message("Error while Compressing a file");
		} catch (Exception e) {
			warning_Message("Make sure to choose a file that is not huff");
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

	// Start of the decompression
	private static boolean Decompress22() {
		try {

			int size = 0;
//			String fileName = "output2.huff";
//			fileName = Path.getText();
			int tracker = 0;
			int bufferTracker = 0;
			boolean flag = true;
			String originalFileName = "";
//			File file = new File(fileName);
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("compressed", "*.huff"));
			file = fileChooser.showOpenDialog(null);
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
			// iterates through each character found tree where each character code is saved
			// into the buffer at index of that char, if zero is found we go left of the
			// constructed tree of
			// De-compress Tree other wise we go right
			// until a leaf is found then we print the compressed char as it's original
			// value
			do {
				while (tree.left != null || tree.right != null) {
					if ((buffer[index] & (1 << 7 - bufferTracker)) == 0) {
						if (tree.left != null)
							tree = tree.left;
					} else {
						if (tree.right != null)
							tree = tree.right;
					}
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
		} catch (IOException e) {
			warning_Message("Error in Decompression");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
//			e.printStackTrace();
			warning_Message("Choosen file to decompress");
			return false;
		}
		return true;

	}// Decompress Finish

	public static void warning_Message(String x) {
		Alert alert = new Alert(AlertType.NONE);
		alert.setAlertType(AlertType.WARNING);
		alert.setContentText(x);
		alert.show();
	}

	public static void confirmation_Message(String x) {
		Alert alert = new Alert(AlertType.NONE);
		alert.setAlertType(AlertType.CONFIRMATION);
		alert.setContentText(x);
		alert.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}