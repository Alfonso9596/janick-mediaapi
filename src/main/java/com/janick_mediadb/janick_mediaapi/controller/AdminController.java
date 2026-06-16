package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.model.RoleModel;
import com.janick_mediadb.janick_mediaapi.model.response.*;
import com.janick_mediadb.janick_mediaapi.service.AdminService;
import com.janick_mediadb.janick_mediaapi.utils.AppConstants;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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

    @GetMapping("/users")
    public UserResponse getAllUsers(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE, required = false) int page,
            @RequestParam(value  ="pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @ParameterObject UserSearchCriteria criteria) {
        return adminService.getAllUsers(page, pageSize, criteria);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleModel>> getAllRoles() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminService.getAllRoles());
    }
}
