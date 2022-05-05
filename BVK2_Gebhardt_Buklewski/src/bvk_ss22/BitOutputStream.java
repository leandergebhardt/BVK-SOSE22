package bvk_ss22;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream {

    ByteArrayOutputStream out;
    private int count;
    //Integer[] buffer;
    Integer[] buffer = new Integer[8];

    public BitOutputStream (ByteArrayOutputStream out){
        this.out = out;

    }

    public Integer write(int value, int bitnumber) throws IOException {

        int r = 0;

        for(int x = bitnumber-1; x >= 0; x--) {
             int b = (value >>x) & 1;
            buffer[x] = b;                          //der part funktioniert
            count++;

            if(count==8) {
                for (int i = 7; i >= 0; i--) {
                    r = r | (buffer[i] << i);
                    //passt

                }
                out.write(r);
                r = 0;
            }
        }

/**
        if(count==8) {
            for (int x = bitnumber - 1; x >= 0; x--) {
                r = r | (buffer[x] << x);
                //passt
            }
        }
           out.write(r);
 **/
        return r;       //test
    }
    public int close() throws IOException {
        int r = 0;
        for (int x = 7; x >= 0; x--) {
            if(buffer[x]== null){buffer[x] = 0;}
            r = r | (buffer[x] << x);
            //out.write(r);
        }
        out.write(r);
        out.close();
        return r;

        
    }
}
