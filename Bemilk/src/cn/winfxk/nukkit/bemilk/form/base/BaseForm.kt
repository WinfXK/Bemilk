package cn.winfxk.nukkit.bemilk.form.base

import cn.nukkit.Player
import cn.nukkit.form.response.FormResponse
import cn.nukkit.form.response.FormResponseCustom
import cn.nukkit.form.response.FormResponseModal
import cn.nukkit.form.response.FormResponseSimple
import cn.winfxk.nukkit.bemilk.form.MyPlayer
import cn.winfxk.nukkit.bemilk.form.api.ModalForm
import cn.winfxk.nukkit.bemilk.form.api.SimpleForm

abstract class BaseForm(var Myplaery: MyPlayer, var UpForm: BaseForm? = null, var isBack: Boolean = true) {
    val K = arrayOf("{Player}", "{Money}");
    val D = arrayOf(Myplaery.getName(), Myplaery.getMoney())
    abstract fun MakeMain(): Boolean;
    fun dispose(data: FormResponseSimple): Boolean {
        return true;
    }

    fun getID(): Int {
        var ID: Int = when (Myplaery.ID) {
            Formdispose.FormID[1] -> Formdispose.FormID[0];
            else -> Formdispose.FormID[1]
        }
        Myplaery.ID = ID;
        return Myplaery.ID;
    }

    fun wasClosed() {}
    fun dispose(data: FormResponseModal): Boolean {
        return true;
    }

    fun dispose(data: FormResponseCustom): Boolean {
        return true;
    }

    fun dispose(data: FormResponse) {
        toFun(data);
    }

    fun toFun(data: FormResponse) {
        if (data is FormResponseCustom) {
            dispose(data)
            return;
        }
        if (data is FormResponseSimple) {
            dispose(data)
            (Myplaery.`fun` as SimpleForm).resolveResponded(getPlayer(), data)
        }
        if (data is FormResponseModal) {
            dispose(data)
            (Myplaery.`fun` as ModalForm).resolveResponded(getPlayer(), data)
        }
    }

    fun getSimple(data: FormResponse): FormResponseSimple {
        return data as FormResponseSimple;
    }

    fun getCustom(data: FormResponse): FormResponseCustom {
        return data as FormResponseCustom;
    }

    fun getModal(data: FormResponse): FormResponseModal {
        return data as FormResponseModal;
    }

    fun toBack(): Boolean {
        if (UpForm == null || !isBack) {
            Myplaery.form = null;
            return false;
        }
        return show(UpForm!!);
    }

    /**
     * 显示一个界面
     */
    fun show(form: BaseForm): Boolean {
        if (form == null) return false;
        Myplaery.form = form;
        return Myplaery.form!!.MakeMain();
    }

    fun getPlayer(): Player {
        return Myplaery.player;
    }
}