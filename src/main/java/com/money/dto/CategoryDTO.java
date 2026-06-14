package com.money.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

	private Long id;
	private Long profileId;
	private String name;
	private String icon;
	private String type;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	
	
}
