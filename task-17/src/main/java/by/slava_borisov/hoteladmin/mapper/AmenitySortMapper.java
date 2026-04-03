package by.slava_borisov.hoteladmin.mapper;

import by.slava_borisov.hoteladmin.util.SortCriteria;
import org.springframework.stereotype.Component;

@Component
public class AmenitySortMapper {

    public SortCriteria map(String sort) {
        if (sort == null) {
            return SortCriteria.BY_ID;
        }

        return switch (sort.toLowerCase()) {
            case "price" -> SortCriteria.BY_PRICE;
            case "category" -> SortCriteria.BY_NAME;
            case "id" -> SortCriteria.BY_ID;
            default -> throw new IllegalArgumentException("Неверный параметр sort: " + sort);
        };
    }
}