package com.am.qr.v3.repositories;

import com.am.common.repositories.DatabaseExecutionContext;
import com.am.qr.v3.models.Route;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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
    public Route findByCode(String code) {
        return jpaApi.withTransaction(em -> {
            String sql1 = "from Route where hash_value = UNHEX(sha2(:code,256))";
            List<Route> routes = em.createQuery(sql1, Route.class)
                                   .setParameter("code", code)
                                   .getResultList();
            if (routes.size() > 0) {
                byte[] hashValue = routes.get(0).getHashValue();
                String hash = Hex.encodeHexString(hashValue);
                logger.debug(hash);
                return routes.get(0);
            }
            return null;
        });
    }

    private Route findByHashAndGroup(EntityManager em, String hash, String group) {
        String sql1 = "from Route where hash_value = UNHEX(:hash) and group = :group";
        List<Route> routes = em.createQuery(sql1, Route.class)
                               .setParameter("hash", hash)
                               .setParameter("group", group)
                               .getResultList();
        if (routes.size() > 0) {
            return routes.get(0);
        }
        return null;
    }

    @Override
    public List<Route> findListByCode(String code) {
        return jpaApi.withTransaction(em -> {
            String sql1 = "from Route where hash_value = UNHEX(sha2(:code,256))";
            List<Route> routes = em.createQuery(sql1, Route.class)
                                   .setParameter("code", code)
                                   .getResultList();
            return routes;
        });
    }

    @Override
    public Route findByCodeAndSvc(String code, String svc) {
        return jpaApi.withTransaction(em -> {
            String sql1 = "from Route where svc = :svc and hash_value = UNHEX(sha2(:code,256))";
            List<Route> routes = em.createQuery(sql1, Route.class)
                                   .setParameter("svc", svc)
                                   .setParameter("code", code)
                                   .getResultList();
            if (routes.size() > 0) {
                byte[] hashValue = routes.get(0).getHashValue();
                String hash = Hex.encodeHexString(hashValue);
                logger.debug(hash);
                return routes.get(0);
            }
            return null;
        });
    }

    @Override
    public Route findByCodeSvcGroup(String code, String svc, String group) {
        return jpaApi.withTransaction(em -> {
            String sql1 = "from Route where svc = :svc and group = :group and hash_value = UNHEX(sha2(:code,256))";
            List<Route> routes = em.createQuery(sql1, Route.class)
                                   .setParameter("svc", svc)
                                   .setParameter("code", code)
                                   .setParameter("group", group)
                                   .getResultList();
            if (routes.size() > 0) {
                return routes.get(0);
            }
            return null;
        });
    }

    @Override
    public boolean importHashes(String svc, List<String> hashes, String status, List<String> groups) {
        String insertSql = "insert into route (svc, hash_value, `status`, `group`) " +
                           "values (:svc, UNHEX(:hashValue), :status, :group)";
        //String updateSql = "update route set svc = concat(svc,',',:svc) where hash_value = UNHEX(:hashValue)";
        //String combineSql = "insert into route (svc, hash_value) values (:svc, UNHEX(:hashValue)) " +
        //                    "on duplicate key update svc=concat(svc,',',:svc)";

        //insert batch
        boolean flag = jpaApi.withTransaction(em -> {
            boolean result = true;
            try {
                for (int i = 0; i < hashes.size(); i++) {
                    String hash = hashes.get(i);
                    if (hash.length() != 64) {
                        logger.error("input is not a hash: {}", hash);
                        result = false;
                        continue;
                    }
                    String group = null;
                    if (null != groups && i < groups.size()) {
                        group = groups.get(i);
                    }
                    Route route = findByHashAndGroup(em, hash, group);
                    if (null != route) {
                        logger.warn("Hash [{}] has been existed. Pls check", hash);
                        continue;
                    }
                    em.createNativeQuery(insertSql)
                      .setParameter("svc", svc)
                      .setParameter("hashValue", hash)
                      .setParameter("status", status)
                      .setParameter("group", group)
                      .executeUpdate();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                result = false;
            }
            return result;
        });

        //if insert batch failed, insert one by one
        if (!flag) {
            logger.error("Batch insert failed, insert one by one");
            flag = true;
            for (int i = 0; i < hashes.size(); i++) {
                String hash = hashes.get(i);
                String group = null;
                if (null != groups && i < groups.size()) {
                    group = groups.get(i);
                }
                if (hash.length() != 64) {
                    logger.error("input is not a hash: {}", hash);
                    continue;
                }
                try {
                    String finalGroup = group;
                    jpaApi.withTransaction(em -> {
                        Route route = findByHashAndGroup(em, hash, finalGroup);
                        if (null != route) {
                            logger.warn("Hash [{}] has been existed. Pls check", hash);
                            return null;
                        }
                        em.createNativeQuery(insertSql)
                          .setParameter("svc", svc)
                          .setParameter("hashValue", hash)
                          .setParameter("status", status)
                          .setParameter("group", finalGroup)
                          .executeUpdate();
                        return null;
                    });
                } catch (Exception e) {
                    logger.error("duplicate: {}", hash);
                    //logger.error(e.getMessage(), e);
                    flag = false;
                }
            }
        }
        return flag;
    }

}
