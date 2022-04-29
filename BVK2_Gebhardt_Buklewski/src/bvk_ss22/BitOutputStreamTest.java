package bvk_ss22;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Assert;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

class BitOutputStreamTest {

    private BitOutputStream out;
    //@org.junit.jupiter.api.Test

    OutputStream s = null;
    @BeforeEach
    public void initOut(){
        //OutputStream s = null;
        BitOutputStream out = new BitOutputStream(s);
    }

    @Test
    public void writeTest1() {

        assertEquals(1,new BitOutputStream(s).write(1,8));

    }
    @Test
    public void writeTest2() {

        assertEquals(2,new BitOutputStream(s).write(2,8));
    }

    @Test
    public void writeTest3() {

        assertEquals(3,new BitOutputStream(s).write(3,8));
    }

    @Test
    public void writeTest4() {

        assertEquals(3,new BitOutputStream(s).write(3,6));
    }

    @Test
    public void writeTest5() {

        assertEquals(126,new BitOutputStream(s).write(126,8));
    }

    @Test
    public void writeTest6() {

        assertEquals(1,new BitOutputStream(s).write(1,1));
    }
}