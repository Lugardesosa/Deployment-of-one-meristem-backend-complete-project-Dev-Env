package org.meristem.oneapp.usersservice.integrations.responses;


import java.time.ZonedDateTime;

public record CreateIndividualCustomerResponse(String customerId, String sourceReference, String status, String message, ZonedDateTime createdDate) {

}
