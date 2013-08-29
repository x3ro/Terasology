package org.terasology.rendering.gui;

import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Vector4f;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StyleApplicatorTest {
    private ApplicatorTargetDummy target;

    @Before
    public void setUp() {
        target = new ApplicatorTargetDummy();
    }

    @Test
    public void testApplyString() {
        StyleApplicator.applyStyle(target, "string", "foobar");
        assertEquals("foobar", target.s);
    }

    @Test
    public void testApplyFloat() {
        StyleApplicator.applyStyle(target, "float", 42.0f);
        assertEquals(42.0f, target.f, 0.1);
    }

    @Test
    public void testApplyVectorFloat() {
        Float[] fs = {1.0f, 2.0f, 3.0f, 4.0f};
        List<Float> list = Arrays.asList(fs);

        StyleApplicator.applyStyle(target, "vector", list);
        assertEquals(2.0f, target.v.getY(), 0.1);
        assertEquals(4.0f, target.v.getW(), 0.1);
    }

    @Test
    public void testApplyVectorDouble() {
        Double[] fs = {1.0, 2.0, 3.0, 4.0};
        List<Double> list = Arrays.asList(fs);

        StyleApplicator.applyStyle(target, "vector", list);
        assertEquals(2.0f, target.v.getY(), 0.1);
        assertEquals(4.0f, target.v.getW(), 0.1);
    }

    @Test
    public void testApplyEnumLowercase() {
        StyleApplicator.applyStyle(target, "enum", "right");
        assertEquals(TestEnum.RIGHT, target.e);
    }

    @Test
    public void testApplyEnumUppercase() {
        StyleApplicator.applyStyle(target, "enum", "RIGHT");
        assertEquals(TestEnum.RIGHT, target.e);
    }

    @Test(expected= IllegalArgumentException.class)
    public void testValueCannotBeAnArray() {
        Double[] fs = {1.0, 2.0, 3.0, 4.0};
        StyleApplicator.applyStyle(target, "vector", fs);
    }



    /**
     * This class serves as a dummy to which styles are applied.
     */
    protected class ApplicatorTargetDummy {
        public String s = null;
        public Vector4f v = null;
        public Float f = null;
        public TestEnum e = null;

        public void setString(String s) { this.s = s; }
        public void setVector(Vector4f v) { this.v = v; }
        public void setFloat(Float f) { this.f = f; }
        public void setEnum(TestEnum e) { this.e = e; }
    }

    public static enum TestEnum { LEFT, CENTER, RIGHT }

}
