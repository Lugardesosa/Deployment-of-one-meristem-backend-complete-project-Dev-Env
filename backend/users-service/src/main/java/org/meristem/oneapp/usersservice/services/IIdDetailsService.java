package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.kafka.dtos.UploadImageDto;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.meristem.oneapp.usersservice.models.Users;

public interface IIdDetailsService {

    UserIdDetails buildAndSaveIdDetails(IdQueryDetailsDto notification, Users loggedInUser);
    void uploadImage(UploadImageDto dto);
}
