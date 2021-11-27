package il.ac.tau.cs.ds.proj1;

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */


public class AVLTree {
	
	private static final int	ERROR_CANNOT_INSERT = -1,
								ERROR_CANNOT_DELETE = -1;
	
	AVLNode root = null;
	int nodeCount = 0;
	
	public AVLTree(AVLNode root, int nodeCount) {
		this.root = root;
		this.nodeCount = nodeCount;
	}

	/**
	* public boolean empty()
	*
	* Returns true if and only if the tree is empty.
	*
	*/
	public boolean empty() {
		return root == null || !root.isRealNode();
	}
	
	public AVLNode searchNode(int k)
	{
		if (empty()) return null;
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
	* Returns the info of an item with key k if it exists in the tree.
	* otherwise, returns null.
	*/
	public String search(int k)
	{
		AVLNode p = searchNode(k);
		if (!p.isRealNode()) return null;
		return p.getValue();
	}
	
	public static void rotateRightAbout(IAVLNode node) {
		assert(node.isRealNode());
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
		if (nodeParent.isRealNode()) {
			assert(A == nodeParent.getLeft() || A == nodeParent.getRight());
			if (A == nodeParent.getLeft()) nodeParent.setLeft(A);
			else nodeParent.setRight(B);
		}
	}
	
	public static void rotateLeftAbout(IAVLNode node) {
		assert(node.isRealNode());
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
		if (nodeParent.isRealNode()) {
			assert(B == nodeParent.getLeft() || B == nodeParent.getRight());
			if (B == nodeParent.getLeft()) nodeParent.setLeft(A);
			else nodeParent.setRight(A);
		}
	}
	
	private int rebalance(AVLNode location, AVLNode node) {
		int countOperations = 0;
		
		int bf = location.getBF();

		if (bf > 1 && node.getKey() < location.getLeft().getKey()) {
			rotateRightAbout(location);
			countOperations += 1;
		}
		
		if (bf > 1 && node.getKey() > location.getLeft().getKey()) {
			rotateLeftAbout(location.getLeft());
			rotateRightAbout(location);
			countOperations += 2;
		}
		
		if (bf < -1 && node.getKey() > location.getRight().getKey()) {
			rotateLeftAbout(location);
			countOperations += 1;
		}
		
		if (bf < -1 && node.getKey() < location.getRight().getKey()) {
			rotateRightAbout(location.getRight());
			rotateLeftAbout(location);
			countOperations += 2;
		}
		
		return countOperations;
	}
	
	private int insertHelper(AVLNode location, AVLNode node) {
		if (!location.isRealNode()) return ERROR_CANNOT_INSERT;
		int countOperations = 0;
		if (location.getKey() < node.getKey()) {
			if (!location.getLeft().isRealNode()) {
				nodeCount++;
				location.setLeft(node);
			}
			else countOperations = insertHelper((AVLNode)location.getLeft(), node);
		} else if (location.getKey() > node.getKey()) {
			if (!location.getRight().isRealNode()) {
				nodeCount++;
				location.setRight(node);
			}
			else countOperations = insertHelper((AVLNode)location.getRight(), node);
		} else {
			countOperations = ERROR_CANNOT_INSERT;
		}
		if (countOperations == ERROR_CANNOT_INSERT) return ERROR_CANNOT_INSERT;
		
		countOperations += rebalance(location, node);
		
		return countOperations;
	}

	/**
	* public int insert(int k, String i)
	*
	* Inserts an item with key k and info i to the AVL tree.
	* The tree must remain valid, i.e. keep its invariants.
	* Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	* A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	* Returns -1 if an item with key k already exists in the tree.
	*/
	public int insert(int k, String i) {
		if (root == null) {
			root = new AVLNode(k, i);
			return 0;
		} else return insertHelper(root, new AVLNode(k, i));
	}
	
	private int deleteHelper(AVLNode location, AVLNode node) {
		if (!location.isRealNode()) return ERROR_CANNOT_DELETE;
		int countOperations = 0;
		if (location.getKey() < node.getKey()) {
			if (!location.getLeft().isRealNode()) {
				return ERROR_CANNOT_DELETE;
			}
			else countOperations = deleteHelper((AVLNode)location.getLeft(), node);
		} else if (location.getKey() > node.getKey()) {
			if (!location.getRight().isRealNode()) {
				return ERROR_CANNOT_DELETE;
			}
			else countOperations = deleteHelper((AVLNode)location.getRight(), node);
		} else {
			AVLNode deadMan = location;
			AVLNode deadManParent = (AVLNode) deadMan.getParent();
			
			if (!deadMan.getLeft().isRealNode() || !deadMan.getRight().isRealNode()) {
				// one or zero children
				AVLNode tmp = null;
				if      (deadMan.getLeft().isRealNode())  tmp = (AVLNode) deadMan.getLeft();
				else if (deadMan.getRight().isRealNode()) tmp = (AVLNode) deadMan.getRight();
				
				if (tmp == null) { // no children
					if (!deadManParent.isRealNode()) {
						root = null;
						nodeCount--;
						assert(nodeCount==0);
						return countOperations;
					} else if (deadMan == deadManParent.getLeft()) {
						deadManParent.setLeft(new AVLNode(deadManParent));
						nodeCount--;
					} else if (deadMan == deadManParent.getRight()) {
						deadManParent.setRight(new AVLNode(deadManParent));
						nodeCount--;
					} else {
						System.out.println("Deletion from corrupt tree");
						assert(false);
					}
				} else { // one child
					location.copyFrom(tmp);
					location.setLeft(tmp.getLeft());
					location.setRight(tmp.getRight());
				}
			} else {
				// two children
				AVLNode inOrderSuccessor = (AVLNode) minNodeBelow(location.getRight());
				location.copyFrom(inOrderSuccessor);
				countOperations += deleteHelper((AVLNode)location.getRight(), inOrderSuccessor);
			}
		}
			
		if (root == null) return countOperations;
		
		if (location.getLeft().getHeight()>location.getRight().getHeight())
			location.setHeight(1+location.getLeft().getHeight());
		else
			location.setHeight(1+location.getRight().getHeight());
		
		countOperations += rebalance(location, node);
		
		return countOperations;
	}

	/**
	* public int delete(int k)
	*
	* Deletes an item with key k from the binary tree, if it is there.
	* The tree must remain valid, i.e. keep its invariants.
	* Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	* A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	* Returns -1 if an item with key k was not found in the tree.
	*/
	public int delete(int k)
	{
		if (root == null) return ERROR_CANNOT_DELETE;
		AVLNode deadMan = new AVLNode(k, "");
		return deleteHelper(root, deadMan);
	}
	
	
	private static IAVLNode minNodeBelow(IAVLNode p) {
		if (p.getLeft()==null || !p.getLeft().isRealNode()) return p;
		return minNodeBelow(p.getLeft());
		
	}

	/**
	* public String min()
	*
	* Returns the info of the item with the smallest key in the tree,
	* or null if the tree is empty.
	*/
	public String min()
	{
		if (empty()) return null;
		IAVLNode p = root;
		while (p.getLeft() != null) {
			p = p.getLeft();
		}
		return p.getValue();
	}

	/**
	* public String max()
	*
	* Returns the info of the item with the largest key in the tree,
	* or null if the tree is empty.
	*/
	public String max()
	{
		if (empty()) return null;
		IAVLNode p = root;
		while (p.getRight() != null) {
			p = p.getRight();
		}
		return p.getValue();
	}
	
	/**
	 * private static int getNodeCountFromPointer(IAVLNode p)
	 * 
	 * Given generic IAVLNode, returns subtree size in O(n)
	 */
	private static int getNodeCountFromPointer(IAVLNode p) {
		if (p==null || !p.isRealNode()) return 0;
		return 1 + getNodeCountFromPointer(p.getLeft()) + getNodeCountFromPointer(p.getRight());
	}
	
	/**
	* public AVLNode[] treeToArray()
	*
	* Returns a list of all the nodes in the tree, sorted by key,
	* or null if the tree is empty
	*/
	// FIXME
	// this might be possible more efficiently
	// by updating this list on the go, while inserting and deleting
	// however without a stack, we might hurt worst case complexity of insert/delete
	private AVLNode[] treeToArray() {
		AVLNode[] arr = new AVLNode[nodeCount];
		AVLNode[] stack_pending = new AVLNode[nodeCount];
		AVLNode p = root;
		int stack_pending_size = 0;
		int arr_idx = 0;
		
		while (stack_pending_size>0 || p.isRealNode()) {
			if (p.isRealNode()) {
				stack_pending[stack_pending_size] = p;
				stack_pending_size++;
				p = (AVLNode) p.getLeft();
			} else {
				p = stack_pending[stack_pending_size-1];
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
	* Returns a sorted array which contains all keys in the tree,
	* or an empty array if the tree is empty.
	*/
	public int[] keysToArray()
	{
		AVLNode nodes[] = this.treeToArray();
		if (nodes == null) return null; //FIXME empty array?
		
		int keys[] = new int[nodes.length];
		for (int i=0; i<nodes.length; i++) keys[i]=nodes[i].getKey();
		return keys;
	}

	/**
	* public String[] infoToArray()
	*
	* Returns an array which contains all info in the tree,
	* sorted by their respective keys,
	* or an empty array if the tree is empty.
	*/
	public String[] infoToArray()
	{
		AVLNode nodes[] = this.treeToArray();
		if (nodes == null) return null; //FIXME empty array?
		
		String info[] = new String[nodes.length];
		for (int i=0; i<nodes.length; i++) info[i]=nodes[i].getValue();
		return info;
	}

	/**
	* public int size()
	*
	* Returns the number of nodes in the tree.
	*/
	public int size()
	{
		return nodeCount;
	}
	
	/**
	* public int getRoot()
	*
	* Returns the root AVL node, or null if the tree is empty
	*/
	public IAVLNode getRoot()
	{
		if (this.empty()) return null; //redundant
		return this.root;
	}
	
	// FIXME not implemented (yes, really)
	/**
	* public AVLTree[] split(int x)
	*
	* splits the tree into 2 trees according to the key x. 
	* Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	* 
	* precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
	* postcondition: none
	*/	
	public AVLTree[] split(int x)
	{
		AVLNode parent = this.searchNode(x);
		assert(parent != null);
		
		AVLNode root1 = (AVLNode) parent.getLeft();
		AVLNode root2 = (AVLNode) parent.getRight();
		
		int nodeCount1 = getNodeCountFromPointer(root1);
		int nodeCount2 = getNodeCountFromPointer(root2);
		
		AVLTree[] trees = new AVLTree[2];
		trees[0] = new AVLTree(root1, nodeCount1);
		trees[1] = new AVLTree(root2, nodeCount2);
		return trees;
	}
	
	// FIXME not implemented
	/**
	* public int join(AVLNode x, AVLTree t)
	*
	* joins t and x with the tree. 	
	* Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	*
	* precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
	* postcondition: none
	*/	
	public int join(IAVLNode x, AVLTree t)
	{
		return -1;
	}

	/** 
	* public interface IAVLNode
	* ! Do not delete or modify this - otherwise all tests will fail !
	*/
	public interface IAVLNode{	
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
	* If you wish to implement classes other than AVLTree
	* (for example AVLNode), do it in this file, not in another file. 
	* 
	* This class can and MUST be modified (It must implement IAVLNode).
	*/
	public class AVLNode implements IAVLNode{
		int key = -1;
		String info = null;
		int height = -1;
		AVLNode parent = null, left = null, right = null;
		
		// not properties of AVLNode
		boolean virtual;
		int BF;
		int size;
		boolean isParentLeft;
		
		
		public AVLNode(int key, String value) {
			this.key = key;
			this.info = value;
			this.parent = null;
			
			this.BF = 0;
			
			if (key != -1) {
				this.left = new AVLNode(-1, null);
				this.right = new AVLNode(-1, null);
				this.parent = new AVLNode(-1, null);
				this.left.setParent(this);
				this.right.setParent(this);
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
		
		public AVLNode(IAVLNode parent) {
			this.left = null;
			this.right = null;
			this.parent = (AVLNode) parent;
			this.height = -1;
			this.size = 0;
			this.virtual = true;
		}

		public int getKey() { return this.key; }
		public String getValue() { return this.info; }
		public IAVLNode getParent() { return this.parent; }
		public IAVLNode getLeft() { return this.left; }
		public IAVLNode getRight() { return this.right; }
		public int getHeight() { return this.height; }
		
		public void setLeft(IAVLNode node) {
			AVLNode old = this.left;
			this.left = (AVLNode) node;
			this.left.setParent(this);
			try {
				assertAVL();
			} catch (AssertionError e) {
				System.out.println("Cannot setLeft() - exception thrown");
				this.left = old;
				return;
			}
			updateSize();
			updateHeight();
			((AVLNode)node).setIsParentLeft(true); // FIXME pray to god this works
		}
		public void setRight(IAVLNode node) {
			AVLNode old = this.right;
			this.right = (AVLNode) node;
			this.right.setParent(this);
			try {
				assertAVL();
			} catch (AssertionError e) {
				System.out.println("Cannot setRight() - exception thrown");
				this.right = old;
				return;
			}
			updateSize();
			updateHeight();
			((AVLNode)node).setIsParentLeft(false); // FIXME pray to god this works
		}
		
		public void setParent(IAVLNode node) {
			this.parent = (AVLNode) node;
		}
		public void setHeight(int height) {
			assert(isRealNode());
			this.height = height;
		}
		
		public boolean isRealNode() {
			return !virtual;
		}
		
		public int getBF() {
			return this.left.getHeight() - this.right.getHeight();
		}
		
		private void updateSize() {
			if (!isRealNode()) return;
			this.size = 1 + this.left.getSize() + this.right.getSize();
		}
		
		private void updateHeight() {
			if (!isRealNode()) return;
			this.setHeight(1+(this.left.getHeight()>this.right.getHeight()?this.left.getHeight():this.right.getHeight()));
		}
		
		private void assertAVL() throws AssertionError {
			if (!isRealNode()) return;
			
			// balanced
			int bf = getBF();
			assert(-1 <= bf && bf <= 1);
			
			// bst
			if (left.isRealNode()) assert(left.getKey() < this.getKey());
			if (right.isRealNode()) assert(this.getKey() < right.getKey());
		}
		
		public void setIsParentLeft(boolean v) { this.isParentLeft = v; }
		public boolean getIsParentLeft() { return this.isParentLeft; }
		
		public int getSize() { return this.size; }
		//public void setKey(int key) { this.key = key; }
		
		public void copyFrom(AVLNode node) {
			this.key = node.getKey();
			this.info = node.getValue();
		}
		
	}

}
	
