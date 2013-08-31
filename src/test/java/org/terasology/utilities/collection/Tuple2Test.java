package org.terasology.utilities.collection;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Tuple2Test {

    @Test
    public void functionality() {
        Tuple2<Integer, String> t = new Tuple2<Integer, String>(5, "foobar");
        assertEquals((long) 5, (long) t._1());
        assertEquals("foobar", t._2());
    }

}
