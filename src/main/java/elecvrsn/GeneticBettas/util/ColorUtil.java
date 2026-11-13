package elecvrsn.GeneticBettas.util;

import java.awt.*;

public class ColorUtil {
    public static float[] getHSBFromHex(String colourHex) {
        if (colourHex.length() == 7) {
            colourHex = colourHex.substring(1, 7);
        }
        int[] color =
                {
                        Integer.valueOf(colourHex.substring(0, 2), 16),
                        Integer.valueOf(colourHex.substring(2, 4), 16),
                        Integer.valueOf(colourHex.substring(4, 6), 16)
                };
        return Color.RGBtoHSB(color[0], color[1], color[2], (float[]) null);
    }

    public static int getARGBFromHex(String colourHex) {
        if (colourHex.length() == 7) {
            colourHex = colourHex.substring(1, 7);
        }
        int[] color =
                {
                        Integer.valueOf(colourHex.substring(0, 2), 16),
                        Integer.valueOf(colourHex.substring(2, 4), 16),
                        Integer.valueOf(colourHex.substring(4, 6), 16)
                };
        return Integer.MIN_VALUE | Math.min(color[0], 255) << 16 | Math.min(color[1], 255) << 8 | Math.min(color[2], 255);
    }

    public static float[] clampRGB(float[] color) {
        float[] clampedColor = {color[0], color[1], color[2]};
        for (int i = 0; i <= 2; i++) {
            if (clampedColor[i] > 1.0F) {
                clampedColor[i] = 1.0F;
            } else if (clampedColor[i] < 0.0F) {
                clampedColor[i] = 0.0F;
            }
        }
        return clampedColor;
    }

}
