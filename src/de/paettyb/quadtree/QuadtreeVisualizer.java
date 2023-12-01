package de.paettyb.quadtree;

import processing.core.PApplet;
import processing.core.PVector;

public class QuadtreeVisualizer extends PApplet {

    private Quadtree<Integer> tree;

    @Override
    public void settings() {
        size(800, 600);
        tree = new Quadtree<>(this, new PVector(0, 0), new PVector(width, height));
        tree.insert(new PVector(40, 100), 45);
    }

    @Override
    public void draw() {
        background(55);
        tree.draw();
    }

    @Override
    public void mousePressed() {
        tree.insert(new PVector(mouseX, mouseY), parseInt(random(1000)));
    }

    public static void main(String[] args) {
        PApplet.main("de.paettyb.quadtree.QuadtreeVisualizer");
    }
}