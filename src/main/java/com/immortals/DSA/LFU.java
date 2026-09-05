package com.immortals.DSA;

import java.util.HashMap;
import java.util.Map;

/**
 meri soch ->
 1. 2 map Key -> Node
 Freq -> DLL (LRU order with freq)
 2. min freq track bucker empty -> ++ , insert rest to 1
 3. O(1) -> time complexity
 */
class LFU {
    private int capacity;
    private int size;
    private int minFreq;
    private Map<Integer, Node> keyNode;
    private Map<Integer, DLL> freqList;

    public LFU(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.minFreq = 0;
        this.keyNode = new HashMap<>();
        this.freqList = new HashMap<>();
    }

    public int get(int key) {
        if (!keyNode.containsKey(key))
            return -1;
        Node node = keyNode.get(key);
        update(node);
        return node.val;
    }

    private void update(Node node) {
        int freq = node.freq;
        DLL list = freqList.get(freq);
        list.remove(node);
        if (freq == minFreq && list.size == 0) {
            minFreq++;
        }

        node.freq++;
        freqList.computeIfAbsent(node.freq, k -> new DLL()).addFront(node);
    }

    public void put(int key, int value) {
        if (capacity == 0)
            return;

        if (keyNode.containsKey(key)) {
            Node node = keyNode.get(key);
            node.val = value;
            update(node);
            return;
        }

        if (size == capacity) {
            DLL minList = freqList.get(minFreq);
            Node removed = minList.removeLast();
            keyNode.remove(removed.key);
            size--;
        }

        Node newNode = new Node(key, value);
        keyNode.put(key, newNode);
        freqList.computeIfAbsent(1, k -> new DLL()).addFront(newNode);
        minFreq = 1;
        size++;
    }

    class Node {
        int key;
        int val;
        int freq = 1;
        Node prev;
        Node next;

        Node() {

        }

        Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    class DLL {
        Node head = new Node();
        Node tail = new Node();
        int size = 0;

        DLL() {
            head.next = tail;
            tail.prev = head;
        }

        void addFront(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
            size++;
        }

        void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            size--;
        }

        Node removeLast() {
            if (size == 0)
                return null;
            Node last = tail.prev;
            remove(last);
            return last;
        }
    }
}
