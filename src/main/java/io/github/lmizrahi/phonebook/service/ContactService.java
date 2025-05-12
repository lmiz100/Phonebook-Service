package io.github.lmizrahi.phonebook.service;

import io.github.lmizrahi.phonebook.dto.PagedResponse;
import io.github.lmizrahi.phonebook.model.Contact;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;


public interface ContactService {
    Mono<PagedResponse<Contact>> getAllContacts(Pageable pageable);
    Mono<Contact> getContactById(Long id);
    Mono<PagedResponse<Contact>> searchContacts(String freeText, Pageable pageable);
    Mono<Contact> createContact(Contact contact);
    Mono<Contact> updateContact(Contact contact);
    Mono<Void> deleteContact(Long id);
}
