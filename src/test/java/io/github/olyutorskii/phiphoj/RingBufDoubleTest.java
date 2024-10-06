/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.Arrays;
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
public class RingBufDoubleTest {

    public RingBufDoubleTest() {
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
     * Test of constructor, of class RingBufDouble.
     */
    @Test
    public void testConstructor() {
        System.out.println("Constructor");

        RingBufDouble rb;

        rb = new RingBufDouble(1);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufDouble(999999);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufDouble(0);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb = new RingBufDouble(-1);
            fail();
            rb.getClass();
        } catch (NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of toString method, of class RingBufDouble.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        RingBufDouble rb;

        rb = new RingBufDouble(0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufDouble(3);
        assertEquals("Empty Ring...", rb.toString());
        rb.pushTail((double)1, (double)2);
        assertEquals("1.0,2.0", rb.toString());
        rb.pushTail((double)3);
        assertEquals("1.0,2.0,3.0", rb.toString());
        rb.chopHead(2);
        assertEquals("3.0", rb.toString());
        rb.pushTail((double)4);
        assertEquals("3.0,4.0", rb.toString());
        rb.pushTail((double)5);
        assertEquals("3.0,4.0,5.0", rb.toString());
        rb.chopHead();
        assertEquals("4.0,5.0", rb.toString());
        rb.chopHead(2);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufDouble(8);
        rb.pushTail((double)11, (double)12, (double)13, (double)14, (double)15, (double)16, (double)17);
        assertEquals("11.0,12.0,13.0,14.0,15.0,16.0,17.0", rb.toString());
        rb.pushTail((double)18);
        assertEquals("11.0,12.0,13.0,14.0,15.0,16.0,17.0,...(x 8)", rb.toString());

        rb = new RingBufDouble(6);
        rb.pushTail(Double.MIN_VALUE, -1.0, -0.0, 0.0, 1.0, Double.MAX_VALUE);
        assertEquals("4.9E-324,-1.0,-0.0,0.0,1.0,1.7976931348623157E308", rb.toString());

        rb = new RingBufDouble(3);
        rb.pushTail(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN);
        assertEquals("-Infinity,Infinity,NaN", rb.toString());

        return;
    }

    /**
     * Test of hashCode method, of class RingBufDouble.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        RingBufDouble rb1, rb2;

        rb1 = new RingBufDouble(0);
        rb2 = new RingBufDouble(3);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(5);
        rb1.pushTail((double)11, (double)12);
        rb2.pushTail((double)11, (double)12);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(3);

        rb1.pushTail((double)11, (double)12, (double)13);
        rb1.chopHead(2);
        rb1.pushTail((double)14);
        rb2.pushTail((double)13, (double)14);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(3);
        rb1.pushTail(Double.NaN);
        rb2.pushTail(Double.NaN);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        return;
    }

    /**
     * Test of equals method, of class RingBufDouble.
     */
    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testEquals() {
        System.out.println("equals");

        RingBufDouble rb1, rb2;

        rb1 = new RingBufDouble(0);
        rb2 = new RingBufDouble(0);

        assertTrue(rb1.equals(rb1));
        assertTrue(rb1.equals(rb2));

        RingBufDouble rbNull = null;
        String txt = "XYZ";
        assertFalse(rb1.equals(rbNull));
        assertFalse(rb1.equals(txt));

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(3);

        rb1.pushTail((double)11);
        assertFalse(rb1.equals(rb2));
        rb2.pushTail((double)11, (double)12);
        assertFalse(rb1.equals(rb2));
        rb1.pushTail((double)12);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(6);

        rb1.pushTail((double)1,(double)2);
        rb2.pushTail((double)1,(double)2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufDouble(6);
        rb2 = new RingBufDouble(6);

        rb1.pushTail((double)1,(double)2,(double)3,(double)4,(double)5,(double)6);
        rb1.chopHead(4);
        rb1.pushTail((double)7, (double)8);

        rb2.pushTail((double)5,(double)6,(double)7,(double)8);

        assertTrue(rb1.equals(rb2));
        rb2.pushTail((double)9);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(3);

        rb1.pushTail((double)11, (double)12, (double)13);
        rb2.pushTail((double)11, (double)12, (double)33);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(3);
        rb1.pushTail(Double.NaN);
        rb2.pushTail(Double.NaN);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(3);
        double nan1 = Double.longBitsToDouble((((1L<<11)-1L)<<52) | 1L);
        double nan2 = Double.longBitsToDouble((((1L<<11)-1L)<<52) | 2L);
        assert Double.isNaN(nan1);
        assert Double.isNaN(nan2);
        rb1.pushTail(nan1);
        rb2.pushTail(nan2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufDouble(3);
        rb2 = new RingBufDouble(3);
        rb1.pushTail(+0.0);
        rb2.pushTail(-0.0);
        assertFalse(rb1.equals(rb2));

        return;
    }

    /**
     * Test of pushTail method, of class RingBufDouble.
     */
    @Test
    public void testPushTail_3args() {
        System.out.println("pushTail");

        RingBufDouble rb;

        rb = new RingBufDouble(3);
        try {
            rb.pushTail(null, 0, 10);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufDouble(3);
        rb.pushTail(new double[]{11, 12, 13}, 0, 3);
        assertEquals("11.0,12.0,13.0", rb.toString());

        rb = new RingBufDouble(3);
        rb.pushTail(new double[]{11, 12, 13}, 0, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufDouble(3);
        rb.pushTail(new double[]{11, 12, 13}, 1, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufDouble(3);
        try {
            rb.pushTail(new double[]{11, 12, 13}, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufDouble(3);
        try {
            rb.pushTail(new double[]{11, 12, 13}, -1, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufDouble(3);
        try {
            rb.pushTail(new double[]{11, 12, 13}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufDouble(3);
        try {
            rb.pushTail(new double[]{11, 12, 13, 14}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufDouble(6);
        rb.pushTail((double)11, (double)12, (double)13, (double)14);
        rb.chopHead(2);
        rb.pushTail(new double[]{14, 15, 16, 17, 18}, 1, 3);
        assertEquals("13.0,14.0,15.0,16.0,17.0", rb.toString());

        return;
    }

    /**
     * Test of pushTail method, of class RingBufDouble.
     */
    @Test
    public void testPushTail_doubleArr() {
        System.out.println("pushTail");

        RingBufDouble rb;

        rb = new RingBufDouble(3);

        try {
            rb.pushTail(null);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb.pushTail(new double[]{11});
        assertEquals("11.0",rb.toString());

        rb.pushTail((double)12, (double)13);
        assertEquals("11.0,12.0,13.0",rb.toString());

        rb = new RingBufDouble(3);

        try {
            rb.pushTail((double)11, (double)12, (double)13, (double)14);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of pushTail method, of class RingBufDouble.
     */
    @Test
    public void testPushTail_double() {
        System.out.println("pushTail");

        RingBufDouble rb;

        rb = new RingBufDouble(3);

        rb.pushTail((double)11);
        rb.pushTail((double)21);
        rb.pushTail((double)31);
        assertEquals("11.0,21.0,31.0",rb.toString());

        try {
            rb.pushTail((double)11);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals("21.0,31.0",rb.toString());

        rb.pushTail((double)41);
        assertEquals("21.0,31.0,41.0",rb.toString());

        return;
    }

    /**
     * Test of peekElem method, of class RingBuf.
     */
    @Test
    public void testPeekElem() {
        System.out.println("peekElem");

        RingBufDouble rb;

        rb = new RingBufDouble(3);

        try {
            rb.peekElem(0);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((double)11);
        assertEquals((double)11, rb.peekElem(0), 0);
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

        rb.pushTail((double)12);
        assertEquals((double)11, rb.peekElem(0), 0);
        assertEquals((double)12, rb.peekElem(1), 0);
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals((double)12, rb.peekElem(0), 0);
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((double)13);
        assertEquals((double)12, rb.peekElem(0), 0);
        assertEquals((double)13, rb.peekElem(1), 0);
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((double)14);
        assertEquals((double)12, rb.peekElem(0), 0);
        assertEquals((double)13, rb.peekElem(1), 0);
        assertEquals((double)14, rb.peekElem(2), 0);
        try {
            rb.peekElem(3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of iterator method, of class RingBufDouble.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");

        RingBufDouble rb;
        PrimitiveIterator.OfDouble it;

        rb = new RingBufDouble(3);
        it = rb.iterator();
        assertFalse(it.hasNext());
        try {
            it.nextDouble();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb.pushTail((double)11);
        rb.pushTail((double)12);
        rb.pushTail((double)13);
        rb.chopHead(2);
        rb.pushTail((double)14);

        it = rb.iterator();
        assertTrue(it.hasNext());
        assertEquals((double)13, it.nextDouble(), 0);
        assertTrue(it.hasNext());
        assertEquals((double)14, it.nextDouble(), 0);
        assertFalse(it.hasNext());
        try {
            it.nextDouble();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb = new RingBufDouble(3);
        rb.pushTail((double)11, (double)12, (double)13);
        it = rb.iterator();
        assertEquals(Double.valueOf((double)11), it.next());
        assertEquals(Double.valueOf((double)12), it.next());
        assertEquals(Double.valueOf((double)13), it.next());
        try {
            it.next();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of copyToArray method, of class RingBufDouble.
     */
    @Test
    public void testCopyToArray() {
        System.out.println("copyToArray");

        RingBufDouble rb;
        double[] dst;

        rb = new RingBufDouble(3);
        rb.pushTail((double)11, (double)12, (double)13);
        rb.chopHead(2);
        rb.pushTail((double)14);

        dst = new double[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, rb.size());
        assertTrue(Arrays.equals(new double[]{13, 14, 99}, dst));

        dst = new double[]{99, 99, 99};
        rb.copyToArray(0, dst, 1, rb.size());
        assertTrue(Arrays.equals(new double[]{99, 13, 14}, dst));

        dst = new double[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, 1);
        assertTrue(Arrays.equals(new double[]{13, 99, 99}, dst));

        dst = new double[]{99, 99, 99};
        rb.copyToArray(1, dst, 0, 1);
        assertTrue(Arrays.equals(new double[]{14, 99, 99}, dst));

        rb = new RingBufDouble(3);
        rb.pushTail((double)11, (double)12, (double)13);
        try {
            rb.copyToArray(0, null, 0, rb.size());
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufDouble(3);
        rb.pushTail((double)1, (double)2, (double)3);
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
     * Test of duplicate method, of class RingBufDouble.
     */
    @Test
    public void testDuplicate() {
        System.out.println("duplicate");

        RingBufDouble old;
        RingBufDouble result;

        old = new RingBufDouble(0);
        result = RingBufDouble.duplicate(old, 10);
        assertEquals(0, result.size());
        assertEquals(10, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufDouble(3);
        old.pushTail((double)11, (double)12, (double)13);
        result = RingBufDouble.duplicate(old, 2);
        assertEquals(2, result.size());
        assertEquals(2, result.capacity());
        assertEquals("11.0,12.0", result.toString());

        old = new RingBufDouble(3);
        old.pushTail((double)11, (double)12);
        result = RingBufDouble.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("11.0,12.0", result.toString());

        old = new RingBufDouble(3);
        old.pushTail((double)11, (double)12, (double)13);
        old.chopHead(2);
        old.pushTail((double)14);
        result = RingBufDouble.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("13.0,14.0", result.toString());

        old = new RingBufDouble(3);
        old.pushTail((double)11, (double)12, (double)13);
        try {
            RingBufDouble.duplicate(null, 2);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        old = new RingBufDouble(3);
        old.pushTail((double)11, (double)12, (double)13);
        result = RingBufDouble.duplicate(old, 0);
        assertEquals(0, result.size());
        assertEquals(0, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufDouble(3);
        old.pushTail((double)11, (double)12, (double)13);
        try {
            RingBufDouble.duplicate(old, -1);
            fail();
        } catch(NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

}
