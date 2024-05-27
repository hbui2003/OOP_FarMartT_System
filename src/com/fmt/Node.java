package com.fmt;

/**
 * Represents a node in a linked list. Each node contains an element and a
 * reference to the next node in the list.
 * 
 * @author huybui
 *
 * @param <T>
 */
public class Node<T> {

	private T element;
	private Node<T> next;

	public Node(T element) {
		this.element = element;
		this.next = null;
	}

	public Node<T> getNext() {
		return next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public T getElement() {
		return element;
	}

}
