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



    public Integer write(int value, int bitnumber) throws IOException {

        for(int x = bitnumber-1; x >= 0; x--) {
             int b = (value >>x) & 1;
            buffer[x] = b;                          //der part funktioniert

        }
        int r = 0;
        for(int x = bitnumber-1; x >= 0; x--){
            r= r | (buffer[x]<<x);
            //passt
        }
            out.write(r);
//test

        return r;
    }
    public void close() throws IOException {
        out.close();
        
    }
}
