package de.paettyb.quadtree;

import processing.core.PApplet;
import processing.core.PVector;

public class Node<T> {

    private final PVector startPos;
    private final PVector endPos;
    private final PVector centerPos;
    private Node<T>[] children;
    private Node<T> parent;
    private boolean leaf = true;
    private PVector valuePos;
    private T value;

    private final PApplet context;

    public Node(PApplet context, Node<T> parent, PVector startPos, PVector endPos) {
        this.context = context;
        this.parent = parent;
        this.startPos = startPos;
        this.endPos = endPos;

        this.centerPos = PVector.sub(endPos, startPos).mult(0.5f).add(startPos);

//        PApplet.println("START: " + startPos);
//        PApplet.println("END: " + endPos);
    }

    public Node(PApplet context, Node<T> parent, PVector startPos, PVector endPos, T value, PVector valuePos) {
        this(context, parent, startPos, endPos);
        this.leaf = true;
        this.value = value;
        this.valuePos = valuePos;
    }

    public void insert(PVector p, T newValue) {
        // A leaf node always has its value set and has no children
        int i = getChildIndex(p);
        if (!leaf) {
            if (children[i] == null) {
                children[i] = new Node<T>(context, this, getNewChildStartPos(i), getNewChildEndPos(i), newValue, p);
            } else {
                children[i].insert(p, newValue);
            }
        } else {
            // We are a leaf and have a value already.
            if (value == null) {
                // We are the root node
                value = newValue;
                valuePos = p;
                return;
            }
            if(valuePos.equals(p)) {
                PApplet.println("Value already exists");
                return;
            }

            // We need to create the children array and put our current value there as well as the new one
            children = new Node[4];

            int oldI = getChildIndex(this.valuePos);
            children[oldI] = new Node<T>(context, this, getNewChildStartPos(oldI), getNewChildEndPos(oldI), value, valuePos);
            if (oldI != i)
                children[i] = new Node<T>(context, this, getNewChildStartPos(i), getNewChildEndPos(i), newValue, p);
            else
                children[oldI].insert(p, newValue);
            leaf = false;
            value = null;
            valuePos = null;
        }
    }

    public T get(PVector p) {
        return null;
    }

    public void draw() {
        context.noFill();
        context.stroke(255);
        context.strokeWeight(1);
        context.rect(startPos.x, startPos.y, endPos.x - startPos.x, endPos.y - startPos.y);
        if (leaf && value != null) {
            context.strokeWeight(5);
            context.colorMode(context.HSB, 360, 100, 100);
            context.stroke((Integer) value, 100, 100);
            context.point(valuePos.x, valuePos.y);
            context.colorMode(context.RGB, 255);
        }

        if(children != null) {
            for (int i = 0; i < children.length; i++) {
                if(children[i] != null) children[i].draw();
            }
        }

    }


    /***
     * @param p position to check
     * @return the indices of the children in the following pattern: <br>
     * [ 0 1 ] <br>
     * [ 2 3 ] <br>
     */

    private int getChildIndex(PVector p) {
        if (p.x < startPos.x || p.x > endPos.x || p.y < startPos.y || p.y > endPos.y)
            throw new RuntimeException("Point " + p + " not in scope of child");
        PVector diff = PVector.sub(p, centerPos);
        if (diff.x <= 0 && diff.y <= 0) return 0;
        if (diff.x > 0 && diff.y <= 0) return 1;
        if (diff.x <= 0 && diff.y > 0) return 2;
        if (diff.x > 0 && diff.y > 0) return 3;
        else throw new RuntimeException("How can this be? " + diff);
    }

    private PVector getNewChildStartPos(int index) {
        return switch (index) {
            case 0 -> startPos.copy();
            case 1 -> new PVector(centerPos.x, startPos.y);
            case 2 -> new PVector(startPos.x, centerPos.y);
            case 3 -> centerPos.copy();
            default -> throw new RuntimeException("Unexpected child index");
        };
    }

    private PVector getNewChildEndPos(int index) {
        return switch (index) {
            case 0 -> centerPos.copy();
            case 1 -> new PVector(endPos.x, centerPos.y);
            case 2 -> new PVector(centerPos.x, endPos.y);
            case 3 -> endPos.copy();
            default -> throw new RuntimeException("Unexpected child index");
        };
    }

    public boolean isLeaf() {
        return leaf;
    }

    public Node<T>[] getChildren() {
        return children;
    }

    public void setChildren(Node<T>[] children) {
        this.children = children;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
