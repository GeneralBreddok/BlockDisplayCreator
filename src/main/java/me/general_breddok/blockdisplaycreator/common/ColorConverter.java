package me.general_breddok.blockdisplaycreator.common;

import lombok.experimental.UtilityClass;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ColorConverter {

    public static final Map<String, Color> NAMED_COLORS = new HashMap<>();

    static {
        NAMED_COLORS.putAll(
                Map.of(
                        "BLACK", Color.BLACK,
                        "WHITE", Color.WHITE,
                        "RED", Color.RED,
                        "GREEN", Color.GREEN,
                        "BLUE", Color.BLUE,
                        "YELLOW", Color.YELLOW,
                        "CYAN", Color.AQUA,
                        "MAGENTA", Color.FUCHSIA,
                        "GRAY", Color.GRAY,
                        "ORANGE", Color.ORANGE
                )
        );
        NAMED_COLORS.putAll(
                Map.of(
                        "SILVER", Color.SILVER,
                        "PINK", Color.fromRGB(255, 192, 203),
                        "PURPLE", Color.PURPLE,
                        "BROWN", Color.fromRGB(165, 42, 42),
                        "LIME", Color.LIME,
                        "NAVY", Color.NAVY,
                        "MAROON", Color.MAROON,
                        "OLIVE", Color.OLIVE,
                        "AQUA", Color.AQUA,
                        "TEAL", Color.TEAL
                )
        );
        NAMED_COLORS.put("FUCHSIA", Color.FUCHSIA);
    }

    public Color fromDecimalRGB(int decimalColor) {
        int red = (decimalColor >> 16) & 0xFF;
        int green = (decimalColor >> 8) & 0xFF;
        int blue = decimalColor & 0xFF;

        return Color.fromRGB(red, green, blue);
    }

    public Color fromDecimalABGR(int decimalColor) {
        //int alpha = (decimalColor >> 24) & 0xFF;
        int blue = (decimalColor >> 16) & 0xFF;
        int green = (decimalColor >> 8) & 0xFF;
        int red = decimalColor & 0xFF;

        return Color.fromRGB(red, green, blue);
    }

    public int toDecimalABGR(Color color) {
        int blue = color.getBlue();
        int green = color.getGreen();
        int red = color.getRed();
        int alpha = color.getAlpha();

        int abgr = (alpha << 24) | (blue << 16) | (green << 8) | red;

        return abgr & 0xFFFFFF;
    }

    public static int toOppositeDecimalABGR(Color color) {
        int red = 255 - color.getRed();
        int green = color.getGreen();
        int blue = 255 - color.getBlue();
        int alpha = color.getAlpha();

        int abgr = (alpha << 24) | (blue << 16) | (green << 8) | red;

        return abgr & 0xFFFFFF;
    }

    public int toDecimalRGB(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        return (red << 16) | (green << 8) | blue;
    }

    public Color fromHex(String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        } else if (hexColor.startsWith("0x")) {
            hexColor = hexColor.substring(2);
        }

        if (hexColor.length() != 6) {
            throw new IllegalArgumentException("Invalid HEX color format. Expected 6 characters.");
        }

        try {
            int decimalColor = Integer.parseInt(hexColor, 16);

            return fromDecimalRGB(decimalColor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid HEX color format. Contains non-hexadecimal characters.", e);
        }
    }

    public String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public boolean isHex(String hex) {
        return hex.matches("^#?[0-9A-Fa-f]{6}$") || hex.matches("^0x[0-9A-Fa-f]{6}$");
    }

    public float[] toHSL(Color color) {
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h, s, l = (max + min) / 2;

        if (max == min) {
            h = s = 0; // Achromatic
        } else {
            float d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

            if (max == r) {
                h = (g - b) / d + (g < b ? 6 : 0);
            } else if (max == g) {
                h = (b - r) / d + 2;
            } else {
                h = (r - g) / d + 4;
            }
            h /= 6;
        }

        return new float[]{h * 360, s * 100, l * 100};
    }

    public float[] toHSV(Color color) {
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h, s, v = max;

        float d = max - min;
        s = max == 0 ? 0 : d / max;

        if (max == min) {
            h = 0; // Achromatic
        } else {
            if (max == r) {
                h = (g - b) / d + (g < b ? 6 : 0);
            } else if (max == g) {
                h = (b - r) / d + 2;
            } else {
                h = (r - g) / d + 4;
            }
            h /= 6;
        }
        return new float[]{h * 360, s * 100, v * 100};
    }

    public Color fromHSL(float h, float s, float l) {
        h = h / 360.0f;
        s = s / 100.0f;
        l = l / 100.0f;

        float r, g, b;

        if (s == 0) {
            r = g = b = l; // Achromatic
        } else {
            float q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRGB(p, q, h + 1.0f / 3.0f);
            g = hueToRGB(p, q, h);
            b = hueToRGB(p, q, h - 1.0f / 3.0f);
        }

        return Color.fromRGB((int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    private static float hueToRGB(float p, float q, float t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1.0f / 6.0f) return p + (q - p) * 6 * t;
        if (t < 1.0f / 2.0f) return q;
        if (t < 2.0f / 3.0f) return p + (q - p) * (2.0f / 3.0f - t) * 6;
        return p;
    }

    public Color fromHSV(float h, float s, float v) {
        h = h / 360.0f;
        s = s / 100.0f;
        v = v / 100.0f;

        int i = (int) (h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        float r, g, b;
        switch (i % 6) {
            case 0 -> { r = v; g = t; b = p; }
            case 1 -> { r = q; g = v; b = p; }
            case 2 -> { r = p; g = v; b = t; }
            case 3 -> { r = p; g = q; b = v; }
            case 4 -> { r = t; g = p; b = v; }
            case 5 -> { r = v; g = p; b = q; }
            default -> throw new IllegalArgumentException("Unexpected case in HSV to RGB conversion.");
        }

        return Color.fromRGB((int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    public static Color invertColor(Color color) {
        int red = 255 - color.getRed();
        int green = 255 - color.getGreen();
        int blue = 255 - color.getBlue();

        return Color.fromRGB(red, green, blue);
    }

    public Color fromAwtColor(java.awt.Color color) {
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    public java.awt.Color toAwtColor(Color color) {
        return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
    }

    public Color fromName(String name) {
        return NAMED_COLORS.get(name.toUpperCase());
    }

    public boolean isNamedColor(String name) {
        return NAMED_COLORS.containsKey(name.toUpperCase());
    }
}
