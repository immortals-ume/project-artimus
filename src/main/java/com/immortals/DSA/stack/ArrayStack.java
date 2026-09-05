package com.immortals.DSA.stack;

public class ArrayStack {

    private Integer[] data = new Integer[2];
    private int size = 0;

    public static void main(String[] args) {
        ArrayStack s = new ArrayStack();

        for (int i = 1; i <= 10; i++) s.push(i * 10);
        System.out.println("Size after 10 pushes: " + s.size());

        System.out.println("Peek: " + s.peek());

        while (!s.isEmpty()) {
            System.out.println("Pop: " + s.pop());
        }

        System.out.println("Empty now: " + s.isEmpty());

        try {
            s.pop();
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    public void push(Integer item) {
        if (size == data.length) data = java.util.Arrays.copyOf(data, size * 2);
        data[size++] = item;
        System.out.println("pushed" + item);
    }

    public Integer pop() {
        if (size == 0) throw new java.util.NoSuchElementException("stack underflow");
        return data[--size];
    }

    int peek() {
        if (size == 0) throw new java.util.NoSuchElementException("stack underflow");
        return data[size - 1];
    }

    boolean isEmpty() { return size == 0; }
    int size() { return size; }
}
