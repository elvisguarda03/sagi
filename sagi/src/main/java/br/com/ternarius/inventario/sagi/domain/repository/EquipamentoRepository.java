package br.com.ternarius.inventario.sagi.domain.repository;

import java.math.BigDecimal;
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

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public interface EquipamentoRepository extends Repository {
	Equipamento save(Equipamento eqp);
	List<Equipamento> findByLaboratorio(Laboratorio laboratorio);
	Long countByCodigoPatrimonio(Long codigoPatrimonio);
	Optional<Equipamento> findByNomeEquipamento(String nomeEquipamento);
	Optional<Equipamento> findById(String id);
	List<Equipamento> findAll();
	List<Equipamento> findAll(Laboratorio laboratorio);
	Page<Equipamento> findAll(Pageable pageable);
	void deleteById(String id);
	boolean existsByNomeEquipamentoContainingIgnoreCase(String nomeEquipamento);

	default List<Equipamento> find(String nome, String localizacao, Long codigoPatrimonio, BigDecimal valor, EntityManager em) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Equipamento> query = criteriaBuilder.createQuery(Equipamento.class);
		Root<Equipamento> root = query.from(Equipamento.class);

		Path<String> nomePath = root.<String> get("nome");
		Path<String> localizacaoPath = root.<Laboratorio> get("laboratorio").<String> get("localizacao");
		Path<Long> codigoPatrimonioPath = root.<Long> get("codigoPatrimonio");
		Path<BigDecimal> valorPath = root.<BigDecimal> get("valor");

		List<Predicate> predicates = new ArrayList<>();

		if (!nome.isEmpty()) {
			predicates.add(criteriaBuilder.like(nomePath, nome));
		}

		if (!localizacao.isEmpty()) {
			predicates.add(criteriaBuilder.like(localizacaoPath, localizacao));
		}

		if (codigoPatrimonio != null) {
			predicates.add(criteriaBuilder.equal(codigoPatrimonioPath, codigoPatrimonio));
		}

		if (valor != null) {
			predicates.add(criteriaBuilder.equal(valorPath, valor));
		}

		query.where((Predicate[]) predicates.toArray(new Predicate[0]));
		TypedQuery<Equipamento> typedQuery = em.createQuery(query);

		return typedQuery.getResultList();

	}
}