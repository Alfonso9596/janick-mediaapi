package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.auth.UserDetailsImpl;
import com.janickmediadb.janickmediaapi.input.admin.UserInput;
import com.janickmediadb.janickmediaapi.model.RoleModel;
import com.janickmediadb.janickmediaapi.model.response.GameResponse;
import com.janickmediadb.janickmediaapi.model.response.MovieResponse;
import com.janickmediadb.janickmediaapi.model.response.MusicResponse;
import com.janickmediadb.janickmediaapi.model.response.RecipeResponse;
import com.janickmediadb.janickmediaapi.model.response.SeriesResponse;
import com.janickmediadb.janickmediaapi.model.response.UserResponse;
import com.janickmediadb.janickmediaapi.model.response.UserSearchCriteria;
import com.janickmediadb.janickmediaapi.service.AdminService;
import com.janickmediadb.janickmediaapi.utils.AppConstants;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
