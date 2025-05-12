package io.github.lmizrahi.phonebook.repositories;

import io.github.lmizrahi.phonebook.model.Contact;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ContactRepository extends R2dbcRepository<Contact, Long> {
    @Query("SELECT * FROM contacts ORDER BY id OFFSET :offset LIMIT :limit")
    Flux<Contact> findAllPaged(long offset, int limit);
    @Query("SELECT * FROM contacts WHERE formatted_phone LIKE '%' || :query || '%' ORDER BY id OFFSET :offset LIMIT :limit")
    Flux<Contact> findByFormattedPhoneNumberContainsPaged(String query, long offset, int limit);
    @Query("SELECT * FROM contacts WHERE search_vector @@ to_tsquery('english', :query) " +
            "ORDER BY ts_rank(search_vector, to_tsquery('english', :query)) DESC OFFSET :offset LIMIT :limit")
    Flux<Contact> findBySearchVectorPaged(String query, long offset, int limit);
    Mono<Boolean> existsByFormattedPhoneNumber(String formattedPhone);
    Mono<Contact> findFirstByFormattedPhoneNumber(String formattedPhone);
}
