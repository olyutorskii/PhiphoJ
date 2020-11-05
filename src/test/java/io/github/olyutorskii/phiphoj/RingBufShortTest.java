/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class RingBufShortTest {

    public RingBufShortTest() {
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
     * Test of constructor, of class RingBufShort.
     */
    @Test
    public void testConstructor() {
        System.out.println("Constructor");

        RingBufShort rb;

        rb = new RingBufShort(1);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufShort(999999);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufShort(0);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb = new RingBufShort(-1);
            fail();
            rb.getClass();
        } catch (NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of toString method, of class RingBufShort.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        RingBufShort rb;

        rb = new RingBufShort(0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufShort(3);
        assertEquals("Empty Ring...", rb.toString());
        rb.pushTail((short)1, (short)2);
        assertEquals("1,2", rb.toString());
        rb.pushTail((short)3);
        assertEquals("1,2,3", rb.toString());
        rb.chopHead(2);
        assertEquals("3", rb.toString());
        rb.pushTail((short)4);
        assertEquals("3,4", rb.toString());
        rb.pushTail((short)5);
        assertEquals("3,4,5", rb.toString());
        rb.chopHead();
        assertEquals("4,5", rb.toString());
        rb.chopHead(2);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufShort(8);
        rb.pushTail((short)11, (short)12, (short)13, (short)14, (short)15, (short)16, (short)17);
        assertEquals("11,12,13,14,15,16,17", rb.toString());
        rb.pushTail((short)18);
        assertEquals("11,12,13,14,15,16,17,...(x 8)", rb.toString());

        rb = new RingBufShort(5);
        rb.pushTail(Short.MIN_VALUE, (short)-1, (short)0, (short)1, Short.MAX_VALUE);
        assertEquals("-32768,-1,0,1,32767", rb.toString());

        return;
    }

    /**
     * Test of hashCode method, of class RingBufShort.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        RingBufShort rb1, rb2;

        rb1 = new RingBufShort(0);
        rb2 = new RingBufShort(3);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufShort(3);
        rb2 = new RingBufShort(5);
        rb1.pushTail((short)11, (short)12);
        rb2.pushTail((short)11, (short)12);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufShort(3);
        rb2 = new RingBufShort(3);

        rb1.pushTail((short)11, (short)12, (short)13);
        rb1.chopHead(2);
        rb1.pushTail((short)14);
        rb2.pushTail((short)13, (short)14);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        return;
    }

    /**
     * Test of equals method, of class RingBufShort.
     */
    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testEquals() {
        System.out.println("equals");

        RingBufShort rb1, rb2;

        rb1 = new RingBufShort(0);
        rb2 = new RingBufShort(0);

        assertTrue(rb1.equals(rb1));
        assertTrue(rb1.equals(rb2));

        RingBufShort rbNull = null;
        String txt = "XYZ";
        assertFalse(rb1.equals(rbNull));
        assertFalse(rb1.equals(txt));

        rb1 = new RingBufShort(3);
        rb2 = new RingBufShort(3);

        rb1.pushTail((short)11);
        assertFalse(rb1.equals(rb2));
        rb2.pushTail((short)11, (short)12);
        assertFalse(rb1.equals(rb2));
        rb1.pushTail((short)12);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufShort(3);
        rb2 = new RingBufShort(6);

        rb1.pushTail((short)1,(short)2);
        rb2.pushTail((short)1,(short)2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufShort(6);
        rb2 = new RingBufShort(6);

        rb1.pushTail((short)1,(short)2,(short)3,(short)4,(short)5,(short)6);
        rb1.chopHead(4);
        rb1.pushTail((short)7, (short)8);

        rb2.pushTail((short)5,(short)6,(short)7,(short)8);

        assertTrue(rb1.equals(rb2));
        rb2.pushTail((short)9);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufShort(3);
        rb2 = new RingBufShort(3);

        rb1.pushTail((short)11, (short)12, (short)13);
        rb2.pushTail((short)11, (short)12, (short)33);
        assertFalse(rb1.equals(rb2));

        return;
    }

    /**
     * Test of pushTail method, of class RingBufShort.
     */
    @Test
    public void testPushTail_3args() {
        System.out.println("pushTail");

        RingBufShort rb;

        rb = new RingBufShort(3);
        try {
            rb.pushTail(null, 0, 10);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufShort(3);
        rb.pushTail(new short[]{11, 12, 13}, 0, 3);
        assertEquals("11,12,13", rb.toString());

        rb = new RingBufShort(3);
        rb.pushTail(new short[]{11, 12, 13}, 0, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufShort(3);
        rb.pushTail(new short[]{11, 12, 13}, 1, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufShort(3);
        try {
            rb.pushTail(new short[]{11, 12, 13}, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufShort(3);
        try {
            rb.pushTail(new short[]{11, 12, 13}, -1, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufShort(3);
        try {
            rb.pushTail(new short[]{11, 12, 13}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufShort(3);
        try {
            rb.pushTail(new short[]{11, 12, 13, 14}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufShort(6);
        rb.pushTail((short)11, (short)12, (short)13, (short)14);
        rb.chopHead(2);
        rb.pushTail(new short[]{14, 15, 16, 17, 18}, 1, 3);
        assertEquals("13,14,15,16,17", rb.toString());

        return;
    }

    /**
     * Test of pushTail method, of class RingBufShort.
     */
    @Test
    public void testPushTail_shortArr() {
        System.out.println("pushTail");

        RingBufShort rb;

        rb = new RingBufShort(3);

        try {
            rb.pushTail(null);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb.pushTail(new short[]{11});
        assertEquals("11",rb.toString());

        rb.pushTail((short)12, (short)13);
        assertEquals("11,12,13",rb.toString());

        rb = new RingBufShort(3);

        try {
            rb.pushTail((short)11, (short)12, (short)13, (short)14);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of pushTail method, of class RingBufShort.
     */
    @Test
    public void testPushTail_short() {
        System.out.println("pushTail");

        RingBufShort rb;

        rb = new RingBufShort(3);

        rb.pushTail((short)11);
        rb.pushTail((short)21);
        rb.pushTail((short)31);
        assertEquals("11,21,31",rb.toString());

        try {
            rb.pushTail((short)11);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals("21,31",rb.toString());

        rb.pushTail((short)41);
        assertEquals("21,31,41",rb.toString());

        return;
    }

    /**
     * Test of peekElem method, of class RingBuf.
     */
    @Test
    public void testPeekElem() {
        System.out.println("peekElem");

        RingBufShort rb;

        rb = new RingBufShort(3);

        try {
            rb.peekElem(0);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((short)11);
        assertEquals((short)11, rb.peekElem(0));
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

        rb.pushTail((short)12);
        assertEquals((short)11, rb.peekElem(0));
        assertEquals((short)12, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals((short)12, rb.peekElem(0));
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((short)13);
        assertEquals((short)12, rb.peekElem(0));
        assertEquals((short)13, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((short)14);
        assertEquals((short)12, rb.peekElem(0));
        assertEquals((short)13, rb.peekElem(1));
        assertEquals((short)14, rb.peekElem(2));
        try {
            rb.peekElem(3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of iterator method, of class RingBufShort.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");

        RingBufShort rb;
        RingBufShort.IteratorOfShort it;

        rb = new RingBufShort(3);
        it = rb.iterator();
        assertFalse(it.hasNext());
        try {
            it.nextShort();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb.pushTail((short)11);
        rb.pushTail((short)12);
        rb.pushTail((short)13);
        rb.chopHead(2);
        rb.pushTail((short)14);

        it = rb.iterator();
        assertTrue(it.hasNext());
        assertEquals((short)13, it.nextShort());
        assertTrue(it.hasNext());
        assertEquals((short)14, it.nextShort());
        assertFalse(it.hasNext());
        try {
            it.nextShort();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb = new RingBufShort(3);
        rb.pushTail((short)11, (short)12, (short)13);
        it = rb.iterator();
        assertEquals(Short.valueOf((short)11), it.next());
        assertEquals(Short.valueOf((short)12), it.next());
        assertEquals(Short.valueOf((short)13), it.next());
        try {
            it.next();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of copyToArray method, of class RingBufShort.
     */
    @Test
    public void testCopyToArray() {
        System.out.println("copyToArray");

        RingBufShort rb;
        short[] dst;

        rb = new RingBufShort(3);
        rb.pushTail((short)11, (short)12, (short)13);
        rb.chopHead(2);
        rb.pushTail((short)14);

        dst = new short[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, rb.size());
        assertArrayEquals(new short[]{13, 14, 99}, dst);

        dst = new short[]{99, 99, 99};
        rb.copyToArray(0, dst, 1, rb.size());
        assertArrayEquals(new short[]{99, 13, 14}, dst);

        dst = new short[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, 1);
        assertArrayEquals(new short[]{13, 99, 99}, dst);

        dst = new short[]{99, 99, 99};
        rb.copyToArray(1, dst, 0, 1);
        assertArrayEquals(new short[]{14, 99, 99}, dst);

        rb = new RingBufShort(3);
        rb.pushTail((short)11, (short)12, (short)13);
        try {
            rb.copyToArray(0, null, 0, rb.size());
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufShort(3);
        rb.pushTail((short)1, (short)2, (short)3);
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
     * Test of duplicate method, of class RingBufShort.
     */
    @Test
    public void testDuplicate() {
        System.out.println("duplicate");

        RingBufShort old;
        RingBufShort result;

        old = new RingBufShort(0);
        result = RingBufShort.duplicate(old, 10);
        assertEquals(0, result.size());
        assertEquals(10, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufShort(3);
        old.pushTail((short)11, (short)12, (short)13);
        result = RingBufShort.duplicate(old, 2);
        assertEquals(2, result.size());
        assertEquals(2, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufShort(3);
        old.pushTail((short)11, (short)12);
        result = RingBufShort.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufShort(3);
        old.pushTail((short)11, (short)12, (short)13);
        old.chopHead(2);
        old.pushTail((short)14);
        result = RingBufShort.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("13,14", result.toString());

        old = new RingBufShort(3);
        old.pushTail((short)11, (short)12, (short)13);
        try {
            RingBufShort.duplicate(null, 2);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        old = new RingBufShort(3);
        old.pushTail((short)11, (short)12, (short)13);
        result = RingBufShort.duplicate(old, 0);
        assertEquals(0, result.size());
        assertEquals(0, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufShort(3);
        old.pushTail((short)11, (short)12, (short)13);
        try {
            RingBufShort.duplicate(old, -1);
            fail();
        } catch(NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

}
