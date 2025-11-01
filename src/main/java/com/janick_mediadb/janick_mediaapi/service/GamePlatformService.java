package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.GamePlatformEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.input.GamePlatformInput;
import com.janick_mediadb.janick_mediaapi.model.GamePlatformModel;
import com.janick_mediadb.janick_mediaapi.repository.GamePlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GamePlatformService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamePlatformService.class);

    private static final String PLATFORM_ALREADY_REGISTERED = "The platform {0} is already registered";

    @Autowired
    private GamePlatformRepository gamePlatformRepository;

    public List<GamePlatformModel> getAllPlatforms() {
        List<GamePlatformModel> platforms = new ArrayList<>();
        gamePlatformRepository.findAllOrderByName().forEach(platform -> platforms.add(platform.toModel()));

        LOGGER.info("getAllPlatforms: Found a total of {} platforms.", platforms.size());
        return platforms;
    }

    public GamePlatformModel savePlatform(GamePlatformInput gamePlatformInput) {
        List<GamePlatformEntity> platforms = getAllPlatformEntities();
        Optional<GamePlatformEntity> op = platforms.stream()
                .filter(platformEntity -> gamePlatformInput.getName().equalsIgnoreCase(platformEntity.getName()))
                .findAny();

        if (op.isPresent()) {
            String message = MessageFormat.format(PLATFORM_ALREADY_REGISTERED, gamePlatformInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        GamePlatformEntity gamePlatformEntity = new GamePlatformEntity();
        gamePlatformEntity.fromInput(gamePlatformInput);

        LOGGER.info("savePlatform: Saving platform {}", gamePlatformEntity.toModel());

        return gamePlatformRepository.save(gamePlatformEntity).toModel();
    }

    private List<GamePlatformEntity> getAllPlatformEntities() {
        return new ArrayList<>(gamePlatformRepository.findAllOrderByName());
    }
}
