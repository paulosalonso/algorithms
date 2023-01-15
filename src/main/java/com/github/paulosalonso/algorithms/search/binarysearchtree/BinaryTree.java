package com.github.paulosalonso.algorithms.search.binarysearchtree;

import lombok.Getter;

@Getter
public class BinaryTree<T extends Comparable<T>> {

    private BinaryNode<T> root;

    public void add(T value) {
        if (this.root == null) {
            this.root = new BinaryNode<>(value);
        } else {
            this.root.add(value);
        }
    }

    public boolean remove(T value) {
        if (root == null) {
            return false;
        }

        return root.remove(value);
    }

    public boolean contains(T value) {
        var node = this.root;

        while (node != null) {
            if (value.compareTo(node.getValue()) < 0) {
                node = node.getLeft();
            } else if (value.compareTo(node.getValue()) > 0) {
                node = node.getRight();
            } else {
                return true;
            }
        }

        return false;
    }
}
