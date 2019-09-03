package xiaokai.tool.data;
/**
*@author Winfxk
*/

import cn.nukkit.potion.Effect;

public class EffectData {
	public int ID;
	public String name;
	public String Path;
	public Effect effect;

	/**
	 * 药水效果数据
	 * 
	 * @param ID   药水效果的ID
	 * @param name 效果名称
	 * @param Path 贴图路径
	 */
	public EffectData(int ID, String name, String Path) {
		this.ID = ID;
		this.name = name;
		this.Path = Path;
		effect = Effect.getEffect(ID);
	}
}
