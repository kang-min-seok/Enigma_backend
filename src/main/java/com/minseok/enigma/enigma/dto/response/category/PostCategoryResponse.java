package com.minseok.enigma.enigma.dto.response.category;

import lombok.Data;

/**
 * 카테고리 응답을 위한 DTO 클래스입니다.
 */
@Data
public class PostCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
}