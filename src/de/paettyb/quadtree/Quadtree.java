package de.paettyb.quadtree;

import processing.core.PApplet;
import processing.core.PVector;

public class Quadtree<T> {

    private Node<T> root;
    private PApplet context;

    public Quadtree(PApplet context, PVector startPos, PVector endPos) {
        root = new Node<T>(context, null, startPos, endPos);
    }

    public void insert(PVector p, T value) {
        root.insert(p, value);
    }

    public T get(PVector p) {
        return root.get(p);
    }

    public void draw(){
        root.draw();
    }


}
