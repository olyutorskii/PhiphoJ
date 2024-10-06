/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class RingBufByteTest {

    public RingBufByteTest() {
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
     * Test of constructor, of class RingBufByte.
     */
    @Test
    public void testConstructor() {
        System.out.println("Constructor");

        RingBufByte rb;

        rb = new RingBufByte(1);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufByte(999999);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufByte(0);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb = new RingBufByte(-1);
            fail();
            rb.getClass();
        } catch (NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of toString method, of class RingBufByte.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        RingBufByte rb;

        rb = new RingBufByte(0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufByte(3);
        assertEquals("Empty Ring...", rb.toString());
        rb.pushTail((byte)1, (byte)2);
        assertEquals("1,2", rb.toString());
        rb.pushTail((byte)3);
        assertEquals("1,2,3", rb.toString());
        rb.chopHead(2);
        assertEquals("3", rb.toString());
        rb.pushTail((byte)4);
        assertEquals("3,4", rb.toString());
        rb.pushTail((byte)5);
        assertEquals("3,4,5", rb.toString());
        rb.chopHead();
        assertEquals("4,5", rb.toString());
        rb.chopHead(2);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufByte(8);
        rb.pushTail((byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17);
        assertEquals("11,12,13,14,15,16,17", rb.toString());
        rb.pushTail((byte)18);
        assertEquals("11,12,13,14,15,16,17,...(x 8)", rb.toString());

        rb = new RingBufByte(5);
        rb.pushTail(Byte.MIN_VALUE, (byte)-1, (byte)0, (byte)1, Byte.MAX_VALUE);
        assertEquals("-128,-1,0,1,127", rb.toString());

        return;
    }

    /**
     * Test of hashCode method, of class RingBufByte.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        RingBufByte rb1, rb2;

        rb1 = new RingBufByte(0);
        rb2 = new RingBufByte(3);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufByte(3);
        rb2 = new RingBufByte(5);
        rb1.pushTail((byte)11, (byte)12);
        rb2.pushTail((byte)11, (byte)12);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufByte(3);
        rb2 = new RingBufByte(3);

        rb1.pushTail((byte)11, (byte)12, (byte)13);
        rb1.chopHead(2);
        rb1.pushTail((byte)14);
        rb2.pushTail((byte)13, (byte)14);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        return;
    }

    /**
     * Test of equals method, of class RingBufByte.
     */
    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testEquals() {
        System.out.println("equals");

        RingBufByte rb1, rb2;

        rb1 = new RingBufByte(0);
        rb2 = new RingBufByte(0);

        assertTrue(rb1.equals(rb1));
        assertTrue(rb1.equals(rb2));

        RingBufByte rbNull = null;
        String txt = "XYZ";
        assertFalse(rb1.equals(rbNull));
        assertFalse(rb1.equals(txt));

        rb1 = new RingBufByte(3);
        rb2 = new RingBufByte(3);

        rb1.pushTail((byte)11);
        assertFalse(rb1.equals(rb2));
        rb2.pushTail((byte)11, (byte)12);
        assertFalse(rb1.equals(rb2));
        rb1.pushTail((byte)12);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufByte(3);
        rb2 = new RingBufByte(6);

        rb1.pushTail((byte)1,(byte)2);
        rb2.pushTail((byte)1,(byte)2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufByte(6);
        rb2 = new RingBufByte(6);

        rb1.pushTail((byte)1,(byte)2,(byte)3,(byte)4,(byte)5,(byte)6);
        rb1.chopHead(4);
        rb1.pushTail((byte)7, (byte)8);

        rb2.pushTail((byte)5,(byte)6,(byte)7,(byte)8);

        assertTrue(rb1.equals(rb2));
        rb2.pushTail((byte)9);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufByte(3);
        rb2 = new RingBufByte(3);

        rb1.pushTail((byte)11, (byte)12, (byte)13);
        rb2.pushTail((byte)11, (byte)12, (byte)33);
        assertFalse(rb1.equals(rb2));

        return;
    }

    /**
     * Test of pushTail method, of class RingBufByte.
     */
    @Test
    public void testPushTail_3args() {
        System.out.println("pushTail");

        RingBufByte rb;

        rb = new RingBufByte(3);
        try {
            rb.pushTail(null, 0, 10);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufByte(3);
        rb.pushTail(new byte[]{11, 12, 13}, 0, 3);
        assertEquals("11,12,13", rb.toString());

        rb = new RingBufByte(3);
        rb.pushTail(new byte[]{11, 12, 13}, 0, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufByte(3);
        rb.pushTail(new byte[]{11, 12, 13}, 1, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufByte(3);
        try {
            rb.pushTail(new byte[]{11, 12, 13}, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufByte(3);
        try {
            rb.pushTail(new byte[]{11, 12, 13}, -1, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufByte(3);
        try {
            rb.pushTail(new byte[]{11, 12, 13}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufByte(3);
        try {
            rb.pushTail(new byte[]{11, 12, 13, 14}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufByte(6);
        rb.pushTail((byte)11, (byte)12, (byte)13, (byte)14);
        rb.chopHead(2);
        rb.pushTail(new byte[]{14, 15, 16, 17, 18}, 1, 3);
        assertEquals("13,14,15,16,17", rb.toString());

        return;
    }

    /**
     * Test of pushTail method, of class RingBufByte.
     */
    @Test
    public void testPushTail_byteArr() {
        System.out.println("pushTail");

        RingBufByte rb;

        rb = new RingBufByte(3);

        try {
            rb.pushTail(null);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb.pushTail(new byte[]{11});
        assertEquals("11",rb.toString());

        rb.pushTail((byte)12, (byte)13);
        assertEquals("11,12,13",rb.toString());

        rb = new RingBufByte(3);

        try {
            rb.pushTail((byte)11, (byte)12, (byte)13, (byte)14);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of pushTail method, of class RingBufByte.
     */
    @Test
    public void testPushTail_byte() {
        System.out.println("pushTail");

        RingBufByte rb;

        rb = new RingBufByte(3);

        rb.pushTail((byte)11);
        rb.pushTail((byte)21);
        rb.pushTail((byte)31);
        assertEquals("11,21,31",rb.toString());

        try {
            rb.pushTail((byte)11);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals("21,31",rb.toString());

        rb.pushTail((byte)41);
        assertEquals("21,31,41",rb.toString());

        return;
    }

    /**
     * Test of peekElem method, of class RingBuf.
     */
    @Test
    public void testPeekElem() {
        System.out.println("peekElem");

        RingBufByte rb;

        rb = new RingBufByte(3);

        try {
            rb.peekElem(0);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((byte)11);
        assertEquals(11L, rb.peekElem(0));
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

        rb.pushTail((byte)12);
        assertEquals((byte)11, rb.peekElem(0));
        assertEquals((byte)12, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals((byte)12, rb.peekElem(0));
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((byte)13);
        assertEquals((byte)12, rb.peekElem(0));
        assertEquals((byte)13, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((byte)14);
        assertEquals((byte)12, rb.peekElem(0));
        assertEquals((byte)13, rb.peekElem(1));
        assertEquals((byte)14, rb.peekElem(2));
        try {
            rb.peekElem(3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of iterator method, of class RingBufByte.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");

        RingBufByte rb;
        RingBufByte.IteratorOfByte it;

        rb = new RingBufByte(3);
        it = rb.iterator();
        assertFalse(it.hasNext());
        try {
            it.nextByte();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb.pushTail((byte)11);
        rb.pushTail((byte)12);
        rb.pushTail((byte)13);
        rb.chopHead(2);
        rb.pushTail((byte)14);

        it = rb.iterator();
        assertTrue(it.hasNext());
        assertEquals((byte)13, it.nextByte());
        assertTrue(it.hasNext());
        assertEquals((byte)14, it.nextByte());
        assertFalse(it.hasNext());
        try {
            it.nextByte();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb = new RingBufByte(3);
        rb.pushTail((byte)11, (byte)12, (byte)13);
        it = rb.iterator();
        assertEquals(Byte.valueOf((byte)11), it.next());
        assertEquals(Byte.valueOf((byte)12), it.next());
        assertEquals(Byte.valueOf((byte)13), it.next());
        try {
            it.next();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of copyToArray method, of class RingBufByte.
     */
    @Test
    public void testCopyToArray() {
        System.out.println("copyToArray");

        RingBufByte rb;
        byte[] dst;

        rb = new RingBufByte(3);
        rb.pushTail((byte)11, (byte)12, (byte)13);
        rb.chopHead(2);
        rb.pushTail((byte)14);

        dst = new byte[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, rb.size());
        assertArrayEquals(new byte[]{13, 14, 99}, dst);

        dst = new byte[]{99, 99, 99};
        rb.copyToArray(0, dst, 1, rb.size());
        assertArrayEquals(new byte[]{99, 13, 14}, dst);

        dst = new byte[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, 1);
        assertArrayEquals(new byte[]{13, 99, 99}, dst);

        dst = new byte[]{99, 99, 99};
        rb.copyToArray(1, dst, 0, 1);
        assertArrayEquals(new byte[]{14, 99, 99}, dst);

        rb = new RingBufByte(3);
        rb.pushTail((byte)11, (byte)12, (byte)13);
        try {
            rb.copyToArray(0, null, 0, rb.size());
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufByte(3);
        rb.pushTail((byte)1, (byte)2, (byte)3);
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
     * Test of duplicate method, of class RingBufByte.
     */
    @Test
    public void testDuplicate() {
        System.out.println("duplicate");

        RingBufByte old;
        RingBufByte result;

        old = new RingBufByte(0);
        result = RingBufByte.duplicate(old, 10);
        assertEquals(0, result.size());
        assertEquals(10, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufByte(3);
        old.pushTail((byte)11, (byte)12, (byte)13);
        result = RingBufByte.duplicate(old, 2);
        assertEquals(2, result.size());
        assertEquals(2, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufByte(3);
        old.pushTail((byte)11, (byte)12);
        result = RingBufByte.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufByte(3);
        old.pushTail((byte)11, (byte)12, (byte)13);
        old.chopHead(2);
        old.pushTail((byte)14);
        result = RingBufByte.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("13,14", result.toString());

        old = new RingBufByte(3);
        old.pushTail((byte)11, (byte)12, (byte)13);
        try {
            RingBufByte.duplicate(null, 2);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        old = new RingBufByte(3);
        old.pushTail((byte)11, (byte)12, (byte)13);
        result = RingBufByte.duplicate(old, 0);
        assertEquals(0, result.size());
        assertEquals(0, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufByte(3);
        old.pushTail((byte)11, (byte)12, (byte)13);
        try {
            RingBufByte.duplicate(old, -1);
            fail();
        } catch(NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

}
