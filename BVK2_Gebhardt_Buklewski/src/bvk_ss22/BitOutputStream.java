package bvk_ss22;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream {

    OutputStream out;
    private int count;
    //Integer[] buffer;
    Integer[] buffer = new Integer[8];
    
    public BitOutputStream (OutputStream out){
        this.out = out;

    }

    public Integer write(int value, int bitnumber) throws IOException {

        for(int x = bitnumber-1; x >= 0; x--) {
             int b = (value >>x) & 1;
            buffer[x] = b;                          //der part funktioniert
            count++;
        }
        int r = 0;

        if(count==8) {
            for (int x = bitnumber - 1; x >= 0; x--) {
                r = r | (buffer[x] << x);
                //passt
            }
        }
            //out.write(r);
        return r;       //test
    }
    public int close() throws IOException {
        int r = 0;
        for (int x = 7; x >= 0; x--) {
            if(buffer[x]== null){buffer[x] = 0;}
            r = r | (buffer[x] << x);

        }
        //out.close();
        return r;

        
    }
}
