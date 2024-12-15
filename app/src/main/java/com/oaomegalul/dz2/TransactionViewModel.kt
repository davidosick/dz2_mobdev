package com.oaomegalul.dz2

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class TransactionViewModel(

    @JsonProperty("id")
    var Id : String = "",

    @JsonProperty("isIncome")
    var IsIncome : Boolean = false,

    @JsonProperty("category")
    var Category : String,

    @JsonProperty("summ")
    var Summ : String,

    @JsonProperty("updatedAt")
    var UpdatedAt : String

) : Serializable
