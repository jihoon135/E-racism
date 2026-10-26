package io.github.jihoon135.api.ratification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jihoon135.api.common.TreatyNotFoundException;
import io.github.jihoon135.api.treaty.Treaty;
import io.github.jihoon135.api.treaty.TreatyRepository;

@Service
@Transactional(readOnly = true)
public class RatificationService {

    private final TreatyRepository treatyRepository;
    private final RatificationRepository ratificationRepository;

    public RatificationService(TreatyRepository treatyRepository,
                               RatificationRepository ratificationRepository) {
        this.treatyRepository = treatyRepository;
        this.ratificationRepository = ratificationRepository;
    }

    public TreatyRatificationsResponse getRatifications(String code) {
        Treaty treaty = treatyRepository.findById(code)
                .orElseThrow(() -> new TreatyNotFoundException(code));
        return new TreatyRatificationsResponse(
                treaty.getCode(),
                treaty.getDataUpdatedAt(),
                ratificationRepository.findItemsByTreatyCode(code));
    }
}
