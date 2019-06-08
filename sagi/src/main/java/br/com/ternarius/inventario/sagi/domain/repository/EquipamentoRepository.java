package br.com.ternarius.inventario.sagi.domain.repository;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public interface EquipamentoRepository extends Repository {
	Equipamento save(Equipamento eqp);
    Equipamento updateIsMaintenance(String id, Boolean isMaintenance);
	Page<Equipamento> findByLaboratorio(Laboratorio laboratorio, Pageable pageable);
	List<Equipamento> findByNomeEquipamentoIgnoreCaseContaining(String nomeEquipamento);
	Page<Equipamento> findByIsDelete(Boolean isDelete, Pageable pageable);
	List<Equipamento> findByIsDelete(Boolean isDelete);
	List<Equipamento> findAll(Boolean isDelete);
	Page<Equipamento> findAll(Pageable pageable);
	Long countByNomeEquipamento(String nomeEquipamento);
	Equipamento findByCodigoPatrimonio(Long codigoPatrimonio);
	Optional<Equipamento> findById(String id);
    boolean existsByCodigoPatrimonio(Long codigoPatrimonio);
	boolean existsByNomeEquipamentoContainingIgnoreCase(String nomeEquipamento);

	default List<Equipamento> find(String nome, String localizacao, Long codigoPatrimonio, BigDecimal valor, EntityManager em) {
		final var PATTERN = "%";

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Equipamento> query = criteriaBuilder.createQuery(Equipamento.class);
		Root<Equipamento> root = query.from(Equipamento.class);

		Path<String> nomePath = root.<String> get("nomeEquipamento");
		Path<String> localizacaoPath = root.<Laboratorio> get("laboratorio").<String> get("localizacao");
		Path<Long> codigoPatrimonioPath = root.<Long> get("codigoPatrimonio");
		Path<BigDecimal> valorPath = root.<BigDecimal> get("valor");

		List<Predicate> predicates = new ArrayList<>();

		if (!isBlank(nome)) {
			predicates.add(criteriaBuilder.like(nomePath, PATTERN + nome + PATTERN));
		}

		if (!isBlank(localizacao)) {
			predicates.add(criteriaBuilder.like(localizacaoPath, PATTERN + localizacao + PATTERN));
		}

		if (!isNull(codigoPatrimonio)) {
			predicates.add(criteriaBuilder.equal(codigoPatrimonioPath, codigoPatrimonio));
		}

		if (!isNull(valor)) {
			predicates.add(criteriaBuilder.equal(valorPath, valor));
		}

		query.where((Predicate[]) predicates.toArray(new Predicate[0]));
		TypedQuery<Equipamento> typedQuery = em.createQuery(query);

		return typedQuery.getResultList();
	}
}