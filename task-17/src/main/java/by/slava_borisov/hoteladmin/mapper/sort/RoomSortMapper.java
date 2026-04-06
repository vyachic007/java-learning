package by.slava_borisov.hoteladmin.mapper.sort;

import by.slava_borisov.hoteladmin.util.SortCriteria;
import org.springframework.stereotype.Component;

@Component
public class RoomSortMapper {

    public SortCriteria map(String sort) {
        if (sort == null) {
            return SortCriteria.BY_ID;
        }

        return switch (sort.toLowerCase()) {
            case "id" -> SortCriteria.BY_ID;
            case "price" -> SortCriteria.BY_PRICE;
            case "capacity" -> SortCriteria.BY_CAPACITY;
            case "stars" -> SortCriteria.BY_STARS;
            default -> throw new IllegalArgumentException("Неверный параметр sort: " + sort);
        };
    }
}