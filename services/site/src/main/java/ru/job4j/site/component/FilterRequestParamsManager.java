package ru.job4j.site.component;

import org.springframework.stereotype.Component;
import ru.job4j.site.dto.FilterDTO;
import ru.job4j.site.dto.FilterRequestParams;

import java.util.List;

@Component
public class FilterRequestParamsManager {

    public FilterRequestParams getParams(FilterDTO filter, List<Integer> topicIds) {
        var result = new FilterRequestParams();
        result.setTopicIds(topicIds);
        setParams(result, filter);
        return result;
    }

    public FilterRequestParams getParams(FilterDTO filter) {
        var result = new FilterRequestParams();
        result.setTopicIds(filter.getTopicId() > 0 ? List.of(filter.getTopicId()) : List.of());
        setParams(result, filter);
        return result;
    }

    private void setParams(FilterRequestParams filterRequestParams, FilterDTO filter) {
        var status = filter.getStatus();
        if (status > 0) {
            filterRequestParams.setStatus(status);
            filterRequestParams.setSubmitterId(filter.getUserId());
        } else {
            setProfileParams(filterRequestParams, filter);
        }
    }

    private void setProfileParams(FilterRequestParams filterRequestParams, FilterDTO filter) {
        switch (filter.getFilterProfile()) {
            case 1 -> filterRequestParams.setSubmitterId(filter.getUserId());
            case 2 -> filterRequestParams.setAgreedWisherId(filter.getUserId());
            case 3 -> {
                filterRequestParams.setSubmitterId(filter.getUserId());
                filterRequestParams.setExclude(true);
            }
            case 4 -> {
                filterRequestParams.setAgreedWisherId(filter.getUserId());
                filterRequestParams.setExclude(true);
            }
            case 5 -> filterRequestParams.setWisherId(filter.getUserId());
            default -> {
            }
        }
    }
}
