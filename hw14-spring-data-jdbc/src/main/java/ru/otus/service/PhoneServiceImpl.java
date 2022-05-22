package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.domain.Phone;
import ru.otus.repository.PhoneRepository;

@Service
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;

    public PhoneServiceImpl(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Phone save(Phone phone) {
        return phoneRepository.save(phone);
    }
}
