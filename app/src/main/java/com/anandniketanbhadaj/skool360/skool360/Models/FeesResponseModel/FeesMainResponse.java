package com.anandniketanbhadaj.skool360.skool360.Models.FeesResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeesMainResponse {
    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("Term1Btn")
    @Expose
    private Boolean term1Btn;
    @SerializedName("Term2Btn")
    @Expose
    private Boolean term2Btn;
    @SerializedName("Term1URL")
    @Expose
    private String term1URL;
    @SerializedName("Term2URL")
    @Expose
    private String term2URL;
    @SerializedName("TermTotal")
    @Expose
    private String termTotal;
    @SerializedName("TermPaid")
    @Expose
    private String termPaid;
    @SerializedName("TermDuePay")
    @Expose
    private String termDuePay;
    @SerializedName("TermDiscount")
    @Expose
    private String termDiscount;
    @SerializedName("FinalArray")
    @Expose
    private List<FeesFinalResponse> finalArray = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Boolean getTerm1Btn() {
        return term1Btn;
    }

    public void setTerm1Btn(Boolean term1Btn) {
        this.term1Btn = term1Btn;
    }

    public Boolean getTerm2Btn() {
        return term2Btn;
    }

    public void setTerm2Btn(Boolean term2Btn) {
        this.term2Btn = term2Btn;
    }

    public String getTerm1URL() {
        return term1URL;
    }

    public void setTerm1URL(String term1URL) {
        this.term1URL = term1URL;
    }

    public String getTerm2URL() {
        return term2URL;
    }

    public void setTerm2URL(String term2URL) {
        this.term2URL = term2URL;
    }

    public String getTermTotal() {
        return termTotal;
    }

    public void setTermTotal(String termTotal) {
        this.termTotal = termTotal;
    }

    public String getTermPaid() {
        return termPaid;
    }

    public void setTermPaid(String termPaid) {
        this.termPaid = termPaid;
    }

    public String getTermDuePay() {
        return termDuePay;
    }

    public void setTermDuePay(String termDuePay) {
        this.termDuePay = termDuePay;
    }

    public String getTermDiscount() {
        return termDiscount;
    }

    public void setTermDiscount(String termDiscount) {
        this.termDiscount = termDiscount;
    }

    public List<FeesFinalResponse> getFinalArray() {
        return finalArray;
    }

    public void setFinalArray(List<FeesFinalResponse> finalArray) {
        this.finalArray = finalArray;
    }
}