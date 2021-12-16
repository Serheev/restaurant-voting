package com.serheev.restaurant.web;

import com.serheev.restaurant.AuthUser;
import com.serheev.restaurant.model.Role;
import com.serheev.restaurant.model.User;
import com.serheev.restaurant.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.EnumSet;
import java.util.List;

import static com.serheev.restaurant.util.ValidationUtil.assureIdConsistent;
import static com.serheev.restaurant.util.ValidationUtil.checkNew;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Do not use {@link org.springframework.data.rest.webmvc.RepositoryRestController (BasePathAwareController}
 * Bugs:
 * NPE with http://localhost:8080/api/account<br>
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/434">data.rest.base-path missed in HAL links</a><br>
 * <a href="https://jira.spring.io/browse/DATAREST-748">Two endpoints created</a>
 * <p>
 * RequestMapping("/${spring.data.rest.basePath}/account") give "Not enough variable values"
 */
@RestController
@RequestMapping(AccountController.URL)
@AllArgsConstructor
@Slf4j
@Tag(name = "Account Controller")
public class AccountController implements RepresentationModelProcessor<RepositoryLinksResource> {
    static final String URL = "/api";

    @SuppressWarnings("unchecked")
    private static final RepresentationModelAssemblerSupport<User, EntityModel<User>> ASSEMBLER =
            new RepresentationModelAssemblerSupport<>(AccountController.class, (Class<EntityModel<User>>) (Class<?>) EntityModel.class) {
                @Override
                public EntityModel<User> toModel(User user) {
                    return EntityModel.of(user, linkTo(AccountController.class).withSelfRel());
                }
            };

    private final UserRepository userRepository;

    @Operation(summary = "Get user profile data", description = "Return logged in user data.")
    @GetMapping(value = "/profile", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<User> get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return ASSEMBLER.toModel(authUser.getUser());
    }

    @Operation(summary = "Get all user data", description = "")
    @GetMapping(value = "/admin/users", produces = MediaTypes.HAL_JSON_VALUE)
    public List<User> getAll() {
        log.info("getAll");
        return userRepository.findAll();
    }

    @Operation(summary = "Delete own profile data", description = "This can only be done by the logged in user.")
    @DeleteMapping(value = "/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", key = "#authUser.username")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser);
        userRepository.deleteById(authUser.id());
    }

    @Operation(summary = "Register a new user", description = "This can be done by an anonymous user.")
    @PostMapping(value = "/profile/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<EntityModel<User>> register(@Valid @RequestBody User user) {
        log.info("register {}", user);
        checkNew(user);
        user.setRoles(EnumSet.of(Role.USER));
        user = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(ASSEMBLER.toModel(user));
    }

    @Operation(summary = "Update user profile", description = "This can only be done by the logged in user.")
    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CachePut(value = "users", key = "#authUser.username")
    public User update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} to {}", authUser, user);
        User oldUser = authUser.getUser();
        assureIdConsistent(user, oldUser.id());
        user.setRoles(oldUser.getRoles());
        if (user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        }
        return userRepository.save(user);
    }

/*
    @GetMapping(value = "/pageDemo", produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<User>> pageDemo(Pageable page, PagedResourcesAssembler<User> pagedAssembler) {
        Page<User> users = userRepository.findAll(page);
        return pagedAssembler.toModel(users, ASSEMBLER);
    }
*/

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.add(linkTo(AccountController.class).withRel("profile"));
        return resource;
    }
}
