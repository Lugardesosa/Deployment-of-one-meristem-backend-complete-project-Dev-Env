package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.models.Users;

public interface IIdDetailsService {

    void saveIdDetails(SmileIdWebhookNotification notification, Users loggedInUser);
}
