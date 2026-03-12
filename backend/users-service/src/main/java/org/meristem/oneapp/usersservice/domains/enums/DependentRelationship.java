package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DependentRelationship {

    CHILD("Child"),
    COUSIN("Cousin"),
    NEPHEW("Nephew"),
    NIECE("Niece"),
    BROTHER("Brother"),
    SISTER("Sister"),
    OTHERS("Others");

    private final String value;

}
