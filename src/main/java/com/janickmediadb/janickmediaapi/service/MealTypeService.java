package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MealTypeEntity;
import com.janickmediadb.janickmediaapi.entity.RecipeEntity;
import com.janickmediadb.janickmediaapi.exception.BadRequestException;
import com.janickmediadb.janickmediaapi.exception.NotFoundException;
import com.janickmediadb.janickmediaapi.input.MealTypeInput;
import com.janickmediadb.janickmediaapi.model.MealTypeModel;
import com.janickmediadb.janickmediaapi.model.response.MealTypeResponse;
import com.janickmediadb.janickmediaapi.model.response.MealTypeSearchCriteria;
import com.janickmediadb.janickmediaapi.repository.MealTypeRepository;
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
public class MealTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MealTypeService.class);

    private static final String MEAL_TYPE_NAME_NOT_EXIST = "Meal type with name {0} does not exist";
    private static final String MEAL_TYPE_ID_NOT_EXIST = "Meal type with id {0} does not exist";
    private static final String MEAL_TYPE_ALREADY_REGISTERED = "The meal type {0} is already registered";

    private final MealTypeRepository mealTypeRepository;

    private final RecipeMealTypeXrefService recipeMealTypeXrefService;

    @Autowired
    public MealTypeService(MealTypeRepository mealTypeRepository, RecipeMealTypeXrefService recipeMealTypeXrefService) {
        this.mealTypeRepository = mealTypeRepository;
        this.recipeMealTypeXrefService = recipeMealTypeXrefService;
    }

    public MealTypeResponse getPageableMealTypes(int page, int pageSize, String sortBy, String sortDir, MealTypeSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<MealTypeEntity> specification = createSpecs(criteria);

        Page<MealTypeEntity> mealTypes = mealTypeRepository.findAll(specification, pageable);

        List<MealTypeEntity> listOfMealTypes = mealTypes.getContent();
        List<MealTypeModel> content = listOfMealTypes.stream().map(MealTypeEntity::toModel).toList();

        MealTypeResponse mealTypeResponse = new MealTypeResponse();
        mealTypeResponse.setContent(content);
        mealTypeResponse.setPage(mealTypes.getNumber());
        mealTypeResponse.setPageSize(mealTypes.getSize());
        mealTypeResponse.setTotalElements(mealTypes.getTotalElements());
        mealTypeResponse.setTotalPages(mealTypes.getTotalPages());
        mealTypeResponse.setLast(mealTypes.isLast());

        return mealTypeResponse;
    }

    public List<MealTypeModel> getAllMealTypes() {
        List<MealTypeModel> mealTypes = new ArrayList<>();
        mealTypeRepository.findAllOrderByName().forEach(mealType -> mealTypes.add(mealType.toModel()));

        mealTypes.sort(Comparator.comparing(MealTypeModel::getName));

        LOGGER.info("getAllMealTypes: Found a total of {} meal types.", mealTypes.size());
        return mealTypes;
    }

    public MealTypeEntity getMealTypeByName(String name) {
        Optional<MealTypeEntity> opMealType = mealTypeRepository.findByName(name);
        if (opMealType.isPresent()) {
            LOGGER.info("getMealTypeByName: Found a meal type with name {}", name);
            return opMealType.get();
        } else {
            String message = MessageFormat.format(MEAL_TYPE_NAME_NOT_EXIST, name);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public MealTypeModel getMealTypeById(int id) {
        Optional<MealTypeEntity> opMealType = mealTypeRepository.findById(id);
        if (opMealType.isPresent()) {
            LOGGER.info("getMealTypeById: Found a meal type with id {}", id);
            return opMealType.get().toModel();
        } else {
            String message = MessageFormat.format(MEAL_TYPE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public MealTypeModel saveMealType(MealTypeInput mealTypeInput) {
        List<MealTypeEntity> mealTypes = getAllMealTypeEntities();
        Optional<MealTypeEntity> op = mealTypes.stream()
                .filter(mealTypeEntity -> mealTypeInput.getName().equalsIgnoreCase(mealTypeEntity.getName()))
                .findAny();

        if (op.isPresent()) {
            String message = MessageFormat.format(MEAL_TYPE_ALREADY_REGISTERED, mealTypeInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        MealTypeEntity mealTypeEntity = new MealTypeEntity();
        mealTypeEntity.fromInput(mealTypeInput);

        LOGGER.info("saveMealType: Saving meal type {}", mealTypeEntity.toModel());

        return mealTypeRepository.save(mealTypeEntity).toModel();
    }

    public String deleteMealType(int id) {
        Optional<MealTypeEntity> opMealType = mealTypeRepository.findById(id);
        if (opMealType.isEmpty()) {
            String message = MessageFormat.format(MEAL_TYPE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        List<RecipeEntity> recipeEntities = recipeMealTypeXrefService.findRecipesByMealType(id);
        if (!recipeEntities.isEmpty()) {
            String message = MessageFormat.format("Meal type {0} cannot be deleted, because there are still recipes with this meal type", opMealType.get().getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        MealTypeEntity mealTypeEntity = opMealType.get();
        LOGGER.info("deleteMealType: Deleting meal type {}", mealTypeEntity.toModel());
        mealTypeRepository.delete(mealTypeEntity);
        return MessageFormat.format("The meal type {0} has been deleted", mealTypeEntity.getName());
    }

    public ResponseEntity<String> updateMealType(int id, MealTypeInput mealTypeInput) {
        MealTypeEntity mealTypeEntity = mealTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Meal type with id " + id + " not found!"));
        mealTypeEntity.setId(id);
        mealTypeEntity.setName(mealTypeInput.getName());

        mealTypeRepository.save(mealTypeEntity);

        return ResponseEntity.ok().body("Meal type has been updated successfully");
    }

    private List<MealTypeEntity> getAllMealTypeEntities() {
        return new ArrayList<>(mealTypeRepository.findAll());
    }

    private Specification<MealTypeEntity> createSpecs(MealTypeSearchCriteria criteria) {
        Specification<MealTypeEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(((root, _, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + criteria.getName() + "%")));
        }

        return spec;
    }

}
