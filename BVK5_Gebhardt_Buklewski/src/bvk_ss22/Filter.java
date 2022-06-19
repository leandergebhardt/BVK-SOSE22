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

    public static double[][] lowPassHorizontal (double[][] image){
        double[] h0 = new double[] {-1.0, 2.0, 6.0, 2.0, -1.0};
        double[][] resultImage = new double[image.length][image[0].length];
        // durch x&y iterieren und h0 als filter anwenden; Randbehandlung: spiegeln
        for(int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                if (x % 2 == 0){
                    //System.out.println("pixelvalue before: " + image[y][x]);
                    //filter anwenden
                    int count = 0;
                    double value = 0;

                    for (int f = -2; f <= 2; f++) {
                        int i = f;
                        if (x + i < 0) {
                            i = i * (-1);
                        }
                        if (x + i >= image[0].length) {
                            i = i * (-1);
                        }

                        value += image[y][x + i] * h0[count];

                        count++;
                    }

                    value = value / 8.0;

                    //System.out.println(value);

                    resultImage[y][x/2] = value;
                }
            }
        }

        return resultImage;
    }

    public static double[][] lowPassVertical (double[][] image){
        double[] h0 = new double[] {-1.0, 2.0, 6.0, 2.0, -1.0};
        double[][] resultImage = new double[image.length][image[0].length];
        // durch x&y iterieren und h0 als filter anwenden; Randbehandlung: spiegeln
        for(int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                if (y % 2 == 0){
                    //System.out.println("pixelvalue before: " + image[y][x]);
                    //filter anwenden
                    int count = 0;
                    double value = 0;

                    for (int f = -2; f <= 2; f++) {
                        int i = f;
                        if (y + i < 0) {
                            i = i * (-1);
                        }
                        if (y + i >= image.length) {
                            i = i * (-1);
                        }

                        value += image[y + i][x] * h0[count];

                        count++;
                    }

                    value = value / 8.0;

                    //System.out.println(value);

                    resultImage[y/2][x] = value;
                }
            }
        }

        return resultImage;
    }

    public static double[][] highPassHorizontal(double[][] image){
        double[] h1 = new double[] {-1.0, 2.0, -1.0};
        double[][] resultImage = new double[image.length][image[0].length];
        // durch x&y iterieren und h0 als filter anwenden; Randbehandlung: spiegeln
        for(int y = 0; y < image.length; y++){
            for(int x = 0; x < image[0].length; x++){
                if (x % 2 == 0) {
                    //System.out.println("pixelvalue before: " + image[y][x]);
                    //filter anwenden
                    int count = 0;
                    double value = 0;

                    for (int f = -1; f <= 1; f++) {
                        int i = f;
                        if (x + i < 0) {
                            i = i * (-1);
                        }
                        if (x + i >= image[0].length) {
                            i = i * (-1);
                        }

                        value += image[y][x + i] * h1[count];

                        count++;
                    }

                    value = value / 2.0;

                    //value += 127;

                    //System.out.println(value);


                    resultImage[y][x/2] = value;
                }
            }
        }

        return resultImage;

    }

    public static double[][] highPassVertical(double[][] image){
        double[] h1 = new double[] {-1.0, 2.0, -1.0};
        double[][] resultImage = new double[image.length][image[0].length];
        // durch x&y iterieren und h0 als filter anwenden; Randbehandlung: spiegeln
        for(int y = 0; y < image.length; y++){
            for(int x = 0; x < image[0].length; x++){
                if (y % 2 == 0) {
                    //System.out.println("pixelvalue before: " + image[y][x]);
                    //filter anwenden
                    int count = 0;
                    double value = 0;

                    for (int f = -1; f <= 1; f++) {
                        int i = f;
                        if (y + i < 0) {
                            i = i * (-1);
                        }
                        if (y + i >= image.length) {
                            i = i * (-1);
                        }

                        value += image[y + i][x] * h1[count];

                        count++;
                    }

                    value = value / 2.0;


                    //System.out.println(value);


                    resultImage[y/2][x] = value;
                }
            }
        }

        return resultImage;

    }

}
