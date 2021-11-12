package com.chang.android.jna.usage;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * 与 jna-usage.c 中的结构体 Company 对应。
 * <p>
 * Created by Nicholas Sean on 2021/11/10 4:20 下午.
 *
 * @version 1.0
 */
public class Company extends Structure {
    public NativeLong id;
    public String name;
    public User.ByReference userArray;
    public int userArrayLength;

    public Company() {
        super();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("id", "name", "userArray", "userArrayLength");
    }

    public Company(NativeLong id, String name, User.ByReference userArray, int userArrayLength) {
        super();
        this.id = id;
        this.name = name;
        this.userArray = userArray;
        this.userArrayLength = userArrayLength;
    }

    public Company(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends Company implements Structure.ByReference {}

    public static class ByValue extends Company implements Structure.ByValue {}

    @Override
    public String toString() {
        return "Company{" + "id=" + id + ", name='" + name + '\'' + ", userArray=" + userArray + ", userArrayLength=" + userArrayLength + '}';
    }
}
