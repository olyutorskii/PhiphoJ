/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * Ring buffer for float primitive type.
 */
@SuppressWarnings("EqualsAndHashcode")
public class RingBufFloat extends AbstractRingBuf implements Iterable<Float> {

    private final float[] rawbuf;


    /**
     * Constructor.
     *
     * <p>Capacity must be 0 or larger.
     *
     * @param capacity max capacity of ring buffer
     * @throws NegativeArraySizeException capacity is negative
     */
    public RingBufFloat(int capacity) throws NegativeArraySizeException {
        super(capacity);
        this.rawbuf = new float[capacity];
        return;
    }


    /**
     * Build new capacity instance and copy contents.
     *
     * @param old old ring buffer
     * @param newCapacity new capacity size
     * @return new capacity ring buffer
     * @throws NullPointerException old buffer argument is null
     * @throws NegativeArraySizeException capacity is negative
     */
    public static RingBufFloat duplicate(RingBufFloat old, int newCapacity)
            throws NullPointerException, NegativeArraySizeException {
        RingBufFloat result = new RingBufFloat(newCapacity);
        copyContents(old, result);
        return result;
    }


    /**
     * Get element value with contents index.
     *
     * @param idx contents index starts with 0
     * @return value of element
     * @throws IndexOutOfBoundsException invalid index
     */
    public float peekElem(int idx) throws IndexOutOfBoundsException {
        int rawIdx = idxToRaw(idx);
        float result = this.rawbuf[rawIdx];
        return result;
    }

    /**
     * Push value to tail of ring buffer.
     *
     * @param val value
     * @return new size
     * @throws IndexOutOfBoundsException buf is full
     */
    public int pushTail(float val) throws IndexOutOfBoundsException {
        if (!hasRemaining()) throw new IndexOutOfBoundsException();

        int tail = getTailRawIdx();
        this.rawbuf[tail] = val;
        int result = forwardTail(1);

        return result;
    }

    /**
     * Push values to tail of ring buffer.
     *
     * @param src values array
     * @param offset push start in array
     * @param length push length in array
     * @return new size
     * @throws NullPointerException array argument is null
     * @throws IndexOutOfBoundsException illegal index
     */
    public int pushTail(float[] src, int offset, int length)
            throws NullPointerException, IndexOutOfBoundsException {
        int result = pushTailImpl(src, offset, length);
        return result;
    }

    /**
     * Push values to tail of ring buffer.
     *
     * @param src values
     * @return new size
     * @throws NullPointerException null argument
     */
    public int pushTail(float... src) throws NullPointerException {
        int result = pushTail(src, 0, src.length);
        return result;
    }

    /**
     * Copy contents to array.
     *
     * @param srcPos starting contents index
     * @param dst destination array
     * @param dstPos starting destination index
     * @param length copy length
     * @throws NullPointerException array argument is null
     * @throws IndexOutOfBoundsException illegal index argument
     */
    public void copyToArray(int srcPos, float[] dst, int dstPos, int length)
            throws NullPointerException, IndexOutOfBoundsException {
        copyToArrayImpl(srcPos, dst, dstPos, length);
        return;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    protected float[] getRawBufArray() {
        return this.rawbuf;
    }

    /**
     * Return element value by raw array index.
     *
     * @param rawIdx raw array index
     * @return element value
     */
    protected float rawIdxToVal(int rawIdx) {
        float result = this.rawbuf[rawIdx];
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @param oldHash {@inheritDoc}
     * @param rawIdx {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected int rawIdxToFnv1aHash(int oldHash, int rawIdx) {
        float fVal = this.rawbuf[rawIdx];
        int iVal = Float.floatToIntBits(fVal);
        int hash = oldHash ^ iVal;
        hash *= HASH_PRIME;
        return hash;
    }

    /**
     * {@inheritDoc}
     *
     * <p>For debugging.
     *
     * @param rawIdx {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected String rawIdxToString(int rawIdx) {
        float val = this.rawbuf[rawIdx];
        String result = Float.toString(val);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>NaN patterns equal NaN patterns.
     *
     * <p>-0.0 not equals +0.0 .
     *
     * @param o {@inheritDoc}
     * @return {@inheritDoc}
     * @see Float#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RingBufFloat)) return false;

        RingBufFloat othr = (RingBufFloat) o;

        if (size() != othr.size()) return false;

        PrimitiveIterator.OfInt thisIt =      rawIdxIterator();
        PrimitiveIterator.OfInt othrIt = othr.rawIdxIterator();

        while (thisIt.hasNext()) {
            int thisRawIdx = thisIt.nextInt();
            int othrRawIdx = othrIt.nextInt();
            float thisVal = this.rawbuf[thisRawIdx];
            float othrVal = othr.rawbuf[othrRawIdx];
            int thisIval = Float.floatToIntBits(thisVal);
            int othrIval = Float.floatToIntBits(othrVal);
            if (thisIval != othrIval) return false;
        }
        assert !(thisIt.hasNext() || othrIt.hasNext());

        return true;
    }

    /**
     * Return contents iterator.
     *
     * @return contents iterator
     */
    @Override
    public IteratorOfFloat iterator() {
        return new IteratorOfFloat();
    }


    /**
     * Contents value iterator.
     *
     * @see PrimitiveIterator
     */
    public class IteratorOfFloat implements Iterator<Float> {

        private final PrimitiveIterator.OfInt rawIdxIt = rawIdxIterator();


        /**
         * Constructor.
         */
        IteratorOfFloat() {
            super();
            return;
        }


        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return this.rawIdxIt.hasNext();
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public Float next() {
            float val = nextFloat();
            return val;
        }

        /**
         * Return next value.
         *
         * @return value
         * @throws NoSuchElementException no more element
         */
        public float nextFloat() throws NoSuchElementException {
            int rawIdx = this.rawIdxIt.nextInt();
            float result = rawIdxToVal(rawIdx);
            return result;
        }

    }

}
