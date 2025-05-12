package io.github.lmizrahi.phonebook;

import io.github.lmizrahi.phonebook.model.Contact;
import io.github.lmizrahi.phonebook.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;


@Component
@RequiredArgsConstructor
@Profile("seed")
@Slf4j
public class ContactSeeder implements ApplicationRunner {

    private final ContactService contactService;

    private static final int TOTAL = 5_000;
    private static final int BATCH_SIZE = 500;

    private static final String[] FIRST_NAMES = {
            "Alice", "Bob", "Charlie", "Dana", "Eli",
            "Fiona", "George", "Hannah", "Ian", "Julia"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Brown", "Taylor", "Anderson",
            "White", "Harris", "Martin", "Clark", "Lewis"
    };

    private static final String[] ADDRESSES = {
            "100 Main St", "200 Oak Ave", "300 Maple Rd", "400 Pine Ln", "500 Birch Blvd",
            "600 Cedar Ct", "700 Elm Dr", "800 Spruce Way", "900 Walnut Ter", "1000 Aspen Pl"
    };

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting contact seeding...");
        Flux.fromStream(LongStream.range(0, TOTAL).boxed())
                .buffer(BATCH_SIZE)
                .concatMap(batch -> {
                    var contacts = batch.stream().map(this::generateContact).toList();
                    return Flux.fromIterable(contacts)
                            .flatMap(contactService::createContact)
                            .onErrorComplete()
                            .then();
                })
                .doOnComplete(() -> log.info("Finished seeding {} contacts", TOTAL))
                .subscribe();
    }

    private Contact generateContact(Long index) {
        String firstName = FIRST_NAMES[(int)(index % FIRST_NAMES.length)] + index;
        String lastName = LAST_NAMES[(int)(index % LAST_NAMES.length)] + index;
        String address = ADDRESSES[(int)(index % ADDRESSES.length)] + index;

        String rawPhone = String.format("+1-%03d-%03d-%04d",
                ThreadLocalRandom.current().nextInt(100, 999),
                ThreadLocalRandom.current().nextInt(100, 999),
                ThreadLocalRandom.current().nextInt(1000, 9999));

        Contact contact = new Contact(firstName, rawPhone, rawPhone);
        contact.setLastName(lastName);
        contact.setAddress(address);
        return contact;
    }
}
