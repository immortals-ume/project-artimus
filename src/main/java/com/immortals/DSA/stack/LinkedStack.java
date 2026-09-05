package com.immortals.DSA.stack;

class LinkedStack {
    private Node head = null;
    private int size = 0;

    void push(int x) { head = new Node(x, head); size++; }

    int pop() {
        if (head == null) throw new java.util.NoSuchElementException("stack underflow");
        int v = head.val; head = head.next; size--; return v;
    }

    int peek() {
        if (head == null) throw new java.util.NoSuchElementException("stack underflow");
        return head.val;
    }

    boolean isEmpty() { return head == null; }

    int size() { return size; }

    private static class Node {
        int val; Node next;
        Node(int val, Node next) { this.val = val; this.next = next; }
    }
}