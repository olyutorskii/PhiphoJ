/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * Ring buffer for double primitive type.
 */
@SuppressWarnings("EqualsAndHashcode")
public class RingBufDouble extends AbstractRingBuf implements Iterable<Double> {

    private final double[] rawbuf;


    /**
     * Constructor.
     *
     * <p>Capacity must be 0 or larger.
     *
     * @param capacity max capacity of ring buffer
     * @throws NegativeArraySizeException capacity is negative
     */
    public RingBufDouble(int capacity) throws NegativeArraySizeException {
        super(capacity);
        this.rawbuf = new double[capacity];
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
    public static RingBufDouble duplicate(RingBufDouble old, int newCapacity)
            throws NullPointerException, NegativeArraySizeException {
        RingBufDouble result = new RingBufDouble(newCapacity);
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
    public double peekElem(int idx) throws IndexOutOfBoundsException {
        int rawIdx = idxToRaw(idx);
        double result = this.rawbuf[rawIdx];
        return result;
    }

    /**
     * Push value to tail of ring buffer.
     *
     * @param val value
     * @return new size
     * @throws IndexOutOfBoundsException buf is full
     */
    public int pushTail(double val) throws IndexOutOfBoundsException {
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
    public int pushTail(double[] src, int offset, int length)
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
    public int pushTail(double... src) throws NullPointerException {
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
    public void copyToArray(int srcPos, double[] dst, int dstPos, int length)
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
    protected double[] getRawBufArray() {
        return this.rawbuf;
    }

    /**
     * Return element value by raw array index.
     *
     * @param rawIdx raw array index
     * @return element value
     */
    protected double rawIdxToVal(int rawIdx) {
        double result = this.rawbuf[rawIdx];
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
        double dVal = this.rawbuf[rawIdx];
        long lVal = Double.doubleToLongBits(dVal);
        int iVal = (int) lVal;
        iVal ^= (int) (lVal >> 32);
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
        double val = this.rawbuf[rawIdx];
        String result = Double.toString(val);
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
     * @see Double#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RingBufDouble)) return false;

        RingBufDouble othr = (RingBufDouble) o;

        if (size() != othr.size()) return false;

        PrimitiveIterator.OfInt thisIt =      rawIdxIterator();
        PrimitiveIterator.OfInt othrIt = othr.rawIdxIterator();

        while (thisIt.hasNext()) {
            int thisRawIdx = thisIt.nextInt();
            int othrRawIdx = othrIt.nextInt();
            double thisVal = this.rawbuf[thisRawIdx];
            double othrVal = othr.rawbuf[othrRawIdx];
            long thisLval = Double.doubleToLongBits(thisVal);
            long othrLval = Double.doubleToLongBits(othrVal);
            if (thisLval != othrLval) return false;
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
    public PrimitiveIterator.OfDouble iterator() {
        return new ContentsIterator();
    }


    /**
     * Contents value iterator.
     */
    private class ContentsIterator implements PrimitiveIterator.OfDouble {

        private final PrimitiveIterator.OfInt rawIdxIt = rawIdxIterator();


        /**
         * Constructor.
         */
        ContentsIterator() {
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
         * @throws NoSuchElementException {@inheritDoc}
         */
        @Override
        public double nextDouble() throws NoSuchElementException {
            int rawIdx = this.rawIdxIt.nextInt();
            double result = rawIdxToVal(rawIdx);
            return result;
        }

    }

}
