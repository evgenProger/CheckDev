package ru.checkdev.notification.repository;

import ru.checkdev.notification.domain.SubscribeCategory;

import java.util.List;

public class SubscribeCategoryRepositoryFake
        extends CrudRepositoryFake<SubscribeCategory>
        implements SubscribeCategoryRepository {

    @Override
    public List<SubscribeCategory> findByUserId(int id) {
        return memory.values()
                .stream()
                .filter(subscribeCategory -> subscribeCategory.getUserId() == id)
                .toList();
    }

    @Override
    public SubscribeCategory findByUserIdAndCategoryId(int userId, int categoryId) {
        return memory.values()
                .stream()
                .filter(subscribeCategory -> subscribeCategory.getUserId() == userId)
                .filter(subscribeCategory -> subscribeCategory.getCategoryId() == categoryId)
                .findFirst().orElseGet(() -> null);
    }

    @Override
    public List<Integer> findUserIdByCategoryId(int categoryId) {
        return memory.values()
                .stream()
                .filter(subscribeCategory -> subscribeCategory.getCategoryId() == categoryId)
                .map(SubscribeCategory::getUserId)
                .toList();
    }

    @Override
    public List<SubscribeCategory> findAll() {
        return memory.values()
                .stream()
                .toList();
    }
}
