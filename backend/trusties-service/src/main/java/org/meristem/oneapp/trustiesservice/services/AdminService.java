package org.meristem.oneapp.trustiesservice.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trustiesservice.domains.requests.UpdateSelectionRequest;
import org.meristem.oneapp.trustiesservice.domains.responses.UpdateSelectionResponse;
import org.meristem.oneapp.trustiesservice.models.Selections;
import org.meristem.oneapp.trustiesservice.repositories.CustomRepository;
import org.meristem.oneapp.trustiesservice.repositories.FormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final CustomRepository customRepository;
    private final ObjectMapper objectMapper;
    private final FormRepository formRepository;

    @Transactional
    public UpdateSelectionResponse updateSelection(UpdateSelectionRequest request) {

        Set<Long> toAddFormIds = new HashSet<>(request.itemsToAdd().stream().map(UpdateSelectionRequest.Items::formId).toList());
        List<Long> existingFormIds = formRepository.findIdsInIds(toAddFormIds);

        List<Selections> selections = request.itemsToAdd().stream().filter(i -> existingFormIds.contains(i.formId()))
                .map(i -> Selections.builder().formId(i.formId()).selectionValue(i.selectionValue()).build())
                .toList();

        int objectsDeleted = customRepository.deleteSelections(request.itemsToRemove());
        customRepository.saveAll(selections);
        return UpdateSelectionResponse.builder().message("Successful").successCount(objectsDeleted + selections.size()).build();
    }

//
//    public UpdateFormResponse updateForm(@Valid UpdateFormRequest request) {
//    }
}
