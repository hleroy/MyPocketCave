package com.myadridev.mypocketcave.models.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.MillesimeEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;

import java.util.ArrayList;
import java.util.List;

public class SuggestBottleCriteriaV2 {

    @Expose(serialize = false, deserialize = false)
    public static final int NumberOfCriteria = 8;

    @SerializedName("wc")
    public WineColorEnumV2 WineColor;
    @SerializedName("iwcr")
    public boolean IsWineColorRequired;
    @SerializedName("d")
    public String Domain;
    @SerializedName("idr")
    public boolean IsDomainRequired;
    @SerializedName("m")
    public MillesimeEnumV2 Millesime;
    @SerializedName("imr")
    public boolean IsMillesimeRequired;
    @SerializedName("rmi")
    public int RatingMinValue;
    @SerializedName("rma")
    public int RatingMaxValue;
    @SerializedName("irr")
    public boolean IsRatingRequired;
    @SerializedName("prmi")
    public int PriceRatingMinValue;
    @SerializedName("prma")
    public int PriceRatingMaxValue;
    @SerializedName("iprr")
    public boolean IsPriceRatingRequired;
    @SerializedName("f")
    public final List<FoodToEatWithEnumV2> FoodToEatWithList;
    @SerializedName("ifr")
    public boolean IsFoodRequired;
    @SerializedName("p")
    public String PersonToShareWith;
    @SerializedName("ipr")
    public boolean IsPersonRequired;
    @SerializedName("c")
    public CaveLightModelV2 Cave;
    @SerializedName("icr")
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
