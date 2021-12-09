package com.chang.android.jna.usage;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * 与 jna-usage.c 中的结构体 MajorCompany 对应。
 * 与 Company.java 区别，直接用指针数组声明数组。
 * <p>
 * Created by Nicholas Sean on 2021/11/10 4:20 下午.
 *
 * @version 1.0
 */
public class MajorCompany extends Structure {
    public NativeLong id;
    public String name;
    public int userArrayLength;
    public User.ByReference userArray[] = (User.ByReference[]) new User.ByReference().toArray(5);

    public MajorCompany() {
        super();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("id", "name", "userArrayLength", "userArray");
    }

    public MajorCompany(NativeLong id, String name, int userArrayLength, User.ByReference userArray[]) {
        super();
        this.id = id;
        this.name = name;
        this.userArrayLength = userArrayLength;
        this.userArray = userArray;
    }

    public MajorCompany(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends MajorCompany implements Structure.ByReference {}

    public static class ByValue extends MajorCompany implements Structure.ByValue {}

    @Override
    public String toString() {
        return "MajorCompany{" + "id=" + id + ", name='" + name + '\'' + ", userArrayLength=" + userArrayLength + ", userArray=" + Arrays.toString(userArray) + '}';
    }
}
