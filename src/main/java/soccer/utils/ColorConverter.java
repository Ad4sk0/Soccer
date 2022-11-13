package soccer.utils;

import java.awt.*;

public class ColorConverter {

    private ColorConverter() {
    }

    public static Color convertHexStringToColor(String colorString) {
        String redHex = colorString.substring(1, 3);
        String greenHex = colorString.substring(3, 5);
        String blueHex = colorString.substring(5, 7);
        int red = Integer.parseInt(redHex, 16);
        int green = Integer.parseInt(greenHex, 16);
        int blue = Integer.parseInt(blueHex, 16);
        return new Color(red, green, blue);
    }

    public static String convertColorToHexString(Color color) {
        String redHex = Integer.toHexString(color.getRed());
        String greenHex = Integer.toHexString(color.getGreen());
        String blueHex = Integer.toHexString(color.getBlue());

        if (redHex.length() == 1) {
            redHex = "0" + redHex;
        }

        if (greenHex.length() == 1) {
            greenHex = "0" + greenHex;
        }

        if (blueHex.length() == 1) {
            blueHex = "0" + blueHex;
        }

        return "#" + redHex + greenHex + blueHex;
    }

}
