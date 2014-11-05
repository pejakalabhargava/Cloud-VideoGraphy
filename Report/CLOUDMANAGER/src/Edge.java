 public class Edge {
        public Edge(Node source, Node target) {
            _source = source;
            _target = target;
        }
        
        public Node getSource() {
            return _source;
        }
        
        public Node getTarget() {
            return _target;
        }
        
        @Override
        public int hashCode() {
            return (_source.getLabel() + _target.getLabel()).hashCode();
        }
        
        public boolean equals(Object o) {
            Edge edge = (Edge)o;
            if (edge.getSource().getLabel().equals(_source.getLabel()) && edge.getTarget().getLabel().equals(_target.getLabel())) {
                return true;
            }
            return false;
        }
        
        private Node _source;
        private Node _target;
    }

