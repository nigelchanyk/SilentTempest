package ca.nigelchan.operationbanana.data.layers;

public interface ILayerDataVisitor {

	public void visit(ActorLayerData data);
	public void visit(FieldLayerData data);

}