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
        for(int y = 0; y < src.height; y++) {
            for(int x = 0; x < src.width; x++) {
                int pos = y * src.width + x;

                int pixel = src.argb[pos];
                int previousPixel;
                if(pos == 0){
                    previousPixel = 128;
                } else {
                    previousPixel = src.argb[pos - 1];
                }

                int diff = pixel - previousPixel;
                diff += 128;

                if(diff > 255){
                    diff = 255;
                }
                if(diff < 0){
                    diff = 0;
                }

                dst.argb[pos] = (0xff << 24) | (diff << 16) | (diff << 8) | diff;
            }
        }
        return dst;
    }
}
