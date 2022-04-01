package cn.winfxk.nukkit.bemilk.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Config {
    private File File;
    private String Contxt;
    private transient MyMap<String, Object> map;
    private static DumperOptions dumperOptions = new DumperOptions();
    public static Yaml yaml;

    static {
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(dumperOptions);
    }

    public int getSize() {
        return map.size();
    }

    public List<String> getKeys() {
        return new ArrayList<>(map.keySet());
    }

    public boolean containsValue(String Data) {
        return map.containsValue(Data);
    }

    public boolean containsKey(String Key) {
        return map.containsKey(Key);
    }

    public long getLong(String Key) {
        return map.getLong(Key);
    }

    public long getLong(String Key, long d) {
        return map.getLong(Key, d);
    }

    public float getFloat(String Key) {
        return map.getFloat(Key);
    }

    public float getFloat(String Key, float d) {
        return map.getFloat(Key, d);
    }

    public List<Object> getList(String Key) {
        return map.getList(Key);
    }

    public <V> List<V> getList(String Key, List<V> d) {
        return map.getList(Key, d);
    }

    public MyMap<String, Object> getMap(String Key) {
        return map.getMap(Key);
    }

    public <K, V> MyMap<K, V> getMap(String Key, MyMap<K, V> d) {
        return map.getMap(Key, d);
    }

    public double getDouble(String Key) {
        return map.getDouble(Key);
    }

    public double getDouble(String Key, double d) {
        return map.getDouble(Key, d);
    }

    public boolean getBoolean(String Key) {
        return map.getBoolean(Key);
    }

    public boolean getBoolean(String Key, boolean d) {
        return map.getBoolean(Key, d);
    }

    public int getInt(String Key) {
        return map.getInt(Key);
    }

    public int getInt(String Key, int i) {
        return map.getInt(Key, i);
    }

    public String getString(String Key) {
        return map.getString(Key);
    }

    public String getString(String Key, String str) {
        return map.getString(Key, str);
    }

    public MyMap<String, Object> getMap() {
        return map;
    }

    public synchronized boolean save() {
        try {
            if (!File.getParentFile().exists() && File.getParentFile() != null)
                File.getParentFile().mkdirs();
            Utils.writeFile(File, Contxt = yaml.dump(this.map));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object get(String Key) {
        return map.get(Key);
    }

    public Object get(String Key, Object obj) {
        if (!map.containsKey(Key))
            return obj;
        Object object = map.get(Key);
        return object == null ? obj : object;
    }

    public Config(String file) {
        this(new File(file), new HashMap<String, Object>());
    }

    public Config(String file, Map<String, Object> map) {
        this(new File(file), map);
    }

    public Config(File file) {
        this(file, new HashMap<String, Object>());
    }

    public Config remove(String Key) {
        map.remove(Key);
        return this;
    }

    /**
     * 配置文件
     *
     * @param file 配置文件的File对象
     * @param map  若文件不存在则会默认生成的数据
     */
    public Config(File file, Map<String, Object> map) {
        try {
            if (file != null && file.exists()) {
                Contxt = Utils.readFile(file);
                this.map = new MyMap<>(yaml.loadAs(Contxt, Map.class));
            } else
                this.map = new MyMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            this.map = new MyMap<>(map);
            Contxt = yaml.dump(this.map);
        }
        File = file;
    }

    public Config set(String Key, Object Date) {
        map.put(Key, Date);
        return this;
    }

    public File getFile() {
        return File;
    }

    public void setFile(File file) {
        File = file;
    }

    public String getContxt() {
        return Contxt;
    }

    public void setAll(Map<String, Object> map) {
        this.map.clear();
        this.map.putAll(map);
    }
}
