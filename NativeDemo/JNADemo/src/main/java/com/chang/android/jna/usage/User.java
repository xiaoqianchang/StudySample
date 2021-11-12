package com.chang.android.jna.usage;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * 与 jna-usage.c 中的结构体 User 对应。
 * <p>
 * Created by Nicholas Sean on 2021/11/10 2:43 下午.
 *
 * @version 1.0
 */
public class User extends Structure {
    public NativeLong id;
    public String name;
    public int age;

    public User() {
        super();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("id", "name", "age");
    }

    public User(NativeLong id, String name, int age) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends User implements Structure.ByReference {}

    public static class ByValue extends User implements Structure.ByValue {}

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
    }
}
