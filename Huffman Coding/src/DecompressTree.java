public class DecompressTree {
	char ch;
	String huffCode;
	DecompressTree left;
	DecompressTree right;

	public DecompressTree() {
		ch = '\0';
		huffCode = "";
		left = right = null;

	}

	public DecompressTree(char ch, String huffCode) {
		this.ch = ch;
		this.huffCode = huffCode;
		left = right = null;
	}

	public static DecompressTree addElt(DecompressTree tree, String st, int ind, char ch) {
		if (ind < st.length()) {

			if (st.charAt(ind) == '0') {
				if (tree.left == null)
					tree.left = new DecompressTree();
				tree.left = addElt(tree.left, st, ind + 1, ch);
			} else {
				if (tree.right == null)
					tree.right = new DecompressTree();
				tree.right = addElt(tree.right, st, ind + 1, ch);
			}
			return tree;
		} else {
			tree.ch = ch;
			return tree;
		}
	}

	@Override
	public String toString() {
		return "DecompressTree [ch=" + ch + ", huffCode=" + huffCode + ", left=" + left + ", right=" + right + "]\n";
	}

}
