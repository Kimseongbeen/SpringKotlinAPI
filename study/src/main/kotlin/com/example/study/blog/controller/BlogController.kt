package com.example.study.blog.controller

import com.example.study.blog.dto.BlogDto
import com.example.study.blog.entity.Wordcount
import com.example.study.blog.service.BlogService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/blog") // 1. 클라이언트쪽에서 api/blog 쪽으로 호출
@RestController
class BlogController (
    val blogService: BlogService
){
    @GetMapping("") // 2. Get으로 호출시
    fun search(@RequestBody @Valid blogDto: BlogDto): String?{ // 3. search에 연결이 되고, 값들은 blogDto에 저장된다.
        val result = blogService.searchKaKao(blogDto) // 4. 그리고 그 결과 값이 kakaoService에 넘어가고 kakaoservice가 실행됨.
        return result
    }
    @GetMapping("/rank")
    fun searchWordRank(): List<Wordcount> = blogService.searchWordRank()
}