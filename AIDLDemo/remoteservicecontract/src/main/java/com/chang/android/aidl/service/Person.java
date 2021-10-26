package com.chang.android.aidl.service;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Description: Person
 * 1. 支持序列化和反序列化；
 * 2. 支持 AIDL in/out/inout；
 *
 * 序列化：由于存在于内存中的对象都是暂时的，无法长期驻存，为了把对象的状态保持下来，这时需要把对象写入到磁盘或者其他介质中，这个过程就叫做序列化。
 * 反序列化：反序列化恰恰是序列化的反向操作，也就是说，把已存在在磁盘或者其他介质中的对象，反序列化（读取）到内存中，以便后续操作，而这个过程就叫做反序列化。
 * <p>
 * Created by Nicholas Sean on 2021/7/1 3:05 PM.
 *
 * @version 1.0
 */
public class Person implements Parcelable {
    private String name;
    private int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 序列化
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    /**
     * 反序列化
     *
     * 用于:
     * 1. 创建新实例；
     * 2. AIDL 文件方法参数中 out/inout 定向tag；
     *
     * @param source
     */
    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.age = source.readInt();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            Person person = new Person();
            person.readFromParcel(source);
            return person;
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
