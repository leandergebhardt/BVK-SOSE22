package bvk_ss22;


public class Filter {
    public void greyScale(RasterImage src, RasterImage dst) {
        for(int y = 0; y < src.height; y++) {
            for(int x = 0; x < src.width; x++) {
                int pos = y * src.width + x;

                int rn = 0, gn = 0, bn = 0;

                int pixels = src.argb[pos];

            }
        }
    }

    public static RasterImage copy(RasterImage src, RasterImage dst) {
        for(int pos = 0; pos < src.argb.length; pos++) {
            dst.argb[pos] = src.argb[pos];
        }
        return dst;
    }

    public static RasterImage dpcm(RasterImage src, RasterImage dst) {

        return dst;
    }
}
