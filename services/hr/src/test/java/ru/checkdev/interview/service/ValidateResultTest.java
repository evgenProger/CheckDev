package ru.checkdev.interview.service;

import org.junit.Ignore;
import org.junit.Test;
import ru.checkdev.interview.domain.*;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Tests for validate result by filter.
 * Field type.
 * SINGLE_LINE, AREA, LIST,
 * MULTILIST, INT, DATE, ATTACH,
 * TASK, TEST
 *
 * List of predicts.
 * EQ, NOT_EQ, CONTAINS, NOT_CONTAINS, GREAT, LESS
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Ignore
public class ValidateResultTest {
    @Test
    public void whenTestFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        validated.init();
        ITask task = new ITask();
        IValue value = new IValue();
        value.setValue("zzzz");
        task.setValues(Collections.singletonList(value));
        Task values = new Task();
        values.setType(Task.Type.SINGLE_LINE);
        TPredict predict = new TPredict();
        predict.setKey(TPredict.Key.LESS);
        predict.setValue("aaaa");
        values.setFilters(Collections.singletonList(predict));
        Track.Key rsl = validated.by(task, values);
        assertThat(rsl, is(Track.Key.FAILURE));
    }

    @Test
    public void whenTaskFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAttachFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenDateFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenListFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenMultiListFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenNumberFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenLineFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAreaFieldFilterLess() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTestFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTaskFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAttachFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenDateFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenListFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenMultiListFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenNumberFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenLineFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAreaFieldFilterGreat() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTestFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTaskFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAttachFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenDateFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenListFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenMultiListFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenNumberFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenLineFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAreaFieldFilterNotContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTestFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTaskFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAttachFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenDateFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenListFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenMultiListFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenNumberFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenLineFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAreaFieldFilterContains() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTestFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTaskFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAttachFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenDateFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenListFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenMultiListFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenNumberFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenLineFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAreaFieldFilterNotEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTestFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenTaskFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAttachFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenDateFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenListFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenMultiListFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenNumberFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenLineFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }

    @Test
    public void whenAreaFieldFilterEq() {
        ValidateResult validated = new ValidateResult(null);
        ITask task = new ITask();
        Task values = new Task();
        validated.by(task, values);
    }
}