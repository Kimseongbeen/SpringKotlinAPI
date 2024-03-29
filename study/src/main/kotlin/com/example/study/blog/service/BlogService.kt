package com.example.study.blog.service

import com.example.study.blog.dto.BlogDto
import com.example.study.core.exception.InvalidInputException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class BlogService {
    @Value("\${REST_API_KEY}")
    lateinit var restApiKey: String
    fun searchKaKao(blogDto: BlogDto): String? {
//blogDto로 들어온 값에 문제가있는지 확인하는 부분. Exception 전처리
        val msgList = mutableListOf<ExceptionMsg>()
        if (blogDto.query.trim().isEmpty()) {
            msgList.add(ExceptionMsg.EMPTY_QUERY) //EMPTY_QUERY("query parameter required")
        }
        if (blogDto.sort.trim() !in arrayOf("accuracy", "recency")) {
            msgList.add(ExceptionMsg.NOT_IN_SORT) //NOT_IN_SORT("sort parameter one of accuracy and recency")
        }
        when {
            blogDto.page < 1 -> msgList.add(ExceptionMsg.LESS_THAN_MIN) // 1보다 작다면,LESS_THAN_MIN("page is less than min")
            blogDto.page > 50 -> msgList.add(ExceptionMsg.MORE_THAN_MAX) // 50보다 크다면, MORE_THAN_MAX("page is more than max")
        }

        // 위에서 문제가 있어서 msgList에 하나라도 데이터가 있다면, 오류 발생이므로 Exception이 동작해야함
        // msgList가 비어있지 않다면, 존재한다면
        if (msgList.isNotEmpty()){
            val message = msgList.joinToString { it.msg } //담긴 내용을 message에 담아서
            throw InvalidInputException(message) // InvalidInputException에 msgList 내용을 던진다.
        }

        val webClient = WebClient
            .builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

        val response = webClient
            .get()
            .uri { it.path("/v2/search/blog")
                .queryParam("query", blogDto.query)
                .queryParam("sort", blogDto.sort)
                .queryParam("page", blogDto.page)
                .queryParam("size", blogDto.size)
                .build()}
            .header("Authorization", "KakaoAK $restApiKey")
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()

        return result
    }
}

private enum class ExceptionMsg(val msg: String) {
    EMPTY_QUERY("query parameter required"),
    NOT_IN_SORT("sort parameter one of accuracy and recency"),
    LESS_THAN_MIN("page is less than min"),
    MORE_THAN_MAX("page is more than max")
}