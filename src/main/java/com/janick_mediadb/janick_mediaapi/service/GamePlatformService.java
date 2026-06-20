package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.GamePlatformEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.input.GamePlatformInput;
import com.janick_mediadb.janick_mediaapi.model.GamePlatformModel;
import com.janick_mediadb.janick_mediaapi.model.response.GamePlatformResponse;
import com.janick_mediadb.janick_mediaapi.model.response.GamePlatformSearchCriteria;
import com.janick_mediadb.janick_mediaapi.repository.GamePlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GamePlatformService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamePlatformService.class);

    private static final String PLATFORM_NAME_NOT_EXIST = "Platform with name {0} does not exist";
    private static final String PLATFORM_ID_NOT_EXIST = "Platform with id {0} does not exist";
    private static final String PLATFORM_ALREADY_REGISTERED = "The platform {0} is already registered";
    private static final String PLATFORM_REFERENCED_BY_GAMES = "The platform {0} cannot be deleted, because there are still games referenced with this platform";

    private final GamePlatformRepository gamePlatformRepository;
    private final GamePlatformXrefService gamePlatformXrefService;

    @Autowired
    public GamePlatformService(GamePlatformRepository gamePlatformRepository,  GamePlatformXrefService gamePlatformXrefService) {
        this.gamePlatformRepository = gamePlatformRepository;
        this.gamePlatformXrefService = gamePlatformXrefService;
    }

    public GamePlatformResponse getPageablePlatforms(int page, int pageSize, String sortBy, String sortDir, GamePlatformSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<GamePlatformEntity> specification = createSpecs(criteria);

        Page<GamePlatformEntity> platforms = gamePlatformRepository.findAll(specification, pageable);

        List<GamePlatformEntity> listOfPlatforms = platforms.getContent();
        List<GamePlatformModel> content = listOfPlatforms.stream().map(GamePlatformEntity::toModel).toList();

        GamePlatformResponse platformResponse = new GamePlatformResponse();
        platformResponse.setContent(content);
        platformResponse.setPage(platforms.getNumber());
        platformResponse.setPageSize(platforms.getSize());
        platformResponse.setTotalElements(platforms.getTotalElements());
        platformResponse.setTotalPages(platforms.getTotalPages());
        platformResponse.setLast(platforms.isLast());

        return platformResponse;
    }

    public List<GamePlatformModel> getAllPlatforms() {
        List<GamePlatformModel> platforms = new ArrayList<>();
        gamePlatformRepository.findAllOrderByName().forEach(platform -> platforms.add(platform.toModel()));

        platforms.sort(Comparator.comparing(GamePlatformModel::getName));

        LOGGER.info("getAllPlatforms: Found a total of {} platforms.", platforms.size());
        return platforms;
    }

    public GamePlatformEntity getPlatformByName(String name) {
        Optional<GamePlatformEntity> opPlatform = gamePlatformRepository.findByName(name);
        if (opPlatform.isPresent()) {
            LOGGER.info("getPlatformByName: Found platform with name {}", name);
            return opPlatform.get();
        } else {
            String message = MessageFormat.format(PLATFORM_NAME_NOT_EXIST, name);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
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

    public String deletePlatform(int id) {
        Optional<GamePlatformEntity> opPlatform = gamePlatformRepository.findById(id);
        if (opPlatform.isEmpty()) {
            String message = MessageFormat.format(PLATFORM_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        List<GameEntity> gameEntities = gamePlatformXrefService.findGamesByPlatform(id);
        if (!gameEntities.isEmpty()) {
            String message = MessageFormat.format(PLATFORM_REFERENCED_BY_GAMES, opPlatform.get().getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        GamePlatformEntity gamePlatformEntity = opPlatform.get();
        LOGGER.info("deletePlatform: Deleting platform {}", gamePlatformEntity.toModel());
        gamePlatformRepository.delete(gamePlatformEntity);
        return MessageFormat.format("The platform {0} has been deleted", gamePlatformEntity.getName());
    }

    public ResponseEntity<String> updatePlatform(int id, GamePlatformInput gamePlatformInput) {
        GamePlatformEntity platformEntity = gamePlatformRepository.findById(id).orElseThrow(() -> new NotFoundException("Platform with id " + id + " not found"));
        platformEntity.setId(id);
        platformEntity.setName(gamePlatformInput.getName());

        gamePlatformRepository.save(platformEntity);

        return ResponseEntity.ok().body("Game platform has been updated successfully");
    }

    private List<GamePlatformEntity> getAllPlatformEntities() {
        return new ArrayList<>(gamePlatformRepository.findAllOrderByName());
    }

    private Specification<GamePlatformEntity> createSpecs(GamePlatformSearchCriteria criteria) {
        Specification<GamePlatformEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(((root, _, criteriaBuilder) ->  criteriaBuilder.like(root.get("name"), "%" + criteria.getName() + "%")));
        }

        return spec;
    }
}
