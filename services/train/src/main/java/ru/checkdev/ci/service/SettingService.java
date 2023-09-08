package ru.checkdev.ci.service;

import ru.checkdev.ci.domain.Setting;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface SettingService {
    List<Setting> getAll();

    Setting save(Setting setting);

    void delete(int id);

    Setting findOne(int id);
}
