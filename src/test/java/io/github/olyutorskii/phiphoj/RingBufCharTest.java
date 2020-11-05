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
public class RingBufCharTest {

    public RingBufCharTest() {
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
     * Test of constructor, of class RingBufChar.
     */
    @Test
    public void testConstructor() {
        System.out.println("Constructor");

        RingBufChar rb;

        rb = new RingBufChar(1);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufChar(999999);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufChar(0);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb = new RingBufChar(-1);
            fail();
            rb.getClass();
        } catch (NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of toString method, of class RingBufChar.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        RingBufChar rb;

        rb = new RingBufChar(0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufChar(3);
        assertEquals("Empty Ring...", rb.toString());
        rb.pushTail((char)1, (char)2);
        assertEquals("1,2", rb.toString());
        rb.pushTail((char)3);
        assertEquals("1,2,3", rb.toString());
        rb.chopHead(2);
        assertEquals("3", rb.toString());
        rb.pushTail((char)4);
        assertEquals("3,4", rb.toString());
        rb.pushTail((char)5);
        assertEquals("3,4,5", rb.toString());
        rb.chopHead();
        assertEquals("4,5", rb.toString());
        rb.chopHead(2);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufChar(8);
        rb.pushTail((char)11, (char)12, (char)13, (char)14, (char)15, (char)16, (char)17);
        assertEquals("11,12,13,14,15,16,17", rb.toString());
        rb.pushTail((char)18);
        assertEquals("11,12,13,14,15,16,17,...(x 8)", rb.toString());

        rb = new RingBufChar(5);
        rb.pushTail(Character.MIN_VALUE, (char)-1, (char)0, (char)1, Character.MAX_VALUE);
        assertEquals("0,65535,0,1,65535", rb.toString());

        return;
    }

    /**
     * Test of hashCode method, of class RingBufChar.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        RingBufChar rb1, rb2;

        rb1 = new RingBufChar(0);
        rb2 = new RingBufChar(3);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufChar(3);
        rb2 = new RingBufChar(5);
        rb1.pushTail((char)11, (char)12);
        rb2.pushTail((char)11, (char)12);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufChar(3);
        rb2 = new RingBufChar(3);

        rb1.pushTail((char)11, (char)12, (char)13);
        rb1.chopHead(2);
        rb1.pushTail((char)14);
        rb2.pushTail((char)13, (char)14);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        return;
    }

    /**
     * Test of equals method, of class RingBufChar.
     */
    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testEquals() {
        System.out.println("equals");

        RingBufChar rb1, rb2;

        rb1 = new RingBufChar(0);
        rb2 = new RingBufChar(0);

        assertTrue(rb1.equals(rb1));
        assertTrue(rb1.equals(rb2));

        RingBufChar rbNull = null;
        String txt = "XYZ";
        assertFalse(rb1.equals(rbNull));
        assertFalse(rb1.equals(txt));

        rb1 = new RingBufChar(3);
        rb2 = new RingBufChar(3);

        rb1.pushTail((char)11);
        assertFalse(rb1.equals(rb2));
        rb2.pushTail((char)11, (char)12);
        assertFalse(rb1.equals(rb2));
        rb1.pushTail((char)12);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufChar(3);
        rb2 = new RingBufChar(6);

        rb1.pushTail((char)1,(char)2);
        rb2.pushTail((char)1,(char)2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufChar(6);
        rb2 = new RingBufChar(6);

        rb1.pushTail((char)1,(char)2,(char)3,(char)4,(char)5,(char)6);
        rb1.chopHead(4);
        rb1.pushTail((char)7, (char)8);

        rb2.pushTail((char)5,(char)6,(char)7,(char)8);

        assertTrue(rb1.equals(rb2));
        rb2.pushTail((char)9);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufChar(3);
        rb2 = new RingBufChar(3);

        rb1.pushTail((char)11, (char)12, (char)13);
        rb2.pushTail((char)11, (char)12, (char)33);
        assertFalse(rb1.equals(rb2));

        return;
    }

    /**
     * Test of pushTail method, of class RingBufChar.
     */
    @Test
    public void testPushTail_3args() {
        System.out.println("pushTail");

        RingBufChar rb;

        rb = new RingBufChar(3);
        try {
            rb.pushTail(null, 0, 10);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufChar(3);
        rb.pushTail(new char[]{11, 12, 13}, 0, 3);
        assertEquals("11,12,13", rb.toString());

        rb = new RingBufChar(3);
        rb.pushTail(new char[]{11, 12, 13}, 0, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufChar(3);
        rb.pushTail(new char[]{11, 12, 13}, 1, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufChar(3);
        try {
            rb.pushTail(new char[]{11, 12, 13}, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufChar(3);
        try {
            rb.pushTail(new char[]{11, 12, 13}, -1, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufChar(3);
        try {
            rb.pushTail(new char[]{11, 12, 13}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufChar(3);
        try {
            rb.pushTail(new char[]{11, 12, 13, 14}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufChar(6);
        rb.pushTail((char)11, (char)12, (char)13, (char)14);
        rb.chopHead(2);
        rb.pushTail(new char[]{14, 15, 16, 17, 18}, 1, 3);
        assertEquals("13,14,15,16,17", rb.toString());

        return;
    }

    /**
     * Test of pushTail method, of class RingBufChar.
     */
    @Test
    public void testPushTail_charArr() {
        System.out.println("pushTail");

        RingBufChar rb;

        rb = new RingBufChar(3);

        try {
            rb.pushTail(null);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb.pushTail(new char[]{11});
        assertEquals("11",rb.toString());

        rb.pushTail((char)12, (char)13);
        assertEquals("11,12,13",rb.toString());

        rb = new RingBufChar(3);

        try {
            rb.pushTail((char)11, (char)12, (char)13, (char)14);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of pushTail method, of class RingBufChar.
     */
    @Test
    public void testPushTail_char() {
        System.out.println("pushTail");

        RingBufChar rb;

        rb = new RingBufChar(3);

        rb.pushTail((char)11);
        rb.pushTail((char)21);
        rb.pushTail((char)31);
        assertEquals("11,21,31",rb.toString());

        try {
            rb.pushTail((char)11);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals("21,31",rb.toString());

        rb.pushTail((char)41);
        assertEquals("21,31,41",rb.toString());

        return;
    }

    /**
     * Test of peekElem method, of class RingBuf.
     */
    @Test
    public void testPeekElem() {
        System.out.println("peekElem");

        RingBufChar rb;

        rb = new RingBufChar(3);

        try {
            rb.peekElem(0);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((char)11);
        assertEquals((char)11, rb.peekElem(0));
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

        rb.pushTail((char)12);
        assertEquals((char)11, rb.peekElem(0));
        assertEquals((char)12, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals((char)12, rb.peekElem(0));
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((char)13);
        assertEquals((char)12, rb.peekElem(0));
        assertEquals((char)13, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((char)14);
        assertEquals((char)12, rb.peekElem(0));
        assertEquals((char)13, rb.peekElem(1));
        assertEquals((char)14, rb.peekElem(2));
        try {
            rb.peekElem(3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of iterator method, of class RingBufChar.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");

        RingBufChar rb;
        RingBufChar.IteratorOfChar it;

        rb = new RingBufChar(3);
        it = rb.iterator();
        assertFalse(it.hasNext());
        try {
            it.nextChar();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb.pushTail((char)11);
        rb.pushTail((char)12);
        rb.pushTail((char)13);
        rb.chopHead(2);
        rb.pushTail((char)14);

        it = rb.iterator();
        assertTrue(it.hasNext());
        assertEquals((char)13, it.nextChar());
        assertTrue(it.hasNext());
        assertEquals((char)14, it.nextChar());
        assertFalse(it.hasNext());
        try {
            it.nextChar();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb = new RingBufChar(3);
        rb.pushTail((char)11, (char)12, (char)13);
        it = rb.iterator();
        assertEquals(Character.valueOf((char)11), it.next());
        assertEquals(Character.valueOf((char)12), it.next());
        assertEquals(Character.valueOf((char)13), it.next());
        try {
            it.next();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of copyToArray method, of class RingBufChar.
     */
    @Test
    public void testCopyToArray() {
        System.out.println("copyToArray");

        RingBufChar rb;
        char[] dst;

        rb = new RingBufChar(3);
        rb.pushTail((char)11, (char)12, (char)13);
        rb.chopHead(2);
        rb.pushTail((char)14);

        dst = new char[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, rb.size());
        assertArrayEquals(new char[]{13, 14, 99}, dst);

        dst = new char[]{99, 99, 99};
        rb.copyToArray(0, dst, 1, rb.size());
        assertArrayEquals(new char[]{99, 13, 14}, dst);

        dst = new char[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, 1);
        assertArrayEquals(new char[]{13, 99, 99}, dst);

        dst = new char[]{99, 99, 99};
        rb.copyToArray(1, dst, 0, 1);
        assertArrayEquals(new char[]{14, 99, 99}, dst);

        rb = new RingBufChar(3);
        rb.pushTail((char)11, (char)12, (char)13);
        try {
            rb.copyToArray(0, null, 0, rb.size());
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufChar(3);
        rb.pushTail((char)1, (char)2, (char)3);
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
     * Test of duplicate method, of class RingBufChar.
     */
    @Test
    public void testDuplicate() {
        System.out.println("duplicate");

        RingBufChar old;
        RingBufChar result;

        old = new RingBufChar(0);
        result = RingBufChar.duplicate(old, 10);
        assertEquals(0, result.size());
        assertEquals(10, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufChar(3);
        old.pushTail((char)11, (char)12, (char)13);
        result = RingBufChar.duplicate(old, 2);
        assertEquals(2, result.size());
        assertEquals(2, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufChar(3);
        old.pushTail((char)11, (char)12);
        result = RingBufChar.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("11,12", result.toString());

        old = new RingBufChar(3);
        old.pushTail((char)11, (char)12, (char)13);
        old.chopHead(2);
        old.pushTail((char)14);
        result = RingBufChar.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("13,14", result.toString());

        old = new RingBufChar(3);
        old.pushTail((char)11, (char)12, (char)13);
        try {
            RingBufChar.duplicate(null, 2);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        old = new RingBufChar(3);
        old.pushTail((char)11, (char)12, (char)13);
        result = RingBufChar.duplicate(old, 0);
        assertEquals(0, result.size());
        assertEquals(0, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufChar(3);
        old.pushTail((char)11, (char)12, (char)13);
        try {
            RingBufChar.duplicate(old, -1);
            fail();
        } catch(NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

}
