package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.meristem.oneapp.usersservice.models.Users;

public interface IIdDetailsService {

    UserIdDetails buildIdDetails(SmileIdWebhookNotification notification, Users loggedInUser);
}
