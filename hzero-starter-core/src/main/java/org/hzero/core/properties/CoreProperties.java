package org.hzero.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 核心通用配置
 *
 * @author bojiangzhou 2019/08/23
 */
@RefreshScope
@ConfigurationProperties(prefix = CoreProperties.PREFIX)
public class CoreProperties {

    public static final String PREFIX = "hzero";

    /**
     * jwt key
     */
    private String oauthJwtKey = "hzero";

    private Resource resource = new Resource();

    /**
     * Actuator配置信息
     */
    private Actuator actuator = new Actuator();

    public static class Resource {

        private String pattern = "/v1/*";

        private String skipPath = "/v2/choerodon/api-docs";

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public String getSkipPath() {
            return skipPath;
        }

        public void setSkipPath(String skipPath) {
            this.skipPath = skipPath;
        }
    }

    public Actuator getActuator() {
        return actuator;
    }

    public String getOauthJwtKey() {
        return oauthJwtKey;
    }

    public void setOauthJwtKey(String oauthJwtKey) {
        this.oauthJwtKey = oauthJwtKey;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    /**
     * Actuator配置信息
     *
     * @author bo.he02@hand-chian.com 2020/07/31 14:00:00
     */
    public static class Actuator {
        /**
         * 权限信息配置
         */
        private Permission permission = new Permission();

        public Permission getPermission() {
            return permission;
        }

        public void setPermission(Permission permission) {
            this.permission = permission;
        }

        /**
         * 权限信息扫描配置信息
         *
         * @author bo.he02@hand-chian.com 2020/07/31 14:00:00
         */
        public static class Permission {
            /**
             * 是否开启权限码重复检测，默认开启
             * 配置全路径： hzero.actuator.permission.duplicatedCodeCheck
             * true     开启      如果发现权限码重复，会抛出异常，应用不能启动成功
             * false    不开启     如果发现权限码重复，会忽略重复的权限数据，并打印错误日志
             */
            private Boolean duplicatedCodeCheck = true;

            public Boolean getDuplicatedCodeCheck() {
                return this.duplicatedCodeCheck;
            }

            public void setDuplicatedCodeCheck(Boolean duplicatedCodeCheck) {
                this.duplicatedCodeCheck = duplicatedCodeCheck;
            }
        }
    }
}
