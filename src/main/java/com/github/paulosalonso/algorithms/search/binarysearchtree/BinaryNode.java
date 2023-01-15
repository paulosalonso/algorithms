package com.github.paulosalonso.algorithms.search.binarysearchtree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor
@Builder(access = PRIVATE, toBuilder = true)
@Getter
@ToString
public class BinaryNode<T extends Comparable<T>> {

    private T value;
    private int height = 0;
    private BinaryNode<T> left;
    private BinaryNode<T> right;

    public BinaryNode(T value) {
        this.value = value;
    }

    public void add(T value) {
        if (value.compareTo(this.value) <= 0) {
            this.left = addToSubTree(this.left, value);

            if (isUnbalancedToLeft()) {
                if (value.compareTo(this.left.value) <= 0) {
                    this.rotateRight();
                } else {
                    this.rotateLeftRight();
                }
            }
        } else {
            this.right = addToSubTree(this.right, value);

            if (isUnbalancedToRight()) {
                if (value.compareTo(this.right.value) > 0) {
                    this.rotateLeft();
                } else {
                    this.rotateRightLeft();
                }
            }
        }

        this.computeHeight();
    }

    public List<BinaryNode<T>> inOrderTraversal() {
        final var traversal = new ArrayList<BinaryNode<T>>();

        if (this.left != null) {
            traversal.addAll(this.left.inOrderTraversal());
        }

        traversal.add(this);

        if (this.right != null) {
            traversal.addAll(this.right.inOrderTraversal());
        }

        return Collections.unmodifiableList(traversal);
    }

    public boolean remove(T value) {
        var removed = false;

        if (value.equals(this.value)) {
            if (this.left == null) {
                this.value = this.right.value;
                this.left = this.right.left;
                this.right = this.right.right;
            } else {
                var parent = this;
                var child = this.left;

                while (child.right != null) {
                    parent = child;
                    child = child.right;
                }

                this.value = child.value;

                if (parent.equals(this)) {
                    this.left = child.left;
                } else {
                    parent.right = child.left;
                }

                if (this.getHeightDifference() == -2) {
                    if (this.right.getHeightDifference() <= 0) {
                        this.rotateLeft();
                    } else {
                        this.rotateRightLeft();
                    }
                }
            }

            removed = true;
        } else if (value.compareTo(this.value) < 0) {
            removed = this.left.remove(value);

            if (this.getHeightDifference() == -2) {
                if (this.right.getHeightDifference() <= 0) {
                    this.rotateLeft();
                } else {
                    this.rotateRightLeft();
                }
            }
        } else {
            removed = this.right.remove(value);

            if (this.getHeightDifference() == 2) {
                if (this.left.getHeightDifference() >= 0) {
                    this.rotateRight();
                } else {
                    this.rotateRightLeft();
                }
            }
        }

        this.computeHeight();

        return removed;
    }

    private BinaryNode<T> addToSubTree(BinaryNode<T> parent, T value) {
        if (parent == null) {
            return new BinaryNode<>(value);
        }

        parent.add(value);

        return parent;
    }

    private boolean isUnbalancedToRight() {
        return getHeightDifference() == -2;
    }

    private boolean isUnbalancedToLeft() {
        return getHeightDifference() == 2;
    }

    private void rotateLeft() {
        var newRoot = this.right;
        var newLeftChild = this.withoutRightChild();
        var newLeftGrandchildLeft = this.left;
        var newLeftGrandchildRight = this.right.left;
        var newRightChild = this.right.right;

        this.value = newRoot.value;

        this.left = newLeftChild;
        newLeftChild.left = newLeftGrandchildLeft;
        newLeftChild.right = newLeftGrandchildRight;

        this.right = newRightChild;

        newLeftChild.computeHeight();
        this.computeHeight();
    }

    private void rotateRight() {
        var newRoot = this.left;
        var newLeftChild = this.left.left;
        var newRightChild = this.withoutLeftChild();
        var newRightGrandchildRight = this.right;
        var newRightGrandchildLeft = this.left.right;

        this.value = newRoot.value;

        this.left = newLeftChild;

        this.right = newRightChild;
        newRightChild.left = newRightGrandchildLeft;
        newRightChild.right = newRightGrandchildRight;

        newRightChild.computeHeight();
        this.computeHeight();
    }

    private void rotateLeftRight() {
        var newRoot = this.left.right;
        var newLeftChild = this.left;
        var newLeftGrandchildLeft = this.left.left;
        var newLeftGrandsonRight = this.left.right.left;
        var newRightChild = this.withoutLeftChild();
        var newRightGrandchildRight = this.right;
        var newRightGrandchildLeft = this.left.right.right;

        this.value = newRoot.value;

        this.left = newLeftChild;
        newLeftChild.left = newLeftGrandchildLeft;
        newLeftChild.right = newLeftGrandsonRight;

        this.right = newRightChild;
        newRightChild.left = newRightGrandchildLeft;
        newRightChild.right = newRightGrandchildRight;

        newLeftChild.computeHeight();
        this.computeHeight();
    }

    private void rotateRightLeft() {
        var newRoot = this.right.left;
        var newLeftChild = this.withoutRightChild();
        var newLeftGrandsonLeft = this.left;
        var newLeftGrandsonRight = this.right.left.left;
        var newRightChild = this.right;
        var newRightGrandsonLeft = this.right.left.right;
        var newRightGrandsonRight = this.right.right;

        this.value = newRoot.value;

        this.left = newLeftChild;
        newLeftChild.left = newLeftGrandsonLeft;
        newLeftChild.right = newLeftGrandsonRight;

        this.right = newRightChild;
        newRightChild.left = newRightGrandsonLeft;
        newRightChild.right = newRightGrandsonRight;


        newRightChild.computeHeight();
        this.computeHeight();
    }

    private BinaryNode<T> withoutLeftChild() {
        return toBuilder()
                .left(null)
                .build();
    }

    private BinaryNode<T> withoutRightChild() {
        return toBuilder()
                .right(null)
                .build();
    }

    private int computeHeight() {
        var height = -1;

        if (this.left != null) {
            height = Math.max(height, this.left.height);
        }

        if (this.right != null) {
            height = Math.max(height, this.right.height);
        }

        this.height = height + 1;

        return this.height;
    }

    private int getHeightDifference() {
        var leftTarget = 0;
        var rightTarget = 0;

        if (this.left != null) {
            leftTarget = 1 + this.left.height;
        }

        if (this.right != null) {
            rightTarget = 1 + this.right.height;
        }

        return leftTarget - rightTarget;
    }

    public String print() {
        var stringBuilder = new StringBuilder();
        var lines = new ArrayList<List<String>>();
        var level = new ArrayList<BinaryNode<T>>();
        var next = new ArrayList<BinaryNode<T>>();

        level.add(this);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            var line = new ArrayList<String>();

            nn = 0;

            for (var node : level) {
                if (node == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = node.getValue() + ":" + node.getHeight();
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(node.getLeft());
                    next.add(node.getRight());

                    if (node.getLeft() != null) nn++;
                    if (node.getRight() != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            var tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);

        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }

                    stringBuilder.append(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            stringBuilder.append(" ");
                        }
                    } else {
                        for (int k = 0; k < hpw; k++) {
                            stringBuilder.append(j % 2 == 0 ? " " : "─");
                        }

                        stringBuilder.append(j % 2 == 0 ? "┌" : "┐");

                        for (int k = 0; k < hpw; k++) {
                            stringBuilder.append(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }

                stringBuilder.append("\n");
            }

            // print line of numbers
            for (int j = 0; j < line.size(); j++) {

                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    stringBuilder.append(" ");
                }

                stringBuilder.append(f);

                for (int k = 0; k < gap2; k++) {
                    stringBuilder.append(" ");
                }
            }

            stringBuilder.append("\n");

            perpiece /= 2;
        }

        return stringBuilder.toString();
    }
}
