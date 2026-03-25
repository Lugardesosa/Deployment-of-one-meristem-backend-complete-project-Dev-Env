package org.meristem.oneapp.kafka.dtos;

public record GeoIPDto(String ip, String cityName, String country, String latitude, String longitude) {
}
