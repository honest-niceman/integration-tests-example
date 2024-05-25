package honest.niceman.it.example.controller;

import com.fasterxml.jackson.databind.JsonNode;
import honest.niceman.it.example.dto.UserDto;
import honest.niceman.it.example.filter.UserFilter;
import honest.niceman.it.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserDto> getList(@ModelAttribute UserFilter filter,
                                 Pageable pageable) {
        return userService.getList(filter, pageable);
    }

    @GetMapping("/{id}")
    public UserDto getOne(@PathVariable Long id) {
        return userService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<UserDto> getMany(@RequestParam List<Long> ids) {
        return userService.getMany(ids);
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@PathVariable Long id,
                         @RequestBody JsonNode patchNode) throws IOException {
        return userService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids,
                                @RequestBody JsonNode patchNode) throws IOException {
        return userService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        userService.deleteMany(ids);
    }
}
