package io.github.lmizrahi.phonebook.service;

import io.github.lmizrahi.phonebook.dto.PagedResponse;
import io.github.lmizrahi.phonebook.model.Contact;
import io.github.lmizrahi.phonebook.repositories.ContactRepository;
import io.github.lmizrahi.phonebook.utils.PhonebookUtils;
import io.github.lmizrahi.phonebook.validation.ContactValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

import io.github.lmizrahi.phonebook.exception.ErrorMessages;


@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepo;
    private final ContactValidator contactValidator;


    @Override
    public Mono<PagedResponse<Contact>> getAllContacts(Pageable pageable) {
        int pageNum = pageable.getPageNumber();
        int limit = pageable.getPageSize();
        long offset = (long) pageNum * pageable.getPageSize();

        Mono<List<Contact>> contactsMono = contactRepo.findAllPaged(offset, limit).collectList();
        Mono<Long> totalCountMono = contactRepo.count();

        return contactsMono.zipWith(totalCountMono)
                .map(tuple -> new PagedResponse<>(
                        tuple.getT1(),
                        tuple.getT2(),
                        pageNum,
                        limit
                ));
    }

    @Override
    public Mono<Contact> getContactById(Long id) {
        return contactRepo.existsById(id)
                .flatMap(isExistingId -> {
                    if(isExistingId) {
                        return contactRepo.findById(id);
                    }

                    return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format(ErrorMessages.CONTACT_ID_NOT_FOUND_FORMAT, id)));
                });
    }


    @Override
    public Mono<PagedResponse<Contact>> searchContacts(String freeText, Pageable pageable) {
        String cleanedQuery = freeText.trim();
        int pageNum = pageable.getPageNumber();
        int limit = pageable.getPageSize();
        long offset = (long) pageNum * pageable.getPageSize();
        Mono<List<Contact>> retrievedContacts;
        Mono<Long> totalCountMono = contactRepo.count();

        if(PhonebookUtils.isLikelyPhoneNumber(cleanedQuery)) {
            String normalizedStr = PhonebookUtils.normalizedPhoneNumber(cleanedQuery);
            log.info("normalizedStr: {}", normalizedStr);
            retrievedContacts = contactRepo.findByFormattedPhoneNumberContainsPaged(normalizedStr, offset, limit)
                    .collectList();
        } else {
            retrievedContacts = contactRepo.findBySearchVectorPaged(String.format("%s:*", freeText), offset, limit).collectList();
        }

        return retrievedContacts.zipWith(totalCountMono)
                .map(tuple -> new PagedResponse<>(
                        tuple.getT1(),
                        tuple.getT2(),
                        pageNum,
                        limit
                ));
    }


    @Override
    public Mono<Contact> createContact(Contact contact) {
        try {
            contactValidator.validate(contact);
        } catch (IllegalArgumentException e) {
            return Mono.error(e);
        }
        String formattedPhone = PhonebookUtils.formatPhoneNumber(contact.getPhoneNumber());
        contact.setFormattedPhoneNumber(formattedPhone);

        return contactRepo.existsByFormattedPhoneNumber(formattedPhone)
                .flatMap(phoneAlreadyExists -> {
                    if (phoneAlreadyExists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                                String.format(ErrorMessages.PHONE_NUMBER_ALREADY_EXISTS_FORMAT, formattedPhone)));
                    }
                    return contactRepo.save(contact);
                });
    }


    @Override
    public Mono<Contact> updateContact(Contact contact) {
        try {
            contactValidator.validate(contact);
        } catch (IllegalArgumentException e) {
            return Mono.error(e);
        }
        String formattedPhone = PhonebookUtils.formatPhoneNumber(contact.getPhoneNumber());
        contact.setFormattedPhoneNumber(formattedPhone);

        return contactRepo.findFirstByFormattedPhoneNumber(formattedPhone)
                .flatMap(retrievedContact -> {
                    if(!retrievedContact.getId().equals(contact.getId())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                                String.format(ErrorMessages.PHONE_NUMBER_ALREADY_EXISTS_FORMAT, formattedPhone)));
                    }

                    return contactRepo.save(contact);
                })
                .switchIfEmpty(contactRepo.save(contact));
    }


    @Override
    public Mono<Void> deleteContact(Long id) {
        return contactRepo.existsById(id)
                .flatMap(isExistingId -> {
                    if(isExistingId) {
                        return contactRepo.deleteById(id);
                    }
                    return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format(ErrorMessages.CONTACT_ID_NOT_FOUND_FORMAT, id)));
                });
    }
}
