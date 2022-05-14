package bvk_ss22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
class BitOutputStreamTest {

    private OutputStream out;
    @org.junit.jupiter.api.Test

    @BeforeEach
    public void initOut(){
        OutputStream s;
        OutputStream out = new BitOutputStream(out);
    }

    @Test
    public void writeTest1() {

        assertEquals(1,new BitOutputStream(out).write(1,8));

    }
    @Test
    public void writeTest2() {

        assertEquals(2,new BitOutputStream(out).write(2,8));
    }

    @Test
    public void writeTest3() {

        assertEquals(3,new BitOutputStream(out).write(3,8));
    }

    @Test
    public void writeTest4() {

        assertEquals(3,new BitOutputStream(out).write(3,6));
    }

    @Test
    public void writeTest5() {

        assertEquals(126,new BitOutputStream(out).write(126,8));
    }

    @Test
    public void writeTest6() {

        assertEquals(1,new BitOutputStream(out).write(1,1));
    }
}
 **/