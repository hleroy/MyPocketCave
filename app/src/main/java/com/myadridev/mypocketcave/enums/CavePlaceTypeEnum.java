package com.myadridev.mypocketcave.enums;

import com.myadridev.mypocketcave.R;

public enum CavePlaceTypeEnum {
    NO_PLACE(0, R.drawable.white, PositionEnum.NO, WineColorEnum.ANY),
    PLACE_BOTTOM_RIGHT(1, R.drawable.place_type_bottom_right, PositionEnum.BOTTOM_RIGHT, WineColorEnum.ANY),
    PLACE_BOTTOM_LEFT(2, R.drawable.place_type_bottom_left, PositionEnum.BOTTOM_LEFT, WineColorEnum.ANY),
    PLACE_TOP_RIGHT(3, R.drawable.place_type_top_right, PositionEnum.TOP_RIGHT, WineColorEnum.ANY),
    PLACE_TOP_LEFT(4, R.drawable.place_type_top_left, PositionEnum.TOP_LEFT, WineColorEnum.ANY),
    PLACE_BOTTOM_RIGHT_RED(11, R.drawable.place_type_bottom_right_red, PositionEnum.BOTTOM_RIGHT, WineColorEnum.RED),
    PLACE_BOTTOM_LEFT_RED(12, R.drawable.place_type_bottom_left_red, PositionEnum.BOTTOM_LEFT, WineColorEnum.RED),
    PLACE_TOP_RIGHT_RED(13, R.drawable.place_type_top_right_red, PositionEnum.TOP_RIGHT, WineColorEnum.RED),
    PLACE_TOP_LEFT_RED(14, R.drawable.place_type_top_left_red, PositionEnum.TOP_LEFT, WineColorEnum.RED),
    PLACE_BOTTOM_RIGHT_WHITE(21, R.drawable.place_type_bottom_right_white, PositionEnum.BOTTOM_RIGHT, WineColorEnum.WHITE),
    PLACE_BOTTOM_LEFT_WHITE(22, R.drawable.place_type_bottom_left_white, PositionEnum.BOTTOM_LEFT, WineColorEnum.WHITE),
    PLACE_TOP_RIGHT_WHITE(23, R.drawable.place_type_top_right_white, PositionEnum.TOP_RIGHT, WineColorEnum.WHITE),
    PLACE_TOP_LEFT_WHITE(24, R.drawable.place_type_top_left_white, PositionEnum.TOP_LEFT, WineColorEnum.WHITE),
    PLACE_BOTTOM_RIGHT_ROSE(31, R.drawable.place_type_bottom_right_rose, PositionEnum.BOTTOM_RIGHT, WineColorEnum.ROSE),
    PLACE_BOTTOM_LEFT_ROSE(32, R.drawable.place_type_bottom_left_rose, PositionEnum.BOTTOM_LEFT, WineColorEnum.ROSE),
    PLACE_TOP_RIGHT_ROSE(33, R.drawable.place_type_top_right_rose, PositionEnum.TOP_RIGHT, WineColorEnum.ROSE),
    PLACE_TOP_LEFT_ROSE(34, R.drawable.place_type_top_left_rose, PositionEnum.TOP_LEFT, WineColorEnum.ROSE),
    PLACE_BOTTOM_RIGHT_CHAMPAGNE(41, R.drawable.place_type_bottom_right_champagne, PositionEnum.BOTTOM_RIGHT, WineColorEnum.CHAMPAGNE),
    PLACE_BOTTOM_LEFT_CHAMPAGNE(42, R.drawable.place_type_bottom_left_champagne, PositionEnum.BOTTOM_LEFT, WineColorEnum.CHAMPAGNE),
    PLACE_TOP_RIGHT_CHAMPAGNE(43, R.drawable.place_type_top_right_champagne, PositionEnum.TOP_RIGHT, WineColorEnum.CHAMPAGNE),
    PLACE_TOP_LEFT_CHAMPAGNE(44, R.drawable.place_type_top_left_champagne, PositionEnum.TOP_LEFT, WineColorEnum.CHAMPAGNE),
    PLACE_BOTTOM_RIGHT_WHISKY(51, R.drawable.place_type_bottom_right_whisky, PositionEnum.BOTTOM_RIGHT, WineColorEnum.WHISKY),
    PLACE_BOTTOM_LEFT_WHISKY(52, R.drawable.place_type_bottom_left_whisky, PositionEnum.BOTTOM_LEFT, WineColorEnum.WHISKY),
    PLACE_TOP_RIGHT_WHISKY(53, R.drawable.place_type_top_right_whisky, PositionEnum.TOP_RIGHT, WineColorEnum.WHISKY),
    PLACE_TOP_LEFT_WHISKY(54, R.drawable.place_type_top_left_whisky, PositionEnum.TOP_LEFT, WineColorEnum.WHISKY),
    PLACE_BOTTOM_RIGHT_RUM(61, R.drawable.place_type_bottom_right_rum, PositionEnum.BOTTOM_RIGHT, WineColorEnum.RUM),
    PLACE_BOTTOM_LEFT_RUM(62, R.drawable.place_type_bottom_left_rum, PositionEnum.BOTTOM_LEFT, WineColorEnum.RUM),
    PLACE_TOP_RIGHT_RUM(63, R.drawable.place_type_top_right_rum, PositionEnum.TOP_RIGHT, WineColorEnum.RUM),
    PLACE_TOP_LEFT_RUM(64, R.drawable.place_type_top_left_rum, PositionEnum.TOP_LEFT, WineColorEnum.RUM),
    PLACE_BOTTOM_RIGHT_VODKA(71, R.drawable.place_type_bottom_right_vodka, PositionEnum.BOTTOM_RIGHT, WineColorEnum.VODKA),
    PLACE_BOTTOM_LEFT_VODKA(72, R.drawable.place_type_bottom_left_vodka, PositionEnum.BOTTOM_LEFT, WineColorEnum.VODKA),
    PLACE_TOP_RIGHT_VODKA(73, R.drawable.place_type_top_right_vodka, PositionEnum.TOP_RIGHT, WineColorEnum.VODKA),
    PLACE_TOP_LEFT_VODKA(74, R.drawable.place_type_top_left_vodka, PositionEnum.TOP_LEFT, WineColorEnum.VODKA),
    PLACE_BOTTOM_RIGHT_APERITIF(81, R.drawable.place_type_bottom_right_aperitif, PositionEnum.BOTTOM_RIGHT, WineColorEnum.APERITIF),
    PLACE_BOTTOM_LEFT_APERITIF(82, R.drawable.place_type_bottom_left_aperitif, PositionEnum.BOTTOM_LEFT, WineColorEnum.APERITIF),
    PLACE_TOP_RIGHT_APERITIF(83, R.drawable.place_type_top_right_aperitif, PositionEnum.TOP_RIGHT, WineColorEnum.APERITIF),
    PLACE_TOP_LEFT_APERITIF(84, R.drawable.place_type_top_left_aperitif, PositionEnum.TOP_LEFT, WineColorEnum.APERITIF),
    PLACE_BOTTOM_RIGHT_LIQUEUR(91, R.drawable.place_type_bottom_right_liqueur, PositionEnum.BOTTOM_RIGHT, WineColorEnum.LIQUEUR),
    PLACE_BOTTOM_LEFT_LIQUEUR(92, R.drawable.place_type_bottom_left_liqueur, PositionEnum.BOTTOM_LEFT, WineColorEnum.LIQUEUR),
    PLACE_TOP_RIGHT_LIQUEUR(93, R.drawable.place_type_top_right_liqueur, PositionEnum.TOP_RIGHT, WineColorEnum.LIQUEUR),
    PLACE_TOP_LEFT_LIQUEUR(94, R.drawable.place_type_top_left_liqueur, PositionEnum.TOP_LEFT, WineColorEnum.LIQUEUR),
    PLACE_BOTTOM_RIGHT_BEER(101, R.drawable.place_type_bottom_right_beer, PositionEnum.BOTTOM_RIGHT, WineColorEnum.BEER),
    PLACE_BOTTOM_LEFT_BEER(102, R.drawable.place_type_bottom_left_beer, PositionEnum.BOTTOM_LEFT, WineColorEnum.BEER),
    PLACE_TOP_RIGHT_BEER(103, R.drawable.place_type_top_right_beer, PositionEnum.TOP_RIGHT, WineColorEnum.BEER),
    PLACE_TOP_LEFT_BEER(104, R.drawable.place_type_top_left_beer, PositionEnum.TOP_LEFT, WineColorEnum.BEER),
    PLACE_BOTTOM_RIGHT_CIDER(111, R.drawable.place_type_bottom_right_cider, PositionEnum.BOTTOM_RIGHT, WineColorEnum.CIDER),
    PLACE_BOTTOM_LEFT_CIDER(112, R.drawable.place_type_bottom_left_cider, PositionEnum.BOTTOM_LEFT, WineColorEnum.CIDER),
    PLACE_TOP_RIGHT_CIDER(113, R.drawable.place_type_top_right_cider, PositionEnum.TOP_RIGHT, WineColorEnum.CIDER),
    PLACE_TOP_LEFT_CIDER(114, R.drawable.place_type_top_left_cider, PositionEnum.TOP_LEFT, WineColorEnum.CIDER),
    PLACE_BOTTOM_RIGHT_SOFT(121, R.drawable.place_type_bottom_right_soft, PositionEnum.BOTTOM_RIGHT, WineColorEnum.SOFT),
    PLACE_BOTTOM_LEFT_SOFT(122, R.drawable.place_type_bottom_left_soft, PositionEnum.BOTTOM_LEFT, WineColorEnum.SOFT),
    PLACE_TOP_RIGHT_SOFT(123, R.drawable.place_type_top_right_soft, PositionEnum.TOP_RIGHT, WineColorEnum.SOFT),
    PLACE_TOP_LEFT_SOFT(124, R.drawable.place_type_top_left_soft, PositionEnum.TOP_LEFT, WineColorEnum.SOFT),
    PLACE_BOTTOM_RIGHT_OTHER(131, R.drawable.place_type_bottom_right_other, PositionEnum.BOTTOM_RIGHT, WineColorEnum.OTHER),
    PLACE_BOTTOM_LEFT_OTHER(132, R.drawable.place_type_bottom_left_other, PositionEnum.BOTTOM_LEFT, WineColorEnum.OTHER),
    PLACE_TOP_RIGHT_OTHER(133, R.drawable.place_type_top_right_other, PositionEnum.TOP_RIGHT, WineColorEnum.OTHER),
    PLACE_TOP_LEFT_OTHER(134, R.drawable.place_type_top_left_other, PositionEnum.TOP_LEFT, WineColorEnum.OTHER);

    public final int Id;
    public final int DrawableResourceId;
    public final PositionEnum Position;
    public final WineColorEnum Color;

    CavePlaceTypeEnum(int id, int drawableResourceId, PositionEnum position, WineColorEnum color) {
        Id = id;
        DrawableResourceId = drawableResourceId;
        this.Position = position;
        this.Color = color;
    }

    public static CavePlaceTypeEnum getById(int id) {
        for (CavePlaceTypeEnum cavePlaceType : CavePlaceTypeEnum.values()) {
            if (cavePlaceType.Id == id) {
                return cavePlaceType;
            }
        }
        return null;
    }

    public static CavePlaceTypeEnum getByPositionAndColor(PositionEnum position, WineColorEnum color) {
        for (CavePlaceTypeEnum cavePlaceType : CavePlaceTypeEnum.values()) {
            if (cavePlaceType.Position == position && cavePlaceType.Color == color) {
                return cavePlaceType;
            }
        }
        return null;
    }

    public static CavePlaceTypeEnum getEmptyByPosition(PositionEnum position) {
        return getByPositionAndColor(position, WineColorEnum.ANY);
    }
}
