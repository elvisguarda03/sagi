package br.com.ternarius.inventario.sagi.domain.repository;

import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author Elvis da Guarda
 *
 */
public interface LaboratorioRepository extends Repository {
	Laboratorio save(Laboratorio lab);
	Laboratorio findByLocalizacaoContainingIgnoreCase(String localizacao);
	Optional<Laboratorio> findById(String id);
    Page<Laboratorio> findAll(Pageable pageable);
	List<Laboratorio> findAll();
    boolean existsByLocalizacaoContainingIgnoreCase(String localizacao);
	void updateLocalizacaoAndEdificioAndAndar(String id, String localizacao, String edificio, Integer andar);
    void deleteById(String id);
	void delete(Laboratorio lab);

	default List<Laboratorio> find(String localizacao, String edificio, Integer andar, EntityManager em) {
		final var pattern = "%";

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Laboratorio> query = criteriaBuilder.createQuery(Laboratorio.class);
		Root<Laboratorio> root = query.from(Laboratorio.class);

		Path<String> localizacaoPath = root.<String> get("localizacao");
		Path<String> edificioPath = root.<String> get("edificio");
		Path<Integer> andarPath = root.<Integer> get("andar");

		List<Predicate> predicates = new ArrayList<>();

		if (!Objects.isNull(localizacao) && !localizacao.isEmpty()) {
			predicates.add(criteriaBuilder.like(localizacaoPath, pattern + localizacao + pattern));
		}

		if (!Objects.isNull(edificio) && !edificio.isEmpty()) {
			predicates.add(criteriaBuilder.like(edificioPath, pattern + edificio + pattern));
		}

		if (!Objects.isNull(andar)) {
			predicates.add(criteriaBuilder.equal(andarPath, andar));
		}

		TypedQuery<Laboratorio> typedQuery = em.createQuery(query);

		return typedQuery.getResultList();
	}

}