package ru.checkdev.forum.service;

import com.google.common.collect.Lists;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.checkdev.forum.domain.Category;
import ru.checkdev.forum.repository.CategoryRepository;

import java.util.List;

/**
 * Implementation of {@link CategoryService} interface.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /**
     * Repository used to manipulate categories in database.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Constructs <code>CategoryService</code> object.
     *
     * @param categoryRepository injected category repository object.
     */
    public CategoryServiceImpl(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> getAll() {
        return Lists.newArrayList(this.categoryRepository.findAll(
                new Sort(Sort.Direction.ASC, "position")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getById(final int id) {
        final Category category = this.categoryRepository.findOne(id);
        if (category == null) {
            throw new DataRetrievalFailureException("Category not found");
        }
        return category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category save(final Category category) {
        return this.categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final int id) {
        this.categoryRepository.delete(id);
    }
}