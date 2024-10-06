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
public class RingBufBooleanTest {

    public RingBufBooleanTest() {
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
     * Test of constructor, of class RingBufBoolean.
     */
    @Test
    public void testConstructor() {
        System.out.println("Constructor");

        RingBufBoolean rb;

        rb = new RingBufBoolean(1);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufBoolean(999999);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufBoolean(0);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb = new RingBufBoolean(-1);
            fail();
            rb.getClass();
        } catch (NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of toString method, of class RingBufBoolean.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        RingBufBoolean rb;

        rb = new RingBufBoolean(0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufBoolean(3);
        assertEquals("Empty Ring...", rb.toString());
        rb.pushTail(true, false);
        assertEquals("true,false", rb.toString());
        rb.pushTail(true);
        assertEquals("true,false,true", rb.toString());
        rb.chopHead(2);
        assertEquals("true", rb.toString());
        rb.pushTail(false);
        assertEquals("true,false", rb.toString());
        rb.pushTail(true);
        assertEquals("true,false,true", rb.toString());
        rb.chopHead();
        assertEquals("false,true", rb.toString());
        rb.chopHead(2);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufBoolean(8);
        rb.pushTail(true, false, true, false, true, false, true);
        assertEquals("true,false,true,false,true,false,true", rb.toString());
        rb.pushTail(false);
        assertEquals("true,false,true,false,true,false,true,...(x 8)", rb.toString());

        return;
    }

    /**
     * Test of hashCode method, of class RingBufBoolean.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        RingBufBoolean rb1, rb2;

        rb1 = new RingBufBoolean(0);
        rb2 = new RingBufBoolean(3);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufBoolean(3);
        rb2 = new RingBufBoolean(5);
        rb1.pushTail(true, false);
        rb2.pushTail(true, false);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufBoolean(3);
        rb2 = new RingBufBoolean(3);

        rb1.pushTail(true, false, true);
        rb1.chopHead(2);
        rb1.pushTail(false);
        rb2.pushTail(true, false);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        return;
    }

    /**
     * Test of equals method, of class RingBufBoolean.
     */
    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testEquals() {
        System.out.println("equals");

        RingBufBoolean rb1, rb2;

        rb1 = new RingBufBoolean(0);
        rb2 = new RingBufBoolean(0);

        assertTrue(rb1.equals(rb1));
        assertTrue(rb1.equals(rb2));

        RingBufBoolean rbNull = null;
        String txt = "XYZ";
        assertFalse(rb1.equals(rbNull));
        assertFalse(rb1.equals(txt));

        rb1 = new RingBufBoolean(3);
        rb2 = new RingBufBoolean(3);

        rb1.pushTail(true);
        assertFalse(rb1.equals(rb2));
        rb2.pushTail(true, false);
        assertFalse(rb1.equals(rb2));
        rb1.pushTail(false);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufBoolean(3);
        rb2 = new RingBufBoolean(6);

        rb1.pushTail(true, false);
        rb2.pushTail(true, false);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufBoolean(6);
        rb2 = new RingBufBoolean(6);

        rb1.pushTail(true, false, true, false, true, false);
        rb1.chopHead(4);
        rb1.pushTail(true, false);

        rb2.pushTail(true, false, true, false);

        assertTrue(rb1.equals(rb2));
        rb2.pushTail(true);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufBoolean(3);
        rb2 = new RingBufBoolean(3);

        rb1.pushTail(true, false, true);
        rb2.pushTail(true, false, false);
        assertFalse(rb1.equals(rb2));

        return;
    }

    /**
     * Test of pushTail method, of class RingBufBoolean.
     */
    @Test
    public void testPushTail_3args() {
        System.out.println("pushTail");

        RingBufBoolean rb;

        rb = new RingBufBoolean(3);
        try {
            rb.pushTail(null, 0, 10);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufBoolean(3);
        rb.pushTail(new boolean[]{true, false, true}, 0, 3);
        assertEquals("true,false,true", rb.toString());

        rb = new RingBufBoolean(3);
        rb.pushTail(new boolean[]{true, false, true}, 0, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufBoolean(3);
        rb.pushTail(new boolean[]{true, false, true}, 1, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufBoolean(3);
        try {
            rb.pushTail(new boolean[]{true, false, true}, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufBoolean(3);
        try {
            rb.pushTail(new boolean[]{true, false, true}, -1, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufBoolean(3);
        try {
            rb.pushTail(new boolean[]{true, false, true}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufBoolean(3);
        try {
            rb.pushTail(new boolean[]{true, false, true, false}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufBoolean(6);
        rb.pushTail(true, false, true, false);
        rb.chopHead(2);
        rb.pushTail(new boolean[]{true, false, true, false, true}, 1, 3);
        assertEquals("true,false,false,true,false", rb.toString());

        return;
    }

    /**
     * Test of pushTail method, of class RingBufBoolean.
     */
    @Test
    public void testPushTail_byteArr() {
        System.out.println("pushTail");

        RingBufBoolean rb;

        rb = new RingBufBoolean(3);

        try {
            rb.pushTail(null);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb.pushTail(new boolean[]{true});
        assertEquals("true",rb.toString());

        rb.pushTail(false, true);
        assertEquals("true,false,true",rb.toString());

        rb = new RingBufBoolean(3);

        try {
            rb.pushTail(true, false, true, false);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of pushTail method, of class RingBufBoolean.
     */
    @Test
    public void testPushTail_boolean() {
        System.out.println("pushTail");

        RingBufBoolean rb;

        rb = new RingBufBoolean(3);

        rb.pushTail(true);
        rb.pushTail(false);
        rb.pushTail(true);
        assertEquals("true,false,true",rb.toString());

        try {
            rb.pushTail(false);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals("false,true",rb.toString());

        rb.pushTail(false);
        assertEquals("false,true,false",rb.toString());

        return;
    }

    /**
     * Test of peekElem method, of class RingBuf.
     */
    @Test
    public void testPeekElem() {
        System.out.println("peekElem");

        RingBufBoolean rb;

        rb = new RingBufBoolean(3);

        try {
            rb.peekElem(0);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail(true);
        assertEquals(true, rb.peekElem(0));
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

        rb.pushTail(false);
        assertEquals(true, rb.peekElem(0));
        assertEquals(false, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals(false, rb.peekElem(0));
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail(true);
        assertEquals(false, rb.peekElem(0));
        assertEquals(true, rb.peekElem(1));
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail(false);
        assertEquals(false, rb.peekElem(0));
        assertEquals(true, rb.peekElem(1));
        assertEquals(false, rb.peekElem(2));
        try {
            rb.peekElem(3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of iterator method, of class RingBufBoolean.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");

        RingBufBoolean rb;
        RingBufBoolean.IteratorOfBoolean it;

        rb = new RingBufBoolean(3);
        it = rb.iterator();
        assertFalse(it.hasNext());
        try {
            it.nextBoolean();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb.pushTail(true);
        rb.pushTail(false);
        rb.pushTail(true);
        rb.chopHead(2);
        rb.pushTail(false);

        it = rb.iterator();
        assertTrue(it.hasNext());
        assertEquals(true, it.nextBoolean());
        assertTrue(it.hasNext());
        assertEquals(false, it.nextBoolean());
        assertFalse(it.hasNext());
        try {
            it.nextBoolean();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb = new RingBufBoolean(3);
        rb.pushTail(true, false, true);
        it = rb.iterator();
        assertEquals(Boolean.TRUE, it.next());
        assertEquals(Boolean.FALSE, it.next());
        assertEquals(Boolean.TRUE, it.next());
        try {
            it.next();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of copyToArray method, of class RingBufBoolean.
     */
    @Test
    public void testCopyToArray() {
        System.out.println("copyToArray");

        RingBufBoolean rb;
        boolean[] dst;

        rb = new RingBufBoolean(3);
        rb.pushTail(true, false, true);
        rb.chopHead(2);
        rb.pushTail(false);

        dst = new boolean[]{false, false, false};
        rb.copyToArray(0, dst, 0, rb.size());
        assertArrayEquals(new boolean[]{true, false, false}, dst);

        dst = new boolean[]{false, false, false};
        rb.copyToArray(0, dst, 1, rb.size());
        assertArrayEquals(new boolean[]{false, true, false}, dst);

        dst = new boolean[]{false, false, false};
        rb.copyToArray(0, dst, 0, 1);
        assertArrayEquals(new boolean[]{true, false, false}, dst);

        dst = new boolean[]{true, true, true};
        rb.copyToArray(1, dst, 0, 1);
        assertArrayEquals(new boolean[]{false, true, true}, dst);

        rb = new RingBufBoolean(3);
        rb.pushTail(true, false, true);
        try {
            rb.copyToArray(0, null, 0, rb.size());
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufBoolean(3);
        rb.pushTail(true, false, true);
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
     * Test of duplicate method, of class RingBufBoolean.
     */
    @Test
    public void testDuplicate() {
        System.out.println("duplicate");

        RingBufBoolean old;
        RingBufBoolean result;

        old = new RingBufBoolean(0);
        result = RingBufBoolean.duplicate(old, 10);
        assertEquals(0, result.size());
        assertEquals(10, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufBoolean(3);
        old.pushTail(true, false, true);
        result = RingBufBoolean.duplicate(old, 2);
        assertEquals(2, result.size());
        assertEquals(2, result.capacity());
        assertEquals("true,false", result.toString());

        old = new RingBufBoolean(3);
        old.pushTail(true, false);
        result = RingBufBoolean.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("true,false", result.toString());

        old = new RingBufBoolean(3);
        old.pushTail(true, false, true);
        old.chopHead(2);
        old.pushTail(false);
        result = RingBufBoolean.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("true,false", result.toString());

        old = new RingBufBoolean(3);
        old.pushTail(true, false, true);
        try {
            RingBufBoolean.duplicate(null, 2);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        old = new RingBufBoolean(3);
        old.pushTail(true, false, true);
        result = RingBufBoolean.duplicate(old, 0);
        assertEquals(0, result.size());
        assertEquals(0, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufBoolean(3);
        old.pushTail(true, false, true);
        try {
            RingBufBoolean.duplicate(old, -1);
            fail();
        } catch(NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

}
