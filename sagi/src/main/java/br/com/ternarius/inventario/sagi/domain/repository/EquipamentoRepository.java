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
import java.util.Objects;
import java.util.Optional;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public interface EquipamentoRepository extends Repository {
	Equipamento save(Equipamento eqp);
    Equipamento updateIsMaintenance(String id, Boolean isMaintenance);
	Page<Equipamento> findByLaboratorio(Laboratorio laboratorio, Pageable pageable);
	Optional<Equipamento> findByNomeEquipamento(String nomeEquipamento);
	List<Equipamento> findByStatus(Boolean status);
	List<Equipamento> findAll();
	Page<Equipamento> findAll(Pageable pageable);
	Long countByNomeEquipamento(String nomeEquipamento);
	Optional<Equipamento> findById(String id);
	void deleteById(String id);
	boolean existsByNomeEquipamentoContainingIgnoreCase(String nomeEquipamento);

	default List<Equipamento> find(String nome, String localizacao, Long codigoPatrimonio, BigDecimal valor, EntityManager em) {
		final var pattern = "%";

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Equipamento> query = criteriaBuilder.createQuery(Equipamento.class);
		Root<Equipamento> root = query.from(Equipamento.class);

		Path<String> nomePath = root.<String> get("nomeEquipamento");
		Path<String> localizacaoPath = root.<Laboratorio> get("laboratorio").<String> get("localizacao");
		Path<Long> codigoPatrimonioPath = root.<Long> get("codigoPatrimonio");
		Path<BigDecimal> valorPath = root.<BigDecimal> get("valor");

		List<Predicate> predicates = new ArrayList<>();

		if (!Objects.isNull(nome) && !nome.isEmpty()) {
			predicates.add(criteriaBuilder.like(nomePath, pattern + nome + pattern));
		}

		if (!Objects.isNull(localizacao) && !localizacao.isEmpty()) {
			predicates.add(criteriaBuilder.like(localizacaoPath, pattern + localizacao + pattern));
		}

		if (!Objects.isNull(codigoPatrimonio)) {
			predicates.add(criteriaBuilder.equal(codigoPatrimonioPath, codigoPatrimonio));
		}

		if (!Objects.isNull(valor)) {
			predicates.add(criteriaBuilder.equal(valorPath, valor));
		}

		query.where((Predicate[]) predicates.toArray(new Predicate[0]));
		TypedQuery<Equipamento> typedQuery = em.createQuery(query);

		return typedQuery.getResultList();
	}
}