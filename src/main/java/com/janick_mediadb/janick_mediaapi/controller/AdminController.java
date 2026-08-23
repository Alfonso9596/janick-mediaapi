package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.auth.UserDetailsImpl;
import com.janick_mediadb.janick_mediaapi.input.admin.UserInput;
import com.janick_mediadb.janick_mediaapi.model.RoleModel;
import com.janick_mediadb.janick_mediaapi.model.response.*;
import com.janick_mediadb.janick_mediaapi.service.AdminService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/movies")
    public MovieResponse getMoviesData() {
        return adminService.getMoviesData();
    }

    @GetMapping("/series")
    public SeriesResponse getSeriesData() {
        return adminService.getSeriesData();
    }

    @GetMapping("/games")
    public GameResponse getGamesData() {
        return adminService.getGamesData();
    }

    @GetMapping("/music")
    public MusicResponse getMusicData() {
        return adminService.getMusicData();
    }

    @GetMapping("/recipes")
    public RecipeResponse getRecipesData() {
        return adminService.getRecipesData();
    }

    @GetMapping("/users")
    public UserResponse getPageableUsers(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @ParameterObject UserSearchCriteria criteria) {
        return adminService.getPageableUsers(page, pageSize, sortBy, sortDir, criteria);
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserInput userInput) {
        LOGGER.info(userInput.toString());
        return adminService.createUser(userInput);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        UserDetailsImpl principal =  (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return adminService.deleteUser(id, principal);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody UserInput userInput) {
        LOGGER.info(userInput.toString());
        return adminService.updateUser(id, userInput);
    }

    @GetMapping("/roles/list")
    public ResponseEntity<List<RoleModel>> getAllRoles() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminService.getAllRoles());
    }
}
