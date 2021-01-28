/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;

/**
 * Abstract class of ring buffer implementations.
 *
 * <p>Ring buffer implements for primitive type.
 *
 * <p>FIFO contents on ring buffer.
 *
 * <p>Fixed length buffer only supported.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Circular_buffer">
 * Circular Buffer (Wikipedia)
 * </a>
 */
public abstract class AbstractRingBuf {

    /**
     * FNV-1a hash basis.
     */
    protected static final int HASH_BASIS = 0x811c9dc5;

    /**
     * FNC-1a hash prime.
     */
    protected static final int HASH_PRIME =
            (((0x0001 << 16) | 0x0001) << 8) | 0x93;

    private static final int OUTS_LIMIT = 7;
    private static final String MSG_EMPTY = "Empty Ring...";


    private final int capacity;

    private int size;

    private int headRawIdx;
    private int tailRawIdx;

    /*

    HEAD indicates top of contents when contents is not empty.

    TAIL indicates next of last contents when contents is not empty.

    TAIL must be 0 if end of content is at end of buffer.

    If TAIL points HEAD, it's mean no contents or no more space.
    Determin by SIZE.

    Both HEAD and TAIL must be less than CAPACITY when CAPACITY is not 0.

    Both HEAD and TAIL must be 0 if CAPACITY is 0.

    */


    /**
     * Constructor.
     *
     * <p>Capacity must be 0 or larger.
     *
     * @param capacity length of raw buffer
     * @throws NegativeArraySizeException capacity is negative
     */
    protected AbstractRingBuf(int capacity) throws NegativeArraySizeException {
        super();

        if (capacity < 0) throw new NegativeArraySizeException();
        this.capacity = capacity;

        this.size = 0;

        this.headRawIdx = 0;
        this.tailRawIdx = 0;

        // assert postCondition();

        return;
    }


    /**
     * Duplicate contents from source to destination.
     *
     * <p>Source and destination must be same type.
     *
     * <p>Contents of destination will be destroyed.
     *
     * <p>Copy length is a smaller of
     * source contents size and newborn destination capacity.
     *
     * @param src source
     * @param dst destination
     * @return contents size
     * @throws NullPointerException argument is null
     * @throws ArrayStoreException raw buffer types between source and destination are not same.
     */
    protected static int copyContents(AbstractRingBuf src, AbstractRingBuf dst)
            throws NullPointerException, ArrayStoreException {
        Objects.requireNonNull(src);
        Objects.requireNonNull(dst);

        int txSize = StrictMath.min(src.size, dst.capacity);
        if (txSize <= 0) return 0;

        dst.clear();
        assert dst.tailRawIdx == 0;

        Object dstBuf = dst.getRawBufArray();
        src.copyToArrayImpl(0, dstBuf, 0, txSize);
        dst.forwardTail(txSize);

        return dst.size;
    }


    /**
     * Return max available length of contents.
     *
     * @return max available length of contents
     */
    public int capacity() {
        return this.capacity;
    }

    /**
     * Return contents length.
     *
     * @return length of contents.
     */
    public int size() {
        return this.size;
    }

    /**
     * Return true if contents are empty.
     *
     * @return true if ring buffer contents are empty
     */
    public boolean isEmpty() {
        boolean result = this.size <= 0;
        return result;
    }

    /**
     * Return true if buffer has remaining space.
     *
     * @return true if ring buffer has remaining space
     */
    public boolean hasRemaining() {
        boolean result = this.size < this.capacity;
        return result;
    }

    /**
     * Return remaining space of ring buffer.
     *
     * @return length of remaining space
     */
    public int remaining() {
        int result = this.capacity - this.size;
        return result;
    }

    /**
     * Clear ring buffer contents.
     *
     * <p>ring buffer contents length will be 0.
     */
    public void clear() {
        this.headRawIdx = 0;
        this.tailRawIdx = 0;
        this.size = 0;

        assert postCondition();

        return;
    }

    /**
     * Forward head and declement contents size.
     *
     * @param n forward count
     * @return new contents length
     */
    protected int forwardHead(int n) {
        assert n >= 0;
        assert this.capacity > 0;

        int rawIdx = this.headRawIdx + n;
        while (rawIdx >= this.capacity) {
            rawIdx -= this.capacity;
        }
        this.headRawIdx = rawIdx;

        this.size -= n;

        assert postCondition();

        return this.size;

    }

    /**
     * Forward tail and inclement contents size.
     *
     * @param n forward count
     * @return new contents length
     */
    protected int forwardTail(int n) {
        assert n >= 0;
        assert this.capacity > 0;

        int rawIdx = this.tailRawIdx + n;
        while (rawIdx >= this.capacity) {
            rawIdx -= this.capacity;
        }
        this.tailRawIdx = rawIdx;

        this.size += n;

        assert postCondition();

        return this.size;

    }

    /**
     * Post conditions.
     *
     * <p>(for assert statement)
     *
     * @return true if post conditions are good
     * @throws AssertionError assert error
     */
    protected boolean postCondition() throws AssertionError {
        int capa = this.capacity;
        int blen = this.size;
        int head = this.headRawIdx;
        int tail = this.tailRawIdx;

        assert capa >= 0;

        assert 0 <= blen && blen <= capa;

        if (capa == 0) {
            assert head == 0 && tail == 0;
        } else {
            assert 0 <= head &&  head < capa;
            assert 0 <= tail &&  tail < capa;
        }

        boolean isEmpty = blen == 0;
        boolean isFull  = blen == capa;

        if (isEmpty || isFull) {
            assert head == tail;
        } else {
            assert head != tail;
        }

        if (head < tail) {
            assert blen == tail - head;
        } else if (head > tail) {
            int gap = head - tail;
            assert blen == capa - gap;
        } else {
            assert isEmpty || isFull;
        }

        return true;
    }

    /**
     * Return raw primitive array.
     *
     * @return raw array
     */
    protected abstract Object getRawBufArray();

    /**
     * Push array elements to tail of ring buffer.
     *
     * <p>Array type free version.
     *
     * @param src primitive array
     * @param offset pushing start position of source
     * @param length length of pushing
     * @return new size
     * @throws NullPointerException array is null
     * @throws ArrayStoreException array type is invalid
     * @throws IndexOutOfBoundsException invalid index
     */
    @SuppressWarnings("SuspiciousSystemArraycopy")
    protected int pushTailImpl(Object src, int offset, int length)
            throws NullPointerException, ArrayStoreException, IndexOutOfBoundsException {
        Objects.requireNonNull(src);

        Object rawArray = getRawBufArray();
        Class<?> rawArrayClass = rawArray.getClass();
        assert rawArrayClass.isArray();

        Class<?> srcArrayClass = src.getClass();

        if (!rawArrayClass.equals(srcArrayClass)) {
            throw new ArrayStoreException();
        }

        boolean negaParam = offset < 0 || length < 0;
        boolean overSrc = offset + length > Array.getLength(src);
        boolean overDst = remaining() < length;
        if (negaParam || overSrc || overDst) {
            throw new IndexOutOfBoundsException();
        }

        if (length == 0) return this.size;

        int toRawIdx = this.tailRawIdx;
        int tx1 = StrictMath.min(length, this.capacity - toRawIdx);
        int tx2 = length - tx1;

        System.arraycopy(src, offset, rawArray, toRawIdx, tx1);
        if (tx2 > 0) {
            System.arraycopy(src, offset + tx1, rawArray, 0, tx2);
        }

        forwardTail(length);

        return this.size;
    }

    /**
     * Chop N-elements from head of contents.
     *
     * @param n num of elements. (nothing happen if 0)
     * @return new contents size
     * @throws IndexOutOfBoundsException n is invalid
     */
    public int chopHead(int n) throws IndexOutOfBoundsException {
        if (n < 0 || this.size < n) {
            throw new IndexOutOfBoundsException();
        }

        if (n != 0) {
            forwardHead(n);
        }

        return this.size;
    }

    /**
     * Chop 1-element from head of contents.
     *
     * @return new contents size
     * @throws IndexOutOfBoundsException no contents
     */
    public int chopHead() throws IndexOutOfBoundsException {
        return chopHead(1);
    }

    /**
     * Return head raw index.
     *
     * @return raw index
     */
    protected int getHeadRawIdx() {
        return this.headRawIdx;
    }

    /**
     * Return tail raw index.
     *
     * @return raw index
     */
    protected int getTailRawIdx() {
        return this.tailRawIdx;
    }

    /**
     * Contents index to raw buf index.
     *
     * @param idx contents index
     * @return raw buf index
     * @throws IndexOutOfBoundsException invalid index
     */
    protected int idxToRaw(int idx) throws IndexOutOfBoundsException {
        if (idx < 0 || this.size <= idx) {
            throw new IndexOutOfBoundsException();
        }

        int rawIdx = this.headRawIdx + idx;
        while (rawIdx >= this.capacity) {
            rawIdx -= this.capacity;
        }

        return rawIdx;
    }

    /**
     * Copy contents to array.
     *
     * <p>Array type is free.
     *
     * @param srcPos starting contents index
     * @param dst destination array
     * @param dstPos starting destination index
     * @param length copy length
     * @throws NullPointerException array argument is null
     * @throws ArrayStoreException array type is invalid
     * @throws IndexOutOfBoundsException illegal index argument
     */
    @SuppressWarnings("SuspiciousSystemArraycopy")
    protected void copyToArrayImpl(int srcPos, Object dst, int dstPos, int length)
            throws NullPointerException, ArrayStoreException, IndexOutOfBoundsException {
        Objects.requireNonNull(dst);

        Object rawArray = getRawBufArray();
        Class<?> rawArrayClass = rawArray.getClass();
        Class<?> dstArrayClass = dst.getClass();

        assert rawArrayClass.isArray();
        if (!rawArrayClass.equals(dstArrayClass)) {
            throw new ArrayStoreException();
        }

        boolean negaParam = srcPos < 0 || dstPos < 0 || length < 0;
        boolean overSrc = srcPos + length > this.size;
        boolean overDst = dstPos + length > Array.getLength(dst);
        if (negaParam || overSrc || overDst) {
            throw new IndexOutOfBoundsException();
        }

        if (length == 0) return;

        int fromRawIdx = idxToRaw(srcPos);
        int tx1 = StrictMath.min(length, this.capacity - fromRawIdx);
        int tx2 = length - tx1;

        System.arraycopy(rawArray, fromRawIdx, dst, dstPos, tx1);
        if (tx2 > 0) {
            System.arraycopy(rawArray, 0, dst, dstPos + tx1, tx2);
        }

        return;
    }

    /**
     * {@inheritDoc}
     *
     * @param o {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * {@inheritDoc}
     *
     * <p>Hash function is FNV-1a.
     *
     * <p>Contents length is hashed.
     *
     * <p>Head, tail and median values are hashed too If any contens.
     *
     * @return {@inheritDoc}
     * @see <a href="https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function#FNV-1a_hash">
     * FNV-1a hash (Wikipedia)
     * </a>
     */
    @Override
    public int hashCode() {
        int hash = HASH_BASIS;

        int length = size();
        hash ^= length;
        hash *= HASH_PRIME;

        if (length == 0) return hash;

        int rawIdx0 = idxToRaw(0);
        int rawIdx1 = idxToRaw(length / 2);
        int rawIdx2 = idxToRaw(length - 1);

        hash = rawIdxToFnv1aHash(hash, rawIdx0);
        hash = rawIdxToFnv1aHash(hash, rawIdx1);
        hash = rawIdxToFnv1aHash(hash, rawIdx2);

        return hash;
    }

    /**
     * Hashing raw array value by raw index.
     *
     * <p>FNV-1a hash function recommended.
     *
     * @param oldHash old hash value
     * @param rawIdx raw array index
     * @return new hash value
     * @see HASH_PRIME
     */
    protected abstract int rawIdxToFnv1aHash(int oldHash, int rawIdx);

    /**
     * Return String form element by raw index.
     *
     * <p>For toString()
     *
     * @param rawIdx raw index
     * @return String form of element
     */
    protected abstract String rawIdxToString(int rawIdx);

    /**
     * {@inheritDoc}
     *
     * <p>For debugging.
     *
     * @return {@inheritDoc}
     */
    @Override
    public String toString() {
        if (isEmpty()) return MSG_EMPTY;

        StringBuilder sb = new StringBuilder();

        int outs = 0;

        PrimitiveIterator.OfInt it = rawIdxIterator();
        while (it.hasNext()) {
            if (outs >= OUTS_LIMIT) break;
            if (outs > 0) sb.append(',');

            int rawIdx = it.nextInt();
            String txt = rawIdxToString(rawIdx);
            sb.append(txt);
            outs++;
        }

        // 13,14,...(x 999)
        if (this.size > OUTS_LIMIT) {
            sb.append(",...(x ");
            sb.append(Integer.toString(this.size));
            sb.append(')');
        }

        String result = sb.toString();
        return result;
    }

    /**
     * Return raw index Iterator.
     *
     * <p>Iterates raw index by contents order.
     *
     * @return raw index iterator
     */
    protected PrimitiveIterator.OfInt rawIdxIterator() {
        return new RawIdxIterator();
    }


    /**
     * Raw array index iterator.
     *
     * <p>Iterates raw array index by contents order.
     */
    private class RawIdxIterator implements PrimitiveIterator.OfInt {

        private final int maxCapa = capacity();
        private int rest = size();
        private int rawCursor = getHeadRawIdx();


        /**
         * Constructor.
         */
        RawIdxIterator() {
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
            boolean result = this.rest > 0;
            return result;
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         * @throws NoSuchElementException {@inheritDoc}
         */
        @Override
        public int nextInt() throws NoSuchElementException {
            if (this.rest <= 0) {
                throw new NoSuchElementException();
            }

            int result = this.rawCursor;

            this.rawCursor++;
            if (this.rawCursor >= this.maxCapa) this.rawCursor = 0;
            this.rest--;

            return result;
        }

    }

}
