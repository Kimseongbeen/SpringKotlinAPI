package com.example.study.blog.dto

import com.example.study.core.annotation.ValidEnum
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class BlogDto (
    @field:NotBlank(message = "query parameter required")
    @JsonProperty("query") //JSON에 담긴 query를 _query에 연결한다.
    private val _query: String?,

    @field:NotBlank(message = "sort parameter required")
    @field:ValidEnum(enumClass = EnumSort::class, message = "sort parameter one of ACCURACY and RECENCY")
    val sort: String?,

    @field:NotNull(message = "page parameter required")
    @field:Max(50, message = "page is more than max")
    @field:Min(1, message = "page is less than min")
    @JsonProperty("page")
    val page: Int?,

    @field:NotNull(message = "size parameter required")
    val size: Int?
) {
    val query: String //해당 query는 string만 올수있고,
        get() = _query!! //이 query를 호출(GET)할 경우 _query는 절대 Null이 들어올수 없다!
    private enum class EnumSort {
        ACCURACY,
        RECENCY
    }
}