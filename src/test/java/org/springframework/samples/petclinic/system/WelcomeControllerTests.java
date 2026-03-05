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

package org.springframework.samples.petclinic.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for {@link WelcomeController}
 */

@WebMvcTest(WelcomeController.class)
@DisabledInNativeImage
@DisabledInAotMode
class WelcomeControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testWelcomeReturnValue() {
		assertThat(new WelcomeController().welcome()).isEqualTo("welcome");
	}

	@Test
	void testWelcomePageStatus() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("welcome"));
	}

	@Test
	void testWelcomePageContainsExpectedContent() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("Welcome")));
	}

	@Test
	void testWelcomePageWithGermanLocale() throws Exception {
		mockMvc.perform(get("/?lang=de"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Willkommen")));
	}

	@Test
	void testWelcomePageWithSpanishLocale() throws Exception {
		mockMvc.perform(get("/?lang=es"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Bienvenido")));
	}

	@Test
	void testWelcomePageWithUnsupportedLocale() throws Exception {
		mockMvc.perform(get("/?lang=xx"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Welcome")));
	}

	@Test
	void testWelcomePageLocaleSessionPersistence() throws Exception {
		MvcResult result = mockMvc.perform(get("/?lang=de"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Willkommen")))
			.andReturn();

		MockHttpSession session = (MockHttpSession) result.getRequest().getSession();

		mockMvc.perform(get("/").session(session))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Willkommen")));
	}

	@Test
	void testPostMethodNotAllowed() throws Exception {
		mockMvc.perform(post("/")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	void testPutMethodNotAllowed() throws Exception {
		mockMvc.perform(put("/")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	void testDeleteMethodNotAllowed() throws Exception {
		mockMvc.perform(delete("/")).andExpect(status().isMethodNotAllowed());
	}

}
