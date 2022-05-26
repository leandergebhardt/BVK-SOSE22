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

    public static RasterImage binarize(RasterImage src, RasterImage dst, int t) {
        for (int pos = 0; pos < src.argb.length; pos++) {
            int newPixel = 0;
            int thisPixel = src.argb[pos] & 0x000000ff;

            if(thisPixel < t){
                newPixel = 0;
            } else {
                newPixel = 255;
            }
            dst.argb[pos] = (0xff << 24) | (newPixel << 16) | (newPixel << 8) | newPixel;
        }
        return dst;
    }
}
