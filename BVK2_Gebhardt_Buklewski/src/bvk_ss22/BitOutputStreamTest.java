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
    @org.junit.jupiter.api.Test

    @BeforeEach
    public void initOut(){
        OutputStream s;
        BitOutputStream out = new BitOutputStream();
    }

    @Test
    public void writeTest1() {

        assertEquals(1,new BitOutputStream().write(1,8));

    }
    @Test
    public void writeTest2() {

        assertEquals(2,new BitOutputStream().write(2,8));
    }
}