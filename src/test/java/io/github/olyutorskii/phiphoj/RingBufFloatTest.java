/*
 * License : The MIT License
 * Copyright(c) 2020 Olyutorskii
 */

package io.github.olyutorskii.phiphoj;

import java.util.Arrays;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import io.github.olyutorskii.phiphoj.RingBufFloat.IteratorOfFloat;

/**
 *
 */
public class RingBufFloatTest {

    public RingBufFloatTest() {
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
     * Test of constructor, of class RingBufFloat.
     */
    @Test
    public void testConstructor() {
        System.out.println("Constructor");

        RingBufFloat rb;

        rb = new RingBufFloat(1);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufFloat(999999);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufFloat(0);
        assertNotNull(rb);
        assertTrue(rb.postCondition());
        assertEquals("Empty Ring...", rb.toString());

        try {
            rb = new RingBufFloat(-1);
            fail();
            rb.getClass();
        } catch (NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of toString method, of class RingBufFloat.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        RingBufFloat rb;

        rb = new RingBufFloat(0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufFloat(3);
        assertEquals("Empty Ring...", rb.toString());
        rb.pushTail((float)1, (float)2);
        assertEquals("1.0,2.0", rb.toString());
        rb.pushTail((float)3);
        assertEquals("1.0,2.0,3.0", rb.toString());
        rb.chopHead(2);
        assertEquals("3.0", rb.toString());
        rb.pushTail((float)4);
        assertEquals("3.0,4.0", rb.toString());
        rb.pushTail((float)5);
        assertEquals("3.0,4.0,5.0", rb.toString());
        rb.chopHead();
        assertEquals("4.0,5.0", rb.toString());
        rb.chopHead(2);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufFloat(8);
        rb.pushTail((float)11, (float)12, (float)13, (float)14, (float)15, (float)16, (float)17);
        assertEquals("11.0,12.0,13.0,14.0,15.0,16.0,17.0", rb.toString());
        rb.pushTail((float)18);
        assertEquals("11.0,12.0,13.0,14.0,15.0,16.0,17.0,...(x 8)", rb.toString());

        rb = new RingBufFloat(6);
        rb.pushTail(Float.MIN_VALUE, -1.0f, -0.0f, 0.0f, 1.0f, Float.MAX_VALUE);
        assertEquals("1.4E-45,-1.0,-0.0,0.0,1.0,3.4028235E38", rb.toString());

        rb = new RingBufFloat(3);
        rb.pushTail(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NaN);
        assertEquals("-Infinity,Infinity,NaN", rb.toString());

        return;
    }

    /**
     * Test of hashCode method, of class RingBufFloat.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        RingBufFloat rb1, rb2;

        rb1 = new RingBufFloat(0);
        rb2 = new RingBufFloat(3);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(5);
        rb1.pushTail((float)11, (float)12);
        rb2.pushTail((float)11, (float)12);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(3);

        rb1.pushTail((float)11, (float)12, (float)13);
        rb1.chopHead(2);
        rb1.pushTail((float)14);
        rb2.pushTail((float)13, (float)14);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(3);
        rb1.pushTail(Float.NaN);
        rb2.pushTail(Float.NaN);
        assertEquals(rb1.hashCode(), rb2.hashCode());

        return;
    }

    /**
     * Test of equals method, of class RingBufFloat.
     */
    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testEquals() {
        System.out.println("equals");

        RingBufFloat rb1, rb2;

        rb1 = new RingBufFloat(0);
        rb2 = new RingBufFloat(0);

        assertTrue(rb1.equals(rb1));
        assertTrue(rb1.equals(rb2));

        RingBufFloat rbNull = null;
        String txt = "XYZ";
        assertFalse(rb1.equals(rbNull));
        assertFalse(rb1.equals(txt));

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(3);

        rb1.pushTail((float)11);
        assertFalse(rb1.equals(rb2));
        rb2.pushTail((float)11, (float)12);
        assertFalse(rb1.equals(rb2));
        rb1.pushTail((float)12);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(6);

        rb1.pushTail((float)1,(float)2);
        rb2.pushTail((float)1,(float)2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufFloat(6);
        rb2 = new RingBufFloat(6);

        rb1.pushTail((float)1,(float)2,(float)3,(float)4,(float)5,(float)6);
        rb1.chopHead(4);
        rb1.pushTail((float)7, (float)8);

        rb2.pushTail((float)5,(float)6,(float)7,(float)8);

        assertTrue(rb1.equals(rb2));
        rb2.pushTail((float)9);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(3);

        rb1.pushTail((float)11, (float)12, (float)13);
        rb2.pushTail((float)11, (float)12, (float)33);
        assertFalse(rb1.equals(rb2));

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(3);
        rb1.pushTail(Float.NaN);
        rb2.pushTail(Float.NaN);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(3);
        float nan1 = Float.intBitsToFloat((((1<<8)-1)<<23) | 1);
        float nan2 = Float.intBitsToFloat((((1<<8)-1)<<23) | 2);
        assert Float.isNaN(nan1);
        assert Float.isNaN(nan2);
        rb1.pushTail(nan1);
        rb2.pushTail(nan2);
        assertTrue(rb1.equals(rb2));

        rb1 = new RingBufFloat(3);
        rb2 = new RingBufFloat(3);
        rb1.pushTail(+0.0f);
        rb2.pushTail(-0.0f);
        assertFalse(rb1.equals(rb2));

        return;
    }

    /**
     * Test of pushTail method, of class RingBufFloat.
     */
    @Test
    public void testPushTail_3args() {
        System.out.println("pushTail");

        RingBufFloat rb;

        rb = new RingBufFloat(3);
        try {
            rb.pushTail(null, 0, 10);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufFloat(3);
        rb.pushTail(new float[]{11, 12, 13}, 0, 3);
        assertEquals("11.0,12.0,13.0", rb.toString());

        rb = new RingBufFloat(3);
        rb.pushTail(new float[]{11, 12, 13}, 0, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufFloat(3);
        rb.pushTail(new float[]{11, 12, 13}, 1, 0);
        assertEquals("Empty Ring...", rb.toString());

        rb = new RingBufFloat(3);
        try {
            rb.pushTail(new float[]{11, 12, 13}, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufFloat(3);
        try {
            rb.pushTail(new float[]{11, 12, 13}, -1, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufFloat(3);
        try {
            rb.pushTail(new float[]{11, 12, 13}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufFloat(3);
        try {
            rb.pushTail(new float[]{11, 12, 13, 14}, 0, 4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        rb = new RingBufFloat(6);
        rb.pushTail((float)11, (float)12, (float)13, (float)14);
        rb.chopHead(2);
        rb.pushTail(new float[]{14, 15, 16, 17, 18}, 1, 3);
        assertEquals("13.0,14.0,15.0,16.0,17.0", rb.toString());

        return;
    }

    /**
     * Test of pushTail method, of class RingBufFloat.
     */
    @Test
    public void testPushTail_floatArr() {
        System.out.println("pushTail");

        RingBufFloat rb;

        rb = new RingBufFloat(3);

        try {
            rb.pushTail(null);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb.pushTail(new float[]{11});
        assertEquals("11.0",rb.toString());

        rb.pushTail((float)12, (float)13);
        assertEquals("11.0,12.0,13.0",rb.toString());

        rb = new RingBufFloat(3);

        try {
            rb.pushTail((float)11, (float)12, (float)13, (float)14);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of pushTail method, of class RingBufFloat.
     */
    @Test
    public void testPushTail_float() {
        System.out.println("pushTail");

        RingBufFloat rb;

        rb = new RingBufFloat(3);

        rb.pushTail((float)11);
        rb.pushTail((float)21);
        rb.pushTail((float)31);
        assertEquals("11.0,21.0,31.0",rb.toString());

        try {
            rb.pushTail((float)11);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals("21.0,31.0",rb.toString());

        rb.pushTail((float)41);
        assertEquals("21.0,31.0,41.0",rb.toString());

        return;
    }

    /**
     * Test of peekElem method, of class RingBuf.
     */
    @Test
    public void testPeekElem() {
        System.out.println("peekElem");

        RingBufFloat rb;

        rb = new RingBufFloat(3);

        try {
            rb.peekElem(0);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((float)11);
        assertEquals((float)11, rb.peekElem(0), 0);
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

        rb.pushTail((float)12);
        assertEquals((float)11, rb.peekElem(0), 0);
        assertEquals((float)12, rb.peekElem(1), 0);
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.chopHead();
        assertEquals((float)12, rb.peekElem(0), 0);
        try {
            rb.peekElem(1);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((float)13);
        assertEquals((float)12, rb.peekElem(0), 0);
        assertEquals((float)13, rb.peekElem(1), 0);
        try {
            rb.peekElem(2);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        rb.pushTail((float)14);
        assertEquals((float)12, rb.peekElem(0), 0);
        assertEquals((float)13, rb.peekElem(1), 0);
        assertEquals((float)14, rb.peekElem(2), 0);
        try {
            rb.peekElem(3);
            fail();
        } catch(IndexOutOfBoundsException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of iterator method, of class RingBufFloat.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");

        RingBufFloat rb;
        IteratorOfFloat it;

        rb = new RingBufFloat(3);
        it = rb.iterator();
        assertFalse(it.hasNext());
        try {
            it.nextFloat();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb.pushTail((float)11);
        rb.pushTail((float)12);
        rb.pushTail((float)13);
        rb.chopHead(2);
        rb.pushTail((float)14);

        it = rb.iterator();
        assertTrue(it.hasNext());
        assertEquals((float)13, it.nextFloat(), 0);
        assertTrue(it.hasNext());
        assertEquals((float)14, it.nextFloat(), 0);
        assertFalse(it.hasNext());
        try {
            it.nextFloat();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        rb = new RingBufFloat(3);
        rb.pushTail((float)11, (float)12, (float)13);
        it = rb.iterator();
        assertEquals(Float.valueOf((float)11), it.next());
        assertEquals(Float.valueOf((float)12), it.next());
        assertEquals(Float.valueOf((float)13), it.next());
        try {
            it.next();
            fail();
        } catch(NoSuchElementException e) {
            assert true;
        }

        return;
    }

    /**
     * Test of copyToArray method, of class RingBufFloat.
     */
    @Test
    public void testCopyToArray() {
        System.out.println("copyToArray");

        RingBufFloat rb;
        float[] dst;

        rb = new RingBufFloat(3);
        rb.pushTail((float)11, (float)12, (float)13);
        rb.chopHead(2);
        rb.pushTail((float)14);

        dst = new float[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, rb.size());
        assertTrue(Arrays.equals(new float[]{13, 14, 99}, dst));

        dst = new float[]{99, 99, 99};
        rb.copyToArray(0, dst, 1, rb.size());
        assertTrue(Arrays.equals(new float[]{99, 13, 14}, dst));

        dst = new float[]{99, 99, 99};
        rb.copyToArray(0, dst, 0, 1);
        assertTrue(Arrays.equals(new float[]{13, 99, 99}, dst));

        dst = new float[]{99, 99, 99};
        rb.copyToArray(1, dst, 0, 1);
        assertTrue(Arrays.equals(new float[]{14, 99, 99}, dst));

        rb = new RingBufFloat(3);
        rb.pushTail((float)11, (float)12, (float)13);
        try {
            rb.copyToArray(0, null, 0, rb.size());
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        rb = new RingBufFloat(3);
        rb.pushTail((float)1, (float)2, (float)3);
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
     * Test of duplicate method, of class RingBufFloat.
     */
    @Test
    public void testDuplicate() {
        System.out.println("duplicate");

        RingBufFloat old;
        RingBufFloat result;

        old = new RingBufFloat(0);
        result = RingBufFloat.duplicate(old, 10);
        assertEquals(0, result.size());
        assertEquals(10, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufFloat(3);
        old.pushTail((float)11, (float)12, (float)13);
        result = RingBufFloat.duplicate(old, 2);
        assertEquals(2, result.size());
        assertEquals(2, result.capacity());
        assertEquals("11.0,12.0", result.toString());

        old = new RingBufFloat(3);
        old.pushTail((float)11, (float)12);
        result = RingBufFloat.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("11.0,12.0", result.toString());

        old = new RingBufFloat(3);
        old.pushTail((float)11, (float)12, (float)13);
        old.chopHead(2);
        old.pushTail((float)14);
        result = RingBufFloat.duplicate(old, 3);
        assertEquals(2, result.size());
        assertEquals(3, result.capacity());
        assertEquals("13.0,14.0", result.toString());

        old = new RingBufFloat(3);
        old.pushTail((float)11, (float)12, (float)13);
        try {
            RingBufFloat.duplicate(null, 2);
            fail();
        } catch(NullPointerException e) {
            assert true;
        }

        old = new RingBufFloat(3);
        old.pushTail((float)11, (float)12, (float)13);
        result = RingBufFloat.duplicate(old, 0);
        assertEquals(0, result.size());
        assertEquals(0, result.capacity());
        assertEquals("Empty Ring...", result.toString());

        old = new RingBufFloat(3);
        old.pushTail((float)11, (float)12, (float)13);
        try {
            RingBufFloat.duplicate(old, -1);
            fail();
        } catch(NegativeArraySizeException e) {
            assert true;
        }

        return;
    }

}
