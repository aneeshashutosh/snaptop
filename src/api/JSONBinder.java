package api;
import json.JSONObject;

public interface JSONBinder<T>
{
	public T bind(JSONObject obj);
}