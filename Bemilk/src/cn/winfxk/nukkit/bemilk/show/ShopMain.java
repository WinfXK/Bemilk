package cn.winfxk.nukkit.bemilk.show;

import cn.nukkit.Player;
import cn.winfxk.nukkit.bemilk.BaseForm;
import cn.winfxk.nukkit.winfxklib.form.BaseFormin;

public class ShopMain extends BaseForm {
    public ShopMain(Player player, BaseFormin Update, boolean isBack) {
        super(player, Update, isBack);
    }

    @Override
    public boolean MakeForm() {
        return false;
    }
}
