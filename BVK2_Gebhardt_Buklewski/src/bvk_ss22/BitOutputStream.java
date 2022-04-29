package bvk_ss22;

import java.io.OutputStream;

public class BitOutputStream {

    OutputStream out;

    //Integer[] buffer;
    Integer[] buffer = new Integer[8];
    public BitOutputStream (){
        this.out = out;

    }



    public Integer write(int value, int bitnumber){

        for(int x = bitnumber-1; x >= 0; x--) {
             int b = (value >>x) & 1;
            buffer[x] = b;                          //der part funktioniert

        }
        int r = 0;
        for(int x = bitnumber-1; x >= 0; x--){
            r= r | (buffer[x]<<x);                                       //passt
        }
        return r;
    }
    public void close(){
        
    }
}
