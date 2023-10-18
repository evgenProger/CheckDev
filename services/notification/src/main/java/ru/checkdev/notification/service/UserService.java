package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.Category;
import ru.checkdev.notification.domain.User;
import ru.checkdev.notification.dto.UserDTO;
import ru.checkdev.notification.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final CategoryService categories;

    public User save(User user) {
        System.out.println("вызываю метод save user UserService"); ////////////
        return repository.save(user);
    }

    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    public UserDTO saveSubscribeCategories(UserDTO userDTO) {
        User user;
        if (findById(userDTO.getId()).isEmpty()) {
            user = new User();
            user.setId(userDTO.getId());
        } else {
            user = findById(userDTO.getId()).get();
        }
        List<Category> list = user.getSubscribeCategoriesList();
        List<Integer> listDTO = userDTO.getSubscribeCategoryIds();
        for (int categoryId : listDTO) {
            Optional<Category> categoryOptional = categories.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                Category category = new Category();
                category.setId(categoryId);
                categories.save(category);
                list.add(category);
            } else {
                if (!list.contains(categoryOptional.get())) {
                    list.add(categoryOptional.get());
                }
            }
        }
        save(user);
        return userDTO;
    }

    public UserDTO deleteSubscribeCategories(UserDTO userDTO) {
        User user = findById(userDTO.getId()).get();
        List<Category> list = user.getSubscribeCategoriesList();
        int categoryId = userDTO.getSubscribeCategoryIds().get(0);
        Category category = categories.findById(categoryId).get();
        list.remove(category);
        save(user);
        return userDTO;
    }

    public UserDTO getUserDTOById(int id) {
        User user;
        UserDTO userDTO = new UserDTO();
        if (findById(id).isEmpty()) {
            List<Category> list = new ArrayList<>();
            user = new User(id, list);
            save(user);
        } else {
            user = findById(id).get();
        }
        userDTO.setId(id);
        List<Integer> listUserDTO = userDTO.getSubscribeCategoryIds(); ///////
        for (Category category : user.getSubscribeCategoriesList()) {
            listUserDTO.add(category.getId());
        }
            //   userDTO.setSubscribeCategoryIds(listUserDTO);  ///////
        return userDTO;
    }
}
