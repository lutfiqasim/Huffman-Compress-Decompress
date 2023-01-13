public class HuffmanCode implements Comparable<HuffmanCode> {
	private char ch;
	private int counter;
	private String huffCode;
	private int codeLength;

	public HuffmanCode(char ch) {
		this.ch = ch;
	}

	public HuffmanCode(char ch, int counter) {
		this.ch = ch;
		this.counter = counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void sethuffCode(String huffCode) {
		this.huffCode = huffCode;
	}

	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}

	public char getCh() {
		return ch;
	}

	public int getCounter() {
		return counter;
	}

	public String getHuffCode() {
		return huffCode;
	}

	public int getCodeLength() {
		return codeLength;
	}

	@Override
	public String toString() {
		return "" + ch + "---->" + counter + "---->" + huffCode + "---->" + +codeLength + "\n";
	}

	@Override
	public int compareTo(HuffmanCode t) {
		return huffCode.compareTo(t.huffCode);
	}

}
