/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of the MontyApplet dynamic graph drawing system.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: GraphDrawing.java,v 1.2 2004/02/01 13:22:59 pjm2 Exp $

*/

import java.awt.*;


public class GraphDrawing extends Component {
    
    public GraphDrawing() {
        
    }
    
    public void clearImage() {
        _image = createImage(getSize().width, getSize().height);//Dimension object.width of component
        Graphics g = _image.getGraphics();
        fillWhite();
        repaint();
    }
    
    public void fillWhite() {
        if (_image != null) {
            Graphics g = _image.getGraphics();
            if (g != null) {
                g.setColor(_blankColor);
                g.fillRect(0, 0, getSize().width, getSize().height);
            }
        }
    }
    
    public Image getImage() {
        return _image;
    }
    
    public void paintNow() {
        Graphics g = getGraphics();
        if (g != null) {
            paint(g);
        }
    }
    
    public void paint(Graphics g) {
	if (_image != null) {
	    g.drawImage(_image, 0, 0, null);
	}   
    }
    
    Image _image = null;
    Color _blankColor = Color.decode("#ffffff");
    
}