package generatorTemplate.constant;


import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @Package: com.study.generator
 * @Description: <表格和数据源配置项>
 * @Author: milla
 * @CreateDate: 2020/11/12 17:48
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/12 17:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class BasicConstant {
    /**
     * 作者
     */
    public static String AUTHOR = "Jimmy" ;
    /**
     * 生成的实体类忽略表前缀: 不需要则置空
     */
    public static String ENTITY_IGNORE_PREFIX = "settings_" ;
    /**
     * 表名
     */
    public static String[] TABLES = {
            "task_group_activity",
            "task_group_activity_apply_record"
    };

    /**
     * 实体类的父类Entity
     */
    public static String SUPER_ENTITY_CLASS = "cn.inno.pala.framework.mybatis.core.dataobject.BaseDO" ;

    /**
     * 基础父类继承字段
     */
    public static String[] SUPER_ENTITY_COLUMNS = {
            "creator","create_time","updater","update_time","deleted","tenant_id"
    };

    /**
     * 数据库
     */
    public static String username = "root" ;
    public static String password = "inno@2021" ;
    public static String url = "jdbc:mysql://10.0.99.191:3306/pala-planet?useSSL=false&allowMultiQueries=true&characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false&maxReconnects=10" ;
    public static DbType DB_TYPE = DbType.MYSQL;
    public static String driverClassName = "com.mysql.jdbc.Driver" ;
}
