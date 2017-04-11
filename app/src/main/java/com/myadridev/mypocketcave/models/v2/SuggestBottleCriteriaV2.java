package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.MillesimeEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = SuggestBottleCriteriaV2.class)
public class SuggestBottleCriteriaV2 {

    @JsonIgnore
    public static int NumberOfCriteria = 8;

    @JsonProperty("wc")
    public WineColorEnumV2 WineColor;
    @JsonProperty("iwcr")
    public boolean IsWineColorRequired;
    @JsonProperty("d")
    public String Domain;
    @JsonProperty("idr")
    public boolean IsDomainRequired;
    @JsonProperty("m")
    public MillesimeEnumV2 Millesime;
    @JsonProperty("imr")
    public boolean IsMillesimeRequired;
    @JsonProperty("rmi")
    public int RatingMinValue;
    @JsonProperty("rma")
    public int RatingMaxValue;
    @JsonProperty("irr")
    public boolean IsRatingRequired;
    @JsonProperty("prmi")
    public int PriceRatingMinValue;
    @JsonProperty("prma")
    public int PriceRatingMaxValue;
    @JsonProperty("iprr")
    public boolean IsPriceRatingRequired;
    @JsonProperty("f")
    public final List<FoodToEatWithEnumV2> FoodToEatWithList;
    @JsonProperty("ifr")
    public boolean IsFoodRequired;
    @JsonProperty("p")
    public String PersonToShareWith;
    @JsonProperty("ipr")
    public boolean IsPersonRequired;
    @JsonProperty("c")
    public CaveLightModelV2 Cave;
    @JsonProperty("icr")
    public boolean IsCaveRequired;

    public SuggestBottleCriteriaV2() {
        WineColor = WineColorEnumV2.a;
        IsWineColorRequired = false;
        Domain = "";
        IsDomainRequired = false;
        Millesime = MillesimeEnumV2.a;
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
