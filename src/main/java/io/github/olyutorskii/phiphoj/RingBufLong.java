/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * Ring buffer for long primitive type.
 */
@SuppressWarnings("EqualsAndHashcode")
public class RingBufLong extends AbstractRingBuf implements Iterable<Long> {

    private final long[] rawbuf;


    /**
     * Constructor.
     *
     * <p>Capacity must be 0 or larger.
     *
     * @param capacity max capacity of ring buffer
     * @throws NegativeArraySizeException capacity is negative
     */
    public RingBufLong(int capacity) throws NegativeArraySizeException {
        super(capacity);
        this.rawbuf = new long[capacity];
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
    public static RingBufLong duplicate(RingBufLong old, int newCapacity)
            throws NullPointerException, NegativeArraySizeException {
        RingBufLong result = new RingBufLong(newCapacity);
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
    public long peekElem(int idx) throws IndexOutOfBoundsException {
        int rawIdx = idxToRaw(idx);
        long result = this.rawbuf[rawIdx];
        return result;
    }

    /**
     * Push value to tail of ring buffer.
     *
     * @param val value
     * @return new size
     * @throws IndexOutOfBoundsException buf is full
     */
    public int pushTail(long val) throws IndexOutOfBoundsException {
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
    public int pushTail(long[] src, int offset, int length)
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
    public int pushTail(long... src) throws NullPointerException {
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
    public void copyToArray(int srcPos, long[] dst, int dstPos, int length)
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
    protected long[] getRawBufArray() {
        return this.rawbuf;
    }

    /**
     * Return element value by raw array index.
     *
     * @param rawIdx raw array index
     * @return element value
     */
    protected long rawIdxToVal(int rawIdx) {
        long result = this.rawbuf[rawIdx];
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
        long lVal = this.rawbuf[rawIdx];
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
        long val = this.rawbuf[rawIdx];
        String result = Long.toString(val);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RingBufLong)) return false;

        RingBufLong othr = (RingBufLong) o;

        if (size() != othr.size()) return false;

        PrimitiveIterator.OfInt thisIt =      rawIdxIterator();
        PrimitiveIterator.OfInt othrIt = othr.rawIdxIterator();

        while (thisIt.hasNext()) {
            int thisRawIdx = thisIt.nextInt();
            int othrRawIdx = othrIt.nextInt();
            long thisVal = this.rawbuf[thisRawIdx];
            long othrVal = othr.rawbuf[othrRawIdx];
            if (thisVal != othrVal) return false;
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
    public PrimitiveIterator.OfLong iterator() {
        return new ContentsIterator();
    }


    /**
     * Contents value iterator.
     */
    private class ContentsIterator implements PrimitiveIterator.OfLong {

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
        public long nextLong() throws NoSuchElementException {
            int rawIdx = this.rawIdxIt.nextInt();
            long result = rawIdxToVal(rawIdx);
            return result;
        }

    }

}
