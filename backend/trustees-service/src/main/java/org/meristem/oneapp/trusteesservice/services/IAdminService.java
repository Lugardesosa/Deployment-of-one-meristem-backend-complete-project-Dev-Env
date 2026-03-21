package org.meristem.oneapp.trusteesservice.services;

import org.meristem.oneapp.trusteesservice.domains.requests.UpdateSelectionRequest;
import org.meristem.oneapp.trusteesservice.domains.responses.UpdateSelectionResponse;

public interface IAdminService {

    UpdateSelectionResponse updateSelection(UpdateSelectionRequest request);
}
