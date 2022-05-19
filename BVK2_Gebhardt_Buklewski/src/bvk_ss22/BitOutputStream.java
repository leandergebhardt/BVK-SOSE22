package bvk_ss22;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream {

    OutputStream out;

    //Integer[] buffer;
    Integer[] buffer = new Integer[8];
    public BitOutputStream (OutputStream out){
        this.out = out;

    }

    int bufferCount = 0;
    int count = 0;

    public void write(int value, int bitnumber) throws IOException {

        for(int x = bitnumber-1; x >= 0; x--) {
            int bit = (value >> x) & 1;
            buffer[count] = bit;
            count++;
            bufferCount++;

            if (bufferCount == 8) {
                int r = 0;
                int rcount = 0;
                for (int y = 8; y > 0; y--) {
                    r = r | (buffer[rcount] << y-1);
                    buffer[rcount] = null;
                    rcount++;
                }
                bufferCount = 0;
                count = 0;
                out.write(r);
            }
        }
    }

    public void close() throws IOException {

        int r = 0;
        int count = 0;
        for(int x = 8; x >0; x--){
            if(buffer[count] == null){buffer[count] = 0;}
            r= r | (buffer[count]<<x-1);
            count++;
        }


        out.write(r);
        out.close();
    }
}
