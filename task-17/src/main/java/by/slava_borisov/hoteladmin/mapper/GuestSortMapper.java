package by.slava_borisov.hoteladmin.mapper;

import by.slava_borisov.hoteladmin.util.SortCriteria;
import org.springframework.stereotype.Component;

@Component
public class GuestSortMapper {

    public SortCriteria map(String sort) {
        if (sort == null) {
            return SortCriteria.BY_CHECK_OUT_DATE;
        }

        return switch (sort.toLowerCase()) {
            case "date" -> SortCriteria.BY_CHECK_OUT_DATE;
            case "id" -> SortCriteria.BY_ID;
            case "name" -> SortCriteria.BY_NAME;
            default -> throw new IllegalArgumentException("Неверный параметр sort: " + sort);
        };
    }
}