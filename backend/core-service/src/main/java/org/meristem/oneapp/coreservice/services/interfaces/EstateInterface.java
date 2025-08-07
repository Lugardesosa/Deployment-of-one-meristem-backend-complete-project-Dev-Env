package org.meristem.oneapp.coreservice.services.interfaces;

public interface EstateInterface<T, R> {

    R save(T request);

    R addBeneficiary(T request);

    R removeBeneficiary(T request);

    R addAsset(T request);

    R removeAsset(T request);
}
