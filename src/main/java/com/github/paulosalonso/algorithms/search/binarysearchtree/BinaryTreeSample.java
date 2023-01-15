package com.github.paulosalonso.algorithms.search.binarysearchtree;

import java.util.List;

public class BinaryTreeSample {

    public static void main(String[] args) {
        final var values = List.of(622,742,483,561,852,794,107,414,675,535,819,307,720,371,563,171,768,78,10,143,640,11,41,592,673,489,239);

        System.out.println("Building a tree with values: "
                + String.join(", ", values.stream().map(String::valueOf).toList()));

        final var tree = new BinaryTree<Integer>();

        for (var value : values) {
            tree.add(value);
        }

        System.out.println("\nTree inorder traversal:\n");
        System.out.println(tree.getRoot().inOrderTraversal().stream().map(BinaryNode::getValue).toList());
        System.out.println("\nTree structure:\n");
        System.out.println(tree.getRoot().print());

        System.out.println("\n\nRemoving value " + 483 + " from tree");
        tree.remove(483);

        System.out.println("\nTree inorder traversal:\n");
        System.out.println(tree.getRoot().inOrderTraversal().stream().map(BinaryNode::getValue).toList());
        System.out.println("\nTree structure:\n");
        System.out.println(tree.getRoot().print());
    }
}
