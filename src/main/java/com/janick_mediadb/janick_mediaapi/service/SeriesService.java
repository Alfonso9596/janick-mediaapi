package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.input.SeriesInput;
import com.janick_mediadb.janick_mediaapi.model.SeriesModel;
import com.janick_mediadb.janick_mediaapi.model.response.SeriesResponse;
import com.janick_mediadb.janick_mediaapi.model.response.SeriesSearchCriteria;
import com.janick_mediadb.janick_mediaapi.model.specifications.SeriesSpecification;
import com.janick_mediadb.janick_mediaapi.repository.SeriesRepository;
import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SeriesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeriesService.class);
    private static final String SERIES_FILES_PATH = "series/";

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeriesGenreXrefService seriesGenreXrefService;

    @Autowired
    private SeriesRatingXrefService seriesRatingXrefService;

    @Autowired
    private MovieGenreService genreService;

    public SeriesResponse getAllSeries(int page, int pageSize, String sortBy, String sortDir, SeriesSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<SeriesEntity> specification = createSpecs(criteria);

        Page<SeriesEntity> series = seriesRepository.findAll(specification, pageable);

        List<SeriesEntity> listOfSeries = series.getContent();
        List<SeriesModel> content = listOfSeries.stream().map(s -> {
            SeriesModel model = s.toModel();
            seriesGenreXrefService.collectGenres(s.getId(), model);
            seriesRatingXrefService.collectRatings(s.getId(), model);
            return model;
        }).toList();

        SeriesResponse seriesResponse = new SeriesResponse();
        seriesResponse.setContent(content);
        seriesResponse.setPage(series.getNumber());
        seriesResponse.setPageSize(series.getSize());
        seriesResponse.setTotalElements(series.getTotalElements());
        seriesResponse.setTotalPages(series.getTotalPages());
        seriesResponse.setLast(series.isLast());

        return seriesResponse;
    }

    public List<String> getAllSeriesNames() {
        List<String> seriesNames = seriesRepository.getAllSeriesNames();
        seriesNames.sort(Comparator.naturalOrder());
        return seriesNames;
    }

    public SeriesModel getSeriesById(int id) {
        Optional<SeriesEntity> opSeries = seriesRepository.findById(id);
        if (opSeries.isPresent()) {
            LOGGER.info("getSeriesById: Found series with id {}", id);
            SeriesModel model = opSeries.get().toModel();
            seriesGenreXrefService.collectGenres(model.getId(), model);
            seriesRatingXrefService.collectRatings(model.getId(), model);
            return model;
        } else {
            String message = MessageFormat.format("Series with id {0} does not exist", id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public SeriesModel saveSeries(SeriesInput seriesInput) {
        List<SeriesEntity> seriesEntities = getAllSeriesEntities();
        Optional<SeriesEntity> opSeries = seriesEntities.stream()
                .filter(series -> seriesInput.getName().equals(series.getName()))
                .filter(series -> seriesInput.getYearStart().equals(series.getYearStart()))
                .findAny();

        if (opSeries.isPresent()) {
            String message = MessageFormat.format("The series {0} is already registered", seriesInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        List<MovieGenreEntity> genreEntities = new ArrayList<>();
        if (!seriesInput.getGenres().isEmpty()) {
            for (String genre : seriesInput.getGenres()) {
                MovieGenreInput input = new MovieGenreInput();
                input.setName(genre);
                try {
                    genreService.saveGenre(input);
                } catch (BadRequestException e) {
                    LOGGER.warn("Genre {} already exists", genre);
                }
            }

            for (String genre : seriesInput.getGenres()) {
                MovieGenreEntity movieGenreEntity = genreService.getGenreByName(genre);
                genreEntities.add(movieGenreEntity);
            }
        }

        SeriesEntity seriesEntity = new SeriesEntity();
        seriesEntity.fromInput(seriesInput);

        String filename = NamingUtility.renameTitleForFilepath(seriesInput.getName()) + "_" + seriesInput.getYearStart();

        String posterFilename = SERIES_FILES_PATH + filename + "/" + filename + FileStorageServiceImpl.POSTER_FILE_TYPE;
        seriesEntity.setPosterFilepath(posterFilename);

        seriesEntity = seriesRepository.save(seriesEntity);
        LOGGER.info("saveSeries: Saving series {}", seriesEntity.toModel());
        seriesGenreXrefService.saveSeriesGenreXref(seriesEntity, genreEntities);

        return seriesEntity.toModel();
    }

    public String deleteSeries(int id) {
        Optional<SeriesEntity> opSeries = seriesRepository.findById(id);
        if (opSeries.isEmpty()) {
            String message = MessageFormat.format("Series with id {0} does not exist", id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        seriesGenreXrefService.deleteSeriesGenreReferenceBySeriesId(id);
        seriesRatingXrefService.deleteSeriesRatingReferenceBySeriesId(id);

        SeriesEntity seriesEntity = opSeries.get();
        LOGGER.info("deleteSeries: Deleting series {}", seriesEntity.toModel());
        seriesRepository.delete(seriesEntity);
        return MessageFormat.format("The series {0} has been deleted", seriesEntity.getName());
    }

    public String rateSeries(int id, int rating) {
        Optional<SeriesEntity> opSeries = seriesRepository.findById(id);
        if (opSeries.isEmpty()) {
            String message = MessageFormat.format("Series with id {0} does not exist", id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        SeriesEntity seriesEntity = opSeries.get();
        seriesRatingXrefService.addRating(seriesEntity, rating);
        return MessageFormat.format("{0} has been rated with {1}", seriesEntity.getName(), rating);
    }

    private List<SeriesEntity> getAllSeriesEntities() {
        return new ArrayList<>(seriesRepository.findAll());
    }

    private Specification<SeriesEntity> createSpecs(SeriesSearchCriteria criteria) {
        Specification<SeriesEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(SeriesSpecification.likeName(criteria.getName()));
        }

        if (criteria.getGenre() != null) {
            spec = spec.and(SeriesSpecification.containsGenre(criteria.getGenre()));
        }

        return spec;
    }
}
