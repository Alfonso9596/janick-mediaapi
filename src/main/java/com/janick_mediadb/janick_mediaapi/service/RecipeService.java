package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.auth.UserDetailsImpl;
import com.janick_mediadb.janick_mediaapi.entity.MealTypeEntity;
import com.janick_mediadb.janick_mediaapi.entity.RecipeEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.RecipeRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.InternalServerException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.exception.UnauthorizedException;
import com.janick_mediadb.janick_mediaapi.input.MealTypeInput;
import com.janick_mediadb.janick_mediaapi.input.RecipeInput;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.model.RatingUpdateModel;
import com.janick_mediadb.janick_mediaapi.model.RecipeModel;
import com.janick_mediadb.janick_mediaapi.model.response.RecipeResponse;
import com.janick_mediadb.janick_mediaapi.model.response.RecipeSearchCriteria;
import com.janick_mediadb.janick_mediaapi.model.specifications.RecipeSpecification;
import com.janick_mediadb.janick_mediaapi.repository.RecipeRepository;
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
public class RecipeService implements MediaService {

    @Value("${server.timezone}")
    private String timezone;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);
    private static final String RECIPE_FILES_PATH = "recipes/";
    public static final String RECIPE_WITH_ID_DOES_NOT_EXIST = "Recipe with id {0} does not exist";

    private final RecipeRepository recipeRepository;

    private final RecipeMealTypeXrefService recipeMealTypeXrefService;

    private final RecipeRatingXrefService recipeRatingXrefService;

    private final MealTypeService mealTypeService;

    private final UserService userService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, RecipeMealTypeXrefService recipeMealTypeXrefService, RecipeRatingXrefService recipeRatingXrefService, MealTypeService mealTypeService, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.recipeMealTypeXrefService = recipeMealTypeXrefService;
        this.recipeRatingXrefService = recipeRatingXrefService;
        this.mealTypeService = mealTypeService;
        this.userService = userService;
    }

    public RecipeResponse getAllRecipes(int page, int pageSize, String sortBy, String sortDir, RecipeSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<RecipeEntity> specification = createSpecs(criteria);

        Page<RecipeEntity> recipes = recipeRepository.findAll(specification, pageable);

        List<RecipeEntity> listOfRecipes = recipes.getContent();
        List<RecipeModel> content = listOfRecipes.stream().map(r -> {
            RecipeModel model = r.toModel(timezone);
            recipeMealTypeXrefService.collectMealTypes(r.getId(), model);
            recipeRatingXrefService.collectRatings(r.getId(), model);
            return model;
        }).toList();

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setContent(content);
        recipeResponse.setPage(recipes.getNumber());
        recipeResponse.setPageSize(recipes.getSize());
        recipeResponse.setTotalElements(recipes.getTotalElements());
        recipeResponse.setTotalPages(recipes.getTotalPages());
        recipeResponse.setLast(recipes.isLast());

        return recipeResponse;
    }

    public RecipeModel getRecipeById(int id) {
        Optional<RecipeEntity> opRecipe = recipeRepository.findById(id);
        if (opRecipe.isPresent()) {
            LOGGER.info("getRecipeById: Found recipe with id {}", id);
            RecipeModel model = opRecipe.get().toModel(timezone);
            recipeMealTypeXrefService.collectMealTypes(id, model);
            recipeRatingXrefService.collectRatings(id, model);
            return model;
        } else {
            String message = MessageFormat.format(RECIPE_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public RecipeModel saveRecipe(RecipeInput recipeInput) {
        List<RecipeEntity> recipeEntities = getAllRecipeEntities();
        Optional<RecipeEntity> opRecipe = recipeEntities.stream()
                .filter(recipe -> recipeInput.getName().equals(recipe.getName()))
                .findAny();

        if (opRecipe.isPresent()) {
            String message = MessageFormat.format("The recipe {0} is already registered", recipeInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        List<MealTypeEntity> mealTypeEntities = new ArrayList<>();
        if (!recipeInput.getMealTypes().isEmpty()) {
            for (String mealType : recipeInput.getMealTypes()) {
                MealTypeInput input =  new MealTypeInput();
                input.setName(mealType);
                try {
                    mealTypeService.saveMealType(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Meal type {} already exists", mealType);
                }
            }

            for (String mealType : recipeInput.getMealTypes()) {
                MealTypeEntity mealTypeEntity = mealTypeService.getMealTypeByName(mealType);
                mealTypeEntities.add(mealTypeEntity);
            }
        }

        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.fromInput(recipeInput);

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());
        recipeEntity.setUser(user);

        String filename = NamingUtility.returnCleanFilepathValue(recipeInput.getName());

        String posterFilename = RECIPE_FILES_PATH + filename + "/" + filename + FileStorageServiceImpl.POSTER_FILE_TYPE;
        recipeEntity.setPosterFilepath(posterFilename);

        Instant currentTime = ZonedDateTime.now(ZoneId.of(timezone)).toInstant();

        recipeEntity.setCreatedAt(currentTime);
        recipeEntity.setLastUpdated(currentTime);

        recipeEntity = recipeRepository.save(recipeEntity);
        LOGGER.info("saveRecipe: Saved recipe {}", recipeEntity.toModel(timezone));
        recipeMealTypeXrefService.saveRecipeMealTypeXref(recipeEntity, mealTypeEntities);

        return recipeEntity.toModel(timezone);
    }

    public RecipeModel updateRecipe(int id, RecipeInput recipeInput) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());

        RecipeEntity recipeEntity = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Recipe with id " + id + " not found!"));

        if (recipeEntity.getUser().getId() != user.getId() && user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN"))) {
            throw new UnauthorizedException("User " + user.getUsername() + " is not allowed to update recipe with id " + id);
        }

        List<MealTypeEntity> mealTypeEntities = new ArrayList<>();
        if (!recipeInput.getMealTypes().isEmpty()) {
            for (String mealType : recipeInput.getMealTypes()) {
                MealTypeInput input =  new MealTypeInput();
                input.setName(mealType);
                try {
                    mealTypeService.saveMealType(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Meal type {} already exists", mealType);
                }
            }

            for (String mealType : recipeInput.getMealTypes()) {
                MealTypeEntity typeEntity = mealTypeService.getMealTypeByName(mealType);
                mealTypeEntities.add(typeEntity);
            }
        }
        recipeMealTypeXrefService.deleteMealTypeReferenceByRecipeId(id);
        mapToEntity(recipeEntity, recipeInput);

        Instant currentTime = ZonedDateTime.now(ZoneId.of(timezone)).toInstant();
        recipeEntity.setLastUpdated(currentTime);
        recipeEntity = recipeRepository.save(recipeEntity);
        LOGGER.info("updateRecipe: Updating recipe {}", recipeEntity.toModel(timezone));
        recipeMealTypeXrefService.saveRecipeMealTypeXref(recipeEntity, mealTypeEntities);

        return recipeEntity.toModel(timezone);
    }

    private void mapToEntity(RecipeEntity existingRecipe, RecipeInput recipeInput) {
        if (recipeInput.getName() != null && !recipeInput.getName().isEmpty()) {
            existingRecipe.setName(recipeInput.getName());
        }
        if (recipeInput.getDescription() != null && !recipeInput.getDescription().isEmpty()) {
            existingRecipe.setDescription(recipeInput.getDescription());
        }
        existingRecipe.setVegetarian(recipeInput.isVegetarian());
        existingRecipe.setVegan(recipeInput.isVegan());
        existingRecipe.setGlutenfree(recipeInput.isGlutenfree());
        existingRecipe.setLactosefree(recipeInput.isLactosefree());
    }

    public ResponseEntity<String> deleteRecipe(int id) {
        Optional<RecipeEntity> opRecipe = recipeRepository.findById(id);
        if (opRecipe.isEmpty()) {
            String message = MessageFormat.format(RECIPE_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        RecipeEntity recipeEntity = opRecipe.get();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId() != recipeEntity.getUser().getId() && principal.getAuthorities().stream().noneMatch(role -> Objects.equals(role.getAuthority(), "ADMIN"))) {
            throw new UnauthorizedException("User " + principal.getUsername() + " is not allowed to delete recipe " + recipeEntity.getName());
        }

        recipeMealTypeXrefService.deleteMealTypeReferenceByRecipeId(id);
        recipeRatingXrefService.deleteRecipeRatingReferenceByRecipeId(id);

        LOGGER.info("deleteRecipe: Deleting recipe {}", recipeEntity.toModel(timezone));
        recipeRepository.delete(recipeEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MessageFormat.format("The recipe {0} has been deleted", recipeEntity.getName()));
    }

    public String rateRecipe(RatingUpdateModel ratingUpdateModel) {
        Optional<RecipeEntity> opRecipe = recipeRepository.findById(ratingUpdateModel.getId());
        if (opRecipe.isEmpty()) {
            String message = MessageFormat.format(RECIPE_WITH_ID_DOES_NOT_EXIST, ratingUpdateModel.getId());
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        RecipeEntity recipeEntity = opRecipe.get();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<RecipeRatingXrefEntity> existingRating = recipeRatingXrefService.findByRecipeIdAndUserId(recipeEntity.getId(), principal.getId());
        UsersEntity user = userService.getUserByUsername(principal.getUsername());

        if (existingRating.isEmpty()) {
            recipeRatingXrefService.addRating(recipeEntity, user, ratingUpdateModel.getRating());
        } else {
            recipeRatingXrefService.updateRating(existingRating.get(), ratingUpdateModel.getRating());
        }

        return MessageFormat.format("{0} has been rated with {1}", recipeEntity.getName(), ratingUpdateModel.getRating());
    }

    public ResponseEntity<List<FileInfoModel>> getRecipeFiles(int id) {
        Optional<RecipeEntity> opRecipe = recipeRepository.findById(id);
        if (opRecipe.isEmpty()) {
            String message = MessageFormat.format(RECIPE_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        RecipeEntity recipeEntity = opRecipe.get();
        String fileStorageName = NamingUtility.returnCleanFilepathValue(recipeEntity.getName());
        Path filePath = FileStorageServiceImpl.recipes.resolve(fileStorageName).resolve("files");

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
        } catch (IOException e) {
            throw new InternalServerException("Could not create directory " + filePath, e);
        }
        List<FileInfoModel> fileInfoModels = FileUtility.getDirList(filePath.toFile());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfoModels);
    }

    @Override
    public Path getMediaFilePath(int mediaId) {
        Optional<RecipeEntity> opRecipe = recipeRepository.findById(mediaId);
        if (opRecipe.isEmpty()) {
            String message = MessageFormat.format(RECIPE_WITH_ID_DOES_NOT_EXIST, mediaId);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        RecipeEntity recipeEntity = opRecipe.get();
        String fileStorageName = NamingUtility.returnCleanFilepathValue(recipeEntity.getName());
        return FileStorageServiceImpl.recipes.resolve(fileStorageName).resolve("files");
    }

    private List<RecipeEntity> getAllRecipeEntities() {
        return new ArrayList<>(recipeRepository.findAll());
    }

    private Specification<RecipeEntity> createSpecs(RecipeSearchCriteria criteria) {
        Specification<RecipeEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(RecipeSpecification.likeName(criteria.getName()));
        }

        if (criteria.getMealType() != null) {
            spec = spec.and(RecipeSpecification.containsMealType(criteria.getMealType()));
        }
        if (criteria.getVegetarian() != null) {
            spec = spec.and(RecipeSpecification.isVegetarian(criteria.getVegetarian()));
        }
        if (criteria.getVegan() != null) {
            spec = spec.and(RecipeSpecification.isVegan(criteria.getVegan()));
        }
        if (criteria.getGlutenfree() != null) {
            spec = spec.and(RecipeSpecification.isGlutenfree(criteria.getGlutenfree()));
        }
        if (criteria.getLactosefree() != null) {
            spec = spec.and(RecipeSpecification.isLactosefree(criteria.getLactosefree()));
        }

        return spec;
    }
}
