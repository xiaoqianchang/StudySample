package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class GreenDaoGenerator {

    public static void main(String[] args) throws Exception {
        /*
        定义一个 Schema,第一个参数为版本号,第二个参数为包名
         */
        Schema schema = new Schema(1, "com.changxiao.greendaodemo.db.entity");
        schema.setDefaultJavaPackageDao("com.changxiao.greendaodemo.db.dao");

        addPerson(schema);

        /*
        最后生成实体类
        第一个参数为 schema
        第二个参数为文件生成路径
         */
        new DaoGenerator().generateAll(schema, "../GreenDaoDemo/app/src/main/java-gen");
    }

    private static void addPerson(Schema schema) {
        // 一个实体类对应一张数据库表，此处表名为 PERSON （即类名）
        Entity person = schema.addEntity("Person");
        // 也可以重新命名表名
         person.setTableName("people");
        // 定义一个主键
        person.addIdProperty();
        // 定义一个非空的列，列名为 NAME
        person.addStringProperty("name").notNull();
        // 可以使用此方法定义实体类的属性名和数据库的列名不同，如下实体类名为 sex，列名为_SEX
        person.addStringProperty("sex").columnName("_sex");
    }
}
