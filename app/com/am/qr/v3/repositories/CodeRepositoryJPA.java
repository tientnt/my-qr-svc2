package com.am.qr.v3.repositories;

import com.am.common.repositories.DatabaseExecutionContext;
import com.am.qr.v3.models.Route;
import org.apache.commons.codec.binary.Hex;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;

public class CodeRepositoryJPA implements CodeRepository {

    protected final JPAApi jpaApi;

    protected final DatabaseExecutionContext executionContext;

    protected Logger.ALogger logger = Logger.of(CodeRepositoryJPA.class);

    @Inject
    public CodeRepositoryJPA(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public String findServiceByCode(String code) {
        String sql = "select svc from route where hash_value = UNHEX(sha2(:code,256))";
        return jpaApi.withTransaction(em -> {
//            String sql1 = "from Route where hash_value = UNHEX(sha2(:code,256))";
//            List<Route> routes = em.createQuery(sql1, Route.class)
//                                   .setParameter("code", code)
//                                   .getResultList();
//            if (routes.size() > 0) {
//                byte[] hashValue = routes.get(0).getHashValue();
//                String hash = Hex.encodeHexString(hashValue).toUpperCase();
//                logger.info(hash);
//            }

            List results = em.createNativeQuery(sql).setParameter("code", code).getResultList();
            if (results.size() > 0) {
                return results.get(0).toString();
            }
            return null;
        });
    }

    @Override
    public boolean importHashes(String svc, List<String> hashes) {
        String insertSql = "insert into route (svc, hash_value) values (:svc, UNHEX(:hashValue))";
        String updateSql = "update route set svc = concat(svc,',',:svc) where hash_value = UNHEX(:hashValue)";
        String combineSql = "insert into route (svc, hash_value) values (:svc, UNHEX(:hashValue)) " +
                            "on duplicate key update svc=concat(svc,',',:svc)";
        return jpaApi.withTransaction(em -> {
            boolean result = true;
            try {
                for (String hash : hashes) {
                    if (hash.length() != 64) {
                        logger.error("input is not a hash: {}", hash);
                        result = false;
                        continue;
                    }
                    try {
                        em.createNativeQuery(combineSql)
                          .setParameter("svc", svc)
                          .setParameter("hashValue", hash)
                          .executeUpdate();
                    } catch (Exception ex) {
                        logger.error("Duplicate hash {}, {}", hash, ex.getMessage());
                        em.createNativeQuery(updateSql)
                          .setParameter("svc", svc)
                          .setParameter("hashValue", hash)
                          .executeUpdate();
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                result = false;
            }
            return result;
        });
    }
}
