import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.*;
import java.io.File;
import paddle.*;


public class ServePulses {
  public static void main(String[] args) throws Exception {

    PulseData c = new PulseData();

    Server testPost 				= new ServerHTTP( c, 9000, "test POST" );
    Server displayLast		 	= new ServerHTTP( c, 9001, "display last pulse" );
    Server display100 			= new ServerHTTP( c, 9002, "display last 100 pulses" );

    Server pulsePost 				= new ServerHTTP( c, 49155, "pulse data POST" );

		

    while(true) {
      Thread.sleep(1000);

    }


  }

}


class PulseData extends ServerState {

  private List<List<Integer>> pulses;
  private List<String> pulseTimes;
  private File dataFile;
  
  public PulseData () throws Exception {
  	this(
			LocalDateTime.now().format(
		  	DateTimeFormatter.ofPattern("dd-MM-yyyy_HHmmss")
		  )+".csv"
    );
  }

  public PulseData ( String fileName ) throws Exception {
  	dataFile = new File( fileName );
  	pulses = new ArrayList<>();
  	pulseTimes = new ArrayList<>();
  	Files.write(dataFile.toPath(), "".getBytes());
    System.out.println( "PulseData initialized!" );
  }
  
  public void addData ( String data ) {
  	System.out.println( data );
  	String dataPoints[];
  	//if (data.equals("") || data.equals("data=")) return;
  	try {
  		dataPoints = data.split("=")[1].split("%2C");
  	} catch (Exception e) {
  		return;
  	}
  	List<Integer> subList = new ArrayList<>();
		for (String point : dataPoints) {
			System.out.println( "> "+point );
			if (!point.equals("")) subList.add( Integer.valueOf( point ) );
		}
  	try {
			Files.write(dataFile.toPath(), (data+",").getBytes(), StandardOpenOption.APPEND);
			Files.write(dataFile.toPath(), "\n".getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			System.out.println( "ERROR: couldn't write to "+dataFile );
			e.printStackTrace();
		}
  	pulses.add( subList );
  	if (pulses.size() > 100) pulses.remove(0);
  	pulseTimes.add( LocalDateTime.now().toString() );
  	if (pulseTimes.size() > 100) pulseTimes.remove(0);
  }
  
  private String arrayIntegerJoin (List<Integer> l) {
  	String str = "";
  	for (Integer i : l) str += ","+i;
  	return str.substring(1);
  }

  public void respondHTTP ( RequestHTTP req, ResponseHTTP res ) {
  	System.out.println( req.path() );
  	String latestPulse = pulses.size() > 0 ? arrayIntegerJoin(pulses.get(pulses.size()-1)) : "";
  	String latestPulseTime = pulseTimes.size() > 0 ? pulseTimes.get(pulses.size()-1) : "";
  	String plotlyStart =
  		"<!DOCTYPE html>\n" +
			"<head>\n" +
//			"<meta charset=\"utf-8\" http-equiv=\"refresh\" content=\"1\" />\n" +
//			"<style type=\"text/css\">\n" +
//			"* {padding: 0;margin: 0;}\n" +
//			"html, body {height: 100%;}\n" +
//			"#container {min-height: 100%;background-color: #DDD;border-left: 2px solid #666;border-right: 2px solid #666;width: 676px;margin: 0 auto;}\n" +
//			"* html #container {height: 100%;}\n" +
//			"</style>\n" +
			"</head>\n" +
			"<body style='background-color:#eeeeee'>\n" +
			"Latest pulse at: "+latestPulseTime+" <button onClick=\"window.open(encodeURI('data:text/csv;charset=utf-8,"+latestPulse+"'))\">Download CSV</button><br>\n" +
			"<br><div id='plot_div'></div>\n" +
			"<script src='https://cdn.plot.ly/plotly-latest.min.js'></script>\n" +
			"<script>\n" +
			"var layout = {\n" +
    	" showlegend: false,\n" +
			"	autosize: false,\n" +
			"	width: 1200,\n" +
			"	height: 720,\n" +
			" scene: {aspectratio: {x:3, y:1, z:0.5}},\n" +
//			"	yaxis: {\n" +
//			"		title: 'Y-axis Title',\n" +
//			"		automargin: true,\n" +
//			"		titlefont: { size:30 },\n" +
//			"	},\n" +
//			"	paper_bgcolor: '#7f7f7f',\n" +
//			"	plot_bgcolor: '#c7c7c7',\n" +
			" barmode: 'overlay'\n" +
			"};\n" ;
			
		String plotlyEnd = 
			"</script>\n" +
			"</body>\n" +
			"</html>\n";
			
		String body = "";

  	if ( req.socket().getLocalPort() == 9000 ) {
  		//System.out.println( req.data() );
  		addData( req.data() );
  		res.setBody(
  			"<form method='post'><br><br>\n" +
  			"<input type='text' name='data'><br>\n" +
  			"<input type='submit' value='Submit'>\n" +
  			"</form><br><br>\n" +
  			pulses
  		);
  		
  	//} else if ( req.socket().getLocalPort() == 9001 ) {
  	} else if ( req.path().toLowerCase().equals("/latest") ) {
  		//System.out.println("9001");
			body +=
				plotlyStart +
				"Plotly.newPlot( 'plot_div', [ \n";
  		//if (pulses.size() > 0) {
  			// only display latest pulse
				body += "{type:'scatter', marker:{opacity:'0.2', color:'#0000ff'}, y:["+latestPulse+"]},\n";
			//}
			body +=
				//" ], {\"barmode\":\"overlay\"} );\n" +
				" ], layout );\n" +
				plotlyEnd;
  		res.setBody( body );
			
  	//} else if ( req.socket().getLocalPort() == 9002 ) {
  	} else if ( req.path().toLowerCase().equals("/last100") ) {
  		//System.out.println("9002");
			body +=
				plotlyStart +
				"Plotly.newPlot( 'plot_div', [ \n";
  		if (pulses.size() > 0) {
				int x = 0;
				for (List<Integer> pulse : pulses) {
					int y = 0;
					String xStr = "";
					String yStr = "";
  				for (int i=0; i<pulse.size(); i++) {
  					xStr += x+",";
  					yStr += (y++)+",";
  				}
					x++;
					body += 	"{\n" +
										"  x: ["+xStr+"],\n" +
										"  y: ["+yStr+"],\n" +
										"  z: "+pulse+",\n" +
										"  marker: {\n" +
										"	   size: 2,\n" +
										"	   line: {\n" +
										"	     width: 0\n" +
										"    },\n" +
										"    opacity: 0.5\n" +
										"  },\n" +
										"  type: 'scatter3d'\n" +
										"},\n" ;
				}
			}
			body +=
				" ], layout );\n" +
				plotlyEnd;
  		res.setBody( body );
  	}
  }

}
