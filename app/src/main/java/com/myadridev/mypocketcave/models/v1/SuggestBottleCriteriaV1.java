package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v1.FoodToEatWithEnumV1;
import com.myadridev.mypocketcave.enums.v1.MillesimeEnumV1;
import com.myadridev.mypocketcave.enums.v1.WineColorEnumV1;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@JsonSerialize(as = SuggestBottleCriteriaV1.class)
public class SuggestBottleCriteriaV1 {

    @JsonIgnore
    public static int NumberOfCriteria = 8;
    public final List<FoodToEatWithEnumV1> FoodToEatWithList;
    public WineColorEnumV1 WineColor;
    public boolean IsWineColorRequired;
    public String Domain;
    public boolean IsDomainRequired;
    public MillesimeEnumV1 Millesime;
    public boolean IsMillesimeRequired;
    public int RatingMinValue;
    public int RatingMaxValue;
    public boolean IsRatingRequired;
    public int PriceRatingMinValue;
    public int PriceRatingMaxValue;
    public boolean IsPriceRatingRequired;
    public boolean IsFoodRequired;
    public String PersonToShareWith;
    public boolean IsPersonRequired;
    public CaveLightModelV1 Cave;
    public boolean IsCaveRequired;

    public SuggestBottleCriteriaV1() {
        WineColor = WineColorEnumV1.ANY;
        IsWineColorRequired = false;
        Domain = "";
        IsDomainRequired = false;
        Millesime = MillesimeEnumV1.ANY;
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
