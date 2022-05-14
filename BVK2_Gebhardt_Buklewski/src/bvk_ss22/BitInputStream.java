
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
        int bufferCount = 8;
        int rcount = 0;
        int r = 0;
        int i;
        public int read(int bitNumber) throws IOException {

                if(bufferCount == 0) {
                        i = in.read();


                        for (int x = 8; x > 0; x--) {           //neuen Buffer einlesen
                                int b = (i >> x - 1) & 1;
                                buffer[count] = b;
                                count++;
                                bufferCount--;
                        }
                        r=0;
                        rcount=0;
                        for (int x = bitNumber-1; rcount != bitNumber; x--) {
                                r = r | (buffer[rcount] << x);
                                rcount++;
                                bufferCount++;
                        }
                }
                else{                                   //es sind noch zahlen im buffer
                        r=0;
                        rcount=0;
                        while(bufferCount < 8) {
                                for (int x = bitNumber - 1; bufferCount < 8; x--) {
                                        r = r | (buffer[bufferCount] << x);
                                        rcount++;
                                        bufferCount++;
                                }
                        }
                        if(rcount != bitNumber) {
                                i = in.read();
                                count = 0;
                                for (int x = 8; x > 0; x--) {           //neuen Buffer einlesen
                                        int b = (i >> x - 1) & 1;
                                        buffer[count] = b;
                                        count++;
                                        bufferCount--;
                                }
                                for (int x = bitNumber - 1; rcount != bitNumber; x--) {
                                        r = r | (buffer[bufferCount] << x);
                                        rcount++;
                                        bufferCount++;
                                }
                        }
                }



                return r;
        }

        public int readByte() throws IOException {
                int bitNumber = 8;
                if(bufferCount==0) {
                        i = in.read();


                        for (int x = 8; x > 0; x--) {           //neuen Buffer einlesen
                                int b = (i >> x - 1) & 1;
                                buffer[count] = b;
                                count++;
                                bufferCount--;
                        }
                        r=0;
                        rcount=0;
                        for (int x = bitNumber-1; rcount != bitNumber; x--) {
                                r = r | (buffer[rcount] << x);
                                rcount++;
                                bufferCount++;
                        }
                }
                else{                                   //es sind noch zahlen im buffer
                        r=0;
                        rcount=0;
                        while(bufferCount<8) {
                                for (int x = bitNumber - 1; bufferCount<8; x--) {
                                        r = r | (buffer[bufferCount] << x);
                                        rcount++;
                                        bufferCount++;
                                }

                        }
                        if(rcount!=bitNumber) {
                                i = in.read();
                                count = 0;
                                for (int x = 8; x > 0; x--) {           //neuen Buffer einlesen
                                        int b = (i >> x - 1) & 1;
                                        buffer[count] = b;
                                        count++;
                                        bufferCount--;
                                }
                                for (int x = bitNumber - 1; rcount != bitNumber; x--) {
                                        r = r | (buffer[bufferCount] << x);
                                        rcount++;
                                        bufferCount++;
                                }
                        }
                }



                return r;
        }

        public void close() throws IOException {
                in.close();
        }


}