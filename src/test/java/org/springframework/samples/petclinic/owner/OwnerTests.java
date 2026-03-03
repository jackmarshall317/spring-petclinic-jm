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

package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link Owner} domain object.
 */
class OwnerTests {

	private Owner owner;

	@BeforeEach
	void setup() {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
	}

	@Test
	void testGettersAndSetters() {
		assertThat(owner.getAddress()).isEqualTo("110 W. Liberty St.");
		assertThat(owner.getCity()).isEqualTo("Madison");
		assertThat(owner.getTelephone()).isEqualTo("6085551023");
	}

	@Test
	void testGetPetsInitiallyEmpty() {
		assertThat(owner.getPets()).isEmpty();
	}

	@Test
	void testAddNewPet() {
		Pet pet = new Pet();
		pet.setName("Fido");
		owner.addPet(pet);
		assertThat(owner.getPets()).hasSize(1);
		assertThat(owner.getPets().get(0).getName()).isEqualTo("Fido");
	}

	@Test
	void testAddExistingPetIgnored() {
		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("Fido");
		owner.addPet(pet);
		assertThat(owner.getPets()).isEmpty();
	}

	@Test
	void testGetPetByName() {
		Pet pet = new Pet();
		pet.setName("Max");
		owner.addPet(pet);
		assertThat(owner.getPet("Max")).isNotNull();
		assertThat(owner.getPet("Max").getName()).isEqualTo("Max");
	}

	@Test
	void testGetPetByNameCaseInsensitive() {
		Pet pet = new Pet();
		pet.setName("Max");
		owner.addPet(pet);
		assertThat(owner.getPet("max")).isNotNull();
		assertThat(owner.getPet("MAX")).isNotNull();
	}

	@Test
	void testGetPetByNameNotFound() {
		assertThat(owner.getPet("NonExistent")).isNull();
	}

	@Test
	void testGetPetByNameIgnoreNew() {
		Pet newPet = new Pet();
		newPet.setName("Buddy");
		owner.addPet(newPet);

		// ignoreNew = false should find the new pet
		assertThat(owner.getPet("Buddy", false)).isNotNull();

		// ignoreNew = true should NOT find the new (unsaved) pet
		assertThat(owner.getPet("Buddy", true)).isNull();
	}

	@Test
	void testGetPetByNameIgnoreNewWithSavedPet() {
		Pet savedPet = new Pet();
		savedPet.setId(10);
		savedPet.setName("Buddy");
		// Can't use addPet since it's not new, add directly
		owner.getPets().add(savedPet);

		// ignoreNew = true should still find the saved pet
		assertThat(owner.getPet("Buddy", true)).isNotNull();
	}

	@Test
	void testGetPetById() {
		Pet pet = new Pet();
		pet.setId(7);
		pet.setName("Leo");
		owner.getPets().add(pet);

		assertThat(owner.getPet(7)).isNotNull();
		assertThat(owner.getPet(7).getName()).isEqualTo("Leo");
	}

	@Test
	void testGetPetByIdNotFound() {
		assertThat(owner.getPet(999)).isNull();
	}

	@Test
	void testGetPetByIdSkipsNewPets() {
		Pet newPet = new Pet();
		newPet.setName("Fido");
		owner.addPet(newPet);

		// New pet has no ID, so getPet(Integer) should not find it
		assertThat(owner.getPet(Integer.valueOf(0))).isNull();
	}

	@Test
	void testAddVisit() {
		Pet pet = new Pet();
		pet.setId(7);
		pet.setName("Leo");
		owner.getPets().add(pet);

		Visit visit = new Visit();
		visit.setDescription("checkup");

		owner.addVisit(7, visit);

		assertThat(pet.getVisits()).hasSize(1);
		assertThat(pet.getVisits().iterator().next().getDescription()).isEqualTo("checkup");
	}

	@Test
	void testAddVisitNullPetId() {
		assertThatThrownBy(() -> owner.addVisit(null, new Visit())).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testAddVisitNullVisit() {
		assertThatThrownBy(() -> owner.addVisit(1, null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testAddVisitInvalidPetId() {
		assertThatThrownBy(() -> owner.addVisit(999, new Visit())).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testToString() {
		String result = owner.toString();
		assertThat(result).contains("George");
		assertThat(result).contains("Franklin");
		assertThat(result).contains("110 W. Liberty St.");
		assertThat(result).contains("Madison");
		assertThat(result).contains("6085551023");
	}

	@Test
	void testGetPetByNameWithNullName() {
		Pet pet = new Pet();
		owner.addPet(pet);
		assertThat(owner.getPet("anything")).isNull();
	}

}
