package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MovieEntity;
import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import com.janickmediadb.janickmediaapi.entity.SeriesEntity;
import com.janickmediadb.janickmediaapi.exception.BadRequestException;
import com.janickmediadb.janickmediaapi.exception.NotFoundException;
import com.janickmediadb.janickmediaapi.input.GenreInput;
import com.janickmediadb.janickmediaapi.model.GenreModel;
import com.janickmediadb.janickmediaapi.model.response.GenreResponse;
import com.janickmediadb.janickmediaapi.model.response.GenreSearchCriteria;
import com.janickmediadb.janickmediaapi.repository.MovieGenreRepository;
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
public class MovieGenreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieGenreService.class);

    private static final String GENRE_NAME_NOT_EXIST = "Genre with name {0} does not exist";
    private static final String GENRE_ID_NOT_EXIST = "Genre with id {0} does not exist";
    private static final String GENRE_ALREADY_REGISTERED = "The genre {0} is already registered";

    private final MovieGenreRepository movieGenreRepository;

    private final MovieGenreXrefService movieGenreXrefService;

    private final SeriesGenreXrefService seriesGenreXrefService;

    @Autowired
    public MovieGenreService(MovieGenreRepository movieGenreRepository, MovieGenreXrefService movieGenreXrefService, SeriesGenreXrefService seriesGenreXrefService) {
        this.movieGenreRepository = movieGenreRepository;
        this.movieGenreXrefService = movieGenreXrefService;
        this.seriesGenreXrefService = seriesGenreXrefService;
    }

    public GenreResponse getPageableGenres(int page, int pageSize, String sortBy, String sortDir, GenreSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<MovieGenreEntity> specification = createSpecs(criteria);

        Page<MovieGenreEntity> genres = movieGenreRepository.findAll(specification, pageable);

        List<MovieGenreEntity> listOfGenres = genres.getContent();
        List<GenreModel> content = listOfGenres.stream().map(MovieGenreEntity::toModel).toList();

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
        movieGenreRepository.findAllOrderByName().forEach(genre -> genres.add(genre.toModel()));

        genres.sort(Comparator.comparing(GenreModel::getName));

        LOGGER.info("getAllGenres: Found a total of {} genres.", genres.size());
        return genres;
    }

    public MovieGenreEntity getGenreByName(String name) {
        Optional<MovieGenreEntity> opGenre = movieGenreRepository.findByName(name);
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
        Optional<MovieGenreEntity> opGenre = movieGenreRepository.findById(id);
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
        List<MovieGenreEntity> genres = getAllGenreEntities();
        Optional<MovieGenreEntity> op = genres.stream()
                .filter(genreEntity -> genreInput.getName().equalsIgnoreCase(genreEntity.getName()))
                .findAny();

        if (op.isPresent()) {
            String message = MessageFormat.format(GENRE_ALREADY_REGISTERED, genreInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        MovieGenreEntity movieGenreEntity = new MovieGenreEntity();
        movieGenreEntity.fromInput(genreInput);

        LOGGER.info("saveGenre: Saving genre {}", movieGenreEntity.toModel());

        return movieGenreRepository.save(movieGenreEntity).toModel();
    }

    public String deleteGenre(int id) {
        Optional<MovieGenreEntity> opGenre = movieGenreRepository.findById(id);
        if (opGenre.isEmpty()) {
            String message = MessageFormat.format(GENRE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        List<MovieEntity> movieEntities = movieGenreXrefService.findMoviesByGenre(id);
        List<SeriesEntity> seriesEntities = seriesGenreXrefService.findSeriesByGenre(id);
        if (!movieEntities.isEmpty() || !seriesEntities.isEmpty()) {
            String message = MessageFormat.format("Genre {0} cannot be deleted, because there are still movies/series with this genre", opGenre.get().getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        MovieGenreEntity movieGenreEntity = opGenre.get();
        LOGGER.info("deleteGenre: Deleting genre {}", movieGenreEntity.toModel());
        movieGenreRepository.delete(movieGenreEntity);
        return MessageFormat.format("The genre {0} has been deleted", movieGenreEntity.getName());
    }

    public ResponseEntity<String> updateGenre(int id, GenreInput genreInput) {
        MovieGenreEntity genreEntity = movieGenreRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre with id " + id + " not found!"));
        genreEntity.setId(id);
        genreEntity.setName(genreInput.getName());

        movieGenreRepository.save(genreEntity);

        return ResponseEntity.ok().body("Movie genre has been updated successfully");
    }

    private List<MovieGenreEntity> getAllGenreEntities() {
        return new ArrayList<>(movieGenreRepository.findAll());
    }

    private Specification<MovieGenreEntity> createSpecs(GenreSearchCriteria criteria) {
        Specification<MovieGenreEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(((root, _, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + criteria.getName() + "%")));
        }

        return spec;
    }

}
