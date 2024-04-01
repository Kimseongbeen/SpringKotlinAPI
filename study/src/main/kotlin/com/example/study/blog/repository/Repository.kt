package com.example.study.blog.repository

import com.example.study.blog.entity.Wordcount
import org.springframework.data.repository.CrudRepository

interface WordRepository : CrudRepository<Wordcount, String> {
    fun findTop10ByOrderByCntDesc(): List<Wordcount> // 상위 10개의 워드 조회
}

