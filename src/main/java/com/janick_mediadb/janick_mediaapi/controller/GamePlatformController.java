package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.input.GamePlatformInput;
import com.janick_mediadb.janick_mediaapi.model.GamePlatformModel;
import com.janick_mediadb.janick_mediaapi.service.GamePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platforms")
public class GamePlatformController {

    private final GamePlatformService gamePlatformService;

    @Autowired
    public  GamePlatformController(GamePlatformService gamePlatformService) {
        this.gamePlatformService = gamePlatformService;
    }

    @GetMapping
    public ResponseEntity<List<GamePlatformModel>> getAllPlatforms() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gamePlatformService.getAllPlatforms());
    }

    @PostMapping
    public ResponseEntity<GamePlatformModel> savePlatform(
            @RequestBody GamePlatformInput gamePlatformInput) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gamePlatformService.savePlatform(gamePlatformInput));
    }
}
