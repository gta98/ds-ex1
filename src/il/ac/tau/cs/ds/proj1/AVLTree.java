package il.ac.tau.cs.ds.proj1;

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with distinct integer keys and info.
 *
 */

public class AVLTree {

	private static final boolean FLAG_DEBUG = true;// true;
	private static final boolean FLAG_VERBOSE = true;

	private static final int ERROR_CANNOT_INSERT = -1, ERROR_CANNOT_DELETE = -1;

	AVLNode root = null;
	BalanceResult lastBalanceResult = null;

	public int joinCostTotal, joinCostCurrent, joinCount;
	public int joinCostMax;
	public float joinCostAvg;

	/**
	 * public AVLTree()
	 * 
	 * @pre: none
	 * @post: empty AVL tree
	 * @complexity: O(1)
	 */
	public AVLTree() {
		this.root = null;
	}

	/**
	 * public AVLTree(AVLNode root)
	 * 
	 * @pre: root=valid AVL subtree root
	 * @post: new AVLTree with specified root as root
	 * @complexity: O(1)
	 */
	public AVLTree(AVLNode root) {
		this.root = root;
	}

	/**
	 * public boolean isValidHierarchy(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: whether or not all recursive children of p are linked to their real
	 *        parents
	 * @complexity: O(n)
	 */
	public boolean isValidHierarchy(IAVLNode p) {
		if (p == null || (!p.isRealNode() && p.getLeft() == null && p.getRight() == null && p.getHeight() == -1)) {
			return true;
		}
		if (p.getLeft().getParent() != p) {
			return false;
		}
		if (p.getRight().getParent() != p) {
			return false;
		}
		return isValidHierarchy(p.getLeft()) && isValidHierarchy(p.getRight());
	}

	/**
	 * public boolean isValidBST(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: forall x in recursive children of p, x.left.key <= x.key <=
	 *        x.right.key
	 * @complexity: O(n)
	 */
	public boolean isValidBST(IAVLNode p) {
		if (p == null || (!p.isRealNode() && p.getLeft() == null && p.getRight() == null)) {
			return true;
		}
		if (p.getLeft().isRealNode() && p.getLeft().getKey() > p.getKey()) {
			return false;
		}
		if (p.getRight().isRealNode() && p.getRight().getKey() < p.getKey()) {
			return false;
		}
		return isValidBST(p.getLeft()) && isValidBST(p.getRight());
	}

	/**
	 * public boolean isValidRank(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: balance factor is either -1, 0 or 1 for all subnodes
	 * @complexity: O(n)
	 */
	public boolean isValidRank(IAVLNode p) {
		if (p == null || (!p.isRealNode() && p.getLeft() == null && p.getRight() == null && p.getHeight() == -1)) {
			return true;
		}
		int diffL, diffR;
		diffL = p.getHeight() - p.getLeft().getHeight();
		diffR = p.getHeight() - p.getRight().getHeight();
		if ((diffL == 1 && diffR == 1) || (diffL == 1 && diffR == 2) || (diffL == 2 && diffR == 1)) {
			return isValidRank(p.getLeft()) && isValidRank(p.getRight());
		}
		logd("Root is " + root.getValue());
		logd("Ranks invalid - put breakpoint here");
		return false;
	}

	/**
	 * public boolean isValidAVL(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: subtree of node p is a valid AVL tree
	 * @complexity: O(n)
	 */
	public boolean isValidAVL(IAVLNode p) {
		if (p==null) return true;
		if (!isValidHierarchy(p)) {
			return false;
		}
		if (!isValidBST(p)) {
			return false;
		}
		if (!isValidRank(p)) {
			return false;
		}
		if (FLAG_DEBUG) {
			if (debugSize(p) != ((AVLNode) p).getSize()) {
				logd("Debug size = " + String.valueOf(debugSize(p)));
				logd("Actual size = " + String.valueOf(((AVLNode) p).getSize()));

				return false;
			}
		}
		return true;
	}

	/**
	 * public boolean isValidAVL()
	 * 
	 * @pre: none
	 * @post: whether or not this is a valid AVL tree
	 * @complexity: O(n)
	 */
	public boolean isValidAVL() {
		return isValidAVL(root);
	}

	/**
	 * public static boolean isValidAVL(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: prints subtree of p inorder
	 * @complexity: O(n)
	 */
	public static void printInOrder(IAVLNode p) {
		if (p == null || (!p.isRealNode() && p.getLeft() == null && p.getRight() == null && p.getHeight() == -1)) {
			return;
		}
		printInOrder(p.getLeft());
		logd(p.getValue());
		printInOrder(p.getRight());
	}

	/**
	 * public static boolean isValidAVL()
	 * 
	 * @pre: none
	 * @post: prints this tree inorder
	 * @complexity: O(n)
	 */
	public void printInOrder() {
		printInOrder(root);
	}

	/**
	 * public boolean empty()
	 *
	 * Returns true if and only if the tree is empty.
	 * 
	 * @complexity: O(1)
	 */
	public boolean empty() {
		return root == null || !root.isRealNode();
	}

	/**
	 * public AVLNode searchNode(int k)
	 * 
	 * @pre: key k of requested node
	 * @post: requested node if exists, else null
	 * @complexity: O(logn)
	 */
	public AVLNode searchNode(int k) {
		if (empty())
			return null;
		AVLNode p = root;
		while (p.isRealNode()) {
			if (p.getKey() == k) {
				return p;
			} else if (p.getKey() < k) {
				p = (AVLNode) p.getRight();
			} else if (p.getKey() > k) {
				p = (AVLNode) p.getLeft();
			}
		}
		return p;
	}

	/**
	 * public String search(int k)
	 *
	 * Returns the info of an item with key k if it exists in the tree. otherwise,
	 * returns null.
	 * 
	 * @complexity: O(logn)
	 */
	public String search(int k) {
		AVLNode p = searchNode(k);
		if (!p.isRealNode())
			return null;
		return p.getValue();
	}
	
	private class BalanceResult {
		AVLNode node;
		int actionCount;
		BalanceResult(AVLNode node, int actionCount) {
			this.node = node;
			this.actionCount = actionCount;
		}
	}

	/**
	 * private int leftLeftCase(AVLNode node)
	 * 
	 * Performs rotation for the left-left case
	 * 
	 * @complexity: O(1)
	 */
	private BalanceResult leftLeftCase(AVLNode node) {
		// logd("leftLeftCase() - START");
		node = (AVLNode) rotateRightAbout(node);
		return new BalanceResult(node,1);
	}

	/**
	 * private int leftRightCase(AVLNode node)
	 * 
	 * Performs rotation for the left-right case
	 * 
	 * @complexity: O(1)
	 */
	private BalanceResult leftRightCase(AVLNode node) {
		// logd("leftRightCase() - START");
		rotateLeftAbout(node.getLeft());
		node = (AVLNode) rotateRightAbout(node);
		return new BalanceResult(node,2);
	}

	/**
	 * private int rightRightCase(AVLNode node)
	 * 
	 * Performs rotation for the right-right case
	 * 
	 * @complexity: O(1)
	 */
	private BalanceResult rightRightCase(AVLNode node) {
		// logd("rightRightCase() - START");
		node = (AVLNode) rotateLeftAbout(node);
		return new BalanceResult(node,1);
	}

	/**
	 * private int rightLeftCase(AVLNode node)
	 * 
	 * Performs rotation for the right-left case
	 * 
	 * @complexity: O(1)
	 */
	private BalanceResult rightLeftCase(AVLNode node) {
		// logd("rightLeftCase() - START");
		rotateRightAbout(node.getRight());
		node = (AVLNode) rotateLeftAbout(node);
		return new BalanceResult(node,2);
	}

	/**
	 * private IAVLNode rotateRightAbout(IAVLNode node)
	 * 
	 * Performs clockwise rotation about specified node
	 * 
	 * @pre: child node of this.root
	 * @complexity: O(1)
	 */
	public IAVLNode rotateRightAbout(IAVLNode node) {
		assertd(node.isRealNode());
		@SuppressWarnings("unused")
		IAVLNode A, B, C, D, E;
		IAVLNode nodeParent = node.getParent();

		A = node;
		B = A.getLeft();
		C = A.getRight();
		D = B.getLeft();
		E = B.getRight();

		A.setLeft(E);
		B.setRight(A);

		E.setParent(A);
		A.setParent(B);

		B.setParent(nodeParent);

		assertd(nodeParent == null || nodeParent.isRealNode());
		if (nodeParent == null) {
			// A was root
			root = (AVLNode) B;
			// logd("Set root");
		} else if (nodeParent.isRealNode()) {
			// logd("lala");
			assertd(A == nodeParent.getLeft() || A == nodeParent.getRight());
			if (A == nodeParent.getLeft())
				nodeParent.setLeft(B);
			else
				nodeParent.setRight(B);
		}

		((AVLNode) A).update();
		((AVLNode) B).update();
		// if (nodeParent != null) ((AVLNode)nodeParent).update();

		return B;
	}

	/**
	 * private IAVLNode rotateLeftAbout(IAVLNode node)
	 * 
	 * Performs counterclockwise rotation about specified node
	 * 
	 * @pre: child node of this.root
	 * @complexity: O(1)
	 */
	public IAVLNode rotateLeftAbout(IAVLNode node) {
		assertd(node.isRealNode());
		@SuppressWarnings("unused")
		IAVLNode A, B, C, D, E;
		IAVLNode nodeParent = node.getParent();

		B = node;
		D = B.getLeft();
		A = B.getRight();
		E = A.getLeft();
		C = A.getRight();

		B.setRight(E);
		A.setLeft(B);

		E.setParent(B);
		B.setParent(A);

		A.setParent(nodeParent);

		assertd(nodeParent == null || nodeParent.isRealNode());
		if (nodeParent == null) {
			// B was root
			root = (AVLNode) A;
		} else if (nodeParent.isRealNode()) {
			assertd(B == nodeParent.getLeft() || B == nodeParent.getRight());
			if (B == nodeParent.getLeft())
				nodeParent.setLeft(A);
			else
				nodeParent.setRight(A);
		}

		((AVLNode) B).update();
		((AVLNode) A).update();
		// if (nodeParent != null) ((AVLNode)nodeParent).update();

		return A;
	}

	/**
	 * private int balance(IAVLNode node)
	 * 
	 * Performs balancing on specified node
	 * 
	 * @pre: child node of this.root
	 * @post: number of balancing actions performed
	 * @complexity: O(1)
	 */
	private int balance(AVLNode node) {
		int bf = node.getBF();
		// logd("BF of " + node.getValue() + " is " + String.valueOf(bf));
		AVLNode left = (AVLNode) node.getLeft();
		AVLNode right = (AVLNode) node.getRight();
		assertd(-2 <= node.getBF() && node.getBF() <= 2);

		BalanceResult balanceResult = null;
		int countRotations = 0;

		AVLNode newNode = null;

		if (bf == -2) {
			if (left.getBF() <= 0) {
				balanceResult = leftLeftCase(node);
				countRotations += balanceResult.actionCount;
			} else {	
				balanceResult = leftRightCase(node);
				countRotations += balanceResult.actionCount;
			}
		} else if (bf == 2) {
			if (right.getBF() >= 0) {
				balanceResult = rightRightCase(node);
				countRotations += balanceResult.actionCount;
			} else {
				balanceResult = rightLeftCase(node);
				countRotations += balanceResult.actionCount;
			}
		}

		if (countRotations > 0) {
			// logd("Performed " + String.valueOf(countRotations) + " rotations for " +
			// node.getValue());
		}

		if (balanceResult == null) {
			balanceResult = new BalanceResult(node, 0);
		}
		lastBalanceResult = balanceResult;
		return countRotations;
	}

	/**
	 * public int fingerInsertion(int k, String i)
	 * 
	 * Performs finger insertion
	 * 
	 * Only used in Q1 for calculation - this is not the regular insert(int k,
	 * String k) method
	 * 
	 * @pre: int k == key of new node
	 * @pre: String k == value of new node
	 * @post: distance between new node and biggest child of root
	 * @complexity: O(k) where k is the distance between our key and maximum value
	 */
	public int fingerInsertion(int k, String i) {
		if (root == null) {
			root = new AVLNode(k, i);
			return 0;
		} else {
			int costToLocate = 0;
			AVLNode location = root.getMaxChild();
			AVLNode node = new AVLNode(k, i);
			while (location.getParent() != null && location.getParent().getKey() >= k) {
				location = (AVLNode) location.getParent();
				costToLocate++;
			}
			return costToLocate + fingerInsertionHelper(location, node);
		}
	}

	/**
	 * public int fingerInsertionHelper(AVLNode location, AVLNode node)
	 * 
	 * Recursive helper for fingerInsertion(int k, String i)
	 * 
	 * Only used in Q1 for calculation - this is not the regular insert() method
	 * 
	 * @pre: AVLNode location == starting location or insertion location
	 * @pre: AVLNode node == new node to insert
	 * @post: distance between location and final insertion location of node
	 * @complexity: O(k) where k is the distance between our key and maximum value
	 */
	private int fingerInsertionHelper(AVLNode location, AVLNode node) {
		if (!location.isRealNode())
			return ERROR_CANNOT_INSERT;
		int countOperations = 0;
		if (node.getKey() < location.getKey()) {
			// if (location.getLeft().)
			if (!location.getLeft().isRealNode()) {
				location.setLeft(node);
			} else
				countOperations += fingerInsertionHelper((AVLNode) location.getLeft(), node);
		} else if (node.getKey() > location.getKey()) {
			if (!location.getRight().isRealNode()) {
				location.setRight(node);
			} else
				countOperations += fingerInsertionHelper((AVLNode) location.getRight(), node);
		} else {
			countOperations = ERROR_CANNOT_INSERT;
		}
		if (countOperations == ERROR_CANNOT_INSERT)
			return ERROR_CANNOT_INSERT;
		int oldHeight;
		boolean hadToUpdateHeight = false;
		if (countOperations != ERROR_CANNOT_INSERT) {
			oldHeight = location.getHeight();
			location.update();
			if (oldHeight != location.getHeight())
				hadToUpdateHeight = true;
		}

		int balanceOperations = balance(location);
		if (balanceOperations == 0) {
			if (hadToUpdateHeight) {
				// countOperations += 1; // commented out because we calculate cost differently
			} else {
				// do nothing
			}
		} else if (balanceOperations > 0) {
			// A promotion/rotation counts as one re-balance operation, double-rotation is
			// counted as 2.
			// countOperations += balanceOperations; // commented out because we calculate
			// cost differently
		}

		return 1 + countOperations;
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * Inserts an item with key k and info i to the AVL tree. The tree must remain
	 * valid, i.e. keep its invariants. Returns the number of re-balancing
	 * operations, or 0 if no re-balancing operations were necessary. A
	 * promotion/rotation counts as one re-balance operation, double-rotation is
	 * counted as 2. Returns -1 if an item with key k already exists in the tree.
	 * 
	 * @pre: int k == key of new node
	 * @pre: String k == value of new node
	 * @post: rebalancing actions performed
	 * @complexity: O(logn)
	 */
	public int insert(int k, String i) {
		if (root == null) {
			root = new AVLNode(k, i);
			return 0;
		} else
			return insertHelper(root, new AVLNode(k, i));
	}

	/**
	 * private int insertHelper(AVLNode location, AVLNode node)
	 * 
	 * Recursive helper for insert(int k, String i)
	 * 
	 * @pre: AVLNode location == starting location or insertion location
	 * @pre: AVLNode node == new node to insert
	 * @post: rebalancing actions performed
	 * @complexity: O(logn)
	 */
	private int insertHelper(AVLNode location, AVLNode node) {
		if (!location.isRealNode())
			return ERROR_CANNOT_INSERT;
		int countOperations = 0;
		if (node.getKey() < location.getKey()) {
			if (!location.getLeft().isRealNode()) {
				location.setLeft(node);
			} else
				countOperations += insertHelper((AVLNode) location.getLeft(), node);
		} else if (node.getKey() > location.getKey()) {
			if (!location.getRight().isRealNode()) {
				location.setRight(node);
			} else
				countOperations += insertHelper((AVLNode) location.getRight(), node);
		} else {
			countOperations = ERROR_CANNOT_INSERT;
		}
		if (countOperations == ERROR_CANNOT_INSERT)
			return ERROR_CANNOT_INSERT;
		int oldHeight;
		boolean hadToUpdateHeight = false;
		if (countOperations != ERROR_CANNOT_INSERT) {
			oldHeight = location.getHeight();
			location.update();
			if (oldHeight != location.getHeight())
				hadToUpdateHeight = true;
		}

		int balanceOperations = balance(location);
		if (balanceOperations == 0) {
			if (hadToUpdateHeight) {
				countOperations += 1;
			} else {
				// do nothing
			}
		} else if (balanceOperations > 0) {
			// A promotion/rotation counts as one re-balance operation, double-rotation is
			// counted as 2.
			countOperations += balanceOperations;
		}

		return countOperations;
	}

	/**
	 * public int delete(int k)
	 *
	 * Deletes an item with key k from the binary tree, if it is there. The tree
	 * must remain valid, i.e. keep its invariants. Returns the number of
	 * re-balancing operations, or 0 if no re-balancing operations were necessary. A
	 * promotion/rotation counts as one re-balance operation, double-rotation is
	 * counted as 2. Returns -1 if an item with key k was not found in the tree.
	 * 
	 * @pre: int k == key of node to delete
	 * @post: number of rebalancing actions performed
	 * @complexity: O(logn)
	 */
	public int delete(int k) {
		if (root == null)
			return ERROR_CANNOT_DELETE;
		return deleteHelper(root, k);
	}

	/**
	 * public int deleteHelper(AVLNode location, int k)
	 *
	 * Deletes an item with key k from the binary tree, if it is there. The tree
	 * must remain valid, i.e. keep its invariants. Returns the number of
	 * re-balancing operations, or 0 if no re-balancing operations were necessary. A
	 * promotion/rotation counts as one re-balance operation, double-rotation is
	 * counted as 2. Returns -1 if an item with key k was not found in the tree.
	 * 
	 * @pre: int k == key of node to delete
	 * @pre: AVLNode location == starting location to perform search for node
	 * @post: number of rebalancing actions performed so far
	 * @complexity: O(logn)
	 */
	private int deleteHelper(AVLNode location, int k) {
		if (!location.isRealNode())
			return ERROR_CANNOT_DELETE;
		int countOperations = 0;
		int deleteResult;
		if (k < location.getKey()) {
			if (!location.getLeft().isRealNode()) {
				return ERROR_CANNOT_DELETE;
			} else {
				deleteResult = deleteHelper((AVLNode) location.getLeft(), k);
				if (deleteResult == ERROR_CANNOT_DELETE)
					return ERROR_CANNOT_DELETE;
				countOperations += deleteResult;
			}
		} else if (k > location.getKey()) {
			if (!location.getRight().isRealNode()) {
				return ERROR_CANNOT_DELETE;
			} else {
				deleteResult = deleteHelper((AVLNode) location.getRight(), k);
				if (deleteResult == ERROR_CANNOT_DELETE)
					return ERROR_CANNOT_DELETE;
				countOperations += deleteResult;
			}
		} else {
			// logd(String.format("k==location.getKey(): %d==%d", k, location.getKey()));
			// this is our node
			AVLNode parent = (AVLNode) location.getParent();
			AVLNode successor = null;
			if (!location.getLeft().isRealNode() && !location.getRight().isRealNode()) {
				// leaf
				location.becomeVirtual();
				return 0;
			} else if (!location.getLeft().isRealNode() || !location.getRight().isRealNode()) {
				// only one child

				if (location.getLeft().isRealNode()) {
					// only left child
					successor = (AVLNode) location.getLeft();
					/* location.setLeft(null); */
				} else if (location.getRight().isRealNode()) {
					// only right child
					successor = (AVLNode) location.getRight();
					/* location.setRight(null); */
				} else {
					assertd(false); // this cannot happen
				}

				/*
				 * if (location == (AVLNode)parent.getLeft()) parent.setLeft(successor); else if
				 * (location == (AVLNode)parent.getRight()) parent.setRight(successor); else {
				 * logd("Deformed tree, this is bad. Try to look ahead instead maybe");
				 * assertd(false); } successor.setParent(parent); location.becomeVirtual();
				 */
				location.fullCopyFrom(successor); // FIXME - if this works, consider trying out the above
			} else {
				// two children

				// we pick the smallest value in the right subtree
				successor = (AVLNode) minNodeBelow(location.getRight());
				location.partialCopyFrom(successor);
				deleteResult = deleteHelper((AVLNode) location.getRight(), successor.getKey());
				if (deleteResult == ERROR_CANNOT_DELETE)
					assertd(false); // we're already midway removal
				countOperations += deleteResult;
			}
		}

		int oldHeight;
		boolean hadToUpdateHeight = false;
		if (countOperations != ERROR_CANNOT_DELETE) {
			oldHeight = location.getHeight();
			location.update();
			if (oldHeight != location.getHeight())
				hadToUpdateHeight = true;
		}

		int balanceOperations = balance(location);
		if (balanceOperations == 0) {
			if (hadToUpdateHeight) {
				countOperations += 1;
			} else {

			}
		} else if (balanceOperations > 0) {
			// A promotion/rotation counts as one re-balance operation, double-rotation is
			// counted as 2.
			countOperations += balanceOperations;
		}

		return countOperations;
	}

	/**
	 * private static IAVLNode minNodeBelow(IAVLNode p)
	 *
	 * Returns the info of the item with the smallest key below p, or p if
	 * the left subtree is empty.
	 * 
	 * @complexity: O(1)
	 */
	private static IAVLNode minNodeBelow(IAVLNode p) {
		if (p.getLeft() == null || !p.getLeft().isRealNode())
			return p;
		return ((AVLNode) p.getLeft()).getMinChild();

	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null if
	 * the tree is empty.
	 * 
	 * @complexity: O(1)
	 */
	public String min() {
		if (empty())
			return null;
		return this.root.getMinChild().getValue();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty.
	 * 
	 * @complexity: O(1)
	 */
	public String max() {
		if (empty())
			return null;
		return this.root.getMaxChild().getValue();
	}

	/**
	 * public AVLNode[] treeToArray()
	 *
	 * Returns a list of all the nodes in the tree, sorted by key, or null if the
	 * tree is empty
	 * 
	 * This could've been done in O(1), by maintaining a list of nodes,
	 * sorted by keys, and sorting while inserting.
	 * Performing search during insertions would've been done in O(logn),
	 * insertion into a the linked list would be O(1) after finding.
	 * So total "damage" to insert and delete would've been O(logn), but since
	 * insertion already costs O(logn), worst case would've stayed the same.
	 * We don't care because amortized is the same -
	 * an (insert^n, treeToArray) sequence takes the same amount of time.
	 * 
	 * @complexity: O(n)
	 */
	private AVLNode[] treeToArray() {
		if (this.root == null) return new AVLNode[0];
		AVLNode[] arr = new AVLNode[this.size()];
		AVLNode[] stack_pending = new AVLNode[this.size()];
		//logd(String.format("Debug size is %d", debugSize(this.root)));
		AVLNode p = root;
		int stack_pending_size = 0;
		int arr_idx = 0;

		while (stack_pending_size > 0 || p.isRealNode()) {
			if (p.isRealNode()) {
				stack_pending[stack_pending_size] = p;
				stack_pending_size++;
				p = (AVLNode) p.getLeft();
			} else {
				p = stack_pending[stack_pending_size - 1];
				stack_pending_size--;
				arr[arr_idx] = p;
				arr_idx++;
				p = (AVLNode) p.getRight();
			}
		}

		return arr;
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 * 
	 * @complexity: O(n)
	 */
	public int[] keysToArray() {
		AVLNode nodes[] = this.treeToArray();
		if (nodes == null)
			return new int[0]; // FIXME empty array?

		int keys[] = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			keys[i] = nodes[i].getKey();
		return keys;
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 * 
	 * @complexity: O(n)
	 */
	public String[] infoToArray() {
		AVLNode nodes[] = this.treeToArray();
		if (nodes == null)
			return null; // FIXME empty array?

		String info[] = new String[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			info[i] = nodes[i].getValue();
		return info;
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 * 
	 * @complexity: O(1)
	 */
	public int size() {
		if (this.empty())
			return 0;
		return this.root.getSize();
	}

	/**
	 * public int debugSize(IAVLNode p)
	 *
	 * ONLY USED IN DEBUGGING
	 *
	 * Returns the number of nodes in the tree -
	 * different implementation
	 * 
	 * @complexity: O(n)
	 */
	public int debugSize(IAVLNode p) {
		if (p==null) return 0;
		int sum = 0;
		int sumLeft, sumRight;
		if (!p.isRealNode()) {
			return 0;
		} else {
			sum += 1;
			sumLeft = debugSize(p.getLeft());
			assertd((!p.getLeft().isRealNode())||((AVLNode)p.getLeft()).getSize()==sumLeft);
			sumRight = debugSize(p.getRight());
			assertd((!p.getRight().isRealNode())||((AVLNode)p.getRight()).getSize()==sumRight);
			return 1+sumLeft+sumRight;
		}
	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 * 
	 * @complexity: O(n)
	 */
	public IAVLNode getRoot() {
		if (this.empty())
			return null; // redundant but yeah
		return this.root;
	}

	/**
	 * public AVLTree clone()
	 * 
	 * Returns a full clone of the current tree
	 * 
	 * @complexity: O(n)
	 */
	@Override
	public AVLTree clone() {
		AVLTree t = new AVLTree();
		t.root = this.root.deepClone();
		return t;
	}

	// only used to pass results back from splitWithClonedNodes
	private static class PointerObject<T> {
		public T x;

		public PointerObject() {
		}
	}

	// FIXME - get rid of clone
	public AVLTree[] splitWithClonedNodes(int x, PointerObject pJoinCostMax, PointerObject pJoinCostAvg,
			PointerObject pJoinCount, PointerObject pJoinCostTotal) {
		AVLNode xNode = searchNode(x);
		if (!xNode.isRealNode()) {
			this.insert(x, null);
			xNode = searchNode(x);
			assertd(xNode.isRealNode());
		}

		AVLTree t1, t2;
		t1 = new AVLTree();
		t2 = new AVLTree();

		/*
		 * if (xNode.left.isRealNode()) { t1.root = xNode.left; } if
		 * (xNode.right.isRealNode()) { t2.root = xNode.right; }
		 */

		joinCostTotal = 0;
		joinCostMax = 0;
		joinCostCurrent = 0;
		joinCount = 0;

		AVLNode p = (AVLNode) xNode;// .getParent();

		while (p != null && p.isRealNode()) {
			if (p.getKey() < x) {
				joinCostCurrent = t1.join(p.clone(), ((AVLNode) p.getLeft()).toTree());
				joinCount++;
			} else if (x < p.getKey()) {
				joinCostCurrent = t2.join(p.clone(), ((AVLNode) p.getRight()).toTree());
				joinCount++;
			} else {
				assertd(p == xNode);
				if (p.left.isRealNode())
					t1.root = p.left;
				if (p.right.isRealNode())
					t2.root = p.right;
			}

			if (p.parent != null) {
				assertd(p == p.parent.left || p == p.parent.right);
				if (p == p.parent.left) {
					p.parent.setLeft(new AVLNode(-1, null));
				} else {
					p.parent.setRight(new AVLNode(-1, null));
				}
			}
			p = (AVLNode) p.getParent();
			joinCostTotal += joinCostCurrent;
			joinCostMax = Math.max(joinCostCurrent, joinCostMax);
		}

		if (joinCount != 0) {
			joinCostAvg = joinCostTotal / joinCount;
		} else {
			joinCostAvg = 0;
		}

		pJoinCostAvg.x = joinCostAvg;
		pJoinCostMax.x = joinCostMax;
		pJoinCount.x = joinCount;
		pJoinCostTotal.x = joinCostTotal;

		AVLTree[] trees = new AVLTree[2];
		trees[0] = t1;
		trees[1] = t2;
		return trees;
	}

	/**
	 * public AVLTree[] split(int x)
	 *
	 * splits the tree into 2 trees according to the key x. Returns an array [t1,
	 * t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * 
	 * precondition: search(x) != null (i.e. you can also assume that the tree is
	 * not empty) postcondition: none
	 */
	public AVLTree[] split(int x) {
		AVLTree mClone = this.clone();
		PointerObject<Float> pJoinCostAvg = new PointerObject<>();
		PointerObject<Integer> pJoinCostMax = new PointerObject<>();
		PointerObject<Integer> pJoinCount = new PointerObject<>();
		PointerObject<Integer> pJoinCostTotal = new PointerObject<>();
		// this.joinCostAvg = mClone.joinCostAvg;
		// this.joinCostMax = mClone.joinCostMax;
		// System.out.println("JoinCostMax="+String.valueOf(mClone.joinCostMax));
		//AVLTree[] trees = mClone.splitWithClonedNodes(x, pJoinCostMax, pJoinCostAvg, pJoinCount, pJoinCostTotal);
		AVLTree[] trees = mClone.splitWithClonedNodes2(x);
		pJoinCostAvg.x = new Float(0);
		pJoinCostMax.x = 0;
		pJoinCount.x = 0;
		pJoinCostTotal.x = 0;
		this.joinCostAvg = pJoinCostAvg.x;
		this.joinCostMax = pJoinCostMax.x;
		this.joinCount = pJoinCount.x;
		this.joinCostTotal = pJoinCostTotal.x;
		return trees;
	}
	
	public AVLTree[] splitWithClonedNodes2(int x) {
		joinCostTotal = 0;
		joinCostMax = 0;
		joinCostCurrent = 0;
		joinCount = 0;
		
		if (this.empty()) return null;
		AVLTree t1 = new AVLTree();
		AVLTree t2 = new AVLTree();
		
		AVLNode p = searchNode(x);
		if (p.isRealNode()) {
			t1.root = (AVLNode) p.getLeft();
			t2.root = (AVLNode) p.getRight();
		}
		p = (AVLNode) p.getParent();
		
		AVLTree fakeTree;
		
		while (p != null) {
			if      (p.getKey() < x) {
				fakeTree = ((AVLNode)p.getLeft()).toTree(); // O(1) - just pointer assignment in toTree()...
				fakeTree.root.setParent(null); // a little duct tape yesh
				joinCostCurrent = t1.join(p.clone(), fakeTree);
				joinCount++;
			}
			else if (p.getKey() > x) {
				fakeTree = ((AVLNode)p.getRight()).toTree(); // O(1) - just pointer assignment in toTree()...
				fakeTree.root.setParent(null); // a little duct tape yesh
				joinCostCurrent = t2.join(p.clone(), fakeTree);
				joinCount++;
				
			} else {
				assertd(false);
			}
			p = (AVLNode) p.getParent();
			joinCostTotal += joinCostCurrent;
			joinCostMax = Math.max(joinCostCurrent, joinCostMax);
		}
		
		if (joinCount != 0) joinCostAvg = joinCostTotal / joinCount;
		
		AVLTree[] trees = new AVLTree[2];
		trees[0] = t1;
		trees[1] = t2;
		return trees;
	}

	/**
	 * public int join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. Returns the complexity of the operation
	 * (|tree.rank - t.rank| + 1).
	 *
	 * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be
	 * empty (rank = -1). postcondition: none
	 * 
	 * @complexity: O(|rank1-rank2|+1)
	 */
	public int join(IAVLNode x, AVLTree t) {
		assertd(!x.getLeft().isRealNode() && !x.getRight().isRealNode());
		if (this.empty() && t.empty()) {
			logd("Edge case #1");
			this.root = (AVLNode) x;
			return 1;
		} else if (this.empty()) {
			logd("Edge case #2");
			t.insert(x.getKey(), x.getValue());
			this.root = (AVLNode) t.getRoot();
			return 2+this.root.getHeight();
		} else if (t.empty()) {
			logd("Edge case #3");
			this.insert(x.getKey(), x.getValue());
			return 2+this.root.getHeight();
		}
		//logd("No edge case");
		
		IAVLNode oldParentT = t.getRoot().getParent();
		
		AVLTree T1 = null, T2 = null;
		AVLNode p1 = null, p2 = null;
		AVLNode p = null, par = null;
		int rank1, rank2;
		if (this.getRoot().getKey() < x.getKey() && x.getKey() < t.getRoot().getKey()) {
			T1 = this;
			T2 = t;
		} else if (t.getRoot().getKey() < x.getKey() && x.getKey() < this.getRoot().getKey()) {
			T1 = t;
			T2 = this;
		} else {
			assertd(false);
		}
		
		p1 = (AVLNode) T1.getRoot();
		p2 = (AVLNode) T2.getRoot();

		rank1 = p1.getHeight();
		rank2 = p2.getHeight();
		
		AVLNode newRootBeforeRebalance = null;
		
		if (rank1 == rank2) {
			x.setLeft(p1);
			x.setRight(p2);
			this.root = (AVLNode) x;
			return 1; // and no need to balance
		}
		else if (rank1 < rank2) {
			while (p2.getHeight() > rank1) {
				if (p2.getLeft().isRealNode()) {
					p2 = (AVLNode) p2.getLeft();
				} else {
					assertd(p2.getHeight() == 1 + rank1);
					// because otherwise p.getHeight() >= 2+rank1
					// since rank1 >= 0, this means p.getHeight() >= 2
					// so p.getRight().getHeight() >= 1
					// and since p.getLeft().getHeight() == -1
					// this gives us BF=2

					assertd(rank1 == 0);
					// BF == (1+rank1-1)-(-1)=1+rank1 <= 1
					// 0<=rank1, 1+rank1<=1

					p2 = (AVLNode) p2.getRight();
					
					assertd(rank1 == p2.getHeight());
					break;
				}
			}

			par = (AVLNode) p2.getParent();
			if (p2 == par.getLeft()) {
				x.setLeft(p1);
				x.setRight(p2);
				balance((AVLNode)x);
				x = lastBalanceResult.node;
				par.setLeft(x);
			} else {
				assertd(rank1 == 0);
				x.setLeft(p1);
				balance((AVLNode)x);
				x = lastBalanceResult.node;
				par.setLeft(x);
			}
			newRootBeforeRebalance =  T2.root;
		} else if (rank2 < rank1) {
			while (p1.getHeight() > rank2) {
				if (p1.getRight().isRealNode()) {
					p1 = (AVLNode) p1.getRight();
				} else {
					assertd(p1.getHeight() == 1 + rank2);
					assertd(rank2 == 0);
					p1 = (AVLNode) p1.getLeft();
					assertd(rank2 == p1.getHeight());
					break;
				}
			}

			par = (AVLNode) p1.getParent();
			if (p1 == par.getRight()) {
				x.setLeft(p1);
				x.setRight(p2);
				balance((AVLNode)x);
				x = lastBalanceResult.node;
				par.setRight(x);
			} else {
				assertd(rank2 == 0);
				x.setRight(p2);
				balance((AVLNode)x);
				x = lastBalanceResult.node;
				par.setRight(x);
			}
			newRootBeforeRebalance = T1.root;
		}
		
		AVLNode parPar;
		
		// the following does not hurt O(|rank1-rank2|+1) complexity
		while (par != null) {
			parPar = (AVLNode) par.getParent();
			((AVLNode)par).update();
			balance((AVLNode) par);
			par = lastBalanceResult.node;
			if (par.getParent() == null) this.root = par;
			par = (AVLNode) par.getParent();
			//par = parPar;
		}

		return Math.abs(rank1 - rank2) + 1;
	}

	/**
	 * public interface IAVLNode ! Do not delete or modify this - otherwise all
	 * tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); // Returns node's key (for virtual node return -1).

		public String getValue(); // Returns node's value [info], for virtual node returns null.

		public void setLeft(IAVLNode node); // Sets left child.

		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.

		public void setRight(IAVLNode node); // Sets right child.

		public IAVLNode getRight(); // Returns right child, if there is no right child return null.

		public void setParent(IAVLNode node); // Sets parent.

		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.

		public void setHeight(int height); // Sets the height of the node.

		public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree (for example AVLNode), do
	 * it in this file, not in another file.
	 * 
	 * This class can and MUST be modified (It must implement IAVLNode).
	 */
	public class AVLNode implements IAVLNode {
		int key = -1;
		String info = null;
		int height = -1;
		AVLNode parent = null, left = null, right = null;

		// not properties of AVLNode
		boolean virtual;
		int BF;
		int size;
		boolean isParentLeft;
		AVLNode minChild = null, maxChild = null;

		public AVLNode(int key, String value) {
			this.key = key;
			this.info = value;
			this.parent = null;

			this.BF = 0;

			if (key != -1) {
				this.left = new AVLNode(-1, null);
				this.right = new AVLNode(-1, null);
				// this.parent = new AVLNode(-1, null);
				this.parent = null;
				this.left.setParent(this);
				this.right.setParent(this);
				this.minChild = this;
				this.maxChild = this;
				this.height = 0;
				this.size = 1;
				this.virtual = false;
			} else {
				this.left = null;
				this.right = null;
				this.parent = null;
				this.height = -1;
				this.size = 0;
				this.virtual = true;
			}
		}

		// O(1)
		public int getKey() {
			return this.key;
		}

		// O(1)
		public String getValue() {
			return this.info;
		}

		// O(1)
		public IAVLNode getParent() {
			return this.parent;
		}

		// O(1)
		public IAVLNode getLeft() {
			return this.left;
		}

		// O(1)
		public IAVLNode getRight() {
			return this.right;
		}

		// O(1)
		public int getHeight() {
			return this.height;
		}

		/**
		 * public AVLNode getMaxChild()
		 * 
		 * @pre: properly maintained current node
		 * @post: largest child of node
		 * @complexity: O(1)
		 */
		public AVLNode getMaxChild() {
			return this.maxChild;
		}

		/**
		 * public AVLNode getMinChild()
		 * 
		 * @pre: properly maintained current node
		 * @post: smallest child of node
		 * @complexity: O(1)
		 */
		public AVLNode getMinChild() {
			return this.minChild;
		}

		// O(1)
		public void setLeft(IAVLNode node) {
			AVLNode old = this.left;
			this.left = (AVLNode) node;
			this.left.setParent(this);
			try {
				assertAVL();
			} catch (Exception e) {
				logd("Cannot setLeft() - exception thrown");
				logd("Message: " + e.getMessage());
				this.left = old;
				return;
			}
			update();
		}

		// O(1)
		public void setRight(IAVLNode node) {
			AVLNode old = this.right;
			this.right = (AVLNode) node;
			this.right.setParent(this);
			try {
				assertAVL();
			} catch (Exception e) {
				logd("Cannot setRight() - exception thrown");
				this.right = old;
				return;
			}
			update();
		}

		// O(1)
		public void setParent(IAVLNode node) {
			this.parent = (AVLNode) node;
		}

		// O(1)
		public void setHeight(int height) {
			assertd(isRealNode());
			this.height = height;
		}

		// O(1)
		public boolean isRealNode() {
			return !virtual;
		}

		// O(1)
		public int getBF() {
			return this.right.getHeight() - this.left.getHeight();
		}

		// O(1)
		private void updateSize() {
			if (!isRealNode())
				return;
			this.size = 1 + this.left.getSize() + this.right.getSize();
		}

		// O(1)
		private void updateHeight() {
			if (!isRealNode())
				return;
			this.setHeight(1 + Math.max(this.left.getHeight(), this.right.getHeight()));
		}

		// O(1)
		private void updateMinMax() {
			if (!isRealNode())
				return;
			this.minChild = this.left.isRealNode() ? this.left.minChild : this;
			this.maxChild = this.right.isRealNode() ? this.right.maxChild : this;
		}

		// O(1)
		public void update() {
			updateSize();
			updateHeight();
			updateMinMax();
		}

		// O(1)
		private void assertAVL() throws Exception {
			if (!isRealNode())
				return;

			// balanced
			int bf = getBF();
			if (!(-1 <= bf && bf <= 1)) {
				// throw new Exception("BF is invalid, BF="+String.valueOf(bf));
			}

			// bst
			assertd(!(left.isRealNode() && left.getKey() > this.getKey()));
			assertd(!(right.isRealNode() && right.getKey() < this.getKey()));
		}

		// O(1)
		public void setIsParentLeft(boolean v) {
			this.isParentLeft = v;
		}

		// O(1)
		public boolean getIsParentLeft() {
			return this.isParentLeft;
		}

		// O(1)
		public int getSize() {
			return this.size;
		}
		// public void setKey(int key) { this.key = key; }

		public void partialCopyFrom(AVLNode node) {
			this.key = node.getKey();
			this.info = node.getValue();
		}

		public void fullCopyFrom(AVLNode node) {
			this.key = node.getKey();
			this.info = node.getValue();
			this.left = (AVLNode) node.getLeft();
			this.right = (AVLNode) node.getRight();
			this.left.setParent(this);
			this.right.setParent(this);
			this.height = node.getHeight();
			this.size = node.getSize();
			this.BF = node.getBF();
			this.virtual = !node.isRealNode();
		}

		@Override
		public AVLNode clone() {
			return new AVLNode(this.key, this.info);
		}

		// O(n)
		public AVLNode deepClone() {
			if (!this.isRealNode())
				return new AVLNode(-1, null);
			AVLNode node = new AVLNode(this.key, this.info);
			node.setLeft(((AVLNode) this.left).deepClone());
			node.setRight(((AVLNode) this.right).deepClone());
			node.setHeight(this.height);
			node.size = this.size;
			node.BF = this.BF;
			node.virtual = this.virtual;
			return node;
		}

		public void select(AVLNode node) {

		}

		public void becomeVirtual() {
			assertd(!this.left.isRealNode() && !this.right.isRealNode());
			this.left = null;
			this.right = null;
			// this.parent = (AVLNode) parent;
			this.height = -1;
			this.size = 0;
			this.key = -1;
			this.virtual = true;
		}

		public AVLTree toTree() {
			AVLTree t = new AVLTree();
			t.root = this;
			return t;
		}

		public AVLTree toTreeClone() {
			AVLTree t = new AVLTree();
			t.root = this.deepClone();
			return t;
		}

	}

	public static void assertd(boolean condition) {
		if (FLAG_DEBUG) {
			if (!condition) {
				ErrorReporter.ASSERTION_TRIGGERS += 1;
				System.out.println("About to throw assertion");
				assert (condition);
			}
		}
	}

	public static void logd(String s) {
		if (FLAG_VERBOSE)
			System.out.println(s);
	}

}

final class ErrorReporter {
	private ErrorReporter() {}
	public static int ASSERTION_TRIGGERS=0;
}
