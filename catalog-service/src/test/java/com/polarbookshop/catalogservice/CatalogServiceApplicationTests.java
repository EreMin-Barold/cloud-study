package com.polarbookshop.catalogservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {
	
	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private BookRepository bookRepository;
	
	@Test
	void whenPostRequestThenBookCreated() {
		var expectedBook = Book.of("1231231231", "Title", "Author",9.90, "publisher");
		
		when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
		
		webTestClient
			.post()
			.uri("/books")
			.bodyValue(expectedBook)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(Book.class).value(actualBook -> {
				assertThat(actualBook).isNotNull();
				assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
			});
	}
}
