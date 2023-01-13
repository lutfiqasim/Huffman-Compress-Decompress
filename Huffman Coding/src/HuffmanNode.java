
public class HuffmanNode implements Comparable<HuffmanNode> {
	private int frequency;
	private char data;
	private HuffmanNode left;
	private HuffmanNode right;

	public HuffmanNode() {
		data = 0;
	}

	public HuffmanNode(int frequency) {
		this.frequency = frequency;
		data = 0;
		left = null;
		right = null;
	}

	public HuffmanNode(int frequency, char data) {
		this.frequency = frequency;
		this.data = data;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public char getData() {
		return data;
	}

	public void setData(char data) {
		this.data = data;
	}

	public HuffmanNode getLeft() {
		return left;
	}

	public void setLeft(HuffmanNode left) {
		this.left = left;
	}

	public HuffmanNode getRight() {
		return right;
	}

	public void setRight(HuffmanNode right) {
		this.right = right;
	}

	@Override
	public int compareTo(HuffmanNode o) {
		return this.getFrequency() - o.getFrequency();
	}
}
