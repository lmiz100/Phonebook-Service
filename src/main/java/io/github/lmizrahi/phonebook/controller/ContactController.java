package io.github.lmizrahi.phonebook.controller;

import io.github.lmizrahi.phonebook.dto.PagedResponse;
import io.github.lmizrahi.phonebook.model.Contact;
import io.github.lmizrahi.phonebook.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    private final ContactService service;


    @GetMapping
    public Mono<PagedResponse<Contact>> getContacts(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        log.info("Calling getContacts");
        Pageable pageable = makePageable(page, size);
        return service.getAllContacts(pageable);
    }

    @GetMapping("/search")
    public Mono<PagedResponse<Contact>> searchContacts(@RequestParam(defaultValue = "") String query,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Calling searchContacts");
        Pageable pageable = makePageable(page, size);
        return service.searchContacts(query, pageable);
    }

    @GetMapping("/{id}")
    public Mono<Contact> getContactById(@PathVariable Long id) {
        log.info("Calling getContactById");
        return service.getContactById(id);
    }

    //TODO: work with DTO without read only contact fields
    @PostMapping
    public Mono<Contact> createContact(@RequestBody Contact contact) {
        log.info("Calling createContact");
        contact.setId(null);
        return service.createContact(contact);
    }

   @DeleteMapping("/{id}")
    public Mono<Void> deleteContact(@PathVariable Long id) {
        log.info("Calling deleteContact");
        return service.deleteContact(id);
    }

    @PutMapping("/{id}")
    public Mono<Contact> updateContactById(@PathVariable Long id, @RequestBody Contact updatedContact) {
        log.info("Calling updateContactById");
        updatedContact.setId(id);
        return service.updateContact(updatedContact);
    }

    private Pageable makePageable(int page, int size) {
        return PageRequest.of(page, Math.min(size, 10));
    }
}
