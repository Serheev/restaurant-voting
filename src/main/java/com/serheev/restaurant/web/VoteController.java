package com.serheev.restaurant.web;

import com.serheev.restaurant.AuthUser;
import com.serheev.restaurant.error.NotFoundException;
import com.serheev.restaurant.model.Restaurant;
import com.serheev.restaurant.model.User;
import com.serheev.restaurant.model.Vote;
import com.serheev.restaurant.repository.RestaurantRepository;
import com.serheev.restaurant.repository.UserRepository;
import com.serheev.restaurant.repository.VoteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.serheev.restaurant.util.VoteUtil.ifChangeAvailable;

@RestController
@RequestMapping(value = VoteController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Vote Controller")
public class VoteController {
    static final String URL = "/api/user/restaurant/vote";

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Operation(summary = "Vote for restaurant with id")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Vote> addVote(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Voting user {} for restaurant {}", authUser.id(), restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant not found: id=" + restaurantId));
        User user = userRepository.findById(authUser.id()).orElseThrow(() -> new NotFoundException("User not found"));
        Vote vote = new Vote(LocalDate.now(), null, null);
        vote.setRestaurant(restaurant);
        vote.setUser(user);
        Vote voted = voteRepository.save(vote);
        return ResponseEntity.ok().body(voted);
    }

    @Operation(summary = "Change vote for restaurant with id")
    @PutMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void changeVote(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Change vote user {} for restaurant {}", authUser.id(), restaurantId);
        ifChangeAvailable(LocalTime.now());
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant not found: id=" + restaurantId));
        Vote vote = voteRepository.findByDate(LocalDate.now(), authUser.id()).orElseThrow(() -> new NotFoundException("Vote not Found"));
        vote.setRestaurant(restaurant);
        voteRepository.save(vote);
    }

}
