package xiaokai.tool.data;

import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Effectrec {
	private static LinkedHashMap<Integer, EffectData> list = new LinkedHashMap<>();
	private static Config config;
	private static Kick kick = Kick.kick;
	public static int Items;

	public Effectrec() {
		Items = Effectrec.reload();
	}

	/**
	 * 重载药水效果列表
	 */
	public static int reload() {
		config = new Config(new File(kick.mis.getDataFolder(), Kick.EffectrowConfigName), Config.YAML);
		List<Effectrow> EffectrowList = Effectrow.getList();
		for (Effectrow row : EffectrowList)
			list.put(row.getID(), new EffectData(row.getID(), row.getName(), row.getPath()));
		Map<String, Object> map = config.getAll();
		for (String ike : map.keySet()) {
			Object obj = map.get(ike);
			Map<String, Object> item = (obj == null || !(obj instanceof Map)) ? new HashMap<>()
					: (Map<String, Object>) obj;
			if (item.size() < 1) {
				kick.mis.getLogger().error("§4自定义效果配置错误！请检查！错误Key：" + ike);
				continue;
			}
			int ID = Tool.ObjectToInt(item.get("ID"));
			list.put(ID, new EffectData(ID, (String) item.get("Name"), (String) item.get("Path")));
		}
		Items = list.size();
		return Items;
	}

	/**
	 * 啥都不知道获取一个效果的贴图路径
	 * 
	 * @param obj
	 * @return
	 */
	public static String UnknownToPath(Object obj, String Default) {
		int ID = UnknownToID(obj, -1);
		if (ID == -1)
			return Default;
		return getPath(ID, Default);
	}

	/**
	 * 啥都不知道获取一个效果的对象
	 * 
	 * @param obj
	 * @return
	 */
	public static Effect UnknownToEffect(Object obj, Effect Default) {
		int ID = UnknownToID(obj, -1);
		if (ID == -1)
			return Default;
		return getEffect(ID, Default);
	}

	/**
	 * 啥都不知道获取一个效果的名称
	 * 
	 * @param obj
	 * @return
	 */
	public static String UnknownToName(Object obj, String Default) {
		int ID = UnknownToID(obj, -1);
		if (ID == -1)
			return Default;
		return getName(ID, Default);
	}

	/**
	 * 啥都不知道获取一个效果的ID
	 * 
	 * @param obj
	 * @return
	 */
	public static int UnknownToID(Object obj, int Default) {
		if (obj == null)
			return Default;
		int ID;
		if (Tool.isInteger(obj) && list.containsKey(ID = Tool.ObjectToInt(obj)))
			return ID;
		if ((ID = getID(String.valueOf(obj))) != -1)
			return ID;
		return Default;
	}

	/**
	 * 获取药水效果
	 * 
	 * @param Name
	 * @param Default
	 * @return
	 */
	public static Effect getEffect(String Name, Effect Default) {
		int ID = getID(Name);
		return ID == -1 ? Default : getEffect(ID, Default);
	}

	/**
	 * 获取药水效果
	 * 
	 * @param ID
	 * @return
	 */
	public static Effect getEffect(int ID) {
		return getEffect(ID, null);
	}

	/**
	 * 获取药水效果
	 * 
	 * @param ID
	 * @param Default
	 * @return
	 */
	public static Effect getEffect(int ID, Effect Default) {
		Effect effect = Effect.getEffect(ID);
		return effect == null ? Default : effect;
	}

	/**
	 * 获取效果ID
	 * 
	 * @param Name
	 * @param Default
	 * @return
	 */
	public static int getID(String Name) {
		return getID(Name, -1);
	}

	/**
	 * 获取效果ID
	 * 
	 * @param Name
	 * @param Default
	 * @return
	 */
	public static int getID(String Name, int Default) {
		for (Integer i : list.keySet()) {
			EffectData row = list.get(i);
			if (row.name.equals(Name))
				return row.ID;
		}
		return Default;
	}

	/**
	 * 获取效果 ID
	 * 
	 * @param effect
	 * @return
	 */
	public static int getID(Effect effect) {
		return effect.getId();
	}

	/**
	 * 获取效果名称
	 * 
	 * @param effect
	 * @return
	 */
	public static String getName(Effect effect) {
		return getName(effect, null);
	}

	/**
	 * 获取效果名称
	 * 
	 * @param effect
	 * @param Default
	 * @return
	 */
	public static String getName(Effect effect, String Default) {
		return getName(effect.getId(), Default);
	}

	/**
	 * 获取效果名称
	 * 
	 * @param ID
	 * @param Default
	 * @return
	 */
	public static String getName(int ID) {
		return getName(ID, null);
	}

	/**
	 * 获取效果名称
	 * 
	 * @param ID
	 * @param Default
	 * @return
	 */
	public static String getName(int ID, String Default) {
		if (!list.containsKey(ID))
			return Default;
		return list.get(ID).name;
	}

	/**
	 * 获取药水贴图路径
	 * 
	 * @param effect
	 * @return
	 */
	public static String getPath(Effect effect) {
		return getPath(effect, null);
	}

	/**
	 * 获取药水贴图路径
	 * 
	 * @param effect
	 * @return
	 */
	public static String getPath(Effect effect, String Default) {
		return getPath(effect.getId(), Default);
	}

	/**
	 * 获取药水贴图路径
	 * 
	 * @param Name
	 * @return
	 */
	public static String getPath(String Name) {
		return getPath(Name, null);
	}

	/**
	 * 获取药水贴图路径
	 * 
	 * @param Name
	 * @return
	 */
	public static String getPath(String Name, String Default) {
		for (Integer i : list.keySet()) {
			EffectData data = list.get(i);
			if (data.name.equals(Name))
				return data.Path;
		}
		return Default;
	}

	/**
	 * 获取药水贴图路径
	 * 
	 * @param ID
	 * @return
	 */
	public static String getPath(int ID) {
		return getPath(ID, null);
	}

	/**
	 * 获取药水贴图路径
	 * 
	 * @param ID
	 * @return
	 */
	public static String getPath(int ID, String Default) {
		if (!list.containsKey(ID))
			return Default;
		return list.get(ID).Path;
	}

	/**
	 * 获取药水效果列表
	 * 
	 * @return
	 */
	public static LinkedHashMap<Integer, EffectData> getList() {
		return list;
	}

	/**
	 * 获取自定义配置文件对象
	 * 
	 * @return
	 */
	public static Config getConfig() {
		return config;
	}

	/**
	 * 获取效果名称列表
	 * 
	 * @return
	 */
	public static List<String> getNameList() {
		List<String> list = new ArrayList<>();
		for (Integer i : Effectrec.list.keySet())
			list.add(Effectrec.list.get(i).name);
		return list;
	}

	/**
	 * 获取一个附魔列表的字符串
	 * 
	 * @return
	 */
	public static String getEffectString() {
		String string = "";
		int a = -1;
		for (Integer i : list.keySet()) {
			EffectData data = list.get(i);
			string += "§2" + data.ID + "§f>§5" + data.Path + (((a++) - 1) % 3 == 0 ? "\n" : "  ");
		}
		return string;
	}
}
