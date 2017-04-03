package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.MillesimeEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = SuggestBottleCriteriaV2.class)
public class SuggestBottleCriteriaV2 {

    @JsonIgnore
    public static int NumberOfCriteria = 8;

    public WineColorEnum WineColor;
    public boolean IsWineColorRequired;
    public String Domain;
    public boolean IsDomainRequired;
    public MillesimeEnum Millesime;
    public boolean IsMillesimeRequired;
    public int RatingMinValue;
    public int RatingMaxValue;
    public boolean IsRatingRequired;
    public int PriceRatingMinValue;
    public int PriceRatingMaxValue;
    public boolean IsPriceRatingRequired;
    public final List<FoodToEatWithEnum> FoodToEatWithList;
    public boolean IsFoodRequired;
    public String PersonToShareWith;
    public boolean IsPersonRequired;
    public CaveLightModelV2 Cave;
    public boolean IsCaveRequired;

    public SuggestBottleCriteriaV2() {
        WineColor = WineColorEnum.ANY;
        IsWineColorRequired = false;
        Domain = "";
        IsDomainRequired = false;
        Millesime = MillesimeEnum.ANY;
        IsMillesimeRequired = false;
        RatingMinValue = 0;
        RatingMaxValue = 0;
        IsRatingRequired = false;
        PriceRatingMinValue = 0;
        PriceRatingMaxValue = 0;
        IsPriceRatingRequired = false;
        FoodToEatWithList = new ArrayList<>();
        IsFoodRequired = false;
        PersonToShareWith = "";
        IsPersonRequired = false;
        Cave = null;
        IsWineColorRequired = false;
    }
}
