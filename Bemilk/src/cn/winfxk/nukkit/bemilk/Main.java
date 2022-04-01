package cn.winfxk.nukkit.bemilk;

import cn.nukkit.Player;
import cn.winfxk.nukkit.bemilk.form.base.BaseForm;

public class Main {
    public BaseForm form, form2;
    public Player player;

    public void SB() {
        if (form != null) {
            form = null;
            form2.show(form2);
            return;
        }
        if (form2 == null) {
            form2 = null;
            form.show(form);
        }
    }
}
