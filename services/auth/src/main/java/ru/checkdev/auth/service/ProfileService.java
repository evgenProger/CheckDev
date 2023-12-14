package ru.checkdev.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.dto.ProfileDTO;
import ru.checkdev.auth.dto.ProfileTgDTO;
import ru.checkdev.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

/**
 * CheckDev пробное собеседование
 * Класс получения ProfileDTO
 *
 * @author Dmitry Stepanov
 * @version 22.09.2023'T'23:41
 */

@Service
@AllArgsConstructor
@Slf4j
public class ProfileService {
    private final PersonRepository personRepository;
    private final PasswordEncoder encoding = new BCryptPasswordEncoder();

    /**
     * Получить ProfileDTO по ID
     *
     * @param id int
     * @return ProfileDTO
     */
    public Optional<ProfileDTO> findProfileByID(int id) {
        return Optional.ofNullable(personRepository.findProfileById(id));
    }

    /**
     * Получить ProfileTgDTO по ID
     *
     * @param id int
     * @return ProfileDTO
     */
    public Optional<ProfileTgDTO> findProfileTgByID(int id) {
        return Optional.ofNullable(personRepository.findProfileTgById(id));
    }

    public Optional<ProfileTgDTO> findProfileTgByEmailAndPassword(String email, String password) {
        Optional<ProfileTgDTO> result = Optional.empty();
        Profile profile = personRepository.findByEmail(email);
        if (profile != null && encoding.matches(password, profile.getPassword())) {
            result = Optional.of(new ProfileTgDTO(
                    profile.getId(),
                    profile.getUsername(),
                    profile.getEmail()));
        }
        return result;
    }

    /**
     * Получить список всех PersonDTO
     *
     * @return List<PersonDTO>
     */
    public List<ProfileDTO> findProfilesOrderByCreatedDesc() {
        return personRepository.findProfileOrderByCreatedDesc();
    }

    /**
     * Разрешить подписки для пользователя с ID
     * Profile.notification = true
     *
     * @param id int
     */
    public void notification(int id) {
        personRepository.setNotification(id, true);
    }

    /**
     * Запретить подписки для пользователя с ID
     * Profile.notification = false
     *
     * @param id int
     */
    public void unNotification(int id) {
        personRepository.setNotification(id, false);
    }
}
