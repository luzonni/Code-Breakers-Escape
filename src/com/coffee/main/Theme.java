package com.coffee.main;

import java.awt.*;

public class Theme {

    public static final int PRIMARY = 0xffffffff;
    public static final int SECONDARY = 0xffcccccc;
    public static final int TERTIARY = 0xff000000;

    public final static Color[][] PALLET = {
                {new Color(180, 180, 180), new Color(80, 80, 80), new Color(0, 0, 0)},
                {new Color(233, 212, 165), new Color(127, 121, 99), new Color(26, 23, 18)},
                {new Color(248, 227, 86), new Color(180, 82, 48), new Color(54, 10, 61)},
                {new Color(117, 234, 241), new Color(58, 100, 150), new Color(40, 8, 75)},
                {new Color(149, 230, 75), new Color(50, 99, 116), new Color(32, 12, 47)},
                {new Color(226, 252, 165), new Color(52, 100, 80), new Color(6, 8, 16)},
                {new Color(249, 247, 196), new Color(120, 100, 125), new Color(6, 10, 48)},
                {new Color(141, 169, 144), new Color(94, 93, 91), new Color(27, 8, 27)},
                {new Color(40, 40, 160), new Color(50, 30, 100), new Color(0, 0, 0)}
            };

    public static Color Color_Tertiary = PALLET[1][2];
    public static Color Color_Secondary = PALLET[1][1];
    public static Color Color_Primary = PALLET[1][0];
    public static volatile int INDEX_PALLET = 1;

    public static void SET_PALLET() {
        Color_Primary = PALLET[INDEX_PALLET][0];
        Color_Secondary = PALLET[INDEX_PALLET][1];
        Color_Tertiary = PALLET[INDEX_PALLET][2];
    }
}
