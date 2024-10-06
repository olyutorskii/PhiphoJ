/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class RingBufLongTest {

    public RingBufLongTest() {
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * Test of constructor, of class RingBufLong.
     */
    @Test
    public void testConstructor() {
        System.out.println("Constructor");

        RingBufLong rb;

        rb = new RingBufLong(1);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufLong(999999);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufLong(0);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb = new RingBufLong(-1);
            fail();
            rb.getClass();
        } catch (NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of toString method, of class RingBufLong.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        RingBufLong rb;

        rb = new RingBufLong(0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufLong(3);
        assertEquals("Empty Ring...", rb.toString());
        rb.pushTail(1, 2);
        assertEquals("1,2", rb.toString());
        rb.pushTail(3);
        assertEquals("1,2,3", rb.toString());
        rb.chopHead(2);
        assertEquals("3", rb.toString());
        rb.pushTail(4);
        assertEquals("3,4", rb.toString());
        rb.pushTail(5);
        assertEquals("3,4,5", rb.toString());
        rb.chopHead();
        assertEquals("4,5", rb.toString());
        rb.chopHead(2);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufLong(8);
        rb.pushTail(11, 12, 13, 14, 15, 16, 17);
        assertEquals("11,12,13,14,15,16,17", rb.toString());
        rb.pushTail(18);
        assertEquals("11,12,13,14,15,16,17,...(x 8)", rb.toString());

        rb = new RingBufLong(5);
        rb.pushTail(Long.MIN_VALUE, -1, 0, 1, Long.MAX_VALUE);
        assertEquals("-9223372036854775808,-1,0,1,9223372036854775807", rb.toString());

        return;
    }

    /**
     * Test of hashCode method, of class RingBufLong.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        RingBufLong rb1, rb2;

        rb1 = new RingBufLong(0);
        rb2 = new RingBufLong(3);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufLong(3);
        rb2 = new RingBufLong(5);
        rb1.pushTail(11, 12);
        rb2.pushTail(11, 12);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufLong(3);
        rb2 = new RingBufLong(3);

        rb1.pushTail(11, 12, 13);
        rb1.chopHead(2);
        rb1.pushTail(14);
        rb2.pushTail(13, 14);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        return;
    }

    /**
     * Test of equals method, of class RingBufLong.
     */
    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testEquals() {
        System.out.println("equals");

        RingBufLong rb1, rb2;

        rb1 = new RingBufLong(0);
        rb2 = new RingBufLong(0);

        assertTrue(rb1.equals(rb1));
        assertTrue(rb1.equals(rb2));

        RingBufLong rbNull = null;
        String txt = "XYZ";
        assertFalse(rb1.equals(rbNull));
        assertFalse(rb1.equals(txt));

        rb1 = new RingBufLong(3);
        rb2 = new RingBufLong(3);

        rb1.pushTail(11);
        assertFalse(rb1.equals(rb2));
        rb2.pushTail(11, 12);
        assertFalse(rb1.equals(rb2));
        rb1.pushTail(12);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufLong(3);
        rb2 = new RingBufLong(6);

        rb1.pushTail(1,2);
        rb2.pushTail(1,2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufLong(6);
        rb2 = new RingBufLong(6);

        rb1.pushTail(1,2,3,4,5,6);
        rb1.chopHead(4);
        rb1.pushTail(7, 8);

        rb2.pushTail(5,6,7,8);

        assertTrue(rb1.equals(rb2));
        rb2.pushTail(9);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufLong(3);
        rb2 = new RingBufLong(3);

        rb1.pushTail(11, 12, 13);
        rb2.pushTail(11, 12, 33);
        assertFalse(rb1.equals(rb2));

        return;
    }

    /**
     * Test of pushTail method, of class RingBufLong.
     */
    @Test
    public void testPushTail_3args() {
        System.out.println("pushTail");

        RingBufLong rb;

        rb = new RingBufLong(3);
        try {
            rb.pushTail(null, 0, 10);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufLong(3);
        rb.pushTail(new long[]{11, 12, 13}, 0, 3);
        assertEquals("11,12,13", rb.toString());

        rb = new RingBufLong(3);
        rb.pushTail(new long[]{11, 12, 13}, 0, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufLong(3);
        rb.pushTail(new long[]{11, 12, 13}, 1, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufLong(3);
        try {
            rb.pushTail(new long[]{11, 12, 13}, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufLong(3);
        try {
            rb.pushTail(new long[]{11, 12, 13}, -1, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufLong(3);
        try {
            rb.pushTail(new long[]{11, 12, 13}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufLong(3);
        try {
            rb.pushTail(new long[]{11, 12, 13, 14}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufLong(6);
        rb.pushTail(11, 12, 13, 14);
        rb.chopHead(2);
        rb.pushTail(new long[]{14, 15, 16, 17, 18}, 1, 3);
        assertEquals("13,14,15,16,17", rb.toString());

        return;
    }

    /**
     * Test of pushTail method, of class RingBufLong.
     */
    @Test
    public void testPushTail_longArr() {
        System.out.println("pushTail");

        RingBufLong rb;

        rb = new RingBufLong(3);

        try {
            rb.pushTail(null);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb.pushTail(new long[]{11});
        assertEquals("11",rb.toString());

        rb.pushTail(12, 13);
        assertEquals("11,12,13",rb.toString());

        rb = new RingBufLong(3);

        try {
            rb.pushTail(11, 12, 13, 14);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of pushTail method, of class RingBufLong.
     */
    @Test
    public void testPushTail_long() {
        System.out.println("pushTail");

        RingBufLong rb;

        rb = new RingBufLong(3);

        rb.pushTail((long)11);
        rb.pushTail((long)21);
        rb.pushTail((long)31);
        assertEquals("11,21,31",rb.toString());

        try {
            rb.pushTail((long)11);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals("21,31",rb.toString());

        rb.pushTail((long)41);
        assertEquals("21,31,41",rb.toString());

        return;
    }

    /**
     * Test of peekElem method, of class RingBuf.
     */
    @Test
    public void testPeekElem() {
        System.out.println("peekElem");

        RingBufLong rb;

        rb = new RingBufLong(3);

        try {
            rb.peekElem(0);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail(11);
        assertEquals((long)11, rb.peekElem(0));
        try {
            rb.peekElem(-1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((long)12);
        assertEquals((long)11, rb.peekElem(0));
        assertEquals((long)12, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals((long)12, rb.peekElem(0));
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((long)13);
        assertEquals((long)12, rb.peekElem(0));
        assertEquals((long)13, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((long)14);
        assertEquals((long)12, rb.peekElem(0));
        assertEquals((long)13, rb.peekElem(1));
        assertEquals((long)14, rb.peekElem(2));
        try {
            rb.peekElem(3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of iterator method, of class RingBufLong.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");

        RingBufLong rb;
        PrimitiveIterator.OfLong it;

        rb = new RingBufLong(3);
        it = rb.iterator();
        assertFalse(it.hasNext());
        try {
            it.nextLong();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb.pushTail((long)11);
        rb.pushTail((long)12);
        rb.pushTail((long)13);
        rb.chopHead(2);
        rb.pushTail((long)14);

        it = rb.iterator();
        assertTrue(it.hasNext());
        assertEquals((long)13, it.nextLong());
        assertTrue(it.hasNext());
        assertEquals((long)14, it.nextLong());
        assertFalse(it.hasNext());
        try {
            it.nextLong();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb = new RingBufLong(3);
        rb.pushTail(11, 12, 13);
        it = rb.iterator();
        assertEquals(Long.valueOf(11), it.next());
        assertEquals(Long.valueOf(12), it.next());
        assertEquals(Long.valueOf(13), it.next());
        try {
            it.next();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of copyToArray method, of class RingBufLong.
     */
    @Test
    public void testCopyToArray() {
        System.out.println("copyToArray");

        RingBufLong rb;
        long[] dst;

        rb = new RingBufLong(3);
        rb.pushTail(11, 12, 13);
        rb.chopHead(2);
        rb.pushTail(14);

        dst = new long[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, rb.size());
        assertArrayEquals(new long[]{13, 14, 99}, dst);

        dst = new long[]{99, 99, 99};
        rb.copyToArray(0, dst, 1, rb.size());
        assertArrayEquals(new long[]{99, 13, 14}, dst);

        dst = new long[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, 1);
        assertArrayEquals(new long[]{13, 99, 99}, dst);

        dst = new long[]{99, 99, 99};
        rb.copyToArray(1, dst, 0, 1);
        assertArrayEquals(new long[]{14, 99, 99}, dst);

        rb = new RingBufLong(3);
        rb.pushTail(11, 12, 13);
        try {
            rb.copyToArray(0, null, 0, rb.size());
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufLong(3);
        rb.pushTail(1, 2, 3);
        try {
            rb.copyToArray(-1, dst, 0, 3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            rb.copyToArray(3, dst, 0, 3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            rb.copyToArray(0, dst, -1, 3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            rb.copyToArray(0, dst, 3, 3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            rb.copyToArray(0, dst, 0, -1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            rb.copyToArray(1, dst, 0, 3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            rb.copyToArray(0, dst, 0, 4);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of duplicate method, of class RingBufLong.
     */
    @Test
    public void testDuplicate() {
        System.out.println("duplicate");

        RingBufLong old;
        RingBufLong result;

        old = new RingBufLong(0);
        result = RingBufLong.duplicate(old, 10);
        assertEquals(0, result.size());
        assertEquals(10, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufLong(3);
        old.pushTail(11, 12, 13);
        result = RingBufLong.duplicate(old, 2);
        assertEquals(2, result.size());
        assertEquals(2, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufLong(3);
        old.pushTail(11, 12);
        result = RingBufLong.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufLong(3);
        old.pushTail(11, 12, 13);
        old.chopHead(2);
        old.pushTail(14);
        result = RingBufLong.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("13,14", result.toString());

        old = new RingBufLong(3);
        old.pushTail(11, 12, 13);
        try {
            RingBufLong.duplicate(null, 2);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        old = new RingBufLong(3);
        old.pushTail(11, 12, 13);
        result = RingBufLong.duplicate(old, 0);
        assertEquals(0, result.size());
        assertEquals(0, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufLong(3);
        old.pushTail(11, 12, 13);
        try {
            RingBufLong.duplicate(old, -1);
            fail();
        } catch(NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

}
