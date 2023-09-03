package ru.job4j.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.interview.domain.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static ru.job4j.interview.service.ValidateResult.Filter.of;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class ValidateResult {

    private final Map<Filter, BiFunction<IValue, TPredict, Track.Key>> dispatch = new HashMap<>();
    private final TAlgoService algos;

    @Autowired
    public ValidateResult(final TAlgoService algos) {
        this.init();
        this.algos = algos;
    }

    /**
     * Load
     * EQ, NOT_EQ, CONTAINS, NOT_CONTAINS, GREAT, LESS
     */
    public void init() {
        this.loadEq();
        this.loadNotEq();
        this.loadContains();
        this.loadNotContains();
        this.loadGreat();
        this.loadLess();
    }

    private void loadGreat() {
        this.dispatch.put(
                of(TPredict.Key.GREAT, Task.Type.SINGLE_LINE),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.GREAT, Task.Type.AREA),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.GREAT, Task.Type.INT),
                this.greatInt()
        );
        this.dispatch.put(
                of(TPredict.Key.GREAT, Task.Type.DATE),
                this.eqDate()
        );
        this.dispatch.put(
                of(TPredict.Key.GREAT, Task.Type.LIST),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.GREAT, Task.Type.MULTILIST),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.GREAT, Task.Type.TASK),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.GREAT, Task.Type.TEST),
                this.greatTest()
        );
    }

    private void loadNotContains() {
        this.dispatch.put(
                of(TPredict.Key.NOT_CONTAINS, Task.Type.SINGLE_LINE),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_CONTAINS, Task.Type.AREA),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_CONTAINS, Task.Type.INT),
                this.eqInt()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_CONTAINS, Task.Type.DATE),
                this.eqDate()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_CONTAINS, Task.Type.LIST),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_CONTAINS, Task.Type.MULTILIST),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_CONTAINS, Task.Type.TASK),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_CONTAINS, Task.Type.TEST),
                this.eqText()
        );
    }

    private void loadContains() {
        this.dispatch.put(
                of(TPredict.Key.CONTAINS, Task.Type.SINGLE_LINE),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.CONTAINS, Task.Type.AREA),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.CONTAINS, Task.Type.INT),
                this.eqInt()
        );
        this.dispatch.put(
                of(TPredict.Key.CONTAINS, Task.Type.DATE),
                this.eqDate()
        );
        this.dispatch.put(
                of(TPredict.Key.CONTAINS, Task.Type.LIST),
                this.containList()
        );
        this.dispatch.put(
                of(TPredict.Key.CONTAINS, Task.Type.MULTILIST),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.CONTAINS, Task.Type.TASK),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.CONTAINS, Task.Type.TEST),
                this.eqText()
        );
    }

    private void loadNotEq() {
        this.dispatch.put(
                of(TPredict.Key.NOT_EQ, Task.Type.SINGLE_LINE),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_EQ, Task.Type.AREA),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_EQ, Task.Type.INT),
                this.eqInt()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_EQ, Task.Type.DATE),
                this.eqDate()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_EQ, Task.Type.LIST),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_EQ, Task.Type.MULTILIST),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_EQ, Task.Type.TASK),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.NOT_EQ, Task.Type.TEST),
                this.eqText()
        );
    }

    private void loadLess() {
        this.dispatch.put(
                of(TPredict.Key.LESS, Task.Type.SINGLE_LINE),
                this.lessText()
        );
        this.dispatch.put(
                of(TPredict.Key.LESS, Task.Type.AREA),
                this.lessText()
        );
        this.dispatch.put(
                of(TPredict.Key.LESS, Task.Type.INT),
                this.lessInt()
        );
        this.dispatch.put(
                of(TPredict.Key.LESS, Task.Type.DATE),
                this.lessText()
        );
        this.dispatch.put(
                of(TPredict.Key.LESS, Task.Type.TEST),
                this.lessTest()
        );
    }

    /**
     *  Load handlers for equals.
     */
    private void loadEq() {
        this.dispatch.put(
                of(TPredict.Key.EQ, Task.Type.SINGLE_LINE),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.EQ, Task.Type.AREA),
                this.eqText()
        );
        this.dispatch.put(
                of(TPredict.Key.EQ, Task.Type.INT),
                this.eqInt()
        );
        this.dispatch.put(
                of(TPredict.Key.EQ, Task.Type.DATE),
                this.eqDate()
        );
        this.dispatch.put(
                of(TPredict.Key.EQ, Task.Type.LIST),
                this.eqList()
        );
        this.dispatch.put(
                of(TPredict.Key.EQ, Task.Type.MULTILIST),
                this.eqList()
        );
        this.dispatch.put(
                of(TPredict.Key.EQ, Task.Type.TASK),
                this.eqTask()
        );
        this.dispatch.put(
                of(TPredict.Key.EQ, Task.Type.TEST),
                this.eqTest()
        );
    }

    public BiFunction<IValue, TPredict, Track.Key> lessText() {
        return (predict, value) -> {
            Track.Key rsl = Track.Key.PASSED;
            if (value.getValue().compareTo(predict.getValue()) > 0) {
                rsl = Track.Key.FAILURE;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> eqText() {
        return (predict, value) -> {
            Track.Key rsl = Track.Key.FAILURE;
            if (value.getValue().equals(predict.getValue())) {
                rsl = Track.Key.SUCCESS;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> greatTest() {
        return (value, predict) -> {
            Track.Key rsl = Track.Key.FAILURE;
            if (Integer.valueOf(value.getValue()) > Integer.valueOf(predict.getValue())) {
                rsl = Track.Key.PASSED;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> lessTest() {
        return (value, predict) -> {
            Track.Key rsl = Track.Key.FAILURE;
            if (Integer.valueOf(value.getValue()) < Integer.valueOf(predict.getValue())) {
                rsl = Track.Key.PASSED;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> containList() {
        return (value, predict) -> {
            Track.Key rsl = Track.Key.FAILURE;
            String selected = value.getTask().getTask().getValues().stream().filter(
                    v -> String.valueOf(v.getId()).equals(value.getValue())
            ).findFirst().get().getValue();
            if (predict.getValue().contains(selected)) {
                rsl = Track.Key.PASSED;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> eqList() {
        return (value, predict) -> {
            Track.Key rsl = Track.Key.FAILURE;
            String selected = value.getTask().getTask().getValues().stream().filter(
                    v -> String.valueOf(v.getId()).equals(value.getValue())
            ).findFirst().get().getValue();
            if (selected.equals(predict.getValue())) {
                rsl = Track.Key.PASSED;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> eqTask() {
        return (value, predict) -> {
            int taskId = Integer.valueOf(
                    value.getTask().getTask().getValues().iterator().next().getValue()
            );
            TAlgo algo = this.algos.findOne(taskId);
            LoadTest.ResultInfo info = new Compile().task(
                    new Compile.Source(algo.getSname(), value.getValue()),
                    new Compile.Source(algo.getTname(), algo.getTest())
            );
            Track.Key rsl = Track.Key.FAILURE;
            if (String.valueOf(info.isSuccess()).equals(predict.getValue())) {
                rsl = Track.Key.PASSED;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> eqTest() {
        return (predict, value) -> {
            Track.Key rsl = Track.Key.PASSED;
            if (value.getValue().equals(predict.getValue())) {
                rsl = Track.Key.FAILURE;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> greatInt() {
        return (value, predict) -> {
            Track.Key rsl = Track.Key.FAILURE;
            if ((Integer.valueOf(value.getValue()) > (Integer.valueOf(predict.getValue())))) {
                rsl = Track.Key.PASSED;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> lessInt() {
        return (value, predict) -> {
            Track.Key rsl = Track.Key.FAILURE;
            if ((Integer.valueOf(value.getValue()) < (Integer.valueOf(predict.getValue())))) {
                rsl = Track.Key.PASSED;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> eqInt() {
        return (predict, value) -> {
            Track.Key rsl = Track.Key.FAILURE;
            if ((Integer.valueOf(value.getValue()).equals(Integer.valueOf(predict.getValue())))) {
                rsl = Track.Key.PASSED;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> notEqInt() {
        return (predict, value) -> {
            Track.Key rsl = Track.Key.PASSED;
            if (!(Integer.valueOf(value.getValue()).equals(Integer.valueOf(predict.getValue())))) {
                rsl = Track.Key.FAILURE;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> notEqDate() {
        return (predict, value) -> {
            Track.Key rsl = Track.Key.PASSED;
            if (!(Long.valueOf(value.getValue()).equals(Long.valueOf(predict.getValue())))) {
                rsl = Track.Key.FAILURE;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> eqDate() {
        return (predict, value) -> {
            Track.Key rsl = Track.Key.PASSED;
            if ((Long.valueOf(value.getValue()).equals(Long.valueOf(predict.getValue())))) {
                rsl = Track.Key.FAILURE;
            }
            return  rsl;
        };
    }

    public BiFunction<IValue, TPredict, Track.Key> notEqText() {
        return (predict, value) -> {
            Track.Key rsl = Track.Key.PASSED;
            if (!value.getValue().equals(predict.getValue())) {
                rsl = Track.Key.FAILURE;
            }
            return  rsl;
        };
    }

    /**
     * Validate value from interview by prediction
     * @param itask values
     * @param task tasks
     * @return Passed or failure
     */
    public Track.Key by(ITask itask, Task task) {
        Track.Key result = Track.Key.PASSED;
        for (IValue value : itask.getValues()) {
            for (TPredict predict : task.getFilters()) {
                if (!dispatch.isEmpty() && dispatch.get(
                        of(predict.getKey(), task.getType())
                ).apply(value, predict) == Track.Key.FAILURE) {
                    result = Track.Key.FAILURE;
                    break;
                }
            }
            if (result == Track.Key.FAILURE) {
                break;
            }
        }
        return result;
    }

    public static class Filter {
        private final TPredict.Key predict;
        private final Task.Type type;

        Filter(final TPredict.Key predict, final Task.Type type) {
            this.predict = predict;
            this.type = type;
        }

        public static Filter of(final TPredict.Key predict, final Task.Type type) {
            return new Filter(predict, type);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filter filter = (Filter) o;

            if (predict != filter.predict) return false;
            return type == filter.type;
        }

        @Override
        public int hashCode() {
            int result = predict != null ? predict.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            return result;
        }
    }
}
