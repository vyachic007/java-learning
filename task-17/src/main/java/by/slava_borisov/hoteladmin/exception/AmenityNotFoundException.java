package by.slava_borisov.hoteladmin.exception;

import by.slava_borisov.hoteladmin.util.Messages;

public class AmenityNotFoundException extends RuntimeException {

    public AmenityNotFoundException(Long serviceId) {
        super(String.format(Messages.AMENITY_NOT_FOUND_EXCEPTION, serviceId));
    }
}
