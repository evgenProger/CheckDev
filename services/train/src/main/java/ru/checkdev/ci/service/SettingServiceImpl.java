package ru.checkdev.ci.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.checkdev.ci.domain.Setting;
import ru.checkdev.ci.repository.SettingRepository;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

@Service
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settings;

    @Autowired
    public SettingServiceImpl(SettingRepository settings) {
        this.settings = settings;
    }

    @Override
    public List<Setting> getAll() {
        return Lists.newArrayList(this.settings.findAll());
    }

    @Override
    public Setting save(Setting setting) {
        this.settings.save(setting);
        return setting;
    }

    @Override
    public void delete(int id) {
        this.settings.deleteById(id);
    }

    @Override
    public Setting findOne(int id) {
        return this.settings.findById(id).get();
    }
}
