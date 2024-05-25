package honest.niceman.it.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import honest.niceman.it.example.dto.UserDto;
import honest.niceman.it.example.maper.UserMapper;
import honest.niceman.it.example.model.User;
import honest.niceman.it.example.filter.UserFilter;
import honest.niceman.it.example.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    public UserService(UserMapper userMapper,
                       UserRepository userRepository,
                       ObjectMapper objectMapper) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public Page<UserDto> getList(UserFilter filter,
                                 Pageable pageable) {
        Specification<User> spec = filter.toSpecification();
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(userMapper::toDto);
    }

    public UserDto getOne(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userMapper.toDto(userOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<UserDto> getMany(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto create(UserDto dto) {
        User user = userMapper.toEntity(dto);
        User resultUser = userRepository.save(user);
        return userMapper.toDto(resultUser);
    }

    public UserDto patch(Long id,
                         JsonNode patchNode) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        UserDto userDto = userMapper.toDto(user);
        objectMapper.readerForUpdating(userDto)
                .readValue(patchNode);
        userMapper.updateWithNull(userDto, user);

        User resultUser = userRepository.save(user);
        return userMapper.toDto(resultUser);
    }

    public List<Long> patchMany(List<Long> ids,
                                JsonNode patchNode) throws IOException {
        Collection<User> users = userRepository.findAllById(ids);

        for (User user : users) {
            UserDto userDto = userMapper.toDto(user);
            objectMapper.readerForUpdating(userDto)
                    .readValue(patchNode);
            userMapper.updateWithNull(userDto, user);
        }

        List<User> resultUsers = userRepository.saveAll(users);
        return resultUsers.stream()
                .map(User::getId)
                .toList();
    }

    public UserDto delete(Long id) {
        User user = userRepository.findById(id)
                .orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return userMapper.toDto(user);
    }

    public void deleteMany(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }
}
