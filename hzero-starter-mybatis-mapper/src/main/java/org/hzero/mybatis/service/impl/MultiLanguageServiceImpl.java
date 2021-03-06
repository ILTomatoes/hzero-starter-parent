package org.hzero.mybatis.service.impl;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.choerodon.mybatis.domain.EntityTable;
import io.choerodon.mybatis.helper.EntityHelper;
import io.choerodon.mybatis.helper.LanguageHelper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hzero.core.util.FieldNameUtils;
import org.hzero.mybatis.domian.Language;
import org.hzero.mybatis.domian.MultiLanguage;
import org.hzero.mybatis.impl.DefaultDynamicSqlMapper;
import org.hzero.mybatis.service.MultiLanguageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 多语言 Service
 * </p>
 *
 * @author qingsheng.chen 2018/9/16 星期日 14:48
 */
@Service("mybatis.multiLanguageServiceImpl")
public class MultiLanguageServiceImpl implements MultiLanguageService {
    private static final Logger logger = LoggerFactory.getLogger(MultiLanguageServiceImpl.class);
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    public MultiLanguageServiceImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<MultiLanguage> listMultiLanguage(String className, String fieldName, Map<String, Object> pkValue) {
        try {
            Class clazz = Class.forName(className);
            EntityTable entityTable = EntityHelper.getTableByEntity(clazz);
            if (!entityTable.isMultiLanguage()) {
                throw new RuntimeException(entityTable.getName() + " hasn't multi-language!");
            }
            Set<String> multiLanguageColumn = entityTable.getMultiLanguageColumns().stream().map(item -> item.getColumn().toLowerCase()).collect(Collectors.toSet());
            final String columnName = FieldNameUtils.camel2Underline(fieldName, false);
            if (!multiLanguageColumn.contains(columnName)) {
                throw new RuntimeException("Unknown column " + columnName + " in multi language table " + entityTable.getMultiLanguageTableName());
            }
            List<String> pkCondition = new ArrayList<>();
            pkValue.forEach((key, value) -> pkCondition.add(FieldNameUtils.camel2Underline(key, false) + " = " + (value instanceof Number ? value : "'" + value + "'")));
            String executeSql = "SELECT \n" + "LANG,\n" + columnName + " AS VALUE\n" + "FROM " + entityTable.getMultiLanguageTableName() + "\nWHERE " + StringUtils.collectionToDelimitedString(pkCondition, " AND ");
            logger.debug(">>> Multi Language Select : {}", executeSql);
            return multiLanguage(new DefaultDynamicSqlMapper(sqlSessionFactory.openSession()).selectList(executeSql)
                    .stream()
                    .collect(Collector.of(HashMap::new, (map, entity) -> map.put(String.valueOf(entity.get("LANG")), entity.containsKey("VALUE") ? String.valueOf(entity.get("VALUE")) : null), (k, v) -> v, Collector.Characteristics.IDENTITY_FINISH)));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<MultiLanguage> emptyMultiLanguage() {
        List<MultiLanguage> multiLanguageList = new ArrayList<>();
        List<Language> languageList = LanguageHelper.languages();
        if (!CollectionUtils.isEmpty(languageList)) {
            for (Language language : languageList) {
                multiLanguageList.add(new MultiLanguage().setCode(language.getCode()).setName(language.getName()));
            }
        }
        return multiLanguageList;
    }

    public static List<MultiLanguage> multiLanguage(Map<String, String> multiLanguageMap) {
        logger.debug("Selected MultiLanguage : {}", multiLanguageMap);
        List<MultiLanguage> multiLanguageList = new ArrayList<>();
        List<Language> languageList = LanguageHelper.languages();
        if (!CollectionUtils.isEmpty(languageList)) {
            for (Language language : languageList) {
                multiLanguageList.add(new MultiLanguage()
                        .setCode(language.getCode())
                        .setName(language.getName())
                        .setValue(multiLanguageMap.get(language.getCode())));
            }
        }
        logger.debug("Selected MultiLanguage : {}", multiLanguageList);
        return multiLanguageList;
    }
}
