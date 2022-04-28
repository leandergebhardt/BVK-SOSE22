package bvk_ss22;

import java.io.OutputStream;

public class BitOutputStream {

    OutputStream out;

    Integer[] buffer;
    public BitOutputStream (OutputStream out){
        this.out = out;
    }
    public void write(int value, int bitnumber){
        for(int x = bitnumber; x == 0; x--) {
            buffer[x] = (value >> x) & 1;
        }
    }
    public void close(){
        
    }
}
