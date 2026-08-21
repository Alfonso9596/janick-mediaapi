package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.auth.UserDetailsImpl;
import com.janick_mediadb.janick_mediaapi.entity.MusicEntity;
import com.janick_mediadb.janick_mediaapi.entity.MusicGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MusicRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.InternalServerException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.exception.UnauthorizedException;
import com.janick_mediadb.janick_mediaapi.input.GenreInput;
import com.janick_mediadb.janick_mediaapi.input.MusicInput;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.model.MusicModel;
import com.janick_mediadb.janick_mediaapi.model.RatingUpdateModel;
import com.janick_mediadb.janick_mediaapi.model.response.MusicResponse;
import com.janick_mediadb.janick_mediaapi.model.response.MusicSearchCriteria;
import com.janick_mediadb.janick_mediaapi.model.specifications.MusicSpecification;
import com.janick_mediadb.janick_mediaapi.repository.MusicRepository;
import com.janick_mediadb.janick_mediaapi.utils.FileUtility;
import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MusicService implements MediaService {

    @Value("${server.timezone}")
    private String timezone;

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicService.class);
    private static final String MUSIC_FILES_PATH = "music/";
    public static final String MUSIC_WITH_ID_DOES_NOT_EXIST = "Music with id {0} does not exist";

    private final MusicRepository musicRepository;

    private final MusicGenreXrefService musicGenreXrefService;

    private final MusicRatingXrefService musicRatingXrefService;

    private final MusicGenreService musicGenreService;

    private final UserService userService;

    @Autowired
    public MusicService(MusicRepository musicRepository, MusicGenreXrefService musicGenreXrefService, MusicRatingXrefService musicRatingXrefService, MusicGenreService musicGenreService, UserService userService) {
        this.musicRepository = musicRepository;
        this.musicGenreXrefService = musicGenreXrefService;
        this.musicRatingXrefService = musicRatingXrefService;
        this.musicGenreService = musicGenreService;
        this.userService = userService;
    }

    public MusicResponse getAllMusic(int page, int pageSize, String sortBy, String sortDir, MusicSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<MusicEntity> specification = createSpecs(criteria);

        Page<MusicEntity> music = musicRepository.findAll(specification, pageable);

        List<MusicEntity> listOfMusic = music.getContent();
        List<MusicModel> content = listOfMusic.stream().map(m -> {
            MusicModel model = m.toModel(timezone);
            musicGenreXrefService.collectGenres(m.getId(), model);
            musicRatingXrefService.collectRatings(m.getId(), model);
            return model;
        }).toList();

        MusicResponse musicResponse = new MusicResponse();
        musicResponse.setContent(content);
        musicResponse.setPage(music.getNumber());
        musicResponse.setPageSize(music.getSize());
        musicResponse.setTotalElements(music.getTotalElements());
        musicResponse.setTotalPages(music.getTotalPages());
        musicResponse.setLast(music.isLast());

        return musicResponse;
    }

    public MusicModel getMusicById(int id) {
        Optional<MusicEntity> opMusic = musicRepository.findById(id);
        if (opMusic.isPresent()) {
            LOGGER.info("getMusicById: Found music with id {}", id);
            MusicModel model = opMusic.get().toModel(timezone);
            musicGenreXrefService.collectGenres(model.getId(), model);
            musicRatingXrefService.collectRatings(model.getId(), model);
            return model;
        } else {
            String message = MessageFormat.format(MUSIC_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public MusicModel saveMusic(MusicInput musicInput) {
        List<MusicEntity> musicEntities = getAllMusicEntities();
        Optional<MusicEntity> opMusic = musicEntities.stream()
                .filter(music -> musicInput.getName().equals(music.getName()))
                .filter(music -> musicInput.getArtist().equals(music.getArtist()))
                .filter(music -> musicInput.getYear().equals(music.getYear()))
                .findAny();

        if (opMusic.isPresent()) {
            String message = MessageFormat.format("The music {0} is already registered", musicInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        List<MusicGenreEntity> genreEntities = new ArrayList<>();
        if (!musicInput.getGenres().isEmpty()) {
            for (String genre : musicInput.getGenres()) {
                GenreInput input = new GenreInput();
                input.setName(genre);
                try {
                    musicGenreService.saveGenre(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Genre {} already exists", genre);
                }
            }

            for (String genre : musicInput.getGenres()) {
                MusicGenreEntity musicGenreEntity = musicGenreService.getGenreByName(genre);
                genreEntities.add(musicGenreEntity);
            }
        }

        MusicEntity musicEntity = new MusicEntity();
        musicEntity.fromInput(musicInput);

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());
        musicEntity.setUser(user);

        String filename = NamingUtility.renameTitleForMusicFilepath(musicInput.getName(), musicInput.getArtist(), musicInput.getYear());

        String posterFilename = MUSIC_FILES_PATH + filename + "/" + filename + FileStorageServiceImpl.POSTER_FILE_TYPE;
        musicEntity.setPosterFilepath(posterFilename);

        Instant currentTime = ZonedDateTime.now(ZoneId.of(timezone)).toInstant();

        musicEntity.setCreatedAt(currentTime);
        musicEntity.setLastUpdated(currentTime);

        musicEntity = musicRepository.save(musicEntity);
        LOGGER.info("saveMusic: Saving music {}", musicEntity.toModel(timezone));
        musicGenreXrefService.saveMusicGenreXref(musicEntity, genreEntities);

        return musicEntity.toModel(timezone);
    }

    public MusicModel updateMusic(int id, MusicInput musicInput) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());

        MusicEntity musicEntity = musicRepository.findById(id).orElseThrow(() -> new NotFoundException("Game with id " + id + " not found!"));

        if (musicEntity.getUser().getId() != user.getId() && user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN"))) {
            throw new UnauthorizedException("User " + user.getUsername() + " is not allowed to update music with id " + id);
        }

        List<MusicGenreEntity> genreEntities = new ArrayList<>();
        if (!musicInput.getGenres().isEmpty()) {
            for (String genre : musicInput.getGenres()) {
                GenreInput input = new GenreInput();
                input.setName(genre);
                try {
                    musicGenreService.saveGenre(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Genre {} already exists", genre);
                }
            }

            for (String genre : musicInput.getGenres()) {
                MusicGenreEntity musicGenreEntity = musicGenreService.getGenreByName(genre);
                genreEntities.add(musicGenreEntity);
            }
        }

        musicGenreXrefService.deleteMusicGenreReferenceByMusicId(id);
        mapToEntity(musicEntity, musicInput);

        Instant currentTime = ZonedDateTime.now(ZoneId.of(timezone)).toInstant();
        musicEntity.setLastUpdated(currentTime);
        musicEntity = musicRepository.save(musicEntity);
        LOGGER.info("updateMusic: Updating music {}", musicEntity.toModel(timezone));
        musicGenreXrefService.saveMusicGenreXref(musicEntity, genreEntities);

        return musicEntity.toModel(timezone);
    }

    public ResponseEntity<String> deleteMusic(int id) {
        Optional<MusicEntity> opMusic = musicRepository.findById(id);
        if (opMusic.isEmpty()) {
            String message = MessageFormat.format(MUSIC_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        MusicEntity musicEntity = opMusic.get();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId() != musicEntity.getUser().getId() && principal.getAuthorities().stream().noneMatch(role -> Objects.equals(role.getAuthority(), "ADMIN"))) {
            throw new UnauthorizedException("User " + principal.getUsername() + " is not allowed to delete music " + musicEntity.getName());
        }

        musicGenreXrefService.deleteMusicGenreReferenceByMusicId(id);
        musicRatingXrefService.deleteMusicRatingReferenceByMusicId(id);

        LOGGER.info("deleteMusic: Deleting music {}", musicEntity.toModel(timezone));
        musicRepository.delete(musicEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MessageFormat.format("The music {0} gas been deleted", musicEntity.getName()));
    }

    public String rateMusic(RatingUpdateModel ratingUpdateModel) {
        Optional<MusicEntity> opMusic = musicRepository.findById(ratingUpdateModel.getId());
        if (opMusic.isEmpty()) {
            String message = MessageFormat.format(MUSIC_WITH_ID_DOES_NOT_EXIST, ratingUpdateModel.getId());
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        MusicEntity musicEntity = opMusic.get();
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<MusicRatingXrefEntity> existingRating = musicRatingXrefService.findByMusicIdAndUser(musicEntity.getId(), userDetails.getId());
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());

        if (existingRating.isEmpty()) {
            musicRatingXrefService.addRating(musicEntity, user, ratingUpdateModel.getRating());
        } else {
            musicRatingXrefService.updateRating(existingRating.get(), ratingUpdateModel.getRating());
        }

        return MessageFormat.format("{0} has been rated with {1}", musicEntity.getName(), ratingUpdateModel.getRating());
    }

    public ResponseEntity<List<FileInfoModel>> getMusicFiles(int id) {
        Optional<MusicEntity> opMusic = musicRepository.findById(id);
        if (opMusic.isEmpty()) {
            String message = MessageFormat.format(MUSIC_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        MusicEntity musicEntity = opMusic.get();
        String fileStorageName = NamingUtility.renameTitleForMusicFilepath(musicEntity.getName(), musicEntity.getArtist(), musicEntity.getYear());
        Path filePath = FileStorageServiceImpl.music.resolve(fileStorageName).resolve("files");

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
        } catch (IOException e) {
            throw new InternalServerException("Could not create directory " + filePath, e);
        }
        List<FileInfoModel> fileInfoModels = FileUtility.getDirList(filePath.toFile());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileInfoModels);
    }

    @Override
    public Path getMediaFilePath(int mediaId) {
        Optional<MusicEntity> opMusic = musicRepository.findById(mediaId);
        if (opMusic.isEmpty()) {
            String message = MessageFormat.format(MUSIC_WITH_ID_DOES_NOT_EXIST, mediaId);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        MusicEntity musicEntity = opMusic.get();
        String fileStorageName = NamingUtility.renameTitleForMusicFilepath(musicEntity.getName(), musicEntity.getArtist(), musicEntity.getYear());
        return FileStorageServiceImpl.music.resolve(fileStorageName).resolve("files");
    }

    private List<MusicEntity> getAllMusicEntities() {
        return new ArrayList<>(musicRepository.findAll());
    }

    private Specification<MusicEntity> createSpecs(MusicSearchCriteria criteria) {
        Specification<MusicEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(MusicSpecification.likeName(criteria.getName()));
        }

        if (criteria.getArtist() != null) {
            spec = spec.and(MusicSpecification.likeArtist(criteria.getArtist()));
        }

        if (criteria.getYear() > 0) {
            spec = spec.and(MusicSpecification.equalsYear(criteria.getYear()));
        }

        if (criteria.getGenre() != null) {
            spec = spec.and(MusicSpecification.containsGenre(criteria.getGenre()));
        }

        return spec;
    }

    private void mapToEntity(MusicEntity existingMusic, MusicInput musicInput) {
        if (musicInput.getName() != null && !musicInput.getName().isEmpty()) {
            existingMusic.setName(musicInput.getName());
        }
        if (musicInput.getArtist() != null && !musicInput.getArtist().isEmpty()) {
            existingMusic.setArtist(musicInput.getArtist());
        }
        if (musicInput.getYear() != null && !musicInput.getYear().isEmpty()) {
            existingMusic.setYear(musicInput.getYear());
        }
        if (musicInput.getDescription() != null && !musicInput.getDescription().isEmpty()) {
            existingMusic.setDescription(musicInput.getDescription());
        }
    }
}
