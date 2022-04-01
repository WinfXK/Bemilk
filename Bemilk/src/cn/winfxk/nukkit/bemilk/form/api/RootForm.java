package cn.winfxk.nukkit.bemilk.form.api;

import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;
import cn.winfxk.nukkit.bemilk.form.MyPlayer;
import cn.winfxk.nukkit.bemilk.form.base.BaseForm;

/**
 * @author Winfxk
 */
public abstract class RootForm {
    protected int ID;
    protected String Title;

    /**
     * @param ID 表单ID
     */
    public RootForm(int ID) {
        this(ID, "");
    }

    /**
     * 根页面
     *
     * @param ID
     * @param Title
     */
    public RootForm(int ID, String Title) {
        this.ID = ID;
        this.Title = Title;
    }

    /**
     * 返回要显示的窗口
     *
     * @return
     */
    public abstract FormWindow getFormWindow();

    /**
     * 设置表单ID
     *
     * @param ID
     * @return
     */
    public RootForm setID(int ID) {
        this.ID = ID;
        return this;
    }

    /**
     * 设置表单标题
     *
     * @param Title
     * @return
     */
    public RootForm setTitle(String Title) {
        this.Title = Title;
        return this;
    }

    /**
     * 将表单发送给指定玩家列表
     *
     * @param player
     * @return
     */
    public int show(Player player) {
        MyPlayer.Companion.getMyPlayer(player).setFun(this);
        player.showFormWindow(getFormWindow(), ID);
        return ID;
    }
    /**
     * 返回界面的标题
     *
     * @return
     */
    public String getTitle() {
        return Title;
    }

    /**
     * 返回界面的ID
     *
     * @return
     */
    public int getID() {
        return ID;
    }
}
