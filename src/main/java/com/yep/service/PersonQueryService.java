package com.yep.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.yep.domain.Person;
import com.yep.domain.*; // for static metamodels
import com.yep.repository.PersonRepository;
import com.yep.service.dto.PersonCriteria;


/**
 * Service for executing complex queries for Person entities in the database.
 * The main input is a {@link PersonCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Person} or a {@link Page} of {@link Person} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonQueryService extends QueryService<Person> {

    private final Logger log = LoggerFactory.getLogger(PersonQueryService.class);


    private final PersonRepository personRepository;

    public PersonQueryService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Return a {@link List} of {@link Person} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Person> findByCriteria(PersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Person> specification = createAndSpecification(criteria);
        return personRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Person} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Person> findByCriteria(PersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Person> specification = createAndSpecification(criteria);
        return personRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public Page<Person> findByOrCriteria(PersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Person> specification = createOrSpecification(criteria);
        return personRepository.findAll(specification, page);
    }

    /**
     * Function to convert PersonCriteria to a {@link Specifications}
     */
    private Specifications<Person> createAndSpecification(PersonCriteria criteria) {
        Specifications<Person> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Person_.id));
            }
            if (criteria.getNationalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationalId(), Person_.nationalId));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Person_.fullName));
            }
        }
        return specification;
    }

    /**
     * Function to convert PersonCriteria to a {@link Specifications}
     */
    private Specifications<Person> createOrSpecification(PersonCriteria criteria) {
        Specifications<Person> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.or(buildSpecification(criteria.getId(), Person_.id));
            }
            if (criteria.getNationalId() != null) {
                specification = specification.or(buildStringSpecification(criteria.getNationalId(), Person_.nationalId));
            }
            if (criteria.getFullName() != null) {
                specification = specification.or(buildStringSpecification(criteria.getFullName(), Person_.fullName));
            }
        }
        return specification;
    }

}
