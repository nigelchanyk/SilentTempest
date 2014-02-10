package ca.nigelchan.silenttempest.data.layers;

import java.util.TreeMap;

public final class TileTemplateCollection {
	
	private TileTemplate defaultTemplate;
	
	private TreeMap<Integer, TileTemplate> templateMapper = new TreeMap<Integer, TileTemplate>();
	
	public TileTemplateCollection(TileTemplate defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
		add(defaultTemplate);
	}
	
	public void add(TileTemplate template) {
		templateMapper.put(template.getId(), template);
	}
	
	public TileTemplate get(int id) {
		return templateMapper.get(id);
	}
	
	public TileTemplate getDefault() {
		return defaultTemplate;
	}

}
