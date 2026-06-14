package com.money.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.money.dto.ExpenseDTO;
import com.money.entity.CategoryEntity;
import com.money.entity.ExpenseEntity;
import com.money.entity.ProfileEntity;
import com.money.repository.CategoryRepository;
import com.money.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

	private final CategoryRepository categoryRepository;
	private final ExpenseRepository expenseRepository;
	private final ProfileService profileService;
	
	
	public ExpenseDTO addExpense(ExpenseDTO dto) {
		ProfileEntity profile =profileService.getCurrentProfile();
	CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
	                  .orElseThrow(()->new RuntimeException("Category not found"));
	ExpenseEntity newExpene = toEntity(dto,profile,category);
	newExpene = expenseRepository.save(newExpene);
	return toDTO(newExpene);
	
	}
	
	
	public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser(){
		ProfileEntity profile =profileService.getCurrentProfile();
		LocalDate now = LocalDate.now();
		LocalDate startDate = now.withDayOfMonth(1);
		LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
		List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
	return list.stream().map(this::toDTO).toList();
		}
	
	
	public void deleteExpense(Long expenseId) {
		ProfileEntity profile =profileService.getCurrentProfile();
	ExpenseEntity entity =	expenseRepository.findById(expenseId)
		              .orElseThrow(()->new RuntimeException("Expense not found"));
	if(!entity.getProfile().getId().equals(profile.getId())) {
		throw new RuntimeException("unauthorized to delete this expense");
	}
	expenseRepository.delete(entity);
	
	}
	// get latest 5 expense for curret user
	public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
		ProfileEntity profile =profileService.getCurrentProfile();
		List<ExpenseEntity> list =  expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
	return list.stream().map(this::toDTO).toList();
	}
	
	//total expense for  current user
	public BigDecimal getTotalExpenseForcurrentUser() {
		ProfileEntity profile =profileService.getCurrentProfile();
	BigDecimal total =	expenseRepository.findTotalExpenseByProfileId(profile.getId());
	return total !=null ? total :BigDecimal.ZERO;
	}
	
	
	//filter expenses
	public List<ExpenseDTO> filterExpenses(LocalDate startDate,LocalDate endDate,String keyword,Sort sort){
		ProfileEntity profile = profileService.getCurrentProfile();
	List<ExpenseEntity> list =	expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
		return list.stream().map(this::toDTO).toList();		
	}
	
public List<ExpenseDTO> getExpenseForUserOnDate(Long profileId,LocalDate date){
List<ExpenseEntity> list =	expenseRepository.findByProfileIdAndDate(profileId, date);
  return list.stream().map(this::toDTO).toList();
}
	
	
	
	
	private ExpenseEntity toEntity(ExpenseDTO dto,ProfileEntity profile, CategoryEntity category) {
		return ExpenseEntity.builder()
				.name(dto.getName())
				.icon(dto.getIcon())
				.amount(dto.getAmount())
				.date(dto.getDate())
				.profile(profile)
				.categgory(category)
				.build();
				
	}
	
	private ExpenseDTO toDTO(ExpenseEntity entity) {
		return ExpenseDTO.builder()
		         .id(entity.getId())
		         .name(entity.getName())
		         .icon(entity.getIcon())
		         .categoryId(entity.getCateggory() != null ? entity.getCateggory().getId() : null)
		         .categoryName(entity.getCateggory() != null? entity.getCateggory().getName():"N/A")
		         .amount(entity.getAmount())
		         .date(entity.getDate())
		         .createdAt(entity.getCreatedAt())
		         .updatedAt(entity.getUpdatedAt())
		         .build();
		         
		}
}

