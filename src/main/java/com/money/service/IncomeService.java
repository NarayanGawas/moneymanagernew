package com.money.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.money.dto.ExpenseDTO;
import com.money.dto.IncomeDTO;
import com.money.entity.CategoryEntity;
import com.money.entity.ExpenseEntity;
import com.money.entity.IncomeEntity;
import com.money.entity.ProfileEntity;
import com.money.repository.CategoryRepository;
import com.money.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {

	
	private final CategoryRepository categoryRepository;
	private final IncomeRepository incomeRepository;
	private final ProfileService profileService;
	
	
	
	
	public IncomeDTO addIncome(IncomeDTO dto) {
		ProfileEntity profile =profileService.getCurrentProfile();
		CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
	                  .orElseThrow(()->new RuntimeException("Category not found"));
	IncomeEntity newExpene = toEntity(dto,profile,category);
	newExpene = incomeRepository.save(newExpene);
	return toDTO(newExpene);
	
	}
	
	public List<IncomeDTO> getCurrentMonthIncomeForCurrentUser(){
		ProfileEntity profile =profileService.getCurrentProfile();
		LocalDate now = LocalDate.now();
		LocalDate startDate = now.withDayOfMonth(1);
		LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
		List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
	return list.stream().map(this::toDTO).toList();
		}
	
	
	public void deleteIncome(Long incomeId) {
		ProfileEntity profile =profileService.getCurrentProfile();
       IncomeEntity entity =	incomeRepository.findById(incomeId)
		              .orElseThrow(()->new RuntimeException("income is not found"));
	if(!entity.getProfile().getId().equals(profile.getId())) {
		throw new RuntimeException("unauthorized to delete this income");
	}
	incomeRepository.delete(entity);

	}
	
	
	// get latest 5 incomes for curret user
		public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
			ProfileEntity profile =profileService.getCurrentProfile();
			List<IncomeEntity> list =  incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
		return list.stream().map(this::toDTO).toList();
		}
	
	//total incomes  for  current user
		public BigDecimal getTotalIncomeForcurrentUser() {
			ProfileEntity profile =profileService.getCurrentProfile();
		BigDecimal total =	incomeRepository.findTotalExpenseByProfileId(profile.getId());
		return total !=null ? total :BigDecimal.ZERO;
		}
	
	
		//filter incomes
		public List<IncomeDTO> filterIncomes(LocalDate startDate,LocalDate endDate,String keyword,Sort sort){
			ProfileEntity profile = profileService.getCurrentProfile();
		List<IncomeEntity> list =	incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
			return list.stream().map(this::toDTO).toList();		
		}	
	
	
	
	
	
	
	private IncomeEntity toEntity(IncomeDTO dto,ProfileEntity profile, CategoryEntity category) {
		return IncomeEntity.builder()
				.name(dto.getName())
				.icon(dto.getIcon())
				.amount(dto.getAmount())
				.date(dto.getDate())
				.profile(profile)
				.categgory(category)
				.build();
				
	}
	
	private IncomeDTO toDTO(IncomeEntity entity) {
		return IncomeDTO.builder()
		         .id(entity.getId())
		         .name(entity.getName())
		         .icon(entity.getIcon())
		         .categoryId(entity.getClass() !=null ? entity.getCateggory().getId():null)
		         .categoryName(entity.getCateggory() != null? entity.getCateggory().getName():"N/A")
		         .amount(entity.getAmount())
		         .date(entity.getDate())
		         .createdAt(entity.getCreatedAt())
		         .updatedAt(entity.getUpdatedAt())
		         .build();
		         
		}
}
