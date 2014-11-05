
import java.util.Vector;

public class Node {
    
        public Node(String label, double x, double y, boolean fixed,String time) {
            _label = label;
            _fixed = fixed;
            _x = x;
            _y = y;
       t=time;
        }
        
        public String getLabel() {
            return _label;
        }
        
        public void addEdgeTo(Node node) {
            _edgesTo.addElement(node);
        }
        
        public Vector getEdgesTo() {
            return _edgesTo;
        }
        
        public double _fx;
        public double _fy;
        public double _x;
        public double _y;
        public boolean _fixed;
        private Vector _edgesTo = new Vector();
        private String _label;
        public String t;
    }
    
   