package com.deltadc.iot.service;

import com.deltadc.iot.model.entities.History;
import com.deltadc.iot.repository.HistoryRepository;
import com.deltadc.iot.specification.HistorySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    /**
     * List the history data with pagination and sorting by params
     * @param params This Map should contain the key-value pairs of the fields to filter of
     *               the History entity class like: name, status, createdAt
     * @param page The page number to retrieve, default is 0
     * @param size The number of items to retrieve per page, default is 30
     * @param sortBy The field to sort by, default is id
     * @param sortDirection The direction to sort by, default is ASC
     * @return A Page of History entities
     */
    @Transactional(readOnly = true)
    public Page<History> list(int page, int size, Map<String, String> params,
                             @RequestParam(required = false) String sortBy,
                             @RequestParam(required = false) String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(
                        sortDirection != null ? sortDirection : "ASC"),
                sortBy != null ? sortBy : "id"
        );

        Pageable pageable = PageRequest.of(page, size, sort);
        return historyRepository.findAll(
                HistorySpecification.withDynamicQuery(params, History.class),
                pageable
        );
    }

    /**
     * List the history data with pagination and sorting by params
     * @param history The History entity to create
     * @return The created History entity
     */
    @Transactional
    public History create(History history) {
        return historyRepository.save(history);
    }
}
