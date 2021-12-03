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
	
	private static final boolean FLAG_DEBUG = true;
	
	private static final int	ERROR_CANNOT_INSERT = -1,
								ERROR_CANNOT_DELETE = -1;
	
	AVLNode root = null;
	int nodeCount = 0;
	
	private int joinCostTotal, joinCostCurrent, joinCount;
	public int joinCostMax;
	public float joinCostAvg;
	
	/**
	 * public AVLTree()
	 * 
	 * @pre: none
	 * @post: empty AVL tree
	 */
	public AVLTree() {
		this.root = null;
		this.nodeCount = 0;
	}
	
	/**
	 * public AVLTree(AVLNode root, int nodeCount)
	 * 
	 * @pre: root=valid AVL subtree root
	 * @pre: nodeCount which matches root children count + 1
	 * @post: new AVLTree with specified root as root
	 */
	public AVLTree(AVLNode root, int nodeCount) {
		this.root = root;
		this.nodeCount = nodeCount;
	}
	
	/**
	 * public boolean isValidHierarchy(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: whether or not all recursive children of p are linked to their real parents
	 */
	public boolean isValidHierarchy(IAVLNode p) { 
		if (p==null || (!p.isRealNode() && p.getLeft()==null && p.getRight()==null && p.getHeight()==-1)) {
			return true;
		}
		if (p.getLeft().getParent()!=p) {
			return false;
		}
		if (p.getRight().getParent()!=p) {
			return false;
		}
		return isValidHierarchy(p.getLeft()) && isValidHierarchy(p.getRight());
	}
	
	/**
	 * public boolean isValidBST(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: forall x in recursive children of p, x.left.key <= x.key <= x.right.key
	 */
	public boolean isValidBST(IAVLNode p) {
		if (p==null || (!p.isRealNode() && p.getLeft()==null && p.getRight()==null)) {
			return true;
		}
		if (p.getLeft().isRealNode() && p.getLeft().getKey()>p.getKey()) {
			return false;
		}
		if (p.getRight().isRealNode() && p.getRight().getKey()<p.getKey()) {
			return false;
		}
		return isValidBST(p.getLeft()) && isValidBST(p.getRight());
	}
	
	/**
	 * public boolean isValidRank(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: balance factor is either -1, 0 or 1
	 */
	public boolean isValidRank(IAVLNode p) {
		if (p==null || (!p.isRealNode() && p.getLeft()==null && p.getRight()==null && p.getHeight()==-1)) {
			return true;
		}
		int diffL, diffR;
		diffL = p.getHeight()-p.getLeft().getHeight();
		diffR = p.getHeight()-p.getRight().getHeight();
		if ((diffL == 1 && diffR == 1) ||
				(diffL == 1 && diffR == 2) ||
				(diffL == 2 && diffR == 1)) {
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
	 */
	public boolean isValidAVL(IAVLNode p) {
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
			if (debugSize(p)!=size()) {
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
	 */
	public boolean isValidAVL() {
		return isValidAVL(root);
	}
	
	/**
	 * public static boolean isValidAVL(IAVLNode p)
	 * 
	 * @pre: node p
	 * @post: prints subtree of p inorder
	 */
	public static void printInOrder(IAVLNode p) {
		if (p==null || (!p.isRealNode() && p.getLeft()==null && p.getRight()==null && p.getHeight()==-1)) {
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
	 */
	public void printInOrder() {
		printInOrder(root);
	}

	/**
	* public boolean empty()
	*
	* Returns true if and only if the tree is empty.
	*
	*/
	public boolean empty() {
		return root==null || !root.isRealNode();
	}
	
	/**
	 * public AVLNode searchNode(int k)
	 * 
	 * @pre: key k of requested node
	 * @post: requested node if exists, else null
	 */
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
	
	private int leftLeftCase(AVLNode node) {
		//logd("leftLeftCase() - START");
		rotateRightAbout(node);
		return 1;
	}

	private int leftRightCase(AVLNode node) {
		//logd("leftRightCase() - START");
		rotateLeftAbout(node.getLeft());
		return 1 + leftLeftCase(node);
	}

	private int rightRightCase(AVLNode node) {
		//logd("rightRightCase() - START");
		rotateLeftAbout(node);
		return 1;
	}

	private int rightLeftCase(AVLNode node) {
		//logd("rightLeftCase() - START");
		rotateRightAbout(node.getRight());
		return 1 + rightRightCase(node);
	}
	
	public void rotateRightAbout(IAVLNode node) {
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

		assertd(nodeParent==null || nodeParent.isRealNode());
		if (nodeParent == null) {
			// A was root
			root = (AVLNode) B;
			//logd("Set root");
		} else if (nodeParent.isRealNode()) {
			//logd("lala");
			assertd(A == nodeParent.getLeft() || A == nodeParent.getRight());
			if (A == nodeParent.getLeft()) nodeParent.setLeft(B);
			else nodeParent.setRight(B);
		}
		
		((AVLNode)A).update();
		((AVLNode)B).update();
		//if (nodeParent != null) ((AVLNode)nodeParent).update();
		
		//return B
	}
	
	public void rotateLeftAbout(IAVLNode node) {
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
		
		assertd(nodeParent==null || nodeParent.isRealNode());
		if (nodeParent == null) {
			// B was root
			root = (AVLNode) A;
		} else if (nodeParent.isRealNode()) {
			assertd(B == nodeParent.getLeft() || B == nodeParent.getRight());
			if (B == nodeParent.getLeft()) nodeParent.setLeft(A);
			else nodeParent.setRight(A);
		}
		
		((AVLNode)B).update();
		((AVLNode)A).update();
		//if (nodeParent != null) ((AVLNode)nodeParent).update();
		
		//return A
	}
	
	private int balance(AVLNode node) {
		int bf = node.getBF();
		//logd("BF of " + node.getValue() + " is " + String.valueOf(bf));
		AVLNode left = (AVLNode) node.getLeft();
		AVLNode right = (AVLNode) node.getRight();
		assertd(-2 <= node.getBF() && node.getBF() <= 2);

		int countRotations = 0;

		AVLNode newNode = null;

		if (bf == -2) {
			if (left.getBF() <= 0) {
				countRotations += leftLeftCase(node);
			} else {
				countRotations += leftRightCase(node);
			}
		} else if (bf == 2) {
			if (right.getBF() >= 0) {
				countRotations += rightRightCase(node);
			} else {
				countRotations += rightLeftCase(node);
			}
		}

		if (countRotations > 0) {
			//logd("Performed " + String.valueOf(countRotations) + " rotations for " + node.getValue());
		}

		return countRotations;
	}
	
	private int insertHelper(AVLNode location, AVLNode node) {
		if (!location.isRealNode()) return ERROR_CANNOT_INSERT;
		int countOperations = 0;
		if        (node.getKey() < location.getKey()) {
			if (!location.getLeft().isRealNode()) {
				nodeCount++;
				location.setLeft(node);
			}
			else countOperations += insertHelper((AVLNode)location.getLeft(), node);
		} else if (node.getKey() > location.getKey()) {
			if (!location.getRight().isRealNode()) {
				nodeCount++;
				location.setRight(node);
			}
			else countOperations += insertHelper((AVLNode)location.getRight(), node);
		} else {
			countOperations = ERROR_CANNOT_INSERT;
		}
		if (countOperations == ERROR_CANNOT_INSERT) return ERROR_CANNOT_INSERT;
		int oldHeight;
		boolean hadToUpdateHeight = false;
		if (countOperations != ERROR_CANNOT_INSERT) {
			oldHeight = location.getHeight();
			location.update();
			if (oldHeight != location.getHeight()) hadToUpdateHeight = true;
		}
		
		int balanceOperations = balance(location);
		if (balanceOperations == 0) {
			if (hadToUpdateHeight) {
				countOperations += 1;
			} else {
				// do nothing
			}
		} else if (balanceOperations > 0){
			// A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
			countOperations += balanceOperations;
		}
		
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
			nodeCount++;
			return 0;
		} else return insertHelper(root, new AVLNode(k, i));
	}
	
	private int deleteHelper(AVLNode location, int k) {
		if (!location.isRealNode()) return ERROR_CANNOT_DELETE;
		int countOperations = 0;
		int deleteResult;
		if        (k < location.getKey()) {
			if (!location.getLeft().isRealNode()) {
				return ERROR_CANNOT_DELETE;
			}
			else {
				deleteResult = deleteHelper((AVLNode)location.getLeft(), k);
				if (deleteResult == ERROR_CANNOT_DELETE) return ERROR_CANNOT_DELETE;
				countOperations += deleteResult;
			}
		} else if (k > location.getKey()) {
			if (!location.getRight().isRealNode()) {
				return ERROR_CANNOT_DELETE;
			}
			else {
				deleteResult = deleteHelper((AVLNode)location.getRight(), k);
				if (deleteResult == ERROR_CANNOT_DELETE) return ERROR_CANNOT_DELETE;
				countOperations += deleteResult;
			}
		} else {
			//logd(String.format("k==location.getKey(): %d==%d", k, location.getKey()));
			// this is our node
			AVLNode parent = (AVLNode) location.getParent();
			AVLNode successor = null;
			if (!location.getLeft().isRealNode() && !location.getRight().isRealNode()) {
				// leaf
				location.becomeVirtual();
				nodeCount--;
				return 0;
			} else if (!location.getLeft().isRealNode() || !location.getRight().isRealNode()) {
				// only one child
				
				if (location.getLeft().isRealNode()) {
					// only left child
					successor = (AVLNode) location.getLeft();
					/*location.setLeft(null);*/
				} else if (location.getRight().isRealNode()) {
					// only right child
					successor = (AVLNode) location.getRight();
					/*location.setRight(null);*/
				} else {
					assertd(false); // this cannot happen
				}
				
				/*if (location == (AVLNode)parent.getLeft()) parent.setLeft(successor);
				else if (location == (AVLNode)parent.getRight()) parent.setRight(successor);
				else {
					logd("Deformed tree, this is bad. Try to look ahead instead maybe");
					assertd(false);
				}
				successor.setParent(parent);
				location.becomeVirtual();*/
				location.fullCopyFrom(successor); // FIXME - if this works, consider trying out the above
				nodeCount--;
			} else {
				// two children
				
				// we pick the smallest value in the right subtree
				successor = (AVLNode) minNodeBelow(location.getRight());
				location.partialCopyFrom(successor);
				deleteResult = deleteHelper((AVLNode)location.getRight(), successor.getKey());
				if (deleteResult == ERROR_CANNOT_DELETE) assertd(false); // we're already midway removal
				countOperations += deleteResult;
				// we don't call nodeCount--
				// recursive call must do this for us
			}
		}
		
		int oldHeight;
		boolean hadToUpdateHeight = false;
		if (countOperations != ERROR_CANNOT_DELETE) {
			oldHeight = location.getHeight();
			location.update();
			if (oldHeight != location.getHeight()) hadToUpdateHeight = true;
		}
		
		int balanceOperations = balance(location);
		if (balanceOperations == 0) {
			if (hadToUpdateHeight) {
				countOperations += 1;
			} else {
				
			}
		} else if (balanceOperations > 0){
			// A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
			countOperations += balanceOperations;
		}
		
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
		return deleteHelper(root, k);
	}
	
	
	private static IAVLNode minNodeBelow(IAVLNode p) {
		if (p.getLeft()==null || !p.getLeft().isRealNode()) return p;
		return ((AVLNode)p.getLeft()).getMinChild();
		
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
		return this.root.getMinChild().getValue();
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
		return this.root.getMaxChild().getValue();
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
		AVLNode[] arr = new AVLNode[this.size()];
		AVLNode[] stack_pending = new AVLNode[this.size()];
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
		if (this.empty()) return 0;
		return this.root.getSize();
	}
	
	public int debugSize(IAVLNode p) {
		if (!p.isRealNode()) return 0;
		return 1+debugSize(p.getLeft())+debugSize(p.getRight());
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
	
	@Override
	public AVLTree clone() {
		AVLTree t = new AVLTree();
		t.root = this.root.deepClone();
		t.nodeCount = this.nodeCount;
		return t;
	}
	
	public AVLTree[] splitWithClonedNodes(int x) {
		AVLNode xNode = searchNode(x);
		if (!xNode.isRealNode()) {
			this.insert(x, null);
			xNode = searchNode(x);
			assertd(xNode.isRealNode());
		}
		
		AVLTree t1, t2;
		t1 = new AVLTree();
		t2 = new AVLTree();
		
		/*if (xNode.left.isRealNode()) {
			t1.root = xNode.left;
		}
		if (xNode.right.isRealNode()) {
			t2.root = xNode.right;
		}*/
		
		joinCostTotal   = 0;
		joinCostMax     = 0;
		joinCostCurrent = 0;
		joinCount       = 0;
		
		AVLNode p = (AVLNode) xNode;//.getParent();
		
		while (p != null && p.isRealNode()) {
			if (p.getKey() < x) {
				joinCostCurrent = t1.join(p.clone(), ((AVLNode)p.getLeft()).toTree());
				joinCount++;
			}
			else if (x < p.getKey()) {
				joinCostCurrent = t2.join(p.clone(), ((AVLNode)p.getRight()).toTree());
				joinCount++;
			} else {
				assertd(p == xNode);
				if (p.left.isRealNode())  t1.root = p.left;
				if (p.right.isRealNode()) t2.root = p.right;
			}
			
			if (p.parent != null) {
				assertd(p == p.parent.left || p == p.parent.right);
				if (p == p.parent.left) {
					p.parent.setLeft(new AVLNode(-1, null));
				}
				else {
					p.parent.setRight(new AVLNode(-1, null));
				}
			}
			p = (AVLNode) p.getParent();
			joinCostTotal += joinCostCurrent;
			joinCostMax    = Math.max(joinCostCurrent, joinCostMax);
		}
		
		if (joinCount!=0) {
			joinCostAvg = joinCostTotal / joinCount;
		} else {
			joinCostAvg = 0;
		}
		
		AVLTree[] trees = new AVLTree[2];
		trees[0] = t1;
		trees[1] = t2;
		return trees;
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
		AVLTree mClone = this.clone();
		this.joinCostAvg = mClone.joinCostAvg;
		this.joinCostMax = mClone.joinCostMax;
		return mClone.splitWithClonedNodes(x);
	}
	
	/**
	* public int join(IAVLNode x, AVLTree t)
	*
	* joins t and x with the tree. 	
	* Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	*
	* precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
	* postcondition: none
	*/	
	public int join(IAVLNode x, AVLTree t)
	{
		int complexity;
		if (this.empty() && t.empty()) {
			//logd("Scenario 1");
			this.root = (AVLNode) x;
			this.nodeCount = 1;
			return 1;
		} else if (this.empty()) {
			//logd("Scenario 2");
			complexity = 2+t.getRoot().getHeight();
			t.insert(x.getKey(), x.getValue());
			this.root = (AVLNode) t.getRoot();
			this.nodeCount = t.size();
			return complexity;
		} else if (t.empty()) {
			//logd("Scenario 3");
			complexity = 2+this.getRoot().getHeight();
			this.insert(x.getKey(), x.getValue());
			return complexity;
		}
		//logd("Scenario 4");
		
		complexity = Math.abs(this.root.getHeight() - t.getRoot().getHeight()) + 1;
		int newCount = this.size() + t.size() + 1;
		int rebalanceOperations = 0;
		AVLTree T1, T2;
		if (x.getKey() < root.getKey()) {
			// case 1: keys(t) < x < keys()
			T1 = t;
			T2 = this;
		} else {
			// case 2: keys(t) > x > keys()
			T1 = this;
			T2 = t;
		}
		
		AVLNode a, b, c;
		
		if (T1.getRoot().getHeight() < T2.getRoot().getHeight()) {
			int k = T1.getRoot().getHeight();
			a = (AVLNode) T1.getRoot();
			b = (AVLNode) T2.getRoot();
			while (b.getHeight() > k) {
				if (b.getLeft().isRealNode()) b=(AVLNode)b.getLeft();
				else                          b=(AVLNode)b.getRight();
			}
			assertd(b.getHeight()==k || b.getHeight()==k-1);
			c = (AVLNode) b.getParent();
			x.setLeft(a);
			// b doesnt have to be the left child of c
			// but if it's not, then by definition c has no left child
			// because we traversed by going left
			// also, this can't go on for more than 2 steps due to BF
			c.setLeft(x);
			x.setRight(b);
			((AVLNode)x).update();
			assertd(c.getHeight()==k+1 || c.getHeight()==k+2);
			//rebalanceOperations += balance((AVLNode)x);
			rebalanceOperations += balance((AVLNode)c);
		} else if (T1.getRoot().getHeight() > T2.getRoot().getHeight()){
			int k = T2.getRoot().getHeight();
			a = (AVLNode) T1.getRoot();
			b = (AVLNode) T2.getRoot();
			while (a.getHeight() > k) {
				if (a.getRight().isRealNode()) a=(AVLNode)a.getRight();
				else                          a=(AVLNode)a.getLeft();
			}
			assertd(a.getHeight()==k || a.getHeight()==k-1);
			c = (AVLNode) a.getParent();
			x.setLeft(a);
			c.setRight(x);
			x.setRight(b);
			((AVLNode)x).update();
			if (!(c.getHeight()==k+1 || c.getHeight()==k+2)) {
				logd("About to throw assertion");
			}
			assertd(c.getHeight()==k+1 || c.getHeight()==k+2);
			//rebalanceOperations += balance((AVLNode)x);
			rebalanceOperations += balance((AVLNode)c);
		} else {
			x.setLeft(T1.getRoot());
			x.setRight(T2.getRoot());
			((AVLNode)x).update();
			// no rebalancing needed?
		}
		AVLNode newRoot = (AVLNode) x;
		while (newRoot.getParent() != null) {
			newRoot = (AVLNode) newRoot.getParent();
		}
		root = newRoot;
		this.nodeCount = newCount;
		return complexity;
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
	
	public AVLNode AVLNodeGenerator(int key, String value) {
		return new AVLNode(key, value);
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
		AVLNode minChild = null, maxChild = null;
		
		
		public AVLNode(int key, String value) {
			this.key = key;
			this.info = value;
			this.parent = null;
			
			this.BF = 0;
			
			if (key != -1) {
				this.left = new AVLNode(-1, null);
				this.right = new AVLNode(-1, null);
				//this.parent = new AVLNode(-1, null);
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

		public int getKey() { return this.key; }
		public String getValue() { return this.info; }
		public IAVLNode getParent() { return this.parent; }
		public IAVLNode getLeft() { return this.left; }
		public IAVLNode getRight() { return this.right; }
		public int getHeight() { return this.height; }
		
		public AVLNode getMaxChild() { return this.maxChild; }
		public AVLNode getMinChild() { return this.minChild; }
		
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
			((AVLNode)node).setIsParentLeft(true); // FIXME pray to god this works
		}
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
			((AVLNode)node).setIsParentLeft(false); // FIXME pray to god this works
		}
		
		public void setParent(IAVLNode node) {
			this.parent = (AVLNode) node;
		}
		public void setHeight(int height) {
			assertd(isRealNode());
			this.height = height;
		}
		
		public boolean isRealNode() {
			return !virtual;
		}
		
		public int getBF() {
			return this.right.getHeight() - this.left.getHeight();
		}
		
		private void updateSize() {
			if (!isRealNode()) return;
			this.size = 1 + this.left.getSize() + this.right.getSize();
		}
		
		private void updateHeight() {
			if (!isRealNode()) return;
			this.setHeight(1+Math.max(this.left.getHeight(), this.right.getHeight()));
		}
		
		private void updateMinMax() {
			if (!isRealNode()) return;
			this.minChild = this.left.isRealNode()?  this.left.minChild  :this;
			this.maxChild = this.right.isRealNode()? this.right.maxChild :this;
		}
		
		public void update() {
			updateSize();
			updateHeight();
			updateMinMax();
		}
		
		private void assertAVL() throws Exception {
			if (!isRealNode()) return;
			
			// balanced
			int bf = getBF();
			if (!(-1 <= bf && bf <= 1)) {
				//throw new Exception("BF is invalid, BF="+String.valueOf(bf));
			}
			
			// bst
			if (left.isRealNode() && !(left.getKey()<this.getKey())) {
				throw new Exception("BST error, left");
			}
			if (right.isRealNode() && !(right.getKey()>this.getKey())) {
				throw new Exception("BST error, right");
			}
		}
		
		public void setIsParentLeft(boolean v) { this.isParentLeft = v; }
		public boolean getIsParentLeft() { return this.isParentLeft; }
		
		public int getSize() { return this.size; }
		//public void setKey(int key) { this.key = key; }
		
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
		
		public AVLNode deepClone() {
			if (!this.isRealNode()) return new AVLNode(-1, null);
			AVLNode node = new AVLNode(this.key, this.info);
			node.setLeft(((AVLNode)this.left).deepClone());
			node.setRight(((AVLNode)this.right).deepClone());
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
			//this.parent = (AVLNode) parent;
			this.height = -1;
			this.size = 0;
			this.key = -1;
			this.virtual = true;
		}
		
		public AVLTree toTree() {
			AVLTree t = new AVLTree();
			t.root = this;
			t.nodeCount = this.size;
			return t;
		}
		
		public AVLTree toTreeClone() {
			AVLTree t = new AVLTree();
			t.root = this.deepClone();
			t.nodeCount = this.size;
			return t;
		}
		
	}
	
	public static void assertd(boolean condition) {
		if (FLAG_DEBUG) assert(condition);
	}
	
	public static void logd(String s) {
		if (FLAG_DEBUG) System.out.println(s);
	}

}
