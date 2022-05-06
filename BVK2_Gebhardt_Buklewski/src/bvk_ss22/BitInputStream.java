
package bvk_ss22;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {

        InputStream in;
        Integer[] buffer  = new Integer[8];

        public BitInputStream (InputStream in){

                this.in = in;
        }

        int count = 0;
        public int read(int bitNumber) throws IOException {
                count = 0;
                int i = in.read();

                for (int x = 8; x>0 ; x--) {
                        int b = (i >> x-1) & 1;
                        buffer[count] = b;
                        count++;


                }
                count = 0;
                int r = 0;
                for (int x = bitNumber-1; count != bitNumber; x--) {
                        r = r | (buffer[count] << x);
                        count++;

                }
                return r;
        }

        public void close() throws IOException {
                in.close();
        }


}