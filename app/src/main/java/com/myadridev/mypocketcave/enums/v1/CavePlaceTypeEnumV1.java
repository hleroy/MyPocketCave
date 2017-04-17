package com.myadridev.mypocketcave.enums.v1;

import com.myadridev.mypocketcave.R;

@Deprecated
public enum CavePlaceTypeEnumV1 {
    NO_PLACE(0, R.drawable.white, PositionEnumV1.NO, WineColorEnumV1.ANY),
    PLACE_BOTTOM_RIGHT(1, R.drawable.place_type_bottom_right, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.ANY),
    PLACE_BOTTOM_LEFT(2, R.drawable.place_type_bottom_left, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.ANY),
    PLACE_TOP_RIGHT(3, R.drawable.place_type_top_right, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.ANY),
    PLACE_TOP_LEFT(4, R.drawable.place_type_top_left, PositionEnumV1.TOP_LEFT, WineColorEnumV1.ANY),
    PLACE_BOTTOM_RIGHT_RED(11, R.drawable.place_type_bottom_right_red, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.RED),
    PLACE_BOTTOM_LEFT_RED(12, R.drawable.place_type_bottom_left_red, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.RED),
    PLACE_TOP_RIGHT_RED(13, R.drawable.place_type_top_right_red, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.RED),
    PLACE_TOP_LEFT_RED(14, R.drawable.place_type_top_left_red, PositionEnumV1.TOP_LEFT, WineColorEnumV1.RED),
    PLACE_BOTTOM_RIGHT_WHITE(21, R.drawable.place_type_bottom_right_white, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.WHITE),
    PLACE_BOTTOM_LEFT_WHITE(22, R.drawable.place_type_bottom_left_white, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.WHITE),
    PLACE_TOP_RIGHT_WHITE(23, R.drawable.place_type_top_right_white, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.WHITE),
    PLACE_TOP_LEFT_WHITE(24, R.drawable.place_type_top_left_white, PositionEnumV1.TOP_LEFT, WineColorEnumV1.WHITE),
    PLACE_BOTTOM_RIGHT_ROSE(31, R.drawable.place_type_bottom_right_rose, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.ROSE),
    PLACE_BOTTOM_LEFT_ROSE(32, R.drawable.place_type_bottom_left_rose, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.ROSE),
    PLACE_TOP_RIGHT_ROSE(33, R.drawable.place_type_top_right_rose, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.ROSE),
    PLACE_TOP_LEFT_ROSE(34, R.drawable.place_type_top_left_rose, PositionEnumV1.TOP_LEFT, WineColorEnumV1.ROSE),
    PLACE_BOTTOM_RIGHT_CHAMPAGNE(41, R.drawable.place_type_bottom_right_champagne, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.CHAMPAGNE),
    PLACE_BOTTOM_LEFT_CHAMPAGNE(42, R.drawable.place_type_bottom_left_champagne, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.CHAMPAGNE),
    PLACE_TOP_RIGHT_CHAMPAGNE(43, R.drawable.place_type_top_right_champagne, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.CHAMPAGNE),
    PLACE_TOP_LEFT_CHAMPAGNE(44, R.drawable.place_type_top_left_champagne, PositionEnumV1.TOP_LEFT, WineColorEnumV1.CHAMPAGNE),
    PLACE_BOTTOM_RIGHT_WHISKY(51, R.drawable.place_type_bottom_right_whisky, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.WHISKY),
    PLACE_BOTTOM_LEFT_WHISKY(52, R.drawable.place_type_bottom_left_whisky, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.WHISKY),
    PLACE_TOP_RIGHT_WHISKY(53, R.drawable.place_type_top_right_whisky, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.WHISKY),
    PLACE_TOP_LEFT_WHISKY(54, R.drawable.place_type_top_left_whisky, PositionEnumV1.TOP_LEFT, WineColorEnumV1.WHISKY),
    PLACE_BOTTOM_RIGHT_RUM(61, R.drawable.place_type_bottom_right_rum, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.RUM),
    PLACE_BOTTOM_LEFT_RUM(62, R.drawable.place_type_bottom_left_rum, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.RUM),
    PLACE_TOP_RIGHT_RUM(63, R.drawable.place_type_top_right_rum, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.RUM),
    PLACE_TOP_LEFT_RUM(64, R.drawable.place_type_top_left_rum, PositionEnumV1.TOP_LEFT, WineColorEnumV1.RUM),
    PLACE_BOTTOM_RIGHT_VODKA(71, R.drawable.place_type_bottom_right_vodka, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.VODKA),
    PLACE_BOTTOM_LEFT_VODKA(72, R.drawable.place_type_bottom_left_vodka, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.VODKA),
    PLACE_TOP_RIGHT_VODKA(73, R.drawable.place_type_top_right_vodka, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.VODKA),
    PLACE_TOP_LEFT_VODKA(74, R.drawable.place_type_top_left_vodka, PositionEnumV1.TOP_LEFT, WineColorEnumV1.VODKA),
    PLACE_BOTTOM_RIGHT_APERITIF(81, R.drawable.place_type_bottom_right_aperitif, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.APERITIF),
    PLACE_BOTTOM_LEFT_APERITIF(82, R.drawable.place_type_bottom_left_aperitif, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.APERITIF),
    PLACE_TOP_RIGHT_APERITIF(83, R.drawable.place_type_top_right_aperitif, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.APERITIF),
    PLACE_TOP_LEFT_APERITIF(84, R.drawable.place_type_top_left_aperitif, PositionEnumV1.TOP_LEFT, WineColorEnumV1.APERITIF),
    PLACE_BOTTOM_RIGHT_LIQUEUR(91, R.drawable.place_type_bottom_right_liqueur, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.LIQUEUR),
    PLACE_BOTTOM_LEFT_LIQUEUR(92, R.drawable.place_type_bottom_left_liqueur, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.LIQUEUR),
    PLACE_TOP_RIGHT_LIQUEUR(93, R.drawable.place_type_top_right_liqueur, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.LIQUEUR),
    PLACE_TOP_LEFT_LIQUEUR(94, R.drawable.place_type_top_left_liqueur, PositionEnumV1.TOP_LEFT, WineColorEnumV1.LIQUEUR),
    PLACE_BOTTOM_RIGHT_BEER(101, R.drawable.place_type_bottom_right_beer, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.BEER),
    PLACE_BOTTOM_LEFT_BEER(102, R.drawable.place_type_bottom_left_beer, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.BEER),
    PLACE_TOP_RIGHT_BEER(103, R.drawable.place_type_top_right_beer, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.BEER),
    PLACE_TOP_LEFT_BEER(104, R.drawable.place_type_top_left_beer, PositionEnumV1.TOP_LEFT, WineColorEnumV1.BEER),
    PLACE_BOTTOM_RIGHT_CIDER(111, R.drawable.place_type_bottom_right_cider, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.CIDER),
    PLACE_BOTTOM_LEFT_CIDER(112, R.drawable.place_type_bottom_left_cider, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.CIDER),
    PLACE_TOP_RIGHT_CIDER(113, R.drawable.place_type_top_right_cider, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.CIDER),
    PLACE_TOP_LEFT_CIDER(114, R.drawable.place_type_top_left_cider, PositionEnumV1.TOP_LEFT, WineColorEnumV1.CIDER),
    PLACE_BOTTOM_RIGHT_SOFT(121, R.drawable.place_type_bottom_right_soft, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.SOFT),
    PLACE_BOTTOM_LEFT_SOFT(122, R.drawable.place_type_bottom_left_soft, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.SOFT),
    PLACE_TOP_RIGHT_SOFT(123, R.drawable.place_type_top_right_soft, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.SOFT),
    PLACE_TOP_LEFT_SOFT(124, R.drawable.place_type_top_left_soft, PositionEnumV1.TOP_LEFT, WineColorEnumV1.SOFT),
    PLACE_BOTTOM_RIGHT_OTHER(131, R.drawable.place_type_bottom_right_other, PositionEnumV1.BOTTOM_RIGHT, WineColorEnumV1.OTHER),
    PLACE_BOTTOM_LEFT_OTHER(132, R.drawable.place_type_bottom_left_other, PositionEnumV1.BOTTOM_LEFT, WineColorEnumV1.OTHER),
    PLACE_TOP_RIGHT_OTHER(133, R.drawable.place_type_top_right_other, PositionEnumV1.TOP_RIGHT, WineColorEnumV1.OTHER),
    PLACE_TOP_LEFT_OTHER(134, R.drawable.place_type_top_left_other, PositionEnumV1.TOP_LEFT, WineColorEnumV1.OTHER);

    public final int Id;
    public final int DrawableResourceId;
    public final PositionEnumV1 Position;
    public final WineColorEnumV1 Color;

    CavePlaceTypeEnumV1(int id, int drawableResourceId, PositionEnumV1 position, WineColorEnumV1 color) {
        Id = id;
        DrawableResourceId = drawableResourceId;
        this.Position = position;
        this.Color = color;
    }

    public static CavePlaceTypeEnumV1 getById(int id) {
        for (CavePlaceTypeEnumV1 cavePlaceType : CavePlaceTypeEnumV1.values()) {
            if (cavePlaceType.Id == id) {
                return cavePlaceType;
            }
        }
        return null;
    }

    public static CavePlaceTypeEnumV1 getByPositionAndColor(PositionEnumV1 position, WineColorEnumV1 color) {
        for (CavePlaceTypeEnumV1 cavePlaceType : CavePlaceTypeEnumV1.values()) {
            if (cavePlaceType.Position == position && cavePlaceType.Color == color) {
                return cavePlaceType;
            }
        }
        return null;
    }

    public static CavePlaceTypeEnumV1 getEmptyByPosition(PositionEnumV1 position) {
        return getByPositionAndColor(position, WineColorEnumV1.ANY);
    }
}
