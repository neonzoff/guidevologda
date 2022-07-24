package ru.neonzoff.guidevologda.service;


import ru.neonzoff.guidevologda.domain.Street;

public interface GeoCoderService {
    String getPos(Street street, String houseNumber);
}
