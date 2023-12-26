package ru.job4j.site.component;

import org.junit.jupiter.api.Test;
import ru.job4j.site.dto.FilterDTO;
import ru.job4j.site.dto.FilterRequestParams;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FilterRequestParamsManagerTest {

    private final FilterRequestParamsManager filterRequestParamsManager = new FilterRequestParamsManager();

    @Test
    void whenCreateRequestParamsWithAuthorFilter() {
        var filter = new FilterDTO(14, 0, 0, 1, 1);
        var expected = new FilterRequestParams(List.of(), 14, 0, 0, 1, false);
        assertThat(filterRequestParamsManager.getParams(filter)).isEqualTo(expected);
    }

    @Test
    void whenCreateRequestParamsWithNotAuthorFilter() {
        var filter = new FilterDTO(14, 0, 0, 3, 1);
        var expected = new FilterRequestParams(List.of(), 14, 0, 0, 1, true);
        assertThat(filterRequestParamsManager.getParams(filter)).isEqualTo(expected);
    }

    @Test
    void whenCreateRequestParamsWithAgreedWisherFilter() {
        var filter = new FilterDTO(14, 0, 0, 2, 1);
        var expected = new FilterRequestParams(List.of(), 0, 0, 14, 1, false);
        assertThat(filterRequestParamsManager.getParams(filter)).isEqualTo(expected);
    }

    @Test
    void whenCreateRequestParamsWithNotAgreedWisherFilter() {
        var filter = new FilterDTO(14, 0, 0, 4, 1);
        var expected = new FilterRequestParams(List.of(), 0, 0, 14, 1, true);
        assertThat(filterRequestParamsManager.getParams(filter)).isEqualTo(expected);
    }

    @Test
    void whenCreateRequestParamsWithWisherFilter() {
        var filter = new FilterDTO(14, 0, 0, 5, 1);
        var expected = new FilterRequestParams(List.of(), 0, 14, 0, 1, false);
        assertThat(filterRequestParamsManager.getParams(filter)).isEqualTo(expected);
    }

    @Test
    void whenCreateRequestParamsWithTopicListFilter() {
        var filter = new FilterDTO(14, 1, 0, 0, 1);
        var expected = new FilterRequestParams(List.of(1, 2, 3),
                0, 0, 0, 1, false);
        assertThat(filterRequestParamsManager.getParams(filter, List.of(1, 2, 3))).isEqualTo(expected);
    }
}
