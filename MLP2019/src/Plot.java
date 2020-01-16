
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Plot extends Frame {
	
	private ArrayList<Cdata> correctList = new ArrayList<Cdata>(); 
	private ArrayList<Cdata> wrongList = new ArrayList<Cdata>();
	
	
		   public Plot(ArrayList<Cdata> corr,ArrayList<Cdata> wrong){
		     // super("M");
			  this.correctList = corr;
			  this.wrongList = wrong;
		      prepareGUI();
		   }


		   private void prepareGUI(){
		      setSize(800,800);
		      addWindowListener(new WindowAdapter() {
		         public void windowClosing(WindowEvent windowEvent){
		            System.exit(0);
		         }        
		      }); 
		   }    

		   @Override
		   public void paint(Graphics g) {
		      Graphics2D g2 = (Graphics2D)g;
		      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		         RenderingHints.VALUE_ANTIALIAS_ON);
		      Font font = new Font("Serif", Font.PLAIN, 15);
		      
		      
		      g2.setFont(font);
		      g2.translate(0, getHeight());
		      g2.scale(1.0, -1.0);
		      
		      for(int i = 0; i < correctList.size(); i++) {
		    	  float t1 = (float)((correctList.get(i).getX1() + 2)/4);
		    	  float t2 = (float)((correctList.get(i).getX2() + 2)/4);
		    	  float x = (1 - t1)*10 + t1*780;
		    	  float y = (1 - t2)*20 + t2*770;
		    	  switch(correctList.get(i).getC()) {
			    	  case "C1": 
			          	g2.setPaint(Color.lightGray);
			            break; 
			          case "C2": 
			          	g2.setPaint(Color.magenta);
			            break; 
			          case "C3": 
			        	g2.setPaint(Color.orange);  
			            break; 
		    	  }
		    	  
		    	  
		    	  g2.drawString("+", x, y);
		      }
		      for(int i = 0; i < wrongList.size(); i++) {
		    	  float t1 = (float)((wrongList.get(i).getX1() + 2)/4);
		    	  float t2 = (float)((wrongList.get(i).getX2() + 2)/4);
		    	  float x = (1 - t1)*10 + t1*780;
		    	  float y = (1 - t2)*20 + t2*770;
		    	  g2.setPaint(Color.RED);
		    	  g2.drawString("-", x, y);
		      }
		   }
		

}
