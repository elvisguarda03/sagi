package br.com.ternarius.inventario.sagi.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Elvis da Guarda
 *
 */
public interface LaboratorioRepository extends Repository {
	Laboratorio save(Laboratorio lab);
	Optional<Laboratorio> findById(String id);
    Page<Laboratorio> findAll(Pageable pageable);
	List<Laboratorio> findAll();
	void updateLocalizacaoAndEdificioAndAndar(String id, String localizacao, String edificio, Integer andar);
	void delete(Laboratorio lab);

	default List<Laboratorio> find(String localizacao, String edificio, Integer andar, EntityManager em) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Laboratorio> query = criteriaBuilder.createQuery(Laboratorio.class);
		Root<Laboratorio> root = query.from(Laboratorio.class);

		Path<String> localizacaoPath = root.<String> get("localizacao");
		Path<String> edificioPath = root.<String> get("edificio");
		Path<Integer> andarPath = root.<Integer> get("andar");

		List<Predicate> predicates = new ArrayList<>();

		if (!localizacao.isEmpty()) {
			predicates.add(criteriaBuilder.like(localizacaoPath, localizacao));
		}

		if (!edificio.isEmpty()) {
			predicates.add(criteriaBuilder.like(edificioPath, edificio));
		}

		if (andar != null) {
			predicates.add(criteriaBuilder.equal(andarPath, andar));
		}

		TypedQuery<Laboratorio> typedQuery = em.createQuery(query);

		return typedQuery.getResultList();
	}
}