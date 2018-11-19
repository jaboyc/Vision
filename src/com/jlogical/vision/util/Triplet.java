package com.jlogical.vision.util;

/**
 * Container that stores 3 values.
 */
public class Triplet<A, B, C> {
    /**
     * The first value.
     */
    private A first;

    /**
     * The second value.
     */
    private B second;

    /**
     * The third value.
     */
    private C third;

    /**
     * Creates a Triplet with all the given values.
     *
     */
    public Triplet(A first, B second, C third){
        this.first = first;
        this.second = second;
        this.third = third;
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

    public C getThird() {
        return third;
    }

    public void setThird(C third) {
        this.third = third;
    }
}
