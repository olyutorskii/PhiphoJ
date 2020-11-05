/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class RingBufIntTest {

    public RingBufIntTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of constructor, of class RingBufInt.
     */
    @Test
    public void testConstructor() {
        System.out.println("Constructor");

        RingBufInt rb;

        rb = new RingBufInt(1);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufInt(999999);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufInt(0);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb = new RingBufInt(-1);
            fail();
            rb.getClass();
        } catch (NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of capacity method, of class RingBufInt.
     */
    @Test
    public void testCapacity() {
        System.out.println("capacity");

        RingBufInt rb;

        rb = new RingBufInt(0);
        assertEquals(0, rb.capacity());

        rb = new RingBufInt(3);
        assertEquals(3, rb.capacity());

        rb.pushTail(11);
        assertEquals(3, rb.capacity());

        rb.pushTail(12);
        assertEquals(3, rb.capacity());

        rb.chopHead();
        assertEquals(3, rb.capacity());

        rb.clear();
        assertEquals(3, rb.capacity());

        rb = new RingBufInt(999999);
        assertEquals(999999, rb.capacity());

        return;
    }

    /**
     * Test of size method, of class RingBufInt.
     */
    @Test
    public void testSize() {
        System.out.println("size");

        RingBufInt rb;

        rb = new RingBufInt(3);
        assertEquals(0, rb.size());

        rb.pushTail(11);
        assertEquals(1, rb.size());

        rb.pushTail(12);
        assertEquals(2, rb.size());

        rb.chopHead();
        assertEquals(1, rb.size());

        rb.pushTail(13);
        assertEquals(2, rb.size());

        rb.pushTail(14);
        assertEquals(3, rb.size());

        rb.clear();
        assertEquals(0, rb.size());

        rb = new RingBufInt(0);
        assertEquals(0, rb.size());

        return;
    }

    /**
     * Test of hasRemaining method, of class RingBufInt.
     */
    @Test
    public void testHasRemaining() {
        System.out.println("hasRemaining");

        RingBufInt rb;

        rb = new RingBufInt(3);
        assertTrue(rb.hasRemaining());

        rb.pushTail(11);
        assertTrue(rb.hasRemaining());

        rb.pushTail(12);
        assertTrue(rb.hasRemaining());

        rb.pushTail(13);
        assertFalse(rb.hasRemaining());

        rb.chopHead();
        assertTrue(rb.hasRemaining());

        rb.pushTail(14);
        assertFalse(rb.hasRemaining());

        rb.clear();
        assertTrue(rb.hasRemaining());

        rb = new RingBufInt(0);
        assertFalse(rb.hasRemaining());

        return;
    }

    /**
     * Test of remaining method, of class RingBufInt.
     */
    @Test
    public void testRemaining() {
        System.out.println("remaining");

        RingBufInt rb;

        rb = new RingBufInt(3);
        assertEquals(3, rb.remaining());

        rb.pushTail(11);
        assertEquals(2, rb.remaining());

        rb.pushTail(12);
        assertEquals(1, rb.remaining());

        rb.pushTail(13);
        assertEquals(0, rb.remaining());

        rb.chopHead();
        assertEquals(1, rb.remaining());

        rb.clear();
        assertEquals(3, rb.remaining());

        rb = new RingBufInt(0);
        assertEquals(0, rb.remaining());

        return;
    }

    /**
     * Test of isEmpty method, of class RingBufInt.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");

        RingBufInt rb;

        rb = new RingBufInt(3);
        assertTrue(rb.isEmpty());

        rb.pushTail(11);
        assertFalse(rb.isEmpty());

        rb.pushTail(12);
        assertFalse(rb.isEmpty());

        rb.pushTail(13);
        assertFalse(rb.isEmpty());

        rb.chopHead();
        assertFalse(rb.isEmpty());

        rb.chopHead();
        assertFalse(rb.isEmpty());

        rb.chopHead();
        assertTrue(rb.isEmpty());

        rb = new RingBufInt(3);
        rb.pushTail(11);
        assertFalse(rb.isEmpty());

        rb.clear();
        assertTrue(rb.isEmpty());

        rb = new RingBufInt(0);
        assertTrue(rb.isEmpty());

        return;
    }

    /**
     * Test of clear method, of class RingBufInt.
     */
    @Test
    public void testClear() {
        System.out.println("clear");

        RingBufInt rb;

        rb = new RingBufInt(3);

        rb.pushTail(11, 12);
        rb.chopHead();
        assertEquals(1, rb.size());
        assertEquals(2, rb.remaining());

        rb.clear();
        assertEquals(0, rb.size());
        assertEquals(3, rb.remaining());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufInt(3);
        rb.clear();
        assertEquals(0, rb.size());
        assertEquals(3, rb.remaining());
        rb.clear();
        assertEquals(0, rb.size());
        assertEquals(3, rb.remaining());

        return;
    }

    /**
     * Test of chopHead method, of class RingBuf.
     */
    @Test
    public void testChopHead_int() {
        System.out.println("chopHead");

        RingBufInt rb;

        rb = new RingBufInt(3);

        rb.pushTail(11);
        rb.chopHead(0);
        assertEquals("11", rb.toString());

        try {
            rb.chopHead(-1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            rb.chopHead(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead(1);
        assertEquals("Empty Ring...", rb.toString());

        rb.pushTail(21);
        rb.pushTail(31);

        rb.chopHead(2);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufInt(6);
        rb.pushTail(11, 12, 13, 14, 15, 16);
        rb.chopHead(4);
        rb.pushTail(17, 18);
        rb.chopHead(3);
        assertEquals("18", rb.toString());

        return;
    }

    /**
     * Test of chopHead method, of class RingBuf.
     */
    @Test
    public void testChopHead_0args() {
        System.out.println("chopHead");

        RingBufInt rb;

        rb = new RingBufInt(3);
        rb.pushTail(11, 12, 13);

        rb.chopHead();
        assertEquals("12,13", rb.toString());

        rb.chopHead();
        assertEquals("13", rb.toString());

        rb.chopHead();
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb.chopHead();
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of toString method, of class RingBufInt.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        RingBufInt rb;

        rb = new RingBufInt(0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufInt(3);
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

        rb = new RingBufInt(8);
        rb.pushTail(11, 12, 13, 14, 15, 16, 17);
        assertEquals("11,12,13,14,15,16,17", rb.toString());
        rb.pushTail(18);
        assertEquals("11,12,13,14,15,16,17,...(x 8)", rb.toString());

        rb = new RingBufInt(5);
        rb.pushTail(Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE);
        assertEquals("-2147483648,-1,0,1,2147483647", rb.toString());

        return;
    }

    /**
     * Test of hashCode method, of class RingBufInt.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        RingBufInt rb1, rb2;

        rb1 = new RingBufInt(0);
        rb2 = new RingBufInt(3);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufInt(3);
        rb2 = new RingBufInt(5);
        rb1.pushTail(11, 12);
        rb2.pushTail(11, 12);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufInt(3);
        rb2 = new RingBufInt(3);

        rb1.pushTail(11, 12, 13);
        rb1.chopHead(2);
        rb1.pushTail(14);
        rb2.pushTail(13, 14);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        return;
    }

    /**
     * Test of equals method, of class RingBufInt.
     */
    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testEquals() {
        System.out.println("equals");

        RingBufInt rb1, rb2;

        rb1 = new RingBufInt(0);
        rb2 = new RingBufInt(0);

        assertTrue(rb1.equals(rb1));
        assertTrue(rb1.equals(rb2));

        RingBufInt rbNull = null;
        String txt = "XYZ";
        assertFalse(rb1.equals(rbNull));
        assertFalse(rb1.equals(txt));

        rb1 = new RingBufInt(3);
        rb2 = new RingBufInt(3);

        rb1.pushTail(11);
        assertFalse(rb1.equals(rb2));
        rb2.pushTail(11, 12);
        assertFalse(rb1.equals(rb2));
        rb1.pushTail(12);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufInt(3);
        rb2 = new RingBufInt(6);

        rb1.pushTail(1,2);
        rb2.pushTail(1,2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufInt(6);
        rb2 = new RingBufInt(6);

        rb1.pushTail(1,2,3,4,5,6);
        rb1.chopHead(4);
        rb1.pushTail(7, 8);

        rb2.pushTail(5,6,7,8);

        assertTrue(rb1.equals(rb2));
        rb2.pushTail(9);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufInt(3);
        rb2 = new RingBufInt(3);

        rb1.pushTail(11, 12, 13);
        rb2.pushTail(11, 12, 33);
        assertFalse(rb1.equals(rb2));

        return;
    }

    /**
     * Test of pushTail method, of class RingBufInt.
     */
    @Test
    public void testPushTail_3args() {
        System.out.println("pushTail");

        RingBufInt rb;

        rb = new RingBufInt(3);
        try {
            rb.pushTail(null, 0, 10);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufInt(3);
        rb.pushTail(new int[]{11, 12, 13}, 0, 3);
        assertEquals("11,12,13", rb.toString());

        rb = new RingBufInt(3);
        rb.pushTail(new int[]{11, 12, 13}, 0, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufInt(3);
        rb.pushTail(new int[]{11, 12, 13}, 1, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufInt(3);
        try {
            rb.pushTail(new int[]{11, 12, 13}, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufInt(3);
        try {
            rb.pushTail(new int[]{11, 12, 13}, -1, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufInt(3);
        try {
            rb.pushTail(new int[]{11, 12, 13}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufInt(3);
        try {
            rb.pushTail(new int[]{11, 12, 13, 14}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufInt(6);
        rb.pushTail(11, 12, 13, 14);
        rb.chopHead(2);
        rb.pushTail(new int[]{14, 15, 16, 17, 18}, 1, 3);
        assertEquals("13,14,15,16,17", rb.toString());

        return;
    }

    /**
     * Test of pushTail method, of class RingBufInt.
     */
    @Test
    public void testPushTail_intArr() {
        System.out.println("pushTail");

        RingBufInt rb;

        rb = new RingBufInt(3);

        try {
            rb.pushTail(null);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb.pushTail(new int[]{11});
        assertEquals("11",rb.toString());

        rb.pushTail(12, 13);
        assertEquals("11,12,13",rb.toString());

        rb = new RingBufInt(3);

        try {
            rb.pushTail(11, 12, 13, 14);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of pushTail method, of class RingBufInt.
     */
    @Test
    public void testPushTail_int() {
        System.out.println("pushTail");

        RingBufInt rb;

        rb = new RingBufInt(3);

        rb.pushTail(11);
        rb.pushTail(21);
        rb.pushTail(31);
        assertEquals("11,21,31",rb.toString());

        try {
            rb.pushTail(11);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals("21,31",rb.toString());

        rb.pushTail(41);
        assertEquals("21,31,41",rb.toString());

        return;
    }

    /**
     * Test of peekElem method, of class RingBuf.
     */
    @Test
    public void testPeekElem() {
        System.out.println("peekElem");

        RingBufInt rb;

        rb = new RingBufInt(3);

        try {
            rb.peekElem(0);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail(11);
        assertEquals(11, rb.peekElem(0));
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

        rb.pushTail(12);
        assertEquals(11, rb.peekElem(0));
        assertEquals(12, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals(12, rb.peekElem(0));
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail(13);
        assertEquals(12, rb.peekElem(0));
        assertEquals(13, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail(14);
        assertEquals(12, rb.peekElem(0));
        assertEquals(13, rb.peekElem(1));
        assertEquals(14, rb.peekElem(2));
        try {
            rb.peekElem(3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of iterator method, of class RingBufInt.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");

        RingBufInt rb;
        PrimitiveIterator.OfInt it;

        rb = new RingBufInt(3);
        it = rb.iterator();
        assertFalse(it.hasNext());
        try {
            it.nextInt();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb.pushTail(11);
        rb.pushTail(12);
        rb.pushTail(13);
        rb.chopHead(2);
        rb.pushTail(14);

        it = rb.iterator();
        assertTrue(it.hasNext());
        assertEquals(13, it.nextInt());
        assertTrue(it.hasNext());
        assertEquals(14, it.nextInt());
        assertFalse(it.hasNext());
        try {
            it.nextInt();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb = new RingBufInt(3);
        rb.pushTail(11, 12, 13);
        it = rb.iterator();
        assertEquals(Integer.valueOf(11), it.next());
        assertEquals(Integer.valueOf(12), it.next());
        assertEquals(Integer.valueOf(13), it.next());
        try {
            it.next();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of copyToArray method, of class RingBufInt.
     */
    @Test
    public void testCopyToArray() {
        System.out.println("copyToArray");

        RingBufInt rb;
        int[] dst;

        rb = new RingBufInt(3);
        rb.pushTail(11, 12, 13);
        rb.chopHead(2);
        rb.pushTail(14);

        dst = new int[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, rb.size());
        assertArrayEquals(new int[]{13, 14, 99}, dst);

        dst = new int[]{99, 99, 99};
        rb.copyToArray(0, dst, 1, rb.size());
        assertArrayEquals(new int[]{99, 13, 14}, dst);

        dst = new int[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, 1);
        assertArrayEquals(new int[]{13, 99, 99}, dst);

        dst = new int[]{99, 99, 99};
        rb.copyToArray(1, dst, 0, 1);
        assertArrayEquals(new int[]{14, 99, 99}, dst);

        rb = new RingBufInt(3);
        rb.pushTail(11, 12, 13);
        try {
            rb.copyToArray(0, null, 0, rb.size());
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufInt(3);
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
     * Test of duplicate method, of class RingBufInt.
     */
    @Test
    public void testDuplicate() {
        System.out.println("duplicate");

        RingBufInt old;
        RingBufInt result;

        old = new RingBufInt(0);
        result = RingBufInt.duplicate(old, 10);
        assertEquals(0, result.size());
        assertEquals(10, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufInt(3);
        old.pushTail(11, 12, 13);
        result = RingBufInt.duplicate(old, 2);
        assertEquals(2, result.size());
        assertEquals(2, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufInt(3);
        old.pushTail(11, 12);
        result = RingBufInt.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufInt(3);
        old.pushTail(11, 12, 13);
        old.chopHead(2);
        old.pushTail(14);
        result = RingBufInt.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("13,14", result.toString());

        old = new RingBufInt(3);
        old.pushTail(11, 12, 13);
        try {
            RingBufInt.duplicate(null, 2);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        old = new RingBufInt(3);
        old.pushTail(11, 12, 13);
        result = RingBufInt.duplicate(old, 0);
        assertEquals(0, result.size());
        assertEquals(0, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufInt(3);
        old.pushTail(11, 12, 13);
        try {
            RingBufInt.duplicate(old, -1);
            fail();
        } catch(NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

}
