
package bvk_ss22;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {

        InputStream in;
        Integer[] buffer = new Integer[8];
        public BitInputStream (InputStream in){
                this.in = in;
        }


        public int read(int bitNumber) throws IOException {
                int i = in.read();

                for (int x = bitNumber - 1; x >= 0; x--) {
                        int b = (i >> x) & 1;
                        buffer[x] = b;                          //der part funktioniert

                }
                int r = 0;
                for (int x = bitNumber - 1; x >= 0; x--) {
                        r = r | (buffer[x] << x);

                }
                return r;
        }

        public void close() throws IOException {
                in.close();
        }


}