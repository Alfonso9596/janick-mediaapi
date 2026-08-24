package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MusicEntity;
import com.janickmediadb.janickmediaapi.entity.MusicGenreEntity;
import com.janickmediadb.janickmediaapi.exception.BadRequestException;
import com.janickmediadb.janickmediaapi.exception.NotFoundException;
import com.janickmediadb.janickmediaapi.input.GenreInput;
import com.janickmediadb.janickmediaapi.model.GenreModel;
import com.janickmediadb.janickmediaapi.model.response.GenreResponse;
import com.janickmediadb.janickmediaapi.model.response.GenreSearchCriteria;
import com.janickmediadb.janickmediaapi.repository.MusicGenreRepository;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

@Service
public class MusicGenreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicGenreService.class);

    private static final String GENRE_NAME_NOT_EXIST = "Genre with name {0} does not exist";
    private static final String GENRE_ID_NOT_EXIST = "Genre with id {0} does not exist";
    private static final String GENRE_ALREADY_REGISTERED = "The genre {0} is already registered";
    private static final String GENRE_REFERENCED_BY_MUSIC = "The genre {0} cannot be deleted, because there is still music referenced with this genre";

    private final MusicGenreRepository musicGenreRepository;

    private final MusicGenreXrefService musicGenreXrefService;

    @Autowired
    public MusicGenreService(MusicGenreRepository musicGenreRepository, MusicGenreXrefService musicGenreXrefService) {
        this.musicGenreRepository = musicGenreRepository;
        this.musicGenreXrefService = musicGenreXrefService;
    }

    public GenreResponse getPageableGenres(int page, int pageSize, String sortBy, String sortDir, GenreSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<MusicGenreEntity> specification = createSpecs(criteria);

        Page<MusicGenreEntity> genres = musicGenreRepository.findAll(specification, pageable);

        List<MusicGenreEntity> listOfGenres = genres.getContent();
        List<GenreModel> content = listOfGenres.stream().map(MusicGenreEntity::toModel).toList();

        GenreResponse genreResponse = new GenreResponse();
        genreResponse.setContent(content);
        genreResponse.setPage(genres.getNumber());
        genreResponse.setPageSize(genres.getSize());
        genreResponse.setTotalElements(genres.getTotalElements());
        genreResponse.setTotalPages(genres.getTotalPages());
        genreResponse.setLast(genres.isLast());

        return genreResponse;
    }

    public List<GenreModel> getAllGenres() {
        List<GenreModel> genres = new ArrayList<>();
        musicGenreRepository.findAll().forEach(genre -> genres.add(genre.toModel()));

        genres.sort(Comparator.comparing(GenreModel::getName));

        LOGGER.info("getAllGenres: Found a total of {} genres", genres.size());
        return genres;
    }

    public MusicGenreEntity getGenreByName(String name) {
        Optional<MusicGenreEntity> opGenre = musicGenreRepository.findByName(name);
        if (opGenre.isPresent()) {
            LOGGER.info("getGenreByName: Found genre with name {}", name);
            return opGenre.get();
        } else {
            String message = MessageFormat.format(GENRE_NAME_NOT_EXIST, name);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public GenreModel getGenreById(int id) {
        Optional<MusicGenreEntity> opGenre = musicGenreRepository.findById(id);
        if (opGenre.isPresent()) {
            LOGGER.info("getGenreById: Found genre with id {}", id);
            return opGenre.get().toModel();
        } else {
            String message = MessageFormat.format(GENRE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public GenreModel saveGenre(GenreInput genreInput) {
        List<MusicGenreEntity> genres = getAllGenreEntities();
        Optional<MusicGenreEntity> op = genres.stream()
                .filter(genreEntity -> genreInput.getName().equalsIgnoreCase(genreEntity.getName()))
                .findAny();

        if (op.isPresent()) {
            String message = MessageFormat.format(GENRE_ALREADY_REGISTERED, genreInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        MusicGenreEntity musicGenreEntity = new MusicGenreEntity();
        musicGenreEntity.fromInput(genreInput);

        LOGGER.info("saveGenre: Saving genre {}", musicGenreEntity.toModel());

        return musicGenreRepository.save(musicGenreEntity).toModel();
    }

    public String deleteGenre(int id) {
        Optional<MusicGenreEntity> opGenre = musicGenreRepository.findById(id);
        if (opGenre.isEmpty()) {
            String message = MessageFormat.format(GENRE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        List<MusicEntity> musicEntities = musicGenreXrefService.findMusicByGenre(id);
        if (!musicEntities.isEmpty()) {
            String message = MessageFormat.format(GENRE_REFERENCED_BY_MUSIC, opGenre.get().getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        MusicGenreEntity musicGenreEntity = opGenre.get();
        LOGGER.info("deleteGenre: Deleting genre {}", musicGenreEntity.toModel());
        musicGenreRepository.delete(musicGenreEntity);
        return MessageFormat.format("The genre {0} has been deleted", musicGenreEntity.getName());
    }

    public ResponseEntity<String> updateGenre(int id, GenreInput genreInput) {
        MusicGenreEntity genreEntity = musicGenreRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre with id " + id + " not found"));
        genreEntity.setId(id);
        genreEntity.setName(genreInput.getName());

        musicGenreRepository.save(genreEntity);

        return ResponseEntity.ok().body("Music genre has been updated successfully");
    }

    private List<MusicGenreEntity> getAllGenreEntities() {
        return new ArrayList<>(musicGenreRepository.findAll());
    }

    private Specification<MusicGenreEntity> createSpecs(GenreSearchCriteria criteria) {
        Specification<MusicGenreEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(((root, _, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + criteria.getName() + "%")));
        }

        return spec;
    }
}
