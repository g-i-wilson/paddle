package paddle;

public abstract class Response extends Request {

	public LinkedHashMap<String,String> metadata = new LinkedHashMap<>();
	public string data;

	// add metadata
	public void metadata ( String key, String value ) {
		metadata.put( key, value );
	}

	// set data
	public String data ( String dataStr ) {
		data = dataStr;
	}
	
	public Map<String,String> metadata () {
		return metadata;
	}

	public String data () {
		return data;
	}
	
}
