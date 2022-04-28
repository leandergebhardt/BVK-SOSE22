package bvk_ss22;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.Assert;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

class BitOutputStreamTest {

    private BitOutputStream out;
    @org.junit.jupiter.api.Test

    @Before
    public void initOut(){
        OutputStream s = null;
        out = new BitOutputStream(s);
    }

    @Test
    public void writeTest() {
        assertEquals(1110, out.write(14,4));
    }
}