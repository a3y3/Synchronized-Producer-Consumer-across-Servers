import java.util.NoSuchElementException;

/*
 *  Storage.java
 *
 *  Version:
 *      1.0
 *
 *  Revisions:
 *      None
 */

/**
 * This class implements a Storage using Generics.
 * @param <E> Element e.
 *
 * @author Satwik Mishra
 * @author Soham Dongargaonkar
 *
 */
public class Storage<E> {

    private Node head = null;
    private Node current = null;
    private Node tail = null;
    private int size = 0;

    /**
     * Represents a single Node
     *
     */
    class Node {
        Node next;
        E data;

        Node(E e) {
            next = null;
            data = e;
        }
    }

    /**
     * Adds a Node to the beginning of the linked list
     *
     * @param e Element e.
     * @return true, as specified in Javadoc LinkedList.
     */
    public boolean add(E e) {
        Node newNode = createNewNode(e);
        if (head == null) {
            head = newNode;
            tail = newNode;
            return true;
        }
        tail.next = newNode;
        tail = newNode;
        return true;
    }

    /**
     * Fetches the head element.
     *
     * @return head element.
     */
    public E element() {
        return head.data;
    }

    /**
     * Fetches the head and deletes it
     *
     * @return the head element
     */
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Empty List");
        }
        Node tempNode = head;
        head = head.next;
        size--;
        return tempNode.data;
    }


    /**
     * Returns size of the list
     *
     * @return size
     */
    public int size() {
        return size;
    }


    /**
     * Creates a new Node and returns it
     *
     * @param e element e
     * @return the newly created node
     */
    public Node createNewNode(E e) {
        Node node = new Node(e);
        size++;
        return node;
    }

    public void consume(){
        remove();
    }

    public void produce(E val){
        add(val);
    }
}