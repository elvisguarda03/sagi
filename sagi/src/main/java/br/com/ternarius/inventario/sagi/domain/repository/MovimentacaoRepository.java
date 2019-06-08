package br.com.ternarius.inventario.sagi.domain.repository;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.entity.Movimentacao;
import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import org.springframework.security.core.parameters.P;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface MovimentacaoRepository extends Repository {
    Movimentacao save(Movimentacao movimentacao);
    Optional<Movimentacao> findById(String id);
    List<Movimentacao> findAll();
    List<Movimentacao> findByDataPermissao(LocalDate localDate);
    void deleteById(String id);

    default List<Movimentacao> find(String nomeEquipamento, String novaLocalizacao, String nomeAdmin, String nomeSolicitante, EntityManager em) {
        final var PATTERN = "%";

        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Movimentacao.class);
        var root = query.from(Movimentacao.class);

        var equipamentoPath = root.<Equipamento>get("equipamento").<String>get("nomeEquipamento");
        var localizacaoPath = root.<Laboratorio>get("localizacaoAtual").<String>get("localizacao");
        var adminPath = root.<Usuario>get("admin").<String>get("nome");
        var solicitantePath = root.<Usuario>get("solicitante").<String>get("nome");

        var predicates = new ArrayList<Predicate>();

        if (!isBlank(nomeEquipamento)) {
            predicates.add(criteriaBuilder.like(equipamentoPath, PATTERN + nomeEquipamento + PATTERN));
        }

        if (!isBlank(novaLocalizacao)) {
            predicates.add(criteriaBuilder.like(localizacaoPath, PATTERN + novaLocalizacao + PATTERN));
        }

        if (!isBlank(nomeAdmin)) {
            predicates.add(criteriaBuilder.like(adminPath, PATTERN + nomeAdmin + PATTERN));
        }

        if (!isBlank(nomeSolicitante)) {
            predicates.add(criteriaBuilder.like(solicitantePath, PATTERN + nomeSolicitante + PATTERN));
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        var typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }
}