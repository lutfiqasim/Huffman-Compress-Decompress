
////import java.io.FileInputStream;
////import java.io.IOException;
////
////public class CharacterCount {
////    public static void main(String[] args) throws IOException {
////        // Create an array of 256 bytes to store the character counts
////        // (one for each possible value of a byte)
////        byte[] charCounts = new byte[256];
////
////        // Open the file
////        FileInputStream inputStream = new FileInputStream("ss.txt");
////        int c;
////        // Read each character in the file one by one
////        while ((c = inputStream.read()) != -1) {
////            // Increment the count for this character
////            charCounts[c]++;
////        }
////        inputStream.close();
////
////        // Print the character counts
////        for (int i = 0; i < 256; i++) {
////            if (charCounts[i] > 0) {
////                System.out.println((char) i + ": " + charCounts[i]);
////            }
////        }
////    }
////}
////From a file
////import java.io.FileInputStream;
////import java.io.IOException;
////
////public class CharacterCount {
////    public static void main(String[] args) throws IOException {
////        // Create an array of 65536 integers to store the frequency of each character
////        int[] charCounts = new int[65536];
////
////        // Open the file
////        FileInputStream inputStream = new FileInputStream("Lotf's Qasim useCase.docx");
////        int c;
////        // Read each character in the file one by one
////        while ((c = inputStream.read()) != -1) {
////            // Increment the count for this character
////            charCounts[c]++;
////        }
////        inputStream.close();
////
////        // Print the character counts
////        for (int i = 0; i < 65536; i++) {
////            if (charCounts[i] > 0) {
////                System.out.println((char) i + ": " + charCounts[i]);
////            }
////        }
////    }
////}
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//public class CharacterCount {
//	public static void main(String[] args) throws IOException {
//		// Open the file
//		File file = new File("ss.txt");
//		FileInputStream inputStream = new FileInputStream(file);
//		int c;
//		int count = 0;
//		// Read each byte in the file one by one
//		while ((c = inputStream.read()) != -1) {
//			// Process the byte
////			String b = String.valueOf(c);
//			System.out.println(Integer.toBinaryString(c));
//			count++;
//		}
//		System.out.println(count);
//		inputStream.close();
//	}
//}