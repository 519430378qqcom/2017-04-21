package cn.youmi.framework.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbTable {
	String columnName() default "";
	String tableName() default "";
	String databaseName() default "";
	boolean primaryKey() default false;
}
