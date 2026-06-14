package com.money.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="tbl_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")   // only use ID — safe for JPA entities
@ToString(exclude = {"password", "activationToken"}) // never leak credentials
public class ProfileEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String fullName;
	@Column(unique=true)
	private String email;
	private String password;
	private String profileImageUrl;
	@Column(updatable=false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	private Boolean isActive;
	private String activationToken;
	
	@PrePersist
	public void prePersist() {
		if(this.isActive==null) {
			isActive=false;
		}
	}

	// ── UserDetails methods ──────────────────────────────────────────────
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	/** Spring Security uses getUsername() as the principal name. */
	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired()  { return true; }

	@Override
	public boolean isAccountNonLocked()   { return true; }

	@Override
	public boolean isCredentialsNonExpired() { return true; }

	/** Account is enabled only when the profile has been activated. */
	@Override
	public boolean isEnabled() {
		return Boolean.TRUE.equals(isActive);
	}
}