package com.jlogical.vision.util;

/**
 * Container that stores two Objects.
 */
public class Pair<A, B> {

    /**
     * The first value.
     */
    private A first;

    /**
     * The second value.
     */
    private B second;

    /**
     * Creates a new Pair with a given first and second object.
     */
    public Pair(A first, B second){
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString(){
        return "{"+first+","+second+"}";
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}
