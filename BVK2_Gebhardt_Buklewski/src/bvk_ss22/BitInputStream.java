
package bvk_ss22;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {

        InputStream in;
        Integer[] buffer  = new Integer[8];

        public BitInputStream (InputStream in){

                this.in = in;
        }

        int bufferCount = 8;

        public int read(int bitNumber) throws IOException {

                int r;              //in r wir eine angeforderte anzahl an bits reinschrieben(bitNumber)
                int rCount;         //wird genutzt um die bits aus den buffer an die korrekte position in r zu schieben
                if(bufferCount == 8){
                        fillBuffer();
                }


                r=0;
                rCount = bitNumber-1;
                for (int x = 0; x != bitNumber; x++) {
                        r = r | (buffer[bufferCount] << rCount);
                        rCount--;
                        bufferCount++;
                        if(bufferCount == 8){
                                fillBuffer();
                        }
                }
                return r;
        }

        public void fillBuffer() throws IOException {
                int count = 0;
                int i = in.read();
                for (int x = 8; x > 0; x--) {           //neuen Buffer einlesen
                        int b = (i >> x-1) & 1;
                        buffer[count] = b;
                        count++;
                        bufferCount--;
                }
        }


        public void close() throws IOException {
                in.close();
        }


}