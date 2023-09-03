package ru.job4j.ci.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Setting;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface SettingRepository extends CrudRepository<Setting, Integer> {
    Setting findByType(Setting.Type type);
}
