/**
 *
 */
package ru.job4j.notification.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.notification.domain.Setting;

/**
 * @author olegbelov
 *
 */
public interface SettingRepository extends CrudRepository<Setting, Integer> {
    Setting findByKey(Setting.Key key);
}
