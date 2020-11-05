/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * Ring buffer for byte primitive type.
 */
@SuppressWarnings("EqualsAndHashcode")
public class RingBufByte extends AbstractRingBuf implements Iterable<Byte> {

    private final byte[] rawbuf;


    /**
     * Constructor.
     *
     * <p>Capacity must be 0 or larger.
     *
     * @param capacity max capacity of ring buffer
     * @throws NegativeArraySizeException capacity is negative
     */
    public RingBufByte(int capacity) throws NegativeArraySizeException {
        super(capacity);
        this.rawbuf = new byte[capacity];
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
    public static RingBufByte duplicate(RingBufByte old, int newCapacity)
            throws NullPointerException, NegativeArraySizeException {
        RingBufByte result = new RingBufByte(newCapacity);
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
    public byte peekElem(int idx) throws IndexOutOfBoundsException {
        int rawIdx = idxToRaw(idx);
        byte result = this.rawbuf[rawIdx];
        return result;
    }

    /**
     * Push value to tail of ring buffer.
     *
     * @param val value
     * @return new size
     * @throws IndexOutOfBoundsException buf is full
     */
    public int pushTail(byte val) throws IndexOutOfBoundsException {
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
    public int pushTail(byte[] src, int offset, int length)
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
    public int pushTail(byte... src) throws NullPointerException {
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
    public void copyToArray(int srcPos, byte[] dst, int dstPos, int length)
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
    protected byte[] getRawBufArray() {
        return this.rawbuf;
    }

    /**
     * Return element value by raw array index.
     *
     * @param rawIdx raw array index
     * @return element value
     */
    protected byte rawIdxToVal(int rawIdx) {
        byte result = this.rawbuf[rawIdx];
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
        byte bVal = this.rawbuf[rawIdx];
        int hash = oldHash ^ bVal;
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
        byte val = this.rawbuf[rawIdx];
        String result = Byte.toString(val);
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
        if (!(o instanceof RingBufByte)) return false;

        RingBufByte othr = (RingBufByte) o;

        if (size() != othr.size()) return false;

        PrimitiveIterator.OfInt thisIt =      rawIdxIterator();
        PrimitiveIterator.OfInt othrIt = othr.rawIdxIterator();

        while (thisIt.hasNext()) {
            int thisRawIdx = thisIt.nextInt();
            int othrRawIdx = othrIt.nextInt();
            byte thisVal = this.rawbuf[thisRawIdx];
            byte othrVal = othr.rawbuf[othrRawIdx];
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
    public IteratorOfByte iterator() {
        return new IteratorOfByte();
    }


    /**
     * Contents value iterator.
     *
     * @see PrimitiveIterator
     */
    public class IteratorOfByte implements Iterator<Byte> {

        private final PrimitiveIterator.OfInt rawIdxIt = rawIdxIterator();


        /**
         * Constructor.
         */
        IteratorOfByte() {
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
        public Byte next() {
            byte val = nextByte();
            return val;
        }

        /**
         * Return next value.
         *
         * @return value
         * @throws NoSuchElementException no more element
         */
        public byte nextByte() throws NoSuchElementException {
            int rawIdx = this.rawIdxIt.nextInt();
            byte result = rawIdxToVal(rawIdx);
            return result;
        }

    }

}
