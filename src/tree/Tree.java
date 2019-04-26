package tree;

/**
 * Common interface for all structures of this package.
 * Contains all common methods.
 * Each class from this package implements this interface one way or another.
 * @param <T> type of elements to be stored in the tree.
 */
public interface Tree<T> extends Iterable<T> {
    /**
     * Returns the size of the tree.
     * @return the size of the tree.
     */
    int getSize();

    /**
     * Returns true if the tree contains no elements.
     * @return tree if the tree contains no elemets; false otherwise.
     */
    boolean isEmpty();

    /**
     * Removes all of the elements from the tree.
     * The tree will be empty after this call returns.
     */
    void clear();

    /**
     * Returns an array containing all of the elements from the tree.
     * @return an array containing all of the elements from the tree.
     */
    Object[] toArray();

    /**
     * Returns an array containing all if the elements from the tree.
     * @param a the array into which the elements of the queue are to
     * be stored, if it is big enough; otherwise, a new array of the
     * same runtime type is allocated for this purpose.
     * @return an array containing all of the elements from the tree.
     */
    <T> T[] toArray(T[] a);
}
