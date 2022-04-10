package cn.winfxk.nukkit.bemilk;

import cn.nukkit.Player;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;

public abstract class BaseForm extends BaseFormin {
    public BaseForm(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
    }
}
