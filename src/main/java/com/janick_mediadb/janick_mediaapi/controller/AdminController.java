package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.model.response.GameResponse;
import com.janick_mediadb.janick_mediaapi.model.response.MovieResponse;
import com.janick_mediadb.janick_mediaapi.model.response.SeriesResponse;
import com.janick_mediadb.janick_mediaapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
