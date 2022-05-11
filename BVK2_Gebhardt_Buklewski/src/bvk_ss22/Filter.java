package bvk_ss22;


public class Filter {
    public static RasterImage greyScale(RasterImage src, RasterImage dst) {
        for(int y = 0; y < src.height; y++) {
            for(int x = 0; x < src.width; x++) {
                int pos = y * src.width + x;

                int pixels = src.argb[pos];

                int r = (pixels >> 16) & 0xff;
                int g = (pixels >> 8)  & 0xff;
                int b = pixels         & 0xff;

                int avg = (r + g + b) / 3;

                dst.argb[pos] = (0xff << 24) | (avg << 16) | (avg << 8) | avg;
            }
        }
        return dst;
    }

    public static RasterImage copy(RasterImage src, RasterImage dst) {
        for(int pos = 0; pos < src.argb.length; pos++) {
            dst.argb[pos] = src.argb[pos];
        }
        return dst;
    }

    public static RasterImage dpcm(RasterImage src, RasterImage dst) {
        for (int y = 0; y < src.height; y++) {
            for (int x = 0; x < src.width; x++) {

                int pos = y * src.width + x;
                int S = src.argb[pos] & 0x000000ff;
                    //prediction ist der Pixel davor
                int P = (pos == 0) ? 128 : src.argb[pos-1] & 0x000000ff;
                    //error == unterschied zwischen P und dem tatsächlichen Wert
                double error = S - P;
                double errorPic = error + 128;
                if (errorPic > 255) errorPic = 255;
                if (errorPic < 0) errorPic = 0;

                dst.argb[pos] = (0xff <<24) |  (((int)errorPic) <<16) | (((int)errorPic) <<8) | (int)errorPic;


                //double reconst = error + P;

                //dst.argb[pos] = (0xff <<24) | ((int)reconst <<16) | ((int)reconst <<8) | (int)reconst;

                //mse += Math.pow((origImg.argb[pos]& 0xff - reconstructedImg.argb[pos] & 0xff), 2);


            }
        }
        return dst;
    }
}
