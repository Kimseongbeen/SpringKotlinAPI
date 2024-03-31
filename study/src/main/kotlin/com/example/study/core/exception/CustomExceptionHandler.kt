    package com.example.study.core.exception

    import com.example.study.core.response.ErrorResponse
    import org.springframework.http.HttpStatus
    import org.springframework.http.ResponseEntity
    import org.springframework.validation.FieldError
    import org.springframework.web.bind.MethodArgumentNotValidException
    import org.springframework.web.bind.annotation.ExceptionHandler
    import org.springframework.web.bind.annotation.RestControllerAdvice

    @RestControllerAdvice
    class CustomExceptionHandler {
        //어떤것이 들어왔을때 반응한다는 핸들러로
        @ExceptionHandler(InvalidInputException::class) //InvalidInput 이 들어왔을때 Exception 한다는 핸들러이다.
        fun invalidInputException(ex: InvalidInputException):ResponseEntity<ErrorResponse>{
            val errors = ErrorResponse(ex.message ?: "Not Exception Message")
            return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
        }

        @ExceptionHandler(MethodArgumentNotValidException::class)
        protected fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
            val errors = mutableMapOf<String, String>()
            ex.bindingResult.allErrors.forEach { error ->
                val fieldName = (error as FieldError).field
                val errorMessage = error.getDefaultMessage()
                errors[fieldName] = errorMessage ?: "NULL"
            }
            return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
        }

    }