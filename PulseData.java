import java.util.*;
import java.time.LocalDateTime;

public class PulseData {

	private LocalDateTime		timeReceived;	
	private byte[] 					dat;
	private int							version;
	private int							captureId;
	private int							count;
	private int							actualCount;
	private long						timestamp;
	private List<Integer>		samples;

  private int intLittleEndian( byte[] buf, int start, int end ) {
  	int leftShift = 0;
  	int total = 0;
  	for (int i=start; i<end; i++) {
			total += ((int)buf[i]) << leftShift;
			leftShift += 8;
		}
		return total;
  }
  
  public PulseData ( String dataStr ) {
  	timeReceived	= LocalDateTime.now();
  	dat 					=	dataStr.getBytes();
  	version 			=	intLittleEndian( dat, 0, 2 );
  	captureId			=	intLittleEndian( dat, 2, 4 );
  	count					=	intLittleEndian( dat, 4, 8 );
  	timestamp			= (long)intLittleEndian( dat, 8, 12 ) + (long)intLittleEndian( dat, 12, 16 ) << 32;
  	samples				= new ArrayList<Integer>();
  	actualCount 	= 0;
  	
  	for (int i=16; i<dat.length; i+=2) {
  		samples.add( intLittleEndian( dat, i, i+2 ) );
  		actualCount++;
  	}
  	
	}
	
	public LocalDateTime timeReceived () {
		return timeReceived;
	}
	
	public byte[] data () {
		return dat;
	}
	
	public int version () {
		return version;
	}
	
	public int captureId () {
		return captureId;
	}
	
	public int count () {
		return count;
	}
	
	public int actualCount () {
		return actualCount;
	}
	
	public long timestamp () {
		return timestamp;
	}
	
	public List<Integer> samples () {
		return samples;
	}
	
	public String toString () {
		return
			"*** PulseData ***"+
			"\ntimeReceived: "+timeReceived+
			"\nversion:      "+version+
			"\ncaptureId:    "+captureId+
			"\ncount:        "+count+
			"\nactualCount:  "+actualCount+
			"\ntimestamp:    "+timestamp+
			"\nsamples:\n"+samples
		;
	}

}