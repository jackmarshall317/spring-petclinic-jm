/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Bean Validation tests for {@link Owner} and {@link Person}.
 */
class OwnerValidationTests {

	private Validator validator;

	@BeforeEach
	void setup() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
		factory.afterPropertiesSet();
		this.validator = factory;
	}

	private Owner createValidOwner() {
		Owner owner = new Owner();
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("1234567890");
		return owner;
	}

	@Test
	void validOwnerShouldHaveNoViolations() {
		Owner owner = createValidOwner();
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isEmpty();
	}

	@Test
	void shouldRejectBlankFirstName() {
		Owner owner = createValidOwner();
		owner.setFirstName("");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
	}

	@Test
	void shouldRejectBlankLastName() {
		Owner owner = createValidOwner();
		owner.setLastName("");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
	}

	@Test
	void shouldRejectBlankAddress() {
		Owner owner = createValidOwner();
		owner.setAddress("");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("address"));
	}

	@Test
	void shouldRejectBlankCity() {
		Owner owner = createValidOwner();
		owner.setCity("");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("city"));
	}

	@Test
	void shouldRejectBlankTelephone() {
		Owner owner = createValidOwner();
		owner.setTelephone("");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telephone"));
	}

	@Test
	void shouldRejectInvalidTelephonePattern() {
		Owner owner = createValidOwner();
		owner.setTelephone("abc");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telephone"));
	}

	@Test
	void shouldRejectTelephoneTooShort() {
		Owner owner = createValidOwner();
		owner.setTelephone("12345");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telephone"));
	}

	@Test
	void shouldRejectTelephoneTooLong() {
		Owner owner = createValidOwner();
		owner.setTelephone("12345678901");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telephone"));
	}

}
