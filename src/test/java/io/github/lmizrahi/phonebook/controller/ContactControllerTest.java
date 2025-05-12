package io.github.lmizrahi.phonebook.controller;

import io.github.lmizrahi.phonebook.model.Contact;
import io.github.lmizrahi.phonebook.dto.PagedResponse;
import io.github.lmizrahi.phonebook.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

class ContactControllerTest {

    private ContactService contactService;
    private WebTestClient webTestClient;
    private Contact sampleContact;


    @BeforeEach
    void setUp() {
        contactService = Mockito.mock(ContactService.class);
        ContactController controller = new ContactController(contactService);
        webTestClient = WebTestClient.bindToController(controller).build();

        sampleContact = new Contact("John",
                "+1-684-107-9724", "+16841079724");
        sampleContact.setId(1L);
        sampleContact.setLastName("Doe");
    }

    @Test
    void testGetContacts() {
        PagedResponse<Contact> pagedResponse = new PagedResponse<>(List.of(sampleContact), 1, 1, 1);
        Mockito.when(contactService.getAllContacts(PageRequest.of(0, 10))).thenReturn(Mono.just(pagedResponse));

        webTestClient.get().uri("/api/contacts")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content[0].firstName").isEqualTo("John");
    }
}
