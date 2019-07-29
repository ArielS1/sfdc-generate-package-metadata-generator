import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class NormalChildren extends Children implements Map<String, NormalChildrenObject> {
	public Map<String, NormalChildrenObject> map = new HashMap<String, NormalChildrenObject>();
	
	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<Entry<String, NormalChildrenObject>> entrySet() {
		return map.entrySet();
	}

	@Override
	public NormalChildrenObject get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public NormalChildrenObject put(String key, NormalChildrenObject value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends NormalChildrenObject> m) {
		map.putAll(m);		
	}

	@Override
	public NormalChildrenObject remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<NormalChildrenObject> values() {
		return map.values();
	}
}