package bvk_ss22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitOutputStreamTest {

    private BitOutputStream out;
    //@org.junit.jupiter.api.Test

    ByteArrayOutputStream s = null;
    @BeforeEach
    public void initOut(){
        //OutputStream s = null;
        BitOutputStream out = new BitOutputStream(s);
    }

    @Test
    public void writeTest1() throws IOException {
        assertEquals(1,new BitOutputStream(s).write(1,8));

    }
    @Test
    public void writeTest2() throws IOException {

        assertEquals(2,new BitOutputStream(s).write(2,8));
    }

    @Test
    public void writeTest3() throws IOException {

        assertEquals(3,new BitOutputStream(s).write(3,8));
    }

    @Test
    public void writeTest4() throws IOException {
        BitOutputStream out = new BitOutputStream(s);

        assertEquals(4,testClose(4,6,out));
    }

    @Test
    public void writeTest5() throws IOException {

        assertEquals(126,new BitOutputStream(s).write(126,8));
    }

    @Test
    public void writeTestSmallBitnumber() throws IOException {
        BitOutputStream out = new BitOutputStream(s);
        assertEquals(3,testClose(3, 3, out));
    }

    private int testClose(int value, int bitnumber, BitOutputStream out) throws IOException {

        out.write(value,bitnumber);
        int r = out.close();
        return r;

    }
}