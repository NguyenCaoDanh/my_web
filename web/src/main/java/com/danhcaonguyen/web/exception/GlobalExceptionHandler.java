package com.danhcaonguyen.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Lớp xử lý ngoại lệ toàn cục.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý tất cả các ngoại lệ loại `ErrorHandler`.
     *
     * @param ex Ngoại lệ được ném.
     * @return Phản hồi chứa thông tin lỗi.
     */
    @ExceptionHandler(ErrorHandler.class)
    public ResponseEntity<ExceptionResponse> handleErrorHandler(ErrorHandler ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response); // Trả về phản hồi với HTTP status từ ErrorHandler
    }

    /**
     * Xử lý các ngoại lệ chung không được dự đoán trước.
     *
     * @param ex Ngoại lệ được ném.
     * @return Phản hồi chứa thông tin lỗi mặc định.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        ExceptionResponse response = new ExceptionResponse("Internal server error: " + ex.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }
}
