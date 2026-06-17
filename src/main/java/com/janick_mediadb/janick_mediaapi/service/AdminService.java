package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.auth.UserDetailsImpl;
import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.RoleEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.input.admin.UserInput;
import com.janick_mediadb.janick_mediaapi.model.*;
import com.janick_mediadb.janick_mediaapi.model.response.*;
import com.janick_mediadb.janick_mediaapi.model.specifications.UserSpecification;
import com.janick_mediadb.janick_mediaapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    private final MovieRepository movieRepository;

    private final SeriesRepository seriesRepository;

    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final MovieGenreXrefService movieGenreXrefService;

    private final MovieRatingXrefService movieRatingXrefService;

    private final SeriesGenreXrefService seriesGenreXrefService;

    private final SeriesRatingXrefService seriesRatingXrefService;

    private final GameGenreXrefService gameGenreXrefService;

    private final GameRatingXrefService gameRatingXrefService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(MovieRepository movieRepository, SeriesRepository seriesRepository, GameRepository gameRepository, UserRepository userRepository, RoleRepository roleRepository, MovieGenreXrefService movieGenreXrefService, MovieRatingXrefService movieRatingXrefService, SeriesGenreXrefService seriesGenreXrefService, SeriesRatingXrefService seriesRatingXrefService, GameGenreXrefService gameGenreXrefService, GameRatingXrefService gameRatingXrefService, PasswordEncoder passwordEncoder) {
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.movieGenreXrefService = movieGenreXrefService;
        this.movieRatingXrefService = movieRatingXrefService;
        this.seriesGenreXrefService = seriesGenreXrefService;
        this.seriesRatingXrefService = seriesRatingXrefService;
        this.gameGenreXrefService = gameGenreXrefService;
        this.gameRatingXrefService = gameRatingXrefService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> createUser(UserInput userInput) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userInput.getUsername()))) {
            throw new BadRequestException("Username already exists!");
        }

        UsersEntity user = new UsersEntity();
        user.setUsername(userInput.getUsername());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        user.setEnabled(userInput.isEnabled());

        Set<RoleEntity> roles = userInput.getRoles().stream().map(r -> roleRepository.findByName(r).orElseThrow(() -> new NotFoundException("Role with name " + r + " not found!"))).collect(Collectors.toSet());
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    public ResponseEntity<String> deleteUser(int id, UserDetailsImpl principal) {
        if (!userRepository.existsById(id)) {
            throw new BadRequestException("User with id " + id + " not found!");
        }
        if (principal.getId() == id) {
            throw new BadRequestException("Can not delete your own user!");
        }

        LOGGER.info("DELETED ROLE REFERENCES");

        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User has been deleted successfully");
    }

    public ResponseEntity<String> updateUser(int id, UserInput userInput) {
        UsersEntity usersEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found!"));
        usersEntity = mapToEntity(usersEntity, userInput);
        usersEntity.setId(id);

        userRepository.save(usersEntity);

        return ResponseEntity.ok().body("User has been updated successfully");
    }

    private UsersEntity mapToEntity(UsersEntity existingUser, UserInput userInput) {
        UsersEntity user = new UsersEntity();

        if (userInput.getUsername() != null &&  !userInput.getUsername().isEmpty()) {
            user.setUsername(userInput.getUsername());
        } else {
            user.setUsername(existingUser.getUsername());
        }
        if (userInput.getPassword() != null && !userInput.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }

        Set<RoleEntity> roles = userInput.getRoles().stream().map(r -> roleRepository.findByName(r).orElseThrow(() -> new NotFoundException("Role with name " + r + " not found!"))).collect(Collectors.toSet());
        user.setRoles(roles);

        user.setEnabled(userInput.isEnabled());

        return user;
    }

    public MovieResponse getMoviesData() {
        List<MovieEntity> movies =  movieRepository.findAll();
        List<MovieModel> content = movies.stream().map(m -> {
            MovieModel model = m.toModel();
            movieGenreXrefService.collectGenres(m.getId(), model);
            movieRatingXrefService.collectRatings(m.getId(), model);
            return model;
        }).toList();

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setContent(content);
        movieResponse.setTotalElements(movies.size());

        return movieResponse;
    }

    public SeriesResponse getSeriesData() {
        List<SeriesEntity> series =  seriesRepository.findAll();
        List<SeriesModel> content = series.stream().map(s -> {
            SeriesModel model = s.toModel();
            seriesGenreXrefService.collectGenres(s.getId(), model);
            seriesRatingXrefService.collectRatings(s.getId(), model);
            return model;
        }).toList();

        SeriesResponse seriesResponse = new SeriesResponse();
        seriesResponse.setContent(content);
        seriesResponse.setTotalElements(series.size());

        return seriesResponse;
    }

    public GameResponse getGamesData() {
        List<GameEntity> games = gameRepository.findAll();
        List<GameModel> content = games.stream().map(g -> {
            GameModel model = g.toModel();
            gameGenreXrefService.collectGenres(g.getId(), model);
            gameRatingXrefService.collectRatings(g.getId(), model);
            return model;
        }).toList();

        GameResponse gameResponse = new GameResponse();
        gameResponse.setContent(content);
        gameResponse.setTotalElements(games.size());

        return gameResponse;
    }

    public UserResponse getAllUsers(int page, int pageSize, UserSearchCriteria criteria) {
        Sort sort = Sort.by("username").ascending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<UsersEntity> specification = createSpecs(criteria);

        Page<UsersEntity> users = userRepository.findAll(specification, pageable);

        List<UsersEntity> listOfUsers = users.getContent();
        List<UserModel> content = listOfUsers.stream().map(UsersEntity::toModel).toList();

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(content);
        userResponse.setPage(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    public List<RoleModel> getAllRoles() {
        List<RoleModel> roles = new ArrayList<>();
        roleRepository.findAllOrderByName().forEach(role -> roles.add(role.toModel()));

        LOGGER.info("getAllRoles: Found a total of {} roles.", roles.size());
        return roles;
    }

    private Specification<UsersEntity> createSpecs(UserSearchCriteria criteria) {
        Specification<UsersEntity> spec = Specification.unrestricted();

        if (criteria.getUsername() != null) {
            spec = spec.and(UserSpecification.likeUsername(criteria.getUsername()));
        }

        if (criteria.getRole() != null) {
            spec = spec.and(UserSpecification.containsRole(criteria.getRole()));
        }

        return spec;
    }
}
