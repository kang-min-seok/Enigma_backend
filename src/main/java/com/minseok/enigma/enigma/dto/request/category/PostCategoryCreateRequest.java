package com.minseok.enigma.enigma.dto.request.category;

import lombok.Data;

/**
 * 카테고리 생성 요청을 위한 DTO 클래스입니다.
 */
@Data
public class PostCategoryCreateRequest {
    private String name;
    private String description;
    private boolean isActive;
}