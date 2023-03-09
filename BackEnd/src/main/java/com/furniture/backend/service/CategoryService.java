package com.furniture.backend.service;

import com.furniture.backend.exception.UnauthorizedException;
import com.furniture.backend.model.Category;
import com.furniture.backend.payload.response.ApiResponse;
import com.furniture.backend.payload.PagedResponse;
import com.furniture.backend.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

	PagedResponse<Category> getAllCategories(int page, int size);

	ResponseEntity<Category> getCategory(Long id);

	ResponseEntity<Category> addCategory(Category category, UserPrincipal currentUser);

	ResponseEntity<Category> updateCategory(Long id, Category newCategory, UserPrincipal currentUser)
			throws UnauthorizedException;

	ResponseEntity<ApiResponse> deleteCategory(Long id, UserPrincipal currentUser) throws UnauthorizedException;

}
