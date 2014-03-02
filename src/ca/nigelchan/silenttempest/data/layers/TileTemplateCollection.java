package ca.nigelchan.silenttempest.data.layers;

import ca.nigelchan.silenttempest.data.layers.TileTemplate.Attribute;

public final class TileTemplateCollection {
	
	private static TileTemplateCollection collection = null;
	private static final TileTemplate.Attribute H = Attribute.HIDING_SPOT;
	private static final TileTemplate.Attribute N = Attribute.NORMAL;
	private static final TileTemplate.Attribute O = Attribute.OBSTACLE;
	
	private TileTemplate[] templates;
	
	private TileTemplateCollection(int size) {
		templates = new TileTemplate[size];
	}
	
	private void add(TileTemplate template) {
		templates[template.getId()] = template;
	}
	
	public TileTemplate get(int id) {
		if (id < 0)
			return null;
		return templates[id];
	}
	
	public static TileTemplateCollection instance() {
		if (collection == null) {
			TileTemplate.Attribute[] attrs = {
				N, N, N, N, N,
				O, N, N, N, N,
				N, N, N, N, N,
				N, N, N, N, N,
				N, N, N, N, N,
			};
			collection = new TileTemplateCollection(attrs.length);
			for (int i = 0; i < attrs.length; ++i)
				collection.add(new TileTemplate(i, attrs[i]));
		}
		return collection;
	}

}
