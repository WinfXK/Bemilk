package cn.winfxk.nukkit.bemilk.tool;

import java.util.ArrayList;
import java.util.Arrays;

public class MyList<E> extends ArrayList<E> {
    public MyList(E... Object) {
        this.addAll(Arrays.asList(Object));
    }

    public static <E> MyList<E> make(E... e) {
        return new MyList<>(e);
    }

    public MyList<E> put(E... e) {
        this.addAll(Arrays.asList(e));
        return this;
    }
}
