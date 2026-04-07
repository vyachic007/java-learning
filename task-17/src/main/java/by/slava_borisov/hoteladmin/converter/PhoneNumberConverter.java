package by.slava_borisov.hoteladmin.converter;

import by.slava_borisov.hoteladmin.model.value.PhoneNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {

    @Override
    public String convertToDatabaseColumn(PhoneNumber attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new PhoneNumber(dbData);
    }
}