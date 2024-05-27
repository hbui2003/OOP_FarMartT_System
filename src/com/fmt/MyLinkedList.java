package com.fmt;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * MyLinkedList class represents a sorted singly linked list of elements of type
 * T. Elements are sorted according to a Comparator<T> passed in the
 * constructor.
 *
 * @author huybui
 *
 * @param <T>
 */
public class MyLinkedList<T> implements Iterable<T> {
	private int size;
	private Node<T> head;
	private Comparator<T> cmp;

	public MyLinkedList(Comparator<T> compare) {
		this.size = 0;
		this.head = null;
		this.cmp = compare;
	}

	// Retrieving the node at the specified index in the list.
	private Node<T> getNodeAtIndex(int index) {
		if (index < 0 || index >= this.size) {
			throw new IllegalArgumentException("Invalid index: " + index);
		}
		Node<T> current = this.head;
		for (int i = 0; i < size; i++) {
			current = current.getNext();
		}
		return current;
	}

	//Removes and returns the element at the specified index in the list
	public T removeElementAtIndex(int index) {
		if (index < 0 || index >= this.size) {
			throw new IllegalArgumentException("Invalid index: " + index);
		} else if (index == 0) {
			T y = head.getElement();
			this.head = this.head.getNext();
			return y;
		} else {
			Node<T> previous = this.getNodeAtIndex(index - 1);
			Node<T> current = previous.getNext();
			Node<T> next = current.getNext();
			previous.setNext(next);
			return current.getElement();
		}
	}
	//Retrieving the element at the specified index in the list.
	public T getElementAtIndex(int index) {
		if (index < 0 || index >= this.size) {
			throw new IllegalArgumentException("Invalid index: " + index);
		} else {
			return this.getNodeAtIndex(index).getElement();
		}
	}

	// Adds the specified element to the list in sorted order.
	public void addElement(T element) {
		if (element == null) {
			throw new IllegalArgumentException("The element is null");
		}
		Node<T> newNode = new Node<>(element);

		if (head == null) {
			head = newNode;
		} else if (this.cmp.compare(head.getElement(), element) >= 0) {
			newNode.setNext(head);
			head = newNode;
		} else {
			Node<T> previous = head;
			Node<T> current = head.getNext();
			while (current != null && this.cmp.compare(current.getElement(), element) < 0) {
				previous = current;
				current = current.getNext();
			}
			if (current == null) {
				previous.setNext(newNode);
			} else if (this.cmp.compare(current.getElement(), element) >= 0) {
				previous.setNext(newNode);
				newNode.setNext(current);
			}
		}
		this.size++;
	}

	public int getSize() {
		return this.size;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Node<T> current = head;

			@Override
			public boolean hasNext() {
				return (current != null);
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				T data = current.getElement();
				current = current.getNext();
				return data;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

}
