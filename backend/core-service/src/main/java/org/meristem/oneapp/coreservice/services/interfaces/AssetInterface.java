package org.meristem.oneapp.coreservice.services.interfaces;



public interface AssetInterface<T, R> {
    R save(T request);
}
