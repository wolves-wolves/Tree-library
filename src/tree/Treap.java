package tree;

import java.util.Comparator;
import java.util.Iterator;

/**
 * <p>This class is an implementation of a set.</p>
 * <p>An instance of this class can contain any type of elements that could be compared
 * via comparator in a natural order.<br>
 * Please, note that all elements should be unique.
 * An attempt to add a duplicate element will no lead to anything.</p>
 * <p>This class offers an almost O(n*log(n)) time performance
 * on add/remove/contains operations.</p>
 * <p><strong>Note that this implementation is not synchronized.</strong></p>
 * @param <T> type of element to be stored in this treap.
 */
public class Treap<T> implements Tree<T> {
    /**
     * The root of this treap.
     */
    private Node root;
    /**
     * The number of elements in this treap.
     */
    private int size;

    /**
     * The comparator or null if treap orders elements as comparable.
     */
    private final Comparator<? super T> comparator;

    /**
     * Creates a treap without any comparator.
     */
    public Treap() {
        this(null);
    }

    /**
     * Creates a treap with a specified comparator
     * @param comparator the comparator that will be used to order elements in this treap.
     */
    public Treap(Comparator<? super T> comparator) {
        root = null;
        this.comparator = comparator;
    }

    /**
     * Adds the specified element into this treap.
     * @param x element to be added to this treap.
     * @throws NullPointerException if the specified element is null.
     */
    public void add(T x) {
        if (x == null)
            throw new NullPointerException();

        if (contains(x))
            return;

        size++;

        Node L, M, R;

        PairOfNodes pair = leftSplit(root, x);
        L = pair.getL();
        R = pair.getR();

        M = new Node(x);

        root = merge(merge(L, M), R);
    }

    /**
     * Removes the specified element from this treap.
     * @param x element to be removed from this treap.
     * @return true if this treap did contain the element; false otherwise.
     */
    public boolean remove(T x) {
        boolean result = false;
        Node L, M, R;

        PairOfNodes pair = rightSplit(root, x);
        L = pair.getL();
        R = pair.getR();

        pair = leftSplit(R, x);
        M = pair.getL();
        R = pair.getR();

        if (M != null) {
            size--;
            result = true;
        }

        root = merge(L, R);
        return result;
    }

    /**
     * Returns true only if this treap contains the specified element.
     * @param x element whose presence in this treap is to be tested.
     * @return true if this treap contains the specified element; false otherwise.
     */
    public boolean contains(T x) {
        Node L, M, R;

        PairOfNodes pair = rightSplit(root, x);
        L = pair.getL();
        R = pair.getR();

        pair = leftSplit(R, x);
        M = pair.getL();

        root = merge(L, R);

        return M != null;
    }

    /**
     * Returns the size of this treap.
     * @return the size of this treap.
     */
    @Override
    public int getSize() {
        return this.size;
    }

    /**
     * Returns true if this treap contains no elements.
     * @return true if this treap contains no elements; false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all of the elements from this treap.
     * The treap will be empty after this call returns.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns an array containing all of the elements from this treap.
     * @return an array containing all of the elements from this treap.
     */
    @Override
    public Object[] toArray() {
        return Trees.<T>toArray(root, size);
    }

    /**
     * Returns an array containing all if the elements from this treap.
     * @param a the array into which the elements of the queue are to
     * be stored, if it is big enough; otherwise, a new array of the
     * same runtime type is allocated for this purpose.
     * @return an array containing all of the elements from this treap.
     */
    @Override
    public <T2> T2[] toArray(T2[] a) {
        return Trees.<T2>toArray(a, root, size);
    }

    /**
     * Returns a string representation of this treap.
     * @return a string representation of this treap.
     */
    @Override
    public String toString() {
        return Trees.<T>toString(root, size);
    }

    /**
     * Returns an iterator over the elements from this treap.
     * @return an iterator over the elements from this treap.
     */
    @Override
    public Iterator<T> iterator() {
        return Trees.<T>getIterator(root, size);
    }

    /**
     * Merges two specified treaps into one.
     * @param L first treap to be merged.
     * @param R second treap to be merged.
     * @return new treap which contains all elements from two that were merged.
     */
    private Node merge(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;

        if (L.y > R.y)
            return new Node(L.x, L.y, L.L, merge(L.R, R));
        else
            return new Node(R.x, R.y, merge(L, R.L), R.R);

    }

    /**
     * Splits a specified treap into two new treaps using the key.
     * @param N treap to be splitted
     * @param x key (if present in the primordial treap(N) <strong>will fall into the left of new treaps</strong>).
     * @return a pair of treaps that contain all element from the one that got splitted.
     */
    private PairOfNodes leftSplit(Node N, T x) {
        if (N == null)
            return new PairOfNodes(null, null);

        Node L;
        Node R;
        Node newNode = null;

        boolean compareResult;
        if (comparator == null) {
            Comparable<? super T> value = (Comparable<? super T>) N.x;

            compareResult = value.compareTo(x) <= 0;
        } else {
            compareResult = comparator.compare(N.x, x) <= 0;
        }

        if (compareResult) {
            if (N.R == null) {
                R = null;
            } else {
                PairOfNodes pair = leftSplit(N.R, x);
                newNode = pair.getL();
                R = pair.getR();
            }
            L = new Node(N.x, N.y, N.L, newNode);
        } else {
            if (N.L == null) {
                L = null;
            } else {
                PairOfNodes pair = leftSplit(N.L, x);
                L = pair.getL();
                newNode = pair.getR();
            }
            R = new Node(N.x, N.y, newNode, N.R);
        }

        return new PairOfNodes(L, R);
    }

    /**
     * Splits a specified treap into two new treaps using the key.
     * @param N treap to be splitted
     * @param x key (if present in the primordial treap(N) <strong>will fall into the right of new treaps</strong>).
     * @return a pair of treaps that contain all element from the one that got splitted.
     */
    private PairOfNodes rightSplit(Node N, T x) {
        if (N == null)
            return new PairOfNodes(null, null);

        Node L;
        Node R;
        Node newNode = null;

        boolean compareResult;
        if (comparator == null) {
            Comparable<? super T> value = (Comparable<? super T>) N.x;

            compareResult = value.compareTo(x) < 0;
        } else {
            compareResult = comparator.compare(N.x, x) < 0;
        }

        if (compareResult) {
            if (N.R == null) {
                R = null;
            } else {
                PairOfNodes pair = rightSplit(N.R, x);
                newNode = pair.getL();
                R = pair.getR();
            }
            L = new Node(N.x, N.y, N.L, newNode);
        } else {
            if (N.L == null) {
                L = null;
            } else {
                PairOfNodes pair = rightSplit(N.L, x);
                L = pair.getL();
                newNode = pair.getR();
            }
            R = new Node(N.x, N.y, newNode, N.R);
        }

        return new PairOfNodes(L, R);
    }

    /**
     * A node of this treap.
     * Actually each node can be considered as a treap.
     */
    private class Node extends Trees.Node<T> {
        /**
         * First key.
         * Also a value that is stored.
         */
        T x;
        /**
         * Second key.
         */
        double y;

        /**
         * Left child.
         */
        Node L;
        /**
         * Right child.
         */
        Node R;

        /**
         * Created a new node with the specified value.
         * @param x value to be stored and used as a first key.
         */
        Node(T x) {
            this(x, Math.random(), null, null);
        }

        /**
         * Created a new node with the specified value and both children.
         * @param x value to be stored and used as a first key.
         * @param L left child.
         * @param R right child.
         */
        Node(T x, Node L, Node R) {
            this(x, Math.random(), L, R);
        }

        /**
         * Created a new node with the specified value, second key and both children.
         * @param x value to be stored and used as a first key.
         * @param y second key.
         * @param L left child.
         * @param R right child.
         */
        Node(T x, double y, Node L, Node R) {
            this.x = x;
            this.y = y;
            this.L = L;
            this.R = R;
        }

        /**
         * Returns the value stored.
         * @return value stored.
         */
        @Override
        T getValue() {
            return x;
        }

        /**
         * Returns left child.
         * @return left child.
         */
        @Override
        Node getL() {
            return L;
        }

        /**
         * Return right child.
         * @return right child.
         */
        @Override
        Node getR() {
            return R;
        }
    }

    /**
     * Helper class.
     * Allows methods to return two treaps at ones.
     * An instance contains two treaps, always after splitting operations.
     * therefore they are referred as left and right.
     */
    private class PairOfNodes {
        /**
         * Left treap.
         */
        private Node L;
        /**
         * Right treap.
         */
        private Node R;

        /**
         * Returns left treap.
         * @return left treap.
         */
        Node getL() {
            return L;
        }

        /**
         * Returns right treap.
         * @return right treap.
         */
        Node getR() {
            return R;
        }

        /**
         * Creates new pair of treaps..
         * @param L first treap (will be referred as left).
         * @param R second treap (will be referred as right).
         */
        PairOfNodes(Node L, Node R) {
            this.L = L;
            this.R = R;
        }
    }
}
