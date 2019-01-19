package com.example.gruppeb.madbestillingsapp.Helper;

import com.google.gson.annotations.SerializedName;

public class DishJSON {
    //Day menu
    @SerializedName("idDayMenu")
    public int idDayMenu;

    @SerializedName("daNameLangDA")
    public String daNameLangDA;

    @SerializedName("daDescriptionLangDA")
    public String daDescriptionLangDA;

    @SerializedName("daCommentLangDA")
    public String daCommentLangDA;

    @SerializedName("daNameLangEN")
    public String daNameLangEN;

    @SerializedName("daDescriptionLangEN")
    public String daDescriptionLangEN;

    @SerializedName("daCommentLangEN")
    public String daCommentLangEN;

    @SerializedName("daNameLangAR")
    public String daNameLangAR;

    @SerializedName("daDescriptionLangAR")
    public String daDescriptionLangAR;

    @SerializedName("daCommentLangAR")
    public String daCommentLangAR;

    @SerializedName("daMenuImage")
    public String daMenuImage;
}
